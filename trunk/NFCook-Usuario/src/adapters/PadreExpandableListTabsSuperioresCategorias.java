package adapters;

import java.util.ArrayList;

/**
 * Clase encargada de contener todos los campos necesarios que necesita la lista expandible de platos para
 * mostrar. 
 * 
 * Los padres eran los tipos de platos, de ah� que necesitemos un atributo para guradar el tipo y tambi�n
 * necesitamos una estructura para guradar todos los platos con su informaci�n que contiene dicho tipo.
 * 
 * @author Abel
 *
 */
public class PadreExpandableListTabsSuperioresCategorias {
	
	private String tipoPlato;
	// Reaprovechamos el padre de la list view de aquellas categor�as con un solo tipo
	private ArrayList<PadreListTabsSuperioresCategorias> platosEnTipo;
	
	public PadreExpandableListTabsSuperioresCategorias(String tipoPlato, ArrayList<PadreListTabsSuperioresCategorias> platosEnTipo){
		this.tipoPlato = tipoPlato;
		this.platosEnTipo = platosEnTipo;
	}
	
	public int getNumPlatosEnTipo(){
		return platosEnTipo.size();
	}
	
	public String getTipoPlato(){
		return tipoPlato;
	}
	
	public PadreListTabsSuperioresCategorias getPlatoEnTipo(int pos){
		return platosEnTipo.get(pos);
	}
	
	public String getNombrePlato(int pos){
		return platosEnTipo.get(pos).getNombrePlato();
	}
}
