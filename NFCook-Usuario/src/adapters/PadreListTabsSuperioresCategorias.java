package adapters;


/**
 * Clase encargada de contener todos los campos necesarios que necesita la lista de platos para
 * mostrar. 
 * 
 * Se mosotrar� para cada plato el nombre, una descripci�n breve e im�gen si dispone el mismo.
 * 
 * @author Abel
 *
 */

public class PadreListTabsSuperioresCategorias {
	private String nombrePlato;
	private String descripcionBreve;
	private int imagenPlato;
	private boolean tieneImagen;
	
	public PadreListTabsSuperioresCategorias(String nombrePlato, String descripcionBreve, int imagenPlato, boolean tieneImagen){
		this.nombrePlato = nombrePlato;
		this.descripcionBreve = descripcionBreve;
		this.imagenPlato = imagenPlato;
		this.tieneImagen = tieneImagen;
	}
	
	public String getNombrePlato(){
		return nombrePlato;
	}
	
	public String getDescripcionBreve(){
		return descripcionBreve;
	}
	
	public int getImagenPlato(){
		return imagenPlato;
	}
	
	public boolean getTieneImagen(){
		return tieneImagen;
	}
}
