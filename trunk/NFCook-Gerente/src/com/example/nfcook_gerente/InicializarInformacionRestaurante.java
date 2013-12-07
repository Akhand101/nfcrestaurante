package com.example.nfcook_gerente;

import java.util.ArrayList;

import adapters.PagerAdapter;
import adapters.ViewPagerBloquearSlide;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import fragments.ClasificacionPlatosFragment;
import fragments.EmpleadosFragment;
import fragments.InformacionRestauranteFragment;
import fragments.IngresosFragment;


/**
 * @author: Alejandro Moran
 * 
 * Esta clase contendr� toda la informaci�n de un restaurante.
 * La informaci�n del restaurante se organizar� por tabs, almacenando cada uno su informaci�n correspondiente.
 * Los tabs son los siguientes:
 * 
 * - Informacion: datos correspondientes al restaurante (telefono, localizaci�n, foto, valoraci�n...)
 * - Empleados: informaci�n de todos los empleados
 * - Facturacion: toda la informaci�n sobre la facturaci�n del mismo
 * - Ranking de platos: todos los platos ordenados en orden descendente en funci�n de la demanda o por ingresos
 * 
 * Se accede a ella al seleccionar un restaurante, 
 * en la lista inicial.
 * 
**/
public class InicializarInformacionRestaurante extends FragmentActivity implements OnTabChangeListener, OnPageChangeListener {

	private static TabHost tabs;
	private View tabContentView;
	private static ViewPagerBloquearSlide miViewPager;
	private PagerAdapter miPagerAdapter;
	private static ArrayList<Fragment> listFragments;
	private Bundle bundleInfoRestaurante;
	private boolean sinInfo;
	
