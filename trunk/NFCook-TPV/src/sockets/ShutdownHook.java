package sockets;


/** Thread que se ejecutar� cuando la aplicaci�n se cierre. 
 * De esta forma podremos eliminar la IP del TPV de la lista de clientes del servidor.
 **/
public class ShutdownHook extends Thread{

	public void run(){
		
		// enviamos la IP del TPV al servidor para eliminarlo de su lista de IPs
		ClienteFichero.enviaIPtpv();

	}
}
