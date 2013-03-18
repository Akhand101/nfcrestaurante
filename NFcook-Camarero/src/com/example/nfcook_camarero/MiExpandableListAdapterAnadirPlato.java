package com.example.nfcook_camarero;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;


import adapters.ContenidoListMesa;
import adapters.HijoExpandableListEditar;
import adapters.MiExpandableListAdapterEditar;
import adapters.PadreExpandableListEditar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MiExpandableListAdapterAnadirPlato extends BaseExpandableListAdapter{
	private LayoutInflater inflater;
    private ArrayList<PadreExpandableListAnadirPlato> padresExpandableList;
    private Context context;
    ArrayList<PlatoView> platos;
    
    private static MiExpandableListAdapterEditar adapterExpandableListEditarExtras;
	private static ExpandableListView expandableListEditarExtras;
	private AutoCompleteTextView actwObservaciones;
    
    public MiExpandableListAdapterAnadirPlato(Context context, ArrayList<PadreExpandableListAnadirPlato> padres){
    	padresExpandableList = padres;
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

	public Object getChild(int groupPosition, int childPosition) {
		return padresExpandableList.get(groupPosition).getHijo();
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
	
			convertView = inflater.inflate(R.layout.contenido_hijo_lista_anadir_plato, parent,false);
			GridView gridViewAnadir = (GridView) convertView.findViewById(R.id.gridViewAnadirPlato);			 
	
			
			ArrayList<String> idHijos = padresExpandableList.get(groupPosition).getHijo().getIds();
			ArrayList<String> imgHijos = padresExpandableList.get(groupPosition).getHijo().getNumImagenes();
			ArrayList<String> nombreHijos = padresExpandableList.get(groupPosition).getHijo().getNombrePl();
			ArrayList<Float> precioHijos = padresExpandableList.get(groupPosition).getHijo().getPrecio();

			platos = new ArrayList<PlatoView>();
			//Recorremos con una variable que indica la posicion, porque necesitariamos tres iteradores.
			//Los tres ArrayList tienen el mismo tama�o
			int pos = 0; 
			while(pos < nombreHijos.size()){
				
				ImageView img = new ImageView(convertView.getContext());				
			    img.setImageResource(getDrawable(convertView.getContext(),imgHijos.get(pos)));
			
				//traer las cosas de platoView				
				PlatoView plato= new PlatoView(nombreHijos.get(pos),imgHijos.get(pos),idHijos.get(pos),precioHijos.get(pos));
	    		platos.add(plato);
	    		
	    		pos++;
			}
			
			//Llamamos al adapter para que muestre en la pantalla los cambios realizados
			AnadirPlatoAdapter adapterAnadir;
			adapterAnadir = new AnadirPlatoAdapter(context, platos);
			gridViewAnadir.setAdapter(adapterAnadir);

			gridViewAnadir.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	//sacar� ventana emergente       	
	            	String idPlatoPulsado = platos.get(position).getIdPlato();
	            	Toast.makeText(context,idPlatoPulsado,Toast.LENGTH_SHORT).show();
	            	
	            	AlertDialog.Builder ventanaEmergente = new AlertDialog.Builder(context);
	  	    		ventanaEmergente.setNegativeButton("Cancelar", null);
	  				onClickBotonAceptarAlertDialog(ventanaEmergente, position);
	  				View vistaAviso = LayoutInflater.from(context).inflate(R.layout.ventana_emergente_editar_anadir_plato, null);
	  				expandableListEditarExtras = (ExpandableListView) vistaAviso.findViewById(R.id.expandableListViewExtras);
	  				actwObservaciones = (AutoCompleteTextView) vistaAviso.findViewById(R.id.autoCompleteTextViewObservaciones);

	  				TextView tituloPlato = (TextView) vistaAviso.findViewById(R.id.textViewTituloPlatoEditarYAnadir);
	  				tituloPlato.setText(platos.get(position).getNombrePlato());
	  				cargarExpandableListAnadirExtras(idPlatoPulsado);
	  				ventanaEmergente.setView(vistaAviso);
	  				ventanaEmergente.show();

	                }
	        });
		
	    return convertView;
	
		}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Object getGroup(int groupPosition) {
		return padresExpandableList.get(groupPosition);
	}

	public int getGroupCount() {
		return padresExpandableList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.padre_anadir_plato, parent,false);
        }
 
        TextView textViewPadrePlato = (TextView) convertView.findViewById(R.id.textViewTipo);
        
        textViewPadrePlato.setText(getGroup(groupPosition).toString());
            
        return convertView;

	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }
	
	/**
	 * 
	 * @param context
	 * @param name
	 * @return devuelve el entero que corresponde al drawable con nombre name
	 */
	public static int getDrawable(Context context, String name)
	{
	    Assert.assertNotNull(context);
	    Assert.assertNotNull(name);

	    return context.getResources().getIdentifier(name,
	            "drawable", context.getPackageName());
	}
	
	
	public void cargarExpandableListAnadirExtras(String idPlatoPulsado){
		HandlerGenerico sqlMiBase=new HandlerGenerico(context, "/data/data/com.example.nfcook_camarero/databases/", "MiBase.db");
		SQLiteDatabase dbMiBase= sqlMiBase.open();
  		String[] campos = new String[]{"Extras"};
  		String[] datos = new String[]{idPlatoPulsado};
  		
  		Cursor cursor = dbMiBase.query("Restaurantes",campos,"Id =?",datos,null,null,null); 
  		cursor.moveToFirst();
  		
  		String extrasPlato = cursor.getString(0);
  		  		
  		if(!extrasPlato.equals("")){
  			String[] tokens = extrasPlato.split("/");
	            ArrayList<PadreExpandableListEditar> categoriasExtras =  new ArrayList<PadreExpandableListEditar>();
		        for(int i= 0; i< tokens.length ;i++){
		        	String[] nombreExtra = null;
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
							extrasPulsados[j] = false;
							extrasHijo.add(elementosExtra[j]);
						}
						HijoExpandableListEditar extrasDeUnaCategoria = new HijoExpandableListEditar(extrasHijo, extrasPulsados);
						// A�adimos la informaci�n del hijo a la lista de hijos
						variedadExtrasListaHijos.add(extrasDeUnaCategoria);
						PadreExpandableListEditar padreCategoriaExtra = new PadreExpandableListEditar(idPlatoPulsado, categoriaExtraPadre, variedadExtrasListaHijos);
						// A�adimos la informaci�n del padre a la lista de padres
						categoriasExtras.add(padreCategoriaExtra);
					}catch(Exception e){
						Toast.makeText(context,"Error en el formato de extra en la BD", Toast.LENGTH_SHORT).show();
					}
				}
		        // Creamos el adapater para adaptar la lista a la pantalla.
		    	adapterExpandableListEditarExtras = new MiExpandableListAdapterEditar(context, categoriasExtras,0);
		        expandableListEditarExtras.setAdapter(adapterExpandableListEditarExtras);  
  		}else{
  			//Actualizamos el adapter a null, ya que es static, para saber que este plato no tiene extras.
  			adapterExpandableListEditarExtras = null;
  			expandableListEditarExtras.setVisibility(ExpandableListView.INVISIBLE);
  		}
	}
	
	protected void onClickBotonAceptarAlertDialog(final Builder ventanaEmergente,final int posicion) {
		
		
		ventanaEmergente.setPositiveButton("A�adir", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				boolean bienEditado = true;
		    	String observaciones = null;
		    	String nuevosExtrasMarcados = null;
		    	if(!actwObservaciones.getText().toString().equals("")){
		        	observaciones = actwObservaciones.getText().toString();
		    	}
		    	if(adapterExpandableListEditarExtras!=null){ //Es un plato con extras
		    		nuevosExtrasMarcados = adapterExpandableListEditarExtras.getExtrasMarcados();
		    		if(nuevosExtrasMarcados == null){
		    			bienEditado = false;
		    		}
		    	}
		    	if(bienEditado){
		    		HandlerGenerico sqlMesas = null;
		    		SQLiteDatabase dbMesas = null;
		    		try{
		    			sqlMesas=new HandlerGenerico(context, "/data/data/com.example.nfcook_camarero/databases/", "Mesas.db");
		    			dbMesas = sqlMesas.open();
		    		}catch(SQLiteException e){
		    		 	Toast.makeText(context,"NO EXISTE BASE DE DATOS MESA",Toast.LENGTH_SHORT).show();
		    		}
		    		//Sacamos la fecha a la que el camarero ha introducido la mesa
                	Calendar cal = new GregorianCalendar();
                    Date date = cal.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formatteDate = df.format(date);
                    //Sacamos la hora a la que el camarero ha introducido la mesa
                    Date dt = new Date();
                    SimpleDateFormat dtf = new SimpleDateFormat("HH:mm:ss");
                    String formatteHour = dtf.format(dt.getTime());
                    
		        	ContentValues plato = new ContentValues();
		        	int idUnico = InicialCamarero.getIdUnico();
		        	plato.put("NumMesa",AnadirPlatos.getNumMesa());
		        	plato.put("IdCamarero",AnadirPlatos.getIdCamarero());
		        	plato.put("IdPlato", platos.get(posicion).getIdPlato());
		        	plato.put("Observaciones", observaciones);
		        	plato.put("Extras", nuevosExtrasMarcados);
		        	plato.put("FechaHora", formatteDate + " " + formatteHour);
		        	plato.put("Nombre", platos.get(posicion).getNombrePlato());
		        	plato.put("Precio",platos.get(posicion).getPrecio());
		        	plato.put("Personas",AnadirPlatos.getNumPersonas());
		        	plato.put("IdUnico", idUnico);
		        	dbMesas.insert("Mesas", null, plato);
		        	dbMesas.close();
		        	ContenidoListMesa platoNuevo = new ContenidoListMesa(platos.get(posicion).getNombrePlato(),nuevosExtrasMarcados,observaciones,platos.get(posicion).getPrecio(),idUnico,platos.get(posicion).getIdPlato());
		        	Mesa.actualizaListPlatos(platoNuevo);
		    	}else{
		    		adapterExpandableListEditarExtras.expandeTodosLosPadres();
		    	}				
			}
			
		});
	}
	
	public static void actualizaExpandableList() {
		expandableListEditarExtras.setAdapter(adapterExpandableListEditarExtras);
	}

	public static void expandeGrupoLista(int groupPositionMarcar) {
		expandableListEditarExtras.expandGroup(groupPositionMarcar);
	}
	
}