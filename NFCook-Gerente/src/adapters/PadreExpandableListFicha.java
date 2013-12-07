package adapters;

/**
 * Clase encargada de contener todos los campos necesarios que mostrar� la expandable list de la ficha de un
 * empleado. 
 * 
 * Los padres ser�n el tipo de informaci�n y los hijos ser�n los campos que almacena dicho tipo de informaci�n.
 * 
 * @author Abel
 *
 */
public class PadreExpandableListFicha {
	
	private String tipoDato;
	private HijoExpandableListFicha datos;
	
	public PadreExpandableListFicha(String tipoDato, HijoExpandableListFicha datos){
		this.tipoDato = tipoDato;
		this.datos = datos;
	}
	
	public String getTipoDato(){
		return tipoDato;
	}
	
	public HijoExpandableListFicha getDatos(){
		return datos;
	}

}