	private int ultimoTabSeleccionado;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenedor_tabs);

        // Recogemos la informaci�n para pasarla como argumento m�s adelante
        bundleInfoRestaurante = getIntent().getExtras(); // Nombre, tel�fono, calle, logo, idRestaurante

        sinInfo = bundleInfoRestaurante.getBoolean("sinInfo");
        
        inicializarTabs();
        cargarTabsEInicializarViewPages();
        
        // Quitamos el separador del �ltimo tab por una cuesti�n est�tica
        quitarSeparadorUltimoTab();
        
        // Ponemos el t�tulo del tab que habr� seleccionado por defecto
	    getActionBar().setTitle(" INFORMACI�N DEL RESTAURANTE...");
	  	
		// Marcamos el tab de informacion como inicialo el de empleados si se han seleccionado varios restaurantes
        tabs.setCurrentTab(0);
        ultimoTabSeleccionado = 0;
        marcarTabSeleccionado();

	    // Cambiamos el fondo al ActionBar
	  	getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#004400")));
	}
	
	
	// Metodo encargado de inicializar los tabs
    private void inicializarTabs(){
    	// Creamos los tabs y los inicializamos
		tabs = (TabHost)findViewById(R.id.tabhost);

		// Los hacemos oyentes
        tabs.setup();  
    }
    
    // Metodo encargado crear los tabs superiores con las funcionalidades que ofrecemos al gerente
    private void cargarTabsEInicializarViewPages(){
    	// Creamos la lista que contendra todos los fragments a cargar en funcion del tab
    	listFragments = new ArrayList<Fragment>();
    	TabHost.TabSpec spec = null;
    	
    	if(!sinInfo){
	    	// Creamos el tab1 --> Informaci�n
    		spec = tabs.newTabSpec("tabInformacion");     
	        // Preparamos la vista del tab con el layout que hemos preparado
	        spec.setIndicator(prepararTabView(getApplicationContext(),"tabInformacion"));
	        // Hacemos referencia a su layout correspondiente
	        spec.setContent(R.id.tab1);
	        // Lo a�adimos
	        tabs.addTab(spec);
			// Creamos el fragment de cada tab
	        listFragments.add(Fragment.instantiate(this, InformacionRestauranteFragment.class.getName(), bundleInfoRestaurante));  
    	}
    	
        // Creamos el tab2 --> Empleados
        spec = tabs.newTabSpec("tabEmpleados");
        if(sinInfo){
        	spec.setContent(R.id.tab1);
        }else{ 
	        spec.setContent(R.id.tab2);
        }
	    spec.setIndicator(prepararTabView(getApplicationContext(),"tabEmpleados"));
        tabs.addTab(spec);
        listFragments.add(Fragment.instantiate(this, EmpleadosFragment.class.getName(), bundleInfoRestaurante));
   
        // Creamos el tab3 --> Ingresos
        spec = tabs.newTabSpec("tabIngresos");
        if(sinInfo){
        	spec.setContent(R.id.tab2);
        }else{ 
	        spec.setContent(R.id.tab3);
        }
        spec.setIndicator(prepararTabView(getApplicationContext(),"tabIngresos"));
        tabs.addTab(spec);
        listFragments.add(Fragment.instantiate(this, IngresosFragment.class.getName(), bundleInfoRestaurante));
     
        // Creamos el tab4 --> Clasificaci�n de platos
        spec = tabs.newTabSpec("tabClasificacionPlatos");
        if(sinInfo){
        	spec.setContent(R.id.tab3);
        }else{ 
	        spec.setContent(R.id.tab4);
        }
        spec.setIndicator(prepararTabView(getApplicationContext(),"tabClasificacionPlatos"));
        tabs.addTab(spec);
        listFragments.add(Fragment.instantiate(this, ClasificacionPlatosFragment.class.getName()));

     	// Aqui creamos el viewPager y el pagerAdapter para el correcto slide entre pesta�as
     	inicializarViewPager();

		// Hacemos oyente al tabhost
        tabs.setOnTabChangedListener(this);
    }
    
    // Metodo encargado de preparar las vistas de cada tab 
    private View prepararTabView(Context context, String nombreTab){
    	// Cargamos el layout
    	tabContentView = LayoutInflater.from(context).inflate(R.layout.tabs, null);
    	// Cargamos el icono del tab
    	ImageView imagenTab = (ImageView)tabContentView.findViewById(R.id.imageViewTabInferior);
    	// Cargamos el titulo del tab
    	TextView textoTab = (TextView)tabContentView.findViewById(R.id.textViewTabInferior);
    	textoTab.setTextColor(Color.WHITE);
    	// Asignamos el t�tulo e icono para cada tab
    	if(nombreTab.equals("tabInformacion")){
     		textoTab.setText("Informaci�n");
     		imagenTab.setImageResource(getResources().getIdentifier("informacion","drawable",this.getPackageName()));
     	}else if(nombreTab.equals("tabEmpleados")){
    		textoTab.setText("Empleados");
    		imagenTab.setImageResource(getResources().getIdentifier("empleados","drawable",this.getPackageName()));
    	}else if(nombreTab.equals("tabIngresos")){
    		textoTab.setText("Facturaci�n");
    		imagenTab.setImageResource(getResources().getIdentifier("ingresos","drawable",this.getPackageName()));
    	}else if(nombreTab.equals("tabClasificacionPlatos")){
    		textoTab.setText("Ranking");
    		imagenTab.setImageResource(getResources().getIdentifier("clasificacion","drawable",this.getPackageName()));
    	}
    	return tabContentView;
    }
    
    @Override
	protected void onSaveInstanceState(Bundle instanceState) {
        // Guardar en "tab" la pesta�a seleccionada.
        instanceState.putString("tab", tabs.getCurrentTabTag());
        super.onSaveInstanceState(instanceState);
	}
    
    public void inicializarViewPager() {
    	 
    	/* listFragments es una lista donde est�n todos los Fragments que 
         * se van a usar en el ViewPager.
         */
    	miPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), listFragments);
        miViewPager = (ViewPagerBloquearSlide) super.findViewById(R.id.viewpager);
        miViewPager.setAdapter(miPagerAdapter);
        
        // Indicamos cu�l es el n�mero m�ximo de p�ginas que puede haber
        miViewPager.setOffscreenPageLimit(listFragments.size());
        miViewPager.setOnPageChangeListener(this);
        
        // Lo hacemos visible
        miViewPager.setVisibility(View.VISIBLE);
    }
    
    @Override
	public void onTabChanged(String tabId) {
		int tabSeleccionado = tabs.getCurrentTab();
		
		// Actualizamos el titulo del ActionBar
		switch(tabSeleccionado){
			case 0: 
			    getActionBar().setTitle(" INFORMACI�N DEL RESTAURANTE...");
			    break;
			case 1:
				getActionBar().setTitle(" EMPLEADOS...");
			    break;
			case 2:
				getActionBar().setTitle(" FACTURACI�N...");
			    break;
			case 3:
				getActionBar().setTitle(" RANKING DE PLATOS...");
			    break;
			default:
				getActionBar().setTitle(" NFCOOK GERENTE...");
		}
		
		// Desmarcamos el tab que acabamos de seleccionar, es simplemente una cuestion estetica
		desmarcarTabSeleccionado();
		
		// Actualizamos el ultimo tab seleccionado y marcamos el tab que acabamos de seleccionar, es simplemente una cuestion estetica
		ultimoTabSeleccionado = tabSeleccionado;
      	tabs.setCurrentTabByTag(tabId);
      	marcarTabSeleccionado();
      	
		// Seleccionar la p�gina en el ViewPager.
        miViewPager.setCurrentItem(tabSeleccionado);
    
        // Realizamos la transicion
        Fragment f = new Fragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutContenedorTabs, f);
        ft.commit();
	}
    
    public void marcarTabSeleccionado(){
	    // Cargamos el layout
    	View vista =  tabs.getTabWidget().getChildTabViewAt(ultimoTabSeleccionado);
    	// Cogemos el layout inferior y lo coloreamos de azul para indicar que el tab esta seleccionado
    	LinearLayout linear = (LinearLayout) vista.findViewById(R.id.tab_seleccionado);
    	linear.setBackgroundColor(Color.parseColor("#63B8FF"));
    }
    
    public void desmarcarTabSeleccionado(){
	    // Cargamos el layout
    	View vista =  tabs.getTabWidget().getChildTabViewAt(ultimoTabSeleccionado);
    	// Cogemos el layout inferior y lo coloreamos de azul para indicar que el tab esta seleccionado
    	LinearLayout linear = (LinearLayout) vista.findViewById(R.id.tab_seleccionado);
    	linear.setBackgroundColor(Color.parseColor("#000000"));
    }
    
    public void quitarSeparadorUltimoTab(){
    	// Cargamos el layout
    	View vista =  tabs.getTabWidget().getChildTabViewAt(tabs.getTabWidget().getChildCount() - 1);
    	// Cogemos el layout inferior y lo coloreamos de azul para indicar que el tab esta seleccionado
    	LinearLayout linear = (LinearLayout) vista.findViewById(R.id.separador_tab);
    	linear.setBackgroundColor(Color.parseColor("#000000"));
    }
    
	public View createTabContent(String tag) {
		return null;
	} 

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int pos) {
		tabs.setCurrentTab(pos);
	}
	
	@Override
	public void onPause () {
		super.onPause();
		Log.v("pause", "onPause"); 
	}       
     	
	public static void pausaViewPager(){
		//Sacamos el estado de los tabs
		boolean estadoTabs = tabs.getTabWidget().getChildAt(0).isEnabled();

		tabs.getTabWidget().setEnabled(!estadoTabs);
		
		miViewPager.setPagingEnabled(!miViewPager.getPagingEnabled());		
	}
}
