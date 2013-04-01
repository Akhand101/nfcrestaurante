package adapters;

/**
 * Clase encargada de contener los campos que se muestran para cada uno de los elementos
 * de la lista de cada usuario cuando est� utilizando la calculadora y se est�n
 * repartiendo los platos. 
 * 
 * Para cada plato se almacena:
 * - nombre del plato.
 * - porci�n que ha tomado del mismo.
 * - precio del plato.
 * - precio que tiene que pagar el comensal, en funci�n de la parte que ha tomado.
 * 
 * @author Abel
 *
 */
public class InformacionPlatoCantidad {
	private String nombrePlato;
	private String porcion;
	private double precioPlato;
	private double precioPagar;
	
	public InformacionPlatoCantidad(String nombrePlato, String porcion, double precioPlato, double precioPagar){
		this.nombrePlato = nombrePlato;
		this.porcion = porcion;
		this.precioPlato = precioPlato;
		this.precioPagar = precioPagar;
	}
	
	public String getNombrePlato(){
		return nombrePlato;
	}
	
	public String getPorcion(){
		return porcion;
	}
	
	public double getPrecioPlato(){
		return precioPlato;
	}
	
	public double getPrecioPagar(){
		return precioPagar;
	}
}
