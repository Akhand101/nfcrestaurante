package usuario;

import java.util.ArrayList;

import baseDatos.HandlerDB;
import com.example.nfcook.R;

import adapters.InfomacionPlatoPantallaReparto;
import adapters.MiGridViewCalculadoraAdapter;
import adapters.MiViewPagerAdapter;
import adapters.PadreGridViewCalculadora;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase encargada de implementar la funcionalidad de la calculadora.
 * 
 * La calculadora se divide principalmente en las siguientes partes:
 * 
 * 1- Secci�n de promociones vigentes, por si los comensales disponen de alguna promoci�n
 * para que puedan calcular el precio definitivo aplicando la promoci�n.
 * 
 * 2- GridView con informaci�n de los comensales. Nos aparecer� su nombre, el total que
 * va a tener que pagar y una lista resumen de los platos que ha consumido cada uno con
 * la porci�n correspondiente de los mismos.
 * 
 * 3- Lista de im�genes con los platos que han consumido durante la estancia. El usuario
 * podr� ir viendo los platos que ha consumido simplemente deslizando el dedo sobre la 
 * im�gen central y si hace click sobre la misma saldr� una ventana emergente para que 
 * diga quien ha consumido dicho plato y as� este se le sume a cada comensal en la parte
 * que le toca. En la ventana emergente saldr� la im�gen del plato para que el usuario 
 * pueda identificar de forma sencilla de que plato se trata y el precio que se va a pagar
 * por el mismo. * 
 * 
 * @author Abel
 *
 */
public class Calculadora extends Activity{
	private static GridView gridViewPersonas;
	private static MiGridViewCalculadoraAdapter adapterGridViewCalculadora;
    private ArrayList<PadreGridViewCalculadora> personas;
    
    private ArrayList<InfomacionPlatoPantallaReparto> platos;
    
    private int numPersonas;

	public void onCreate(Bundle savedInstanceState) {   
        //Quitamos barra de titulo de la aplicacion
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Quitamos barra de notificaciones
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculadora);
        
        // Recogemos el n�mero de comensales que vendr� de la ventana emergente anterior
        Bundle bundle = getIntent().getExtras();
		numPersonas = bundle.getInt("numeroComensales");
        
        // creamos el arraylist de las imagenes
    	platos = new ArrayList<InfomacionPlatoPantallaReparto>();
    	
    	/*****************************CARGAMOS EL SPINNER******************************/
    	Spinner spinnerPromociones = (Spinner) findViewById(R.id.spinnerPromociones);
    	ArrayList<String> promociones = new ArrayList<String>();
    	/*
    	 * FIXME Actualmente no vamos a poner promociones, simplemente ser� para que 
    	 * en un futuro se lean de la bd y se a�adan aqu�.
    	 * 
    	 * Por otro lado el Adapter no est� redefinido, utilizamos el b�sico que para
    	 * lo que vamos a hacer ahora nos vale.
    	 */
    	// Metemos las promociones que haya en el restaurante
    	promociones.add("Seleccione la promoci�n que desee...");
    	
    	// Como ahora no hay promociones lo deshabilitamos
    	/*
    	 * FIXME El d�a que haya promociones hay que habilitarlo
    	 */
    	spinnerPromociones.setClickable(false);
    	    	
