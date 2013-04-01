package adapters;
import java.util.ArrayList;

import usuario.Calculadora;

import com.example.nfcook.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Clase encargada de implementar el adpater de la im�gen deslizante colocada en la parte
 * inferior central de la pantalla calculadora. Por ella pueden pasar todos los platos
 * que han consumido los comensales.
 * 
 * En el adapter de esta clase es donde se implementa el oyente cada im�gen que muestra
 * la im�gen deslizante. El oyente lo que har� ser� mostrarnos una ventana emergente
 * en la que se nos da la opci�n de repartir el plato seleccionado entre los comensales 
 * que lo han consumido. Actualiz�ndose la informaci�n de los mismos (precio a pagar, 
 * platos consumidos, etc).
 * 
 * Hemos utilizado dos matrices de booleanos para poder asegurar que no asignamos un plato
 * dos veces a una misma persona y tambi�n para mantener las selecciones que vaya realizando
 * el usuario, es decir que si reparte un plato y vuelve a abrir dicho plato para realizar
 * alguna modificaci�n, se preecarga la configuraci�n que tuviese de la vez anterior.
 * 
 * Una vez repartido un plato, aplicamos el adapter del gridview de las personas para que
 * se actualice la informaci�n en la pantalla. Esto a efectos de usuario no lo nota.
 * 
 * @author Abel
 *
 */
public class MiViewPagerAdapter extends PagerAdapter{
	 private Activity activity;
	 private ArrayList<InfomacionPlatoPantallaReparto> platos;
	 private int numPersonas;
	 /*
	 * Matriz de booleanos que nos ayuda a saber las personas a las que ha sido
	 * asignado un determinado plato. De esta forma si intentamos hacer el reparto
	 * dos veces de un determinado plato porque nos hemos equivocado o por la raz�n
	 * que sea, se cargan las personas que ya hubieramos seleccionado y le ahorramos
	 * ese trabajo al usuario de tener que volver a marcar todo.
	 */
	 private static boolean[][] marcados; 
	 /*
	 * Matriz de booleanos que nos ayuda a saber los platos que se le han asignado a
	 * una persona. Nos ayuda a no introducir a un usuario dos veces un mismo plato.
	 */
	 private static boolean[][] platosSeleccionados;
	 
	 public MiViewPagerAdapter(Activity act, ArrayList<InfomacionPlatoPantallaReparto> platos, int numPersonas) {
		activity = act;
		this.platos = platos;
		this.numPersonas = numPersonas;
		int numPlatos = platos.size();
		
		// Inicializamos las matrices de booleanos a false ambas
		marcados = new boolean [numPlatos][numPersonas];
		for (int i=0; i<numPlatos;i++){
			for (int j=0; j<numPersonas;j++){
				marcados[i][j] = false;
			}
		}
		
		platosSeleccionados = new boolean [numPlatos][numPersonas];
		for (int i=0; i<numPlatos;i++){
			for (int j=0; j<numPersonas;j++){
				platosSeleccionados[i][j] = false;
			}
		}
	 }
	 
	 public static void marcaCheckBox(int posPlato, int posPersona){
		 marcados[posPlato][posPersona] = true;
	 }
	 
	 public static void desmarcaCheckBox(int posPlato, int posPersona){
		 marcados[posPlato][posPersona] = false;
	 }

	 @Override
	 public int getCount() {
	 	return platos.size();
	 }

