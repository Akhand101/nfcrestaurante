package com.example.nfcook_camarero;
import baseDatos.HandlerGenerico;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class SincronizarTpv extends Activity implements CreateNdefMessageCallback,OnNdefPushCompleteCallback{ 

	NfcAdapter mNfcAdapter;
    TextView mInfoText;
    private static final int MESSAGE_SENT = 1;
    Context context;
    private String abreviaturaRest;
    
    boolean enviado;
	//Variables para los pedidos
	String restaurante;
    String pedido;
    String numeroMesa;
    String numPersonas;
    
    int numeroRestaurante;
	String abreviatura;
	/*Variables para obtener el valor equivalente del restaurante*/
	String ruta="/data/data/com.example.nfcook_camarero/databases/";
	
	HandlerGenerico sqlMesas,sqlRestaurante;
	SQLiteDatabase dbMesas,dbRestaurante;
    
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sincronizacion_beam_nfc);
        context= this;
         enviado=false;            	
        // Ponemos el t�tulo a la actividad
        // Recogemos ActionBar
        ActionBar actionbar = getActionBar();
    	actionbar.setTitle(" SINCRONIZAR PEDIDO");
    	
    	// atras en el action bar
        actionbar.setDisplayHomeAsUpEnabled(true);
        
        Bundle bundle = getIntent().getExtras();
      	restaurante = bundle.getString("Restaurante");
      	numeroMesa=bundle.getString("Mesa");
      	numPersonas=bundle.getString("Personas");
     
      	 pedido= damePedidoStr();
      	     	    	
      	// Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
        //    mInfoText = (TextView) findViewById(R.id.textView);
            mInfoText.setText("NFC no esta activo en el dispositivo.");
        } else {
             
          // Register callback to set NDEF message
            mNfcAdapter.setNdefPushMessageCallback(this, this);
         // Register callback to listen for message-sent success
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
           
        }
    }
	
	//  para el atras del action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){       
    	finish();
		return false;
    }
    
    //---Codificar los platos---------------------------------
	
	
	

	/** 
	 
	 * @return
	 */
	private String damePedidoStr() {
		
		//Obtengo los datos del restaurante su numero y abreviatura
        try{ //Abrimos la base de datos para consultarla
 	       	sqlMesas = new HandlerGenerico(getApplicationContext(),"Mesas.db"); 
 	        dbMesas = sqlMesas.open();
 	     
 	    }catch(SQLiteException e){
 	        	Toast.makeText(getApplicationContext(),"No existe la base de datos Mesas",Toast.LENGTH_SHORT).show();
 	       }
        // 
        
		String listaPlatosStr = dameCodigoRestaurante()+numeroMesa+"@"+numPersonas+"@";
		String[] campos = new String[]{"Sincro","IdPlato","Ingredientes","Extras"};//Campos que quieres recuperar
		String[] datosMesa = new String[]{numeroMesa};	
		Cursor cursorPedido = dbMesas.query("Mesas", campos, "NumMesa=?", datosMesa,null, null,null);
		int sincro=0;
    	while(cursorPedido.moveToNext()){
    		
    		sincro=cursorPedido.getInt(0);
    		if (sincro==0){//No estasincronizado
	    		listaPlatosStr += cursorPedido.getString(1);
	    		if(!cursorPedido.getString(2).equals(""))
	    			listaPlatosStr+="+"+cursorPedido.getString(2);
	    		if(!cursorPedido.getString(3).equals(""))
	        	listaPlatosStr+= "*"+cursorPedido.getString(3);
	    		
	    		listaPlatosStr+="@";
    		
    		}
    }
    	//System.out.println("PLATOS:"+listaPlatosStr);
    	Toast.makeText(getApplicationContext(), listaPlatosStr, Toast.LENGTH_LONG).show();
    	// para indicar que ha finalizado el pedido escribo un 255 
    	listaPlatosStr += "255"+"@";
    	
    	return listaPlatosStr;
	}
	
	private String dameCodigoRestaurante(){
		// Campos que quieres recuperar
		
		//Obtengo los datos del restaurante su numero y abreviatura
        try{ //Abrimos la base de datos para consultarla
 	       	sqlRestaurante = new HandlerGenerico(getApplicationContext(),"/data/data/com.example.nfcook_camarero/databases/","Equivalencia_Restaurantes.db"); 
 	        dbRestaurante = sqlRestaurante.open();
 	     
 	    }catch(SQLiteException e){
 	        	Toast.makeText(getApplicationContext(),"No existe la base de datos Restaurante",Toast.LENGTH_SHORT).show();
 	       }
		
		String[] campos = new String[] { "Numero", "Abreviatura" };
		String[] datos = new String[] { restaurante };
		Cursor cursorPedido = dbRestaurante.query("Restaurantes", campos, "Restaurante=?",datos, null, null, null);

		cursorPedido.moveToFirst();
		String codigoRest = cursorPedido.getString(0) + "@";
		abreviaturaRest = cursorPedido.getString(1);
		
		return codigoRest;
		
	}

    /**
     * Implementation for the OnNdefPushCompleteCallback interface
     */
    public void onNdefPushComplete(NfcEvent arg0) {
        // A handler is needed to send messages to the activity when this
        // callback occurs, because it happens from a binder thread
       if (enviado == false)
    	{mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
        enviado=true;
    	}
    }

    /**
     * Metodo encargado de crear el pedido que queremos enviar
     */
    @SuppressLint("NewApi")
    public NdefMessage createNdefMessage(NfcEvent event) {
    	
        	NdefMessage msg = new NdefMessage(NdefRecord.createMime(
                "application/com.example.android.beam", pedido.getBytes())
       
        );
        
        return msg;
    }
    /**
     * Metodo encargado de cerrar la ventana
     */
    public void cerrarVentana()
    {
    	this.finish();
    }
    
    
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_SENT:
            	Toast.makeText(getApplicationContext(), "Pedido Sincronizado", Toast.LENGTH_LONG).show();
            	Mesa.actualizarSincronizadosBaseMesas();
        		//Notificas que has borrado un elemento del adapter y que repinte la lista
        		Mesa.actualizaListPlatos();
            	cerrarVentana();
                break;
            }
        }
    };
    
    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
    
    }

    /**
     * Metodo encargado de procesar el mensaje
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        mInfoText.setText(new String(msg.getRecords()[0].getPayload()));
    }


	
}
 