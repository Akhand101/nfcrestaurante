package adapters;

import java.util.ArrayList;

/**
 * Contenido del adapter de la pantalla que contiene los platos de cada mesa.
 * 
 *  -Atributos-
 * nombre, extras y observaciones: Nombre, extras y observaciones de cada plato.
 * id: Identificador unico de ese plato. 
 * idPlato: Udentificador de ese tipo de plato en un restaurante concreto.
 * precio: Precio por unidad del plato.
 * cantidad: Numero de elementos de ese plato que hay en en pedido de una mesa concreta.
 * repetidos: Conjunto de ids de los platos que son exactamente iguales.
 
 * @author Rober
 *
 */

public class PadreListMesa {
	
	private String nombre, extras, observaciones;
	private int id;
	private String idPlato;
	private double precio;
	private int cantidad;
	private ArrayList<Integer> repetidos;
	private int sincronizado;
	
	public PadreListMesa(String nombre,String extras,String observaciones,double precio,int id,String idPlato,int s){
		this.nombre = nombre;
		this.extras = extras;
		this.observaciones = observaciones;
		this.precio = precio;
		this.id = id;
		this.idPlato = idPlato;
		this.cantidad = 1;
		repetidos=new ArrayList<Integer>();
		repetidos.add(id);
		sincronizado=s;
	}
	
	public int getSincronizado(){
		return sincronizado;
	}
	
	public void setSincronizado(int s){
		sincronizado = s;
	}
	
	public String getIdPlato(){
		return idPlato;
	}
	
	public int getId() {
		return repetidos.get(0);
	}
	
	public String getNombre() {
		return nombre;
	}

	public String getExtras() {
		return extras;
	}
	
	public void setExtras(String extras) {
		this.extras = extras;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public double getPrecio() {
		return precio*cantidad;
	}
	
	public double getPrecioUnidad() {
		return precio;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public int getCantidad() {
		return cantidad;
	}

	public void sumaCantidad() {
		cantidad++;
		
	}

	public void restaCantidad() {
		cantidad--;
		
	}

	public void aniadeId(int id) {
		repetidos.add(id);
		
	}
	
	public int getIdRepetido() {
		return repetidos.get(0);
		
	}

	public void eliminaId() {
		repetidos.remove(0);
		
	}
	
	public int getIdRepetido(int i) {
		return repetidos.get(i);
		
	}

	

}