	 @Override
	 public Object instantiateItem(View collection, int position) {
		 final int pos = position;
		 ImageView view = new ImageView(activity);
		 view.setOnClickListener(new OnClickListener(){
			// Definimos el oyente de cada im�gen
			public void onClick(View arg0) {
				Log.i("OYENTE IMAGEN","Hola");
		        
				/*************PREPARAMOS EL LAYOUT DE LA VENTANA EMERGENTE**************/
				AlertDialog.Builder ventanaEmergente = new AlertDialog.Builder(activity);           
				// Creamos la vista que tendr� la ventana
				View vistaVentanaEmergente = LayoutInflater.from(activity).inflate(R.layout.ventana_emergente_repartir_plato_calculadora, null);
		        
		        // Damos valor al campo nombre del plato, precio e imagen del plato
				TextView textViewNombrePlato = (TextView) vistaVentanaEmergente.findViewById(R.id.textViewNombrePlatoRepartoCalculadora);
				textViewNombrePlato.setText(platos.get(pos).getNombrePlato());
				TextView textViewPrecioPlato = (TextView) vistaVentanaEmergente.findViewById(R.id.textViewPrecioPlatoRepartoCalculadora);
				textViewPrecioPlato.setText(platos.get(pos).getPrecioPlato() + " �");
				ImageView imageViewPlato = (ImageView) vistaVentanaEmergente.findViewById(R.id.imageViewPlatoRepartoCalculadora);
				imageViewPlato.setImageResource(platos.get(pos).getFotoPlato());
				
				GridView gridViewRepartoPlato = (GridView) vistaVentanaEmergente.findViewById(R.id.gridViewRepartirPlatoCalculadora);
				MiGridViewRepartirPlatoCalculadoraAdapter miGridViewRepartirPlatocalculadoraAdapter = new MiGridViewRepartirPlatoCalculadoraAdapter(activity, MiGridViewCalculadoraAdapter.getPersonas(), marcados, pos);
				gridViewRepartoPlato.setAdapter(miGridViewRepartirPlatocalculadoraAdapter);
				
				// Implementamos la funcionalidad de los dos botones
				ventanaEmergente.setNegativeButton("Cancelar", null);
			    ventanaEmergente.setPositiveButton("Aceptar", new  DialogInterface.OnClickListener() { // si le das al aceptar
			        public void onClick(DialogInterface dialog, int whichButton) {
			        	// Contamos cuantas personas han compartido ese plato
			        	int total = 0;
			        	for(int i=0; i<numPersonas; i++){
			        		if(marcados[pos][i]){
			        			// Aumentamos el n�mero de personas que han tomado ese plato
			        			total++;
			        			// Aumentamos en una unidad las personas para luego calcular el precio
			        			// Miramos que no estuviese ya esa persona
			        			if(!platosSeleccionados[pos][i]){
			        				platos.get(pos).anyadePersonaAlReparto();
			        			}
			        		}else{
			        			// Miramos si habia sido asignada esa persona
			        			if(platosSeleccionados[pos][i]){
			        				platos.get(pos).quitaPersonaAlReparto();
			        				platosSeleccionados[pos][i] = false;
			        				// Le quitamos el plato al comensal para que lo vea reflejado en su total a pagar
			        				MiGridViewCalculadoraAdapter.quitarPlatoPersona(i, platos.get(pos).getIdPlatoEnPedido());
			        			}
			        		}
			        	}
			        	
			        	// Miramos si ha sido asignado a al menos alg�n comensal
			        	if(total>0){
			        		/*
			        		 * Recorremos de nuevo a todos los comensales para a�adirle
			        		 * dicho plato a su respectiva lista y le incrementamos lo 
			        		 * que ha de pagar.
			        		 */
			        		for(int i=0; i<numPersonas; i++){
				        		if (marcados[pos][i]){
				        			// Miramos si no hab�a seleccionado antes el plato esa persona
				        			if(!platosSeleccionados[pos][i]){
				        				MiGridViewCalculadoraAdapter.anyadirPlatoPersona(i, platos.get(pos).getIdPlatoEnPedido(), platos.get(pos).getNombrePlato(), platos.get(pos).getPrecioPlato(),total);
				        				platosSeleccionados[pos][i] = true;
				        			}else{
				        				// Reajustamos el precio del plato por si se hubiera metido alguien mas a ese plato
				        				MiGridViewCalculadoraAdapter.reajustarPrecioPlato(i, platos.get(pos).getIdPlatoEnPedido(), total);
				        			}
				        		}
				        	}
			        	}		        	
			        	Calculadora.actualizaGridViewPersonas();
			        }
			    });
				// Aplicamos la vista a la ventana y la lanzamos
				ventanaEmergente.setView(vistaVentanaEmergente);
				ventanaEmergente.show();
				/********************FIN VENTANA EMERGENTE REPARTO*********************/
			}
			  
		  });
		  view.setImageResource(platos.get(position).getFotoPlato());
		  ((ViewPager) collection).addView(view, 0);
		  return view;
	 }

	 @Override
	 public void destroyItem(View arg0, int arg1, Object arg2) {
		 ((ViewPager) arg0).removeView((View) arg2);
	 }

	 @Override
	 public boolean isViewFromObject(View arg0, Object arg1) {
		 return arg0 == ((View) arg1);
	 }

	 @Override
	 public Parcelable saveState() {
		 return null;
	 }
}
