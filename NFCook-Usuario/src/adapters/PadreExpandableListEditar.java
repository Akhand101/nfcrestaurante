package adapters;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Configura los padres del adapter de la ExpandableList de la pantalla de edici�n de un plato,
 * cada padre sera el titulo de una categoria de extra.
 * 
 * -Atributos-
 * categoriaExtra : almacena el titulo de la categor�a de extras a mostrar.
 * idPlato        : campo necesario para hacer modificaciones sobre la base de datos.
 * expandido      : indica si estaba expandido o no, de esta forma podremos mostrarlo as� tras una edici�n.
 * hijosExtras    : conjunto de extras de una categoria.
 * 
 * @author Prado
 *
 */
public class PadreExpandableListEditar {
	private String categoriaExtra;
	private boolean expandido;
	private ArrayList<HijoExpandableListEditar> hijosExtras;
	
	public PadreExpandableListEditar(String idPlato, String categoriaExtra,ArrayList<HijoExpandableListEditar> hijosExtras) {
		this.categoriaExtra = categoriaExtra;
		this.hijosExtras = hijosExtras;
	}

	public HijoExpandableListEditar getHijoAt(int childPosition) {
		return hijosExtras.get(childPosition);
	}

	public int getSize() {
		return hijosExtras.size();
	}

	public String getCategoriaExtra() {
		return categoriaExtra;
	}

	public void setExpandido(boolean expandido) {
		this.expandido = expandido;
	}

	public boolean isExpandido() {
		return expandido;
	}
	
	public String getExtrasMarcados(){
		Iterator<HijoExpandableListEditar> it = hijosExtras.iterator();
		String extras = "";
		while(it.hasNext()){
			HijoExpandableListEditar unHijo = it.next();
			if(unHijo.getExtraMarcado() == null){
				return null;
			}else{
				extras += unHijo.getExtraMarcado();
			}
		}
		return extras;
	}
	
	/**
	 * Devuelve en forma de String los extras marcados con 1 o 0 segun esten marcados o no
	 * @return
	 */
	public String getExtrasBinarios(){
		Iterator<HijoExpandableListEditar> it = hijosExtras.iterator();
		String extrasBinarios = "";
		while(it.hasNext()){
			HijoExpandableListEditar unHijo = it.next();
			extrasBinarios += unHijo.getExtrasBinarios();
		}
		return extrasBinarios;
	}
	
}
