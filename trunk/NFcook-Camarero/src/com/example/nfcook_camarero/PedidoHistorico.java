package com.example.nfcook_camarero;

import java.util.ArrayList;

import adapters.ContenidoListMesa;
import adapters.ContenidoListPedidoHistorico;
import adapters.HijoExpandableListEditar;
import adapters.MiExpandableListAdapterEditar;
import adapters.MiListAdapterMesa;
import adapters.MiListAdapterPedidoHistorico;
import adapters.PadreExpandableListEditar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A�ade los componentes de cada pedido a la mesa
 * 
 * -Atributos-
 * numMesa : Indica el numero de la mesa actual.
 * dbPedido : Base de datos con todos los pedidos de todas las mesas.
 * dbHistorico : Base de datos donde se almacenan todos los platos pedidos una vez cobrada esa mesa.
 * platos : Componente ListView que mostrar� los platos de la mesa actual.
 * elemLista : ArrayList con los platos de la mesa actual, que se utiliza para crear el adapter del componente ListView.
 * adapter : Objeto de la clase ListaMesaAdapter.
 * precioTotal : TextView que contiene el precio total de los pedidos de la mesa actual.
 * 
 * @author Rober
 */


public class PedidoHistorico extends Activity {

	private HandlerGenerico sqlHistorico;
	private String numMesa;
	private SQLiteDatabase dbHistorico;
	private static ListView platos;
	private ArrayList<ContenidoListPedidoHistorico> elemLista;
	private static MiListAdapterPedidoHistorico adapter;
	private static TextView precioTotal;
	private String hora;
	
	
	private static Context context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Quitamos barra de titulo de la aplicacion
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.pedidohistorico);
		//Necesario para actualizar la lista de las mesas al a�adir un plato
		context = PedidoHistorico.this;
		
		//El numero de la mesa se obtiene de la pantalla anterior
		Bundle bundle = getIntent().getExtras();
		numMesa = bundle.getString("mesa");
		hora = bundle.getString("hora");
		
		TextView mesa = (TextView)findViewById(R.id.tituloPedidoHistorico);
		mesa.setText("Mesa "+ numMesa+"\n" +"Pedido " + hora.substring(hora.indexOf(" ")+1));
		
		try{
			sqlHistorico=new HandlerGenerico(getApplicationContext(), "/data/data/com.example.nfcook_camarero/databases/", "Historico.db");
			dbHistorico= sqlHistorico.open();
			
			//A�adir platos a la ListView----------------------------------------------------
	  	  	platos = (ListView)findViewById(R.id.listaPlatosHistorico);
		    elemLista = obtenerElementos();
	         
	  	    adapter = new MiListAdapterPedidoHistorico(this, elemLista);
	  	     
	  	    precioTotal = (TextView)findViewById(R.id.precioTotal);
	  	    precioTotal.setText(Double.toString( Math.rint(adapter.getPrecio()*100)/100 )+" �");
	  	     
	  	    platos.setAdapter(adapter);
	  	    
	  	 
	    
		}catch(Exception e){
			System.out.println("Error lectura base de datos de Pedido");
		}
		

	}
	
	private ArrayList<ContenidoListPedidoHistorico> obtenerElementos() {
		ArrayList<ContenidoListPedidoHistorico> elementos=null;
		try{
			String[] campos = new String[]{"Nombre","Observaciones","Extras","Precio"};
		    String[] consulta = new String[]{numMesa,hora};
		    
		    Cursor c = dbHistorico.query("Historico",campos, "NumMesa=? AND FechaHora=?",consulta, null,null, null);
		    
		    elementos = new ArrayList<ContenidoListPedidoHistorico>();
		     
		    while(c.moveToNext())
		    	elementos.add(new ContenidoListPedidoHistorico(c.getString(0) ,c.getString(2),c.getString(1),c.getDouble(3)));
		    	
		    return elementos;
		    
		}catch(Exception e){
			System.out.println("Error en obtenerElementos");
			return elementos;
		}
	}
	
	
	public static ListView getPlatos(){
		return platos;
	}

	public static TextView getPrecioTotal() {
		return precioTotal;
	}

	public static Context getContext() {
		return context;
	}
	
}