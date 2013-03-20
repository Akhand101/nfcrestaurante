package usuario;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import baseDatos.HandlerDB;

import com.example.nfcook.R;

import fragments.ContenidoTabSuperiorCategoriaBebidas;
import fragments.CuentaFragment;
import fragments.MiTabsSuperioresListener;
import fragments.PantallaInicialRestaurante;
import fragments.PedidoFragment;
import fragments.ContenidoTabsSuperioresFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Esta clase es la encargada de cargar toda la informaci�n del restaurarnte que hemos seleccionado.
 * 
 * Carga los tabs superiores con las distintas categor�as que ofreza el restaurante.
 * Carga el logo y el texto de bienvenida del restaurante.
 * Carga los tabs inferiores, que ser�n comunes para todos los restaurantes.
 * @author Abel
 *
 */
public class InicializarRestaurante extends Activity implements TabContentFactory, OnTabChangeListener{
	
	private ImageView imagenRestaurante;
	private String restaurante;
	
	// Base de datos
	private HandlerDB sql;
	private SQLiteDatabase db;
	
	// Actionbar para los tabs superiores con las categr�as de los platos
	private ActionBar actionbar;
	
	// Tabs inferiores con las funcionalidades de la aplicacion
	private TabHost tabs;
	// Vista de los tabs inferiores
	private View tabInferiorContentView;
	
	// Informaci�n de que tab est� seleccionado
	private static String tabInferiorPulsado;
	private static boolean pulsadoTabSuperior;
	
	//Ventana emergente
	public AlertDialog ventanaEmergente;
	
	public static void setPulsadoTabSuperior(boolean pulsado){
		pulsadoTabSuperior = pulsado;
	}
	
