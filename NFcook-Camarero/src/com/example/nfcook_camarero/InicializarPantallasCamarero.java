package com.example.nfcook_camarero;

import fragments.PantallaHistoricoFragment;
import fragments.PantallaMesasFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class InicializarPantallasCamarero extends Activity implements TabContentFactory, OnTabChangeListener {
	
	// Tabs con las mesas y el hist�rico
	private TabHost tabs;
	
	private View tabContentView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenedor_tabs);
        
        inicializarTabs();
		cargarTabs();
	}
	
	// Metodo encargado de inicializar los tabs inferiores
    private void inicializarTabs(){
    	// Creamos los tabs inferiores y los inicializamos
		tabs = (TabHost)findViewById(android.R.id.tabhost);
		// Les hacemos oyentes
		tabs.setOnTabChangedListener(this);
        tabs.setup();
    }
    
    // Metodo encargado crear los tabs inferiores con las funcionalidades que ofrecemos al usuario
    private void cargarTabs(){
    	// Creamos el tab1 --> Inicio
        TabHost.TabSpec spec = tabs.newTabSpec("tabMesas");
        // Hacemos referencia a su layout correspondiente
        spec.setContent(R.id.tab1);
        // Preparamos la vista del tab con el layout que hemos preparado
        spec.setIndicator("MESAS", null);
        // Lo a�adimos
        tabs.addTab(spec);
        
        // Creamos el tab2 --> Promociones
        spec = tabs.newTabSpec("tabHistorico");
        spec.setContent(R.id.tab2);
        spec.setIndicator("HIST�RICO", null);
        tabs.addTab(spec);
        
    }
    
    // Metodo encargado de definir la acci�n de cada tab cuando sea seleccionado
	public void onTabChanged(String tabId) {
		if(tabs.getCurrentTabTag().equals("tabMesas")){
			// Cargamos en el fragment la pantalla de bienvenida del restaurante
			Fragment fragmentPantallaMesas = new PantallaMesasFragment();
	        FragmentTransaction m = getFragmentManager().beginTransaction();
	        m.replace(R.id.FrameLayoutPestanas, fragmentPantallaMesas);
	        m.commit();
		}else if(tabId.equals("tabHistorico")){
			// Cargamos en el fragment la pantalla de bienvenida del restaurante
			Fragment fragmentPantallaHistorico = new PantallaHistoricoFragment();
	        FragmentTransaction m = getFragmentManager().beginTransaction();
	        m.replace(R.id.FrameLayoutPestanas, fragmentPantallaHistorico);
	        m.commit();
	       //OTROS TIPOS POR SI LOS NECESITO @ WITE
		}/*else if(tabId.equals("tabCuenta")){
			// Descamarcamos el tab superior activado para evitar confusiones
			desmarcarTabSuperiorActivo();
			// Marcamos el tab falso
            tabs.setCurrentTabByTag("tabFalso");
            
			Fragment fragmentCuenta = new CuentaFragment();
			((CuentaFragment) fragmentCuenta).setRestaurante(restaurante);
	        FragmentTransaction m = getFragmentManager().beginTransaction();
	        m.replace(R.id.FrameLayoutPestanas, fragmentCuenta);
	        m.addToBackStack("Cuenta");
	        m.commit();
		}else if(tabId.equals("tabCalculadora")){
			// Marcamos el tab falso
            tabs.setCurrentTabByTag("tabFalso");
			// Vemos si se ha sincronizado alg�n pedido para poder utilizar la calculadora
			if(hayAlgunPedidoSincronizado()){				
				lanzarVentanaEmergenteParaIndicarNumeroComensales();
			}else{
				lanzarVentanaEmergenteAvisoSeNecesitaMinimoUnPedido();
			}
		}*/
	}

	public View createTabContent(String tag) {
		return null;
	}
}
