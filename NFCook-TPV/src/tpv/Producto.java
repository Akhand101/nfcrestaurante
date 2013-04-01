package tpv;

public class Producto {
	private String id;
	private String categoria;
	private String tipo;
	private String nombre;
	private String descripci�n;
	private String foto;
	private double precio;
	private String observaciones;
	
	public Producto(String id, String categoria, String tipo, String nombre,
			String descripci�n, String foto, double precio, String observaciones) {
		this.id = id;
		this.categoria = categoria;
		this.tipo = tipo;
		this.nombre = nombre;
		this.descripci�n = descripci�n;
		this.foto = foto;
		this.precio = precio;
		this.observaciones = observaciones;
	}

	public String getId() {
		return id;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getTipo() {
		return tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripci�n() {
		return descripci�n;
	}

	public String getFoto() {
		return foto;
	}

	public double getPrecio() {
		return precio;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	
}