	public static void setTabInferiorPulsado(String tab){
		tabInferiorPulsado = tab;
	}	
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenedor_tabs);

        // Cargamos el logo del restaurante en el layout y asignamos el nombre del restaurante
        imagenRestaurante = (ImageView)findViewById(R.id.imageViewLogoRestaurante);
        Bundle bundle = getIntent().getExtras();
	    imagenRestaurante.setImageResource(bundle.getInt("logoRestaurante"));
		restaurante = bundle.getString("nombreRestaurante");
		
		// Importamos la base de datos
		importarBaseDatatos();
	 
		// Inicializamos y cargamos Tabs superiores que contendr� el actionBar
		inicializarActionBarConTab();
		cargarTabsSuperiores();
		
		// Cerramos la base de datos
		sql.close();
		
		// Inicializamos y cargamos Tabs inferiores
		inicializarTabsInferiores();
		cargarTabsInferiores();
		
		// Inicializamos el control de los tabs
		tabInferiorPulsado = tabs.getCurrentTabTag();
		pulsadoTabSuperior = false;
		
    }
    
    /* Metodo encargado de implementar el bot�n back.
     * De la pantalla de navegaci�n de platos, si se pulsa back, volver� a la pantalla 
     * de selecci�n de restaurantes.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Si pulsamos el bot�n back
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
           finish();
        }
        return super.onKeyDown(keyCode, event);
    }
     
    // Importamos la base de datos
    private void importarBaseDatatos(){
        try{
        	sql = new HandlerDB(getApplicationContext()); 
        	db = sql.open();
        }catch(SQLiteException e){
         	Toast.makeText(getApplicationContext(),"NO EXISTE",Toast.LENGTH_SHORT).show();
        }	
	}
    
    // Metodo encargado de inicializar el actionBar y ponerlo en modo tabs
    private void inicializarActionBarConTab(){
    	// Recogemos ActionBar
        actionbar = getActionBar();
        
        // Seleccionamos el modo tabs en el actionBar
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }
    
    // Metodo encargado crear los tabs superiores con la informacion referente a las categorias del restaurante
	private void cargarTabsSuperiores(){
    	Stack<String> tipos = new Stack<String>();
    	
    	// Obtenemos las distintas categor�as de platos que hay
    	try{
    		String[] campos = new String[]{"Categoria"};
	    	String[] datos = new String[]{restaurante};
	    	Cursor c = db.query("Restaurantes", campos, "Restaurante=?", datos,null, null,null);
	    	
	    	// Recorremos todos los registros
	    	while(c.moveToNext()){
	    		String tipo = c.getString(0);
	    		if(!tipos.contains(tipo))
	    			tipos.add(tipo);
	    	} 

	    }catch(SQLiteException e){
	        Toast.makeText(getApplicationContext(),"ERROR BASE DE DATOS -> TABS",Toast.LENGTH_SHORT).show();	
	    }
    	
    	Stack<String> pilaTipos = new Stack<String>();
    	
    	// Solo sirve para mostrar bien los nombres de los tabs (para que salgan primero los principales...)
    	while (!tipos.isEmpty()){
    		pilaTipos.add(tipos.pop());
    	}
    	
    	// Vamos a�adiendo cada categor�a a los tabs
    	String tipoTab="";
    	while (!pilaTipos.isEmpty()){
    		// Sacamos el nombre de la categor�a
    		tipoTab = pilaTipos.pop();
    		// Creamos el tab
    		ActionBar.Tab tab = actionbar.newTab().setText(tipoTab);
    		// Creamos el fragment de cada tab y le metemos el restaurante al que pertenece
    		// Miramos si se trata de la categor�a bebidas que tendr� un fragment distinto
    		if(tipoTab.toLowerCase().equals("bebidas")){
    			Fragment tabFragment = new ContenidoTabSuperiorCategoriaBebidas();
	    		ContenidoTabSuperiorCategoriaBebidas.setTipoTab(tipoTab);
	    		ContenidoTabSuperiorCategoriaBebidas.setRestaurante(restaurante);
	    		// Hacemos oyente al tab
	    		tab.setTabListener(new MiTabsSuperioresListener(tabFragment,tipoTab));
    		}else{
	    		Fragment tabFragment = new ContenidoTabsSuperioresFragment();
	    		((ContenidoTabsSuperioresFragment) tabFragment).setTipoTab(tipoTab);
	    		((ContenidoTabsSuperioresFragment) tabFragment).setRestaurante(restaurante);
	    		// Hacemos oyente al tab
	    		tab.setTabListener(new MiTabsSuperioresListener(tabFragment,tipoTab));
    		}
    		// A�adimos dicha categor�a como tab
    		actionbar.addTab(tab);
    	}
    	
    	// Quitamos la barra del actionbar
    	/*
    	 * TODO En un futuro incorporaremos funciones al actionbar y habr� que habilitarla de buevo
    	 */
    	actionbar.setDisplayShowHomeEnabled(false);
    	actionbar.setDisplayShowTitleEnabled(false);
    	actionbar.setDisplayUseLogoEnabled(false);
	}
    
    // Metodo encargado de inicializar los tabs inferiores
    private void inicializarTabsInferiores(){
    	// Creamos los tabs inferiores y los inicializamos
		tabs = (TabHost)findViewById(android.R.id.tabhost);
		// Les hacemos oyentes
		tabs.setOnTabChangedListener(this);
        tabs.setup();
    }
    
    // Metodo encargado crear los tabs inferiores con las funcionalidades que ofrecemos al usuario
    private void cargarTabsInferiores(){
    	// Creamos el tab1 --> Inicio
        TabHost.TabSpec spec = tabs.newTabSpec("tabInicio");
        // Hacemos referencia a su layout correspondiente
        spec.setContent(R.id.tab1);
        // Preparamos la vista del tab con el layout que hemos preparado
        spec.setIndicator(prepararTabView(getApplicationContext(),"tabInicio"));
        // Lo a�adimos
        tabs.addTab(spec);
        
        // Creamos el tab2 --> Promociones
        spec = tabs.newTabSpec("tabPromociones");
        spec.setContent(R.id.tab2);
        spec.setIndicator(prepararTabView(getApplicationContext(),"tabPromociones"));
        tabs.addTab(spec);
        
        // Creamos el tab3 --> Pedido a sincronizar
        spec = tabs.newTabSpec("tabPedidoSincronizar");
        spec.setContent(R.id.tab3);
        spec.setIndicator(prepararTabView(getApplicationContext(),"tabPedidoSincronizar"));
        tabs.addTab(spec);
        
        // Creamos el tab4 --> Cuenta
        spec = tabs.newTabSpec("tabCuenta");
        spec.setContent(R.id.tab4);
        spec.setIndicator(prepararTabView(getApplicationContext(),"tabCuenta"));
        tabs.addTab(spec);
        
        // Creamos el tab5 --> Calculadora
        spec = tabs.newTabSpec("tabCalculadora");
        spec.setContent(R.id.tab5);
        spec.setIndicator(prepararTabView(getApplicationContext(),"tabCalculadora"));
        tabs.addTab(spec);
        
        int numeroTabs = tabs.getTabWidget().getChildCount();
        for(int i=0; i<numeroTabs; i++){
            tabs.getTabWidget().getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
            	/* Implementamos la acci�n de cada tab cuando se pinche en �l
            	 * Es distinto del onTabChange, puesto que au�o entra si pulsamos 
            	 * sobre un tab ya seleccionado.
            	 */
            	public boolean onTouch(View v, MotionEvent event){
            		// Cuando pulsamos el tab
            		if(event.getAction()==MotionEvent.ACTION_UP){
            			if(tabs.getCurrentTabTag().equals("tabInicio")){
	                		/*
	            			 * TODO Completar funcionalidad de la pantalla de bienvenida
	            			 */
	        				// Cargamos en el fragment de la pantalla de bienvenida del restaurante
	        				Fragment fragmentPantallaInicioRes = new PantallaInicialRestaurante();
	        				((PantallaInicialRestaurante)fragmentPantallaInicioRes).setRestaurante(restaurante);
	        		        FragmentTransaction m = getFragmentManager().beginTransaction();
	        		        m.replace(R.id.FrameLayoutPestanas, fragmentPantallaInicioRes);
	        		        m.commit();
            			}else if(tabs.getCurrentTabTag().equals("tabPromociones")){
							Intent intent = new Intent(Intent.ACTION_VIEW);
							if(restaurante.equals("Foster")){
								intent.setData(Uri.parse("http://www.elchequegorron.es/"));
							}else if(restaurante.equals("vips")){
								intent.setData(Uri.parse("http://www.vips.es/promociones"));
							}
	                		startActivity(intent);
	        			}else if(tabs.getCurrentTabTag().equals("tabPedidoSincronizar")){
	        				Fragment fragmentPedido = new PedidoFragment();
	        				PedidoFragment.setRestaurante(restaurante);
	        		        FragmentTransaction m = getFragmentManager().beginTransaction();
	        		        m.replace(R.id.FrameLayoutPestanas, fragmentPedido);
	        		        m.commit();
	        			}else if(tabs.getCurrentTabTag().equals("tabCuenta")){
	        				Fragment fragmentCuenta = new CuentaFragment();
	        				((CuentaFragment) fragmentCuenta).setRestaurante(restaurante);
	        		        FragmentTransaction m = getFragmentManager().beginTransaction();
	        		        m.replace(R.id.FrameLayoutPestanas, fragmentCuenta);
	        		        m.commit();
	        			}else if(tabs.getCurrentTabTag().equals("tabCalculadora")){
	        				/*
	        				 * TODO Hacer su layout y su funcionalidad
	        				 * Actualmente se muestra un aviso de que la secci�n no se encuentra disponible a�n
	        				 */
	        			}
            		}
            		return false;
                }
            });
        }
    }
    
    // Metodo encargado de preparar las vistas de cada tab inferior
    private View prepararTabView(Context context, String nombreTab){
    	// Cargamos el layout
    	tabInferiorContentView = LayoutInflater.from(context).inflate(R.layout.tabs_inferiores, null);
    	// Cargamos el icono del tab
    	ImageView imagenTab = (ImageView)tabInferiorContentView.findViewById(R.id.imageViewTabInferior);
    	// Cargamos el titulo del tab
    	TextView textoTab = (TextView)tabInferiorContentView.findViewById(R.id.textViewTabInferior);
    	textoTab.setTextColor(Color.BLACK);
    	// Asignamos el t�tulo e icono para cada tab
    	if(nombreTab.equals("tabInicio")){
     		textoTab.setText("Inicio");
     		imagenTab.setImageResource(getResources().getIdentifier("inicio","drawable",this.getPackageName()));
     	}else if(nombreTab.equals("tabPromociones")){
    		textoTab.setText("Promociones");
    		imagenTab.setImageResource(getResources().getIdentifier("ofertas","drawable",this.getPackageName()));
    	}else if(nombreTab.equals("tabPedidoSincronizar")){
    		textoTab.setText("Pedido a \nSincronizar");
    		imagenTab.setImageResource(getResources().getIdentifier("pedido","drawable",this.getPackageName()));
    	}else if(nombreTab.equals("tabCuenta")){
    		textoTab.setText("Cuenta");
    		imagenTab.setImageResource(getResources().getIdentifier("pagar","drawable",this.getPackageName()));
    	}else if(nombreTab.equals("tabCalculadora")){
    		textoTab.setText("Calculadora");
    		imagenTab.setImageResource(getResources().getIdentifier("calculadora","drawable",this.getPackageName()));
    	}
    	return tabInferiorContentView;
    }
    
    // Metodo encargado de definir la acci�n de cada tab cuando sea seleccionado
	public void onTabChanged(String tabId) {
		if(tabs.getCurrentTabTag().equals("tabInicio")){
			pulsadoTabSuperior = false;
			tabInferiorPulsado = "tabInicio";
    		/*
			 * TODO Completar funcionalidad de la pantalla de bienvenida
			 */
			// Cargamos en el fragment la pantalla de bienvenida del restaurante
			Fragment fragmentPantallaInicioRes = new PantallaInicialRestaurante();
			((PantallaInicialRestaurante)fragmentPantallaInicioRes).setRestaurante(restaurante);
	        FragmentTransaction m = getFragmentManager().beginTransaction();
	        m.replace(R.id.FrameLayoutPestanas, fragmentPantallaInicioRes);
	        m.commit();
		}else if(tabId.equals("tabPromociones")){
			/*
			 * TODO Hacer su layout y su funcionalidad
			 * Actualmente se muestra un aviso de que la secci�n no se encuentra disponible a�n.
			 */
			//Creaci�n y configuraci�n de la ventana emergente
			ventanaEmergente = new AlertDialog.Builder(InicializarRestaurante.this).create();
			View vistaAviso = LayoutInflater.from(InicializarRestaurante.this).inflate(R.layout.aviso_seccion_no_disponible, null);
			ventanaEmergente.setView(vistaAviso);
			ventanaEmergente.show();
			
			//Crea el timer para que el mensaje solo aparezca durante 3 segundos
			final Timer t = new Timer();
			t.schedule(new TimerTask() {
	         public void run() {
	            ventanaEmergente.dismiss(); 
	             t.cancel(); 
	         }
			}, 2000);
			
			
			/*
			 * FIXME Chapuza para que no vuelva a entrar en el selected al desmarcarse y vuelva a mostrar el aviso
			 */
			/**********************************************************************************************/
			if(pulsadoTabSuperior){
				tabs.setCurrentTab(0);
				actionbar.selectTab(actionbar.getSelectedTab());
			}else{
				tabs.setCurrentTabByTag(tabInferiorPulsado);
			}
			/**********************************************************************************************/
		}else if(tabId.equals("tabPedidoSincronizar")){
			pulsadoTabSuperior = false;
			tabInferiorPulsado = "tabPedidoSincronizar";
			Fragment fragmentPedido = new PedidoFragment();
			PedidoFragment.setRestaurante(restaurante);
	        FragmentTransaction m = getFragmentManager().beginTransaction();
	        m.replace(R.id.FrameLayoutPestanas, fragmentPedido);
	        m.addToBackStack("Pedido");
	        m.commit();
		}else if(tabId.equals("tabCuenta")){
			pulsadoTabSuperior = false;
			tabInferiorPulsado = "tabCuenta";
			Fragment fragmentCuenta = new CuentaFragment();
			((CuentaFragment) fragmentCuenta).setRestaurante(restaurante);
	        FragmentTransaction m = getFragmentManager().beginTransaction();
	        m.replace(R.id.FrameLayoutPestanas, fragmentCuenta);
	        m.addToBackStack("Cuenta");
	        m.commit();
		}else if(tabId.equals("tabCalculadora")){
			/*
			 * TODO Hacer su layout y su funcionalidad
			 * Actualmente se muestra un aviso de que la secci�n no se encuentra disponible a�n.
			 */
			//Creaci�n y configuraci�n de la ventana emergente
			ventanaEmergente = new AlertDialog.Builder(InicializarRestaurante.this).create();
			View vistaAviso = LayoutInflater.from(InicializarRestaurante.this).inflate(R.layout.aviso_seccion_no_disponible, null);
			ventanaEmergente.setView(vistaAviso);
			ventanaEmergente.show();
			
			//Crea el timer para que el mensaje solo aparezca durante 3 segundos
			final Timer t = new Timer();
			t.schedule(new TimerTask() {
	         public void run() {
	            ventanaEmergente.dismiss(); 
	             t.cancel(); 
	         }
			}, 2000);
			
			
			/*
			 * FIXME Chapuza para que no vuelva a entrar en el selected al desmarcarse y vuelva a mostrar el aviso
			 */
			/**********************************************************************************************/
			if(pulsadoTabSuperior){
				tabs.setCurrentTab(0);
				actionbar.selectTab(actionbar.getSelectedTab());
			}else{
				tabs.setCurrentTabByTag(tabInferiorPulsado);
			}
			/**********************************************************************************************/
		}
	}

	// Metodo encargado de devolver el contenido de cada tab
	public View createTabContent(String tag) {
        return tabInferiorContentView;
	}
}