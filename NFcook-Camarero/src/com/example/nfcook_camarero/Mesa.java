package com.example.nfcook_camarero;

import java.util.ArrayList;

import baseDatos.HandlerGenerico;
import adapters.MiGridViewSeleccionarIngredientesPlato;
import adapters.PadreListMesa;
import adapters.HijoExpandableListEditar;
import adapters.MiExpandableListEditarAdapter;
import adapters.MiListMesaAdapter;
import adapters.PadreExpandableListEditar;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
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
public class Mesa extends Activity {

	private static HandlerGenerico sqlMesas,sqlMiBaseFav;
	private static String numMesa;
	private String idCamarero;
	private String numPersonas; 
	private static SQLiteDatabase dbMesas,dbMiBaseFav;
	private static ListView platos;
	private static ArrayList<PadreListMesa> elemLista;
	private static MiListMesaAdapter adapter;
	private static TextView precioTotal;
	private static Activity actividad;
	
	private View.OnTouchListener tocuhListener;
	private boolean moviendose;
	private View vista;
	private int inicial, itemId,XinicialEvento,YinicialEvento;
	private boolean noBorrar=false;
	private boolean noVolver=false;
	private boolean entrar=false;
	private boolean primeraVez,mueves;
	
	private	ArrayList<Boolean> ingredientesMarcadosBoolean;
    private ArrayList<String> ingredientesTotales;
	
	
	private AutoCompleteTextView actwObservaciones;
	
	
	private static MiExpandableListEditarAdapter adapterExpandableListEditarExtras;
	private static ExpandableListView expandableListEditarExtras;
	private static Context context;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pedidomesa);
		actividad = this;
		
		//Necesario para actualizar la lista de las mesas al a�adir un plato
		context = Mesa.this;
		
		//El numero de la mesa se obtiene de la pantalla anterior
		Bundle bundle = getIntent().getExtras();
		numMesa = bundle.getString("NumMesa");
		numPersonas = bundle.getString("Personas");
		idCamarero = bundle.getString("IdCamarero");
		
		ingredientesTotales = new ArrayList<String>();
		ingredientesMarcadosBoolean = new ArrayList<Boolean>();
		
		// Recogemos ActionBar
        ActionBar actionbar = getActionBar();
    	actionbar.setTitle(" MESA " + numMesa + ": PEDIDO ACTUAL");
    	actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0B3861")));
    	
    	// atras en el action bar
        actionbar.setDisplayHomeAsUpEnabled(true);
    	
		try{
			sqlMesas=new HandlerGenerico(getApplicationContext(), "Mesas.db");
			dbMesas= sqlMesas.open();
			
			//A�adir platos a la ListView----------------------------------------------------
	  	  	platos = (ListView)findViewById(R.id.listaPlatos);
		    elemLista = obtenerElementos();
		    
	  	    adapter = new MiListMesaAdapter(this, elemLista);
	  	     
	  	    precioTotal = (TextView)findViewById(R.id.precioTotal);
	  	    precioTotal.setText(Double.toString( Math.rint(adapter.getPrecio()*100)/100 )+" �");
	  	     
	  	    platos.setAdapter(adapter);

	  	    platos.setOnItemClickListener(new OnItemClickListener() {
	  	    	
	  	    	public void onItemClick(AdapterView<?> arg0, View vista,int posicion, long id){
	  	    	//Si un plato ya esta sincronizado no tiene sentido poder editarlo
					if(!Mesa.sincronizado(posicion)){
	  	    			AlertDialog.Builder ventanaEmergente = new AlertDialog.Builder(Mesa.this);
		  	    		ventanaEmergente.setNegativeButton("Cancelar", null);
		  	    		onClickBotonAceptarAlertDialog(ventanaEmergente, posicion);
		  				//onClickBotonCancelarAlertDialog(ventanaEmergente);
		  				View vistaAviso = LayoutInflater.from(Mesa.this).inflate(R.layout.ventana_emergente_editar_anadir_plato, null);
		  				expandableListEditarExtras = (ExpandableListView) vistaAviso.findViewById(R.id.expandableListViewExtras);
		  				TextView encabezadoDialog = (TextView) vistaAviso.findViewById(R.id.textViewEditarAnadirPlato);
		  				encabezadoDialog.setText("Editar Plato");
		  				TextView tituloPlato = (TextView) vistaAviso.findViewById(R.id.textViewTituloPlatoEditarYAnadir);
		  				//actwObservaciones = (AutoCompleteTextView) vistaAviso.findViewById(R.id.autoCompleteTextViewObservaciones);
		  				tituloPlato.setText(adapter.getNombrePlato(posicion));
		  				//actwObservaciones.setText(adapter.getObservacionesPlato(posicion));
		  				cargarExpandableListExtras(posicion);
		  				cargarGridViewIngredientes(posicion, vistaAviso);
		  				ventanaEmergente.setView(vistaAviso);
		  				ventanaEmergente.show();
					} else
						   Toast.makeText(getApplicationContext(),"Plato ya sincronizado",Toast.LENGTH_SHORT).show();
					
	  	    	}
	  	    });
	  	    
	  	    //Esta linea llama al evento onTouch de abajo
	  	    tocuhListener = new View.OnTouchListener() {
	  	    	
	  	    	public boolean onTouch(View v, MotionEvent event) {
	            	switch (event.getAction() ) {
	            		case MotionEvent.ACTION_DOWN:
	            			mueves=false;
	            			primeraVez=true;
	            			noVolver=false;
	            			XinicialEvento=(int) event.getRawX();
	            			YinicialEvento=(int) event.getRawY();
	            			
	            			itemId = platos.pointToPosition((int) event.getX(), (int) event.getY());
	        	        	int pos=itemId-platos.getFirstVisiblePosition();
	        	        	vista= platos.getChildAt(pos);
	        	        	
	        	        	int[] coord=new int[2];//Vista
        		        	vista.getLocationOnScreen(coord);
        		        	inicial=coord[0];
        		        	
        		        	//Si le pones un return true no funciona el onclick
        		        	break;
	        	        	
	            		case MotionEvent.ACTION_UP:
	            			if(mueves){
		            			if(!noBorrar){
			            			if(moviendose){	            				
			            				WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			            				Display display = wm.getDefaultDisplay();
			            				int ancho = display.getWidth()/2;
				            			if (event.getX()>inicial && event.getX() - XinicialEvento > ancho && event.getX() > XinicialEvento){
				            				//Calculas para ver si donde levantas el dedo es la misma vista de donde empezaste
			            		        	int altoVista=vista.getHeight();
			            		        	int[] coordenadas=new int[2];//Vista
			            		        	vista.getLocationOnScreen(coordenadas);//Guarda X e Y
			            		        	int finVista=coordenadas[1]+altoVista;
			            		        	
			            		        	//Entras si sigues con el dedo en la misma vista
			            		        	if(event.getRawY()>coordenadas[1] && event.getRawY()<finVista){
			            		        		try{
				            		        		PadreListMesa platoSeleccionado = (PadreListMesa)adapter.getItem(itemId);
				            		        		String identificador = Integer.toString(platoSeleccionado.getIdRepetido());
			            	    				
				            	    				try{
				            	    					sqlMesas=new HandlerGenerico(context, "Mesas.db");
				            	    					dbMesas= sqlMesas.open();
				            	    					
				            	    					dbMesas.delete("Mesas", "IdUnico=?",new String[]{identificador});
				            	    				}catch(Exception e){
				            	    					System.out.println("Error borrar de la base pedido en up");
				            	    				}
			            	    				
			            	    				
			            	    				//Resta 1 a la cantidad de esa posicion del adapter
			            	    				adapter.deletePosicion(itemId);
			            	    				
			            	    				//Pones que esa vista tenga una distancia de la izquierda de 0 porque si no la 
			            	    				//siguiente vista la coloca con el margen de donde estuviera el raton al levantar
			            	    				vista.setTranslationX(0);
			            	    				
			            	    				actualizaListPlatos();//FIXME o esto: adapter.notifyDataSetChanged();
			            	    				//Notificas que has borrado un elemento del adapter y que repinte la lista
			            	    							            	    				
			            	    				//Recalculamos el precio(ser� cero ya que no quedan platos en la lista)
			            	    				precioTotal.setText(Double.toString( Math.rint( adapter.getPrecio()*100 )/100) +" �");
			            		        	}catch(Exception e){
			            	    				System.out.println("Acceso fuera de rango, al borrar con deletePosicion(itemId) ");
			            		        	}  
			            	            		}
				            		     }else{
				            		    	 //Si no se ha borrado, se devuelve a su sitio
				            				 vista.setTranslationX(0);
				            			 }
			            			}
			            			if(noVolver)
			            				//Si has activado el scroll piedes la seleccion de la vista
			            				noVolver=false;
		            			}
	            			}else
	            				//Si no pasas por el ACTION_MOVE, es un click, asique permites devolviendo false que ejecute el onClick
		                        return false;
	            			
	            			break;
	            		  
	            		case MotionEvent.ACTION_MOVE:
	            			mueves=true;
	            			if(primeraVez){
	            				
	            				int x,y;
	            				y = (int) Math.abs(event.getRawY()-YinicialEvento);
	            				x = (int) Math.abs(event.getRawX()-XinicialEvento);
	            				
	            				if(y>x)
	            					entrar = false;//No hacerlo
	            				else
	            					entrar = true;
	            			}
	            			
	            			if(entrar){
	            				primeraVez=false;
		            			try{
		            				int xActual=(int) event.getRawX();
		            				int desplazamiento=XinicialEvento-xActual;
		            				
		            				int altoV=vista.getHeight();
	            		        	int[] coordena=new int[2];//Vista
	            		        	vista.getLocationOnScreen(coordena);//Guarda X e Y
	            		        	int finVis=coordena[1]+altoV;
	            		        	
	            		        	//Desplazas la vista si no te sales de ella verticalmente, si es la primera vez y si no has activado el scroll
			            			if(!moviendose && !(event.getRawY()<coordena[1]) && !(event.getRawY()>finVis) && !noVolver){
			            				moviendose=true;
			            				noBorrar=false;
			            				
			            				if(desplazamiento<0){//Si mueves el dedo a la derecha
				            				vista.setTranslationX(event.getRawX()-XinicialEvento);
				            				
				            				
				            			}
				            			else if(desplazamiento>0 && vista.getTranslationX()==0)
				            				vista.setTranslationX(0);
				            			
				        	        
			        	        	//Si te sales de la vista, devuelves false para que lo procese el metodo(el que 
			        	        	//activa el scroll) y pierdes el poder desplazar la vista(y la devuelves a su sitio)
			            			}else if( event.getRawY()<coordena[1] || event.getRawY()>finVis ){	 
			            				vista.setTranslationX(0);
			            				noBorrar=true;
			            				noVolver=true;
			            				return false;
			            			
			            			//Si no has perdido la vista, la mueves horizontalmente
			            			}else if(!noVolver){
			            				noBorrar=false;
			            				
			            				if(desplazamiento<=0)//Si desplazas a la derecha
				            				vista.setTranslationX(event.getRawX()-XinicialEvento);
			            				else if(desplazamiento>0 && vista.getTranslationX()==0)
				            				vista.setTranslationX(0);
				            			else if(desplazamiento>0)
				            				vista.setTranslationX(event.getRawX()-XinicialEvento);
				            			else if(desplazamiento==0)
				            				vista.setTranslationX(event.getRawX());
			            				
			            			}
		            			
		            			}catch(Exception e){
		            				System.out.println("catch MOVE");	
		            			}
	            			}else
	            				return false;//Consigues que actue el scroll 
	            			break;
	            	}
	            	return true;//Si esta a false, se pone a la vez scroll y desplazamiento horiz. de la vista
	            }
	        };
	        
	        platos.setOnTouchListener(tocuhListener);
	    
		}catch(Exception e){
			System.out.println("Error lectura base de datos de Pedido");
		}
			
		//Boton A�adirPlato---------------------------------------------------------------
		LinearLayout aniadirPlato = (LinearLayout)findViewById(R.id.linearLayoutaAniadirPlato);
		aniadirPlato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	try{
            		Intent intent = new Intent(actividad, AnadirPlatos.class);
            		//Le pasamos a la siguiente pantalla el numero de la mesa que se ha pulsado
            		intent.putExtra("NumMesa", numMesa);
            		intent.putExtra("IdCamarero",idCamarero);
            		intent.putExtra("Personas", numPersonas);
            		intent.putExtra("Restaurante", MainActivity.restaurante);
            		startActivity(intent);
            		
            	}catch(Exception e){
            		System.out.println("Error funcionalidad de boton A�adirPlato");
            		
            	}
            }
        });		
		
		//Boton A�adirBebida--------------------------------------------------------------
		LinearLayout aniadirBebida = (LinearLayout)findViewById(R.id.linearLayoutaAniadirBebida);
		aniadirBebida.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	try{
        		    Intent intent = new Intent(actividad, AnadirBebida.class);
            		intent.putExtra("NumMesa", numMesa);
            		intent.putExtra("IdCamarero",idCamarero);
            		intent.putExtra("Personas", numPersonas);
            		intent.putExtra("Restaurante", MainActivity.restaurante);
            		startActivity(intent);
            		
            	}catch(Exception e){
            		System.out.println("Catch funcionalidad de boton A�adirBebida "+e.getMessage()+
            				"LOcalizacion: "+e.getCause());
            		
            	}
            }
        });
		//Boton A�adirBebida--------------------------------------------------------------
	}

	
	private static boolean sincronizado(int pos) {
		importarBaseDatatosMesa();
    	
		String[] campos = new String[]{"Sincro"};
    	
    	String[] numeroDeMesa = new String[]{numMesa,String.valueOf(adapter.getIdPlatoUnico(pos))};
    	Cursor c = dbMesas.query("Mesas",campos, "NumMesa=? AND IdUnico=?",numeroDeMesa, null,null, null);
    	c.moveToNext();
    	if(c.getInt(0) == 1)
    		return true;
    	else 
    		return false;	        				
	}

	/**
	 * Obtiene los elementos del aArrayList, en funcion de la base de datos y del contenido 
	 * de la mesa actual, que servir� para confeccionar el adapter de la ListView.
	 * 
	 * @return un ArrayList con los elementos de la mesa actual.
	 */
	private static ArrayList<PadreListMesa> obtenerElementos() {
		ArrayList<PadreListMesa> elementos=null;
		try{
			String[] campos = new String[]{"Nombre","Ingredientes","Extras","Precio","IdUnico","IdPlato","Sincro"};
		    String[] numeroDeMesa = new String[]{numMesa};
		    
		    Cursor c = dbMesas.query("Mesas",campos, "NumMesa=?",numeroDeMesa, null,null, null);
		    
		    elementos = new ArrayList<PadreListMesa>();
		    
		    boolean primero = true;
		    
		    while(c.moveToNext()){
		    	if(primero){

		    		elementos.add(new PadreListMesa(c.getString(0), c.getString(2), c.getString(1), Double.parseDouble(c.getString(3)),c.getInt(4),c.getString(5),c.getInt(6)));

		    		primero=false;
		    	}else{
		    		int i = 0;
		    		boolean repetido = false;
		    		while(i<elementos.size() && !repetido){
	    				String n = elementos.get(i).getNombre();
	    				String e = elementos.get(i).getExtras();	    				
	    				int sincronizado = elementos.get(i).getSincronizado();
	
	    				String o = elementos.get(i).getObservaciones();
	    				
			    		if( n.equals(c.getString(0)) &&
			    			e.equals(c.getString(2)) &&
			    			o.equals(c.getString(1)) &&
			    			sincronizado == c.getInt(6) ){
			    				repetido = true;
			    				elementos.get(i).sumaCantidad();//Le sumas 1 a ese elemento del array que esta repetido
			    				elementos.get(i).aniadeId(Integer.parseInt(c.getString(4)));
			    		} else
			    			i++;
		    		}
		    		if(!repetido){
		    			elementos.add(new PadreListMesa(c.getString(0), c.getString(2), c.getString(1),Double.parseDouble(c.getString(3)),c.getInt(4),c.getString(5),c.getInt(6)));
		    		}
		    	}
		    	
		    }
		    	
		    return elementos;
		    
		}catch(Exception e){
			System.out.println("Error en obtenerElementos");
			return elementos;
		}
	}
	

	protected void onClickBotonAceptarAlertDialog(Builder ventanaEmergente,final int posicion) {
		
		ventanaEmergente.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				
					importarBaseDatatosMesa();
					String nuevosExtrasMarcados = "";
					
					String ingredientesStr = "";
					//String ingredientesBinarios = null;
					
					if(adapterExpandableListEditarExtras != null){ // El plato tiene extras
						nuevosExtrasMarcados = adapterExpandableListEditarExtras.getExtrasMarcados();
					}else{ // No es un plato con extras
						nuevosExtrasMarcados = "Sin guarnici�n";
					}
			    	
			    	if(ingredientesMarcadosBoolean.size() > 0){
						//ingredientesBinarios = "";
						for(int i=0; i<ingredientesMarcadosBoolean.size(); i++){
							if(!ingredientesMarcadosBoolean.get(i)) // == true
								//ingredientesBinarios += "1";
							//else{						   // == false
								//ingredientesBinarios += "0";
								ingredientesStr += ingredientesTotales.get(i) + ", sin ";
							//}
						}
						if (ingredientesStr.equals("")){
							ingredientesStr = "Con todos los ingredientes";
						} else {
							ingredientesStr = "Sin " + ingredientesStr.substring(0, ingredientesStr.length()-6).toLowerCase();
						}    			
					}else{
						ingredientesStr = "No hay ingredientes definidos";
					}
			    	
			    	if(nuevosExtrasMarcados==null)
						nuevosExtrasMarcados="Sin guarnici�n";
					
			    	ContentValues platoEditado = new ContentValues();
			    	platoEditado.put("Extras", nuevosExtrasMarcados);
			    	platoEditado.put("Ingredientes", ingredientesStr);
			    	
			        String[] camposUpdate = {numMesa,adapter.getIdPlato(posicion),String.valueOf(adapter.getIdPlatoUnico(posicion))};
			        
			        dbMesas.update("Mesas", platoEditado, "NumMesa=? AND IdPlato =? AND IdUnico=?", camposUpdate);
					
			        sqlMesas.close();
					
			        actualizaListPlatos();
			   }

			
		});
		
	}
	
	
	public static void importarBaseDatatosMesa(){
		try{
			sqlMesas=new HandlerGenerico(actividad, "Mesas.db");
			dbMesas= sqlMesas.open();
		}catch(SQLiteException e){
		 	Toast.makeText(actividad,"NO EXISTE BASE DE DATOS MESA",Toast.LENGTH_SHORT).show();
		}	
	}
	
	public void cargarExpandableListExtras(int posicion){
		HandlerGenerico sqlMiBase=new HandlerGenerico(getApplicationContext(), "MiBase.db");
		SQLiteDatabase dbMiBase= sqlMiBase.open();
  		String[] campos = new String[]{"Extras"};
  		String[] datos = new String[]{adapter.getIdPlato(posicion)};
  		
  		Cursor cursor = dbMiBase.query("Restaurantes",campos,"Id =?",datos,null,null,null); 
  		cursor.moveToFirst();
  		
  		String extrasPlato = cursor.getString(0);
  		String extrasMarcados = adapter.getExtrasMarcados(posicion);
  		  		
  		if(!extrasPlato.equals("")){
  			String[] tokensExtrasMarcados = extrasMarcados.split(",");
  			String[] tokens = extrasPlato.split("/");
	            ArrayList<PadreExpandableListEditar> categoriasExtras =  new ArrayList<PadreExpandableListEditar>();
		        for(int i= 0; i< tokens.length ;i++){
		        	String[] nombreExtra = null;
		        	String extraSeleccionadoPradreI = tokensExtrasMarcados[i];
					try{
						nombreExtra = tokens[i].split(":");
						
						String categoriaExtraPadre = nombreExtra[0];
						
						// Creamos los hijos, ser�n la variedad de extras
						String[] elementosExtra = null;

						elementosExtra = nombreExtra[1].split(",");
						
						ArrayList<HijoExpandableListEditar> variedadExtrasListaHijos = new ArrayList<HijoExpandableListEditar>();
						ArrayList<String> extrasHijo = new ArrayList<String>();
						boolean[] extrasPulsados = new boolean[elementosExtra.length];
						for(int j=0; j<elementosExtra.length;j++)
						{
							if(extraSeleccionadoPradreI.contains(elementosExtra[j])){
								extrasPulsados[j] = true;
							}else{
								extrasPulsados[j] = false;
							}
							extrasHijo.add(elementosExtra[j]);
						}
						HijoExpandableListEditar extrasDeUnaCategoria = new HijoExpandableListEditar(extrasHijo, extrasPulsados);
						// A�adimos la informaci�n del hijo a la lista de hijos
						variedadExtrasListaHijos.add(extrasDeUnaCategoria);
						PadreExpandableListEditar padreCategoriaExtra = new PadreExpandableListEditar(adapter.getIdPlato(posicion),categoriaExtraPadre, variedadExtrasListaHijos);
						if(i==0){//Expandimos el primer padre por estetica
							padreCategoriaExtra.setExpandido(true);
						}
						// A�adimos la informaci�n del padre a la lista de padres
						categoriasExtras.add(padreCategoriaExtra);
					}catch(Exception e){
						Toast.makeText(getApplicationContext(),"Error en el formato de extra en la BD", Toast.LENGTH_SHORT).show();
					}
				}
		        // Creamos el adapater para adaptar la lista a la pantalla.
		    	adapterExpandableListEditarExtras = new MiExpandableListEditarAdapter(getApplicationContext(), categoriasExtras,1);
		        expandableListEditarExtras.setAdapter(adapterExpandableListEditarExtras);  
  		}else{
  			//Actualizamos el adapter a null, ya que es static, para saber que este plato no tiene extras.
  			adapterExpandableListEditarExtras = null;
  			expandableListEditarExtras.setVisibility(ExpandableListView.INVISIBLE);
  		}
	}
	
	public void cargarGridViewIngredientes(int posicion, View vistaVentanaEmergente){
		HandlerGenerico sqlMiBase=new HandlerGenerico(getApplicationContext(), "/data/data/com.example.nfcook_camarero/databases/", "MiBase.db");
		SQLiteDatabase dbMiBase= sqlMiBase.open();
  		String[] campos = new String[]{"Ingredientes"};
  		String[] datos = new String[]{adapter.getIdPlato(posicion)};
  		
  		Cursor cursor = dbMiBase.query("Restaurantes",campos,"Id =?",datos,null,null,null); 
  		cursor.moveToFirst();
  		
  		String ingredientesPlatoCadena = cursor.getString(0);
  		String ingredientesMarcadosCadena = adapter.getObservacionesPlato(posicion);

        String[] ingredientesMarcadosString = ingredientesMarcadosCadena.split(",");
        ingredientesMarcadosBoolean = new ArrayList<Boolean>();
        ingredientesTotales = new ArrayList<String>();
        
        if (!ingredientesPlatoCadena.equals("")){
   
        	if (ingredientesMarcadosCadena.equals("Con todos los ingredientes")){
		    	String[] tokens = ingredientesPlatoCadena.split("%");
		        for (int i=0; i<tokens.length; i++){
		        	ingredientesTotales.add(tokens[i]);
		        	ingredientesMarcadosBoolean.add(true);
		        }
        	}else{
        		String[] tokens = ingredientesPlatoCadena.split("%");
        		for (int i=0; i<tokens.length; i++){
        			ingredientesTotales.add(tokens[i]);
       
        			int j = 0, espacio = 0; // espacios es para comparar sin espacios
        			boolean marcado = false;
        			while(!marcado && j <ingredientesMarcadosString.length){
        				
        				if (tokens[i].toLowerCase().equals(ingredientesMarcadosString[j].substring(4 + espacio)))
        					marcado = true;
        				else 
        					j++;
        				if (j == 1) espacio = 1;
        			}
        			if (marcado)
        				ingredientesMarcadosBoolean.add(false);
        			else 
        				ingredientesMarcadosBoolean.add(true);
        		}
        	}
   
        }
        GridView gridViewIngredientes = (GridView) vistaVentanaEmergente.findViewById(R.id.gridViewIngredientes);
		MiGridViewSeleccionarIngredientesPlato miGridViewSeleccionarIngredientesPlato = new MiGridViewSeleccionarIngredientesPlato(Mesa.this, ingredientesTotales, ingredientesMarcadosBoolean);
		gridViewIngredientes.setAdapter(miGridViewSeleccionarIngredientesPlato);
	}
	
	public static void actualizaListPlatos(){
		try{
			importarBaseDatatosMesa();						
			elemLista = obtenerElementos();
	        adapter = new MiListMesaAdapter(actividad, elemLista);   
	        platos.setAdapter(adapter);	        
			sqlMesas.close();			
			precioTotal.setText( Math.rint(adapter.getPrecio()*100)/100 +" �");
			
		}catch(Exception e){
			System.out.println("Error en actualizaListPlatos al leer la base dbMesas");
		}
	}

	public static void actualizaExpandableList() {
		expandableListEditarExtras.setAdapter(adapterExpandableListEditarExtras);
	}

	public static void expandeGrupoLista(int groupPositionMarcar) {
		expandableListEditarExtras.expandGroup(groupPositionMarcar);
	}
	
	public static MiListMesaAdapter getAdapter(){
		return adapter;
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

	public static void actualizarNumeroVecesPlatoPedido(String id) {
		try{
			sqlMiBaseFav = new HandlerGenerico(actividad, "MiBaseFav.db");
			dbMiBaseFav = sqlMiBaseFav.open();
			
			String[] campo = new String[]{"VecesPedido"};//Consultas el numero de veces que ya se ha pedido
    		String[] info = new String[]{MainActivity.restaurante, id};//En un restaurante concreto y con un IdPlato 
    		
      		Cursor campoVeces = dbMiBaseFav.query("Restaurantes",campo,"Restaurante=? AND Id=?",info,null,null,null); 
      		campoVeces.moveToFirst();
			
      		String veces = campoVeces.getString(0);
      		if(veces==null)
      			veces=Integer.toString(0);
      		String nuevoVeces = Integer.toString( Integer.parseInt(veces) + 1 );
			
      		ContentValues platoAumentarVeces = new ContentValues();
      		platoAumentarVeces.put("VecesPedido", nuevoVeces);
      		String[] camposUpdate = {MainActivity.restaurante,id};
      		dbMiBaseFav.update("Restaurantes", platoAumentarVeces, "Restaurante=? AND Id=?", camposUpdate);
				
      		dbMiBaseFav.close();
      		
			
		}catch(SQLiteException e){
			System.out.println("CATCH abrir MiBaseFav en A�adirPlatos.java");
			Toast.makeText(actividad,"NO EXISTE LA BASE DE DATOS",Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public static void pintarBaseDatosMiFav() {
		try{
			sqlMiBaseFav = new HandlerGenerico(actividad, "MiBaseFav.db");
			dbMiBaseFav = sqlMiBaseFav.open();
        	
			String[] campos = new String[]{"Nombre","VecesPedido"};
        	
        	String[] restau = new String[]{MainActivity.restaurante};
        	Cursor c = dbMiBaseFav.query("Restaurantes",campos, "Restaurante=?",restau, null,null, null);
        	
        	while(c.moveToNext()){
		    	String v=c.getString(1);
		    	if (v==null)
		    		v="0";
		    }
		}catch(Exception e){
			System.out.println("Error en pintarBaseDatosMesaFav"+e.getCause()+" "+e.getLocalizedMessage()+" "+e.getStackTrace());
		}
				
			}
	
	/*Metemos en el action bar un boton para sincronizar la mesa*/
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mesamenu, menu);
        
        return true;
    }
    
    /*Metodo que realiza la accion del boton introducido en el ActionBar (Sincronizar)*/
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.botonNFC) {
    		Intent intent = new Intent(this,SincronizarTpv.class);
    		intent.putExtra("Restaurante", MainActivity.restaurante);
    		intent.putExtra("Mesa", numMesa);
    		intent.putExtra("Personas", numPersonas);
    		startActivityForResult(intent,0);
    		
    		
    		
    	} else finish();
    	return false;	
    }
    public void onActivityResult(int requestCode, int resultCode,Intent data)
    {
    	
    	
    }
    /*Metodo que actualiza el campo Sincro de la base de datos Mesas tras sincronizar el pedido con el TPV*/
    public static void actualizarSincronizadosBaseMesas() {
    	try{
    		importarBaseDatatosMesa();
    		/*sqlMesas=new HandlerGenerico(actividad, "/data/data/com.example.nfcook_camarero/databases/", "Mesas.db");
			dbMesas= sqlMesas.open();*/
			
			for(int i=0;i<adapter.getCount();i++){
				PadreListMesa posAdapter = (PadreListMesa)adapter.getItem(i);
				
				/*En caso de que haya varios platos iguales(agrupados) tenemos que recorrer el array de repetidos e ir poniendo
				a 1 el campo Sincro de cada uno en la base de datos*/
				if(posAdapter.getCantidad() > 1){
					for(int j=0;j<posAdapter.getCantidad();j++){
						
						int idUnico = posAdapter.getIdRepetido(j);
						
						String[] info = new String[]{Integer.toString(idUnico),numMesa};//En un restaurante concreto y con un IdPlato 
			    		Cursor campoSincro = dbMesas.query("Mesas",null,"IdUnico=? AND NumMesa=?",info,null,null,null); 
			    		campoSincro.moveToFirst();
			    		
			    		ContentValues platoSincronizado = new ContentValues();
			      		platoSincronizado.put("Sincro", 1);
			      		String[] camposUpdate = {Integer.toString(idUnico),numMesa};
			      		dbMesas.update("Mesas", platoSincronizado, "IdUnico=? AND NumMesa=?", camposUpdate);
						
					}
				//En caso de que haya un unico plato(sin agrupar), solo actualizamos su campo Sincro en la base Mesas
				}else{
					int idUnico = posAdapter.getId();//Devuelve el id de la posicion 0 del array de repetidos(el unico que hay)
					
					String[] info = new String[]{Integer.toString(idUnico),numMesa};//En un restaurante concreto y con un IdPlato 
		    		Cursor campoSincro = dbMesas.query("Mesas",null,"IdUnico=? AND NumMesa=?",info,null,null,null); 
		    		campoSincro.moveToFirst();
		    		
		    		ContentValues platoSincronizado = new ContentValues();
		      		platoSincronizado.put("Sincro", 1);
		      		String[] camposUpdate = {Integer.toString(idUnico),numMesa};
		      		dbMesas.update("Mesas", platoSincronizado, "IdUnico=? AND NumMesa=?", camposUpdate);
					
				}
	    	}
			dbMesas.close();
			
    	}catch(SQLiteException e){
			System.out.println("CATCH abrir actualizarSincronizadosDelAdapter() en Mesa.java");
			Toast.makeText(actividad,"NO EXISTE LA BASE DE DATOS",Toast.LENGTH_SHORT).show();
		}
    }
    
}