    	// Creamos el adapter por defecto y se lo aplicamos
    	ArrayAdapter<String> adapterSpinnerPromociones = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, promociones);
    	adapterSpinnerPromociones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinnerPromociones.setAdapter(adapterSpinnerPromociones);

        /*************************************PERSONAS*********************************/
        // Creamos la lista personas
        personas = new ArrayList<PadreGridViewCalculadora>();
        gridViewPersonas = (GridView) findViewById(R.id.gridViewCalculadora);
        
        // Creamos las personas
        PadreGridViewCalculadora persona;
        for(int i=1; i<=numPersonas; i++){
        	persona = new PadreGridViewCalculadora(i);
        	personas.add(persona);
        }
        
		// Creamos el adapater del gridView para que se muestren las personas
        adapterGridViewCalculadora = new MiGridViewCalculadoraAdapter(this, personas);
        gridViewPersonas.setAdapter(adapterGridViewCalculadora);
        
        /**************************INFORMACION DE LOS PLATOS******************************/
        
        // Sacamos la imformaci�n de los platos que haya en cuenta.db
        try{
			// Abrimos la base de datos de cuenta
        	/*
        	 * FIXME Hay que poner la base de datos de cuenta no la de pedido
        	 */
        	HandlerDB sqlCuenta = new HandlerDB(this.getApplicationContext(),"Pedido.db"); 
        	SQLiteDatabase dbCuenta = sqlCuenta.open();
        	
        	// Sacamos el id de todos los platos de cuenta
        	String[] camposSacar = new String[]{"Id","Restaurante", "IdHijo"};
	    	//String[] datosQueCondicionan = new String[]{restaurante};
    		Cursor cP = dbCuenta.query("Pedido", camposSacar, null, null, null, null,null);
	    	
	    	// Importamos la base de datos de los platos
        	HandlerDB sqlPlatos = new HandlerDB(this.getApplicationContext()); 
        	SQLiteDatabase dbPlatos = sqlPlatos.open();
        	
    	    // Recorremos todos los platos que hubiera en cuenta
    	    while(cP.moveToNext()){
        		// Sacamos la informaci�n de ese plato para luego mostrarla (foto, nombre...)
            	String[] camposSacarPlato = new String[]{"Foto", "Nombre", "Precio"};
    	    	String[] datosQueCondicionanPlato = new String[]{cP.getString(1),cP.getString(0)};
        		Cursor cPlato = dbPlatos.query("Restaurantes", camposSacarPlato, "Restaurante=? AND Id=?",datosQueCondicionanPlato,null, null,null);

        		InfomacionPlatoPantallaReparto infoPlato;
        		int imagenPlato;
        		while(cPlato.moveToNext()){
        			imagenPlato = getResources().getIdentifier(cPlato.getString(0),"drawable",this.getPackageName());
        			infoPlato = new InfomacionPlatoPantallaReparto(cP.getString(2), cPlato.getString(1),imagenPlato,cPlato.getDouble(2));
        			platos.add(infoPlato);
        		}
    	    }
	    		    	
	    	// Cerramos las bases de datos de cuenta y platos
        	sqlCuenta.close();
        	sqlPlatos.close();
        	
        	// Damos valor a las im�genes que acompa�an a la im�gen deslizable
        	// Traemos las im�genes del layout que mostrar�n el plato anterior y el siguiente
  			ImageView imgIzq = (ImageView)findViewById(R.id.imageViewIzquierdaCalculadora);
			ImageView imgDer = (ImageView)findViewById(R.id.imageViewDerechaCalaculadora);

  			int numPlatos = platos.size();
  			// Si solo hay un plato ocultamos las dos im�genes de los lados
  			if(numPlatos <= 1){
  				imgIzq.setVisibility(ImageView.INVISIBLE);
  				imgDer.setVisibility(ImageView.INVISIBLE);
  			}else if(numPlatos>1){
  				// Ocultamos la im�gen de la izquierda hasta que corra una
  				imgIzq.setVisibility(ImageView.INVISIBLE);
  				// Desolcultamos la im�gen de la derecha y le damos valor
  				imgDer.setVisibility(ImageView.VISIBLE);
  				imgDer.setImageResource(platos.get(1).getFotoPlato());
  			}
        	
        	// Creamos la imagen que se desliza y va pasando y aplicamos su adapter (ViewPager)
        	MiViewPagerAdapter miImagenDeslizanteAdpater = new MiViewPagerAdapter(this, platos, personas.size());
      	  	ViewPager imagenDeslizante = (ViewPager) findViewById(R.id.imagenDeslizanteCalculadora);
	      	imagenDeslizante.setAdapter(miImagenDeslizanteAdpater);
	      	imagenDeslizante.setCurrentItem(0);
	      	
	      	// Creamos su oyente
	      	imagenDeslizante.setOnPageChangeListener(new OnPageChangeListener(){
	      		
      	  		public void onPageScrollStateChanged(int arg0) {	      				
	      		}
	
	      		public void onPageScrolled(int arg0, float arg1, int arg2) {	      				
	      		}
	
	      		/*
	      		 * Metodo encargado de implementar la acci�n correspondiente cuando
	      		 * deslicemos el dedo sobre la im�gen central y se cambie la im�gen.
	      		 * (non-Javadoc)
	      		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
	      		 */
	      		public void onPageSelected(int arg0) {
	      			// Traemos las im�genes del layout que mostrar�n el plato anterior y el siguiente
	      			ImageView imgIzq = (ImageView)findViewById(R.id.imageViewIzquierdaCalculadora);
    				ImageView imgDer = (ImageView)findViewById(R.id.imageViewDerechaCalaculadora);

	      			int numPlatos = platos.size();
      				// Miramos si estamos en la primera im�gen
      				if(arg0 == 0){
      					imgIzq.setVisibility(ImageView.INVISIBLE);
      					imgDer.setVisibility(ImageView.VISIBLE);
      					imgDer.setImageResource(platos.get(arg0+1).getFotoPlato());
      				// Si nos encontramos en la �ltima im�gen ocultamos la derecha
      				}else if(arg0 == numPlatos-1){
      					imgDer.setVisibility(ImageView.INVISIBLE);
      					imgIzq.setVisibility(ImageView.VISIBLE);
      					imgIzq.setImageResource(platos.get(arg0-1).getFotoPlato());
      				/*
      				 * Si no nos encontramos en ninguno de los casos anteriores, le damos
      				 * valor a la im�gen izquierda con la anteroir im�gen vista y la
      				 * im�gen derecha con el plato siguiente que vamos a ver.
      				 */
      				}else{
      					imgIzq.setVisibility(ImageView.VISIBLE);
      					imgDer.setVisibility(ImageView.VISIBLE);
      					imgIzq.setImageResource(platos.get(arg0-1).getFotoPlato());
      					imgDer.setImageResource(platos.get(arg0+1).getFotoPlato());
      				}
      				
      				// Damos valor al campo texto del nombre del plato activo
      	  			TextView textViewNombrePlatoDeslizante = (TextView) findViewById(R.id.textViewNombrePlatoDeslizante);
      	  			textViewNombrePlatoDeslizante.setText(platos.get(arg0).getNombrePlato());
	      		}
	      		 
	      	 });
	      	
	      	// Damos valor al campo texto del nombre del plato activo
	      	if(platos.size() > 0){
	      		TextView textViewNombrePlatoDeslizante = (TextView) findViewById(R.id.textViewNombrePlatoDeslizante);
	      		textViewNombrePlatoDeslizante.setText(platos.get(0).getNombrePlato());
	      	}else{
		         Toast.makeText(getApplicationContext(),"Debe haber realizado alg�n pedido para poder utilizar esta utilizad.",Toast.LENGTH_SHORT).show();
	      	}
  			
	    }catch(SQLiteException e){
	         Toast.makeText(getApplicationContext(),"ERROR AL ABRIR LA BD DE CUENTA EN LA PANTALLA CALCULADORA",Toast.LENGTH_SHORT).show();
	    }
	}
	
	public static void actualizaGridViewPersonas(){
		// Actualizamos el adapter
		gridViewPersonas.setAdapter(adapterGridViewCalculadora);
	}
}
