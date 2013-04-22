package tpv;

import java.util.ArrayList;
import java.util.Iterator;

public class Cobro {

	private ArrayList<Producto> cobro;
	private FechaYHora horaEnvioYFecha;
	private String idCamarero;
	private String idMesa;
	private double total;
	private String restaurante;
	
	public Cobro(ArrayList<Producto> cobro, String idMesa, String idCamarero, double total , String restaurante) {
		this.cobro = cobro;
		this.idCamarero = idCamarero;
		this.idMesa = idMesa;
		horaEnvioYFecha = new FechaYHora();
		this.total = total;
		this.restaurante = restaurante;
		enviarCobroAImpresora();
	}

	public ArrayList<Producto> getComanda() {
		return cobro;
	}

	public FechaYHora getHoraEnvioYFecha() {
		return horaEnvioYFecha;
	}

	public String getIdCamarero() {
		return idCamarero;
	}
	
	public String getIdMesa(){
		return idMesa;
	}
	
	public void enviarCobroAImpresora(){
		Iterator<Producto> itProductos = cobro.iterator();
		String comida = "";
		String bebida = "";
		while(itProductos.hasNext()){
			Producto producto = itProductos.next();
			if(producto instanceof Plato){
//				comida +=  ((Plato) producto).toString() + " " + calculaPuntos(60,((Plato)producto).toString()+producto.getPrecio()+"") + producto.getPrecio() + "";
				comida +=  ((Plato) producto).toString() + " -->" + "Precio: " + producto.getPrecio() + " ";
			}else{
//				bebida += ((Bebida) producto).toString() + calculaPuntos(60,((Bebida)producto).toString()+producto.getPrecio()+"") + producto.getPrecio() + "";
				bebida += ((Bebida) producto).toString() + " -->" + "Precio: " + producto.getPrecio() + " ";
			}
		}
		boolean comidaBool = false;
		boolean bebidaBool = false;
		if(!comida.equals("")){
			comidaBool = true;
		}
		if(!bebida.equals("")){
			bebidaBool = true;
		}
		
		String textoAImprimir = "";
		//cuando lea "Nombre: " saltara de linea
		textoAImprimir = 	restaurante.toUpperCase() +
							"Nombre: " + "Mesa: " + idMesa +  ".  Le atendi๓ " + idCamarero + 
							"Nombre: " + "Fecha y hora: " + horaEnvioYFecha; 
		
		if (comidaBool && bebidaBool){
			textoAImprimir += comida + bebida; //Para que salga todo en la misma impresion. Quitar cuando separemos en dos impresoras
		}else if(comidaBool){//no hay bebida
			textoAImprimir += comida ; //Para que salga todo en la misma impresion. Quitar cuando separemos en dos impresoras
		}else{//no hay comida
			textoAImprimir += bebida; //Para que salga todo en la misma impresion. Quitar cuando separemos en dos impresoras
		}
		textoAImprimir += "Nombre: " + "Total: " + total + " " + "Nombre: " + "Gracias por su visita";
		
		Imprimir.imprime(textoAImprimir);
		
	}
	
	public String calculaPuntos(int carPorLinea, String restoDeTexto){
		int aux = restoDeTexto.length() % carPorLinea; //ocupadas
		aux = carPorLinea - aux + 2;
		String result = "";
		for (int i = 0 ; i < aux ; i++){
			result += ".";
		}
		return result;
	}
	
}

