package sockets;
/**
 * 
 * 
 * Programa de ejemplo de como transmitir un fichero por un socket.
 * Esta es el mensaje que contiene los cachos de fichero que se van enviando
 * 
 */


import java.io.Serializable;

/**
 * Mensaje que contiene parte del fichero que se est� transmitiendo.
 * 
 * 
 *
 */
@SuppressWarnings("serial")
public class MensajeTomaFichero implements Serializable
{
	/*
	public MensajeTomaFichero (String nombreFichero, boolean ultimoMensaje, int bytesValidos, byte[] contenidoFichero){
		this.nombreFichero = nombreFichero;
		this.ultimoMensaje = ultimoMensaje;
		this.bytesValidos = bytesValidos;
		this.contenidoFichero = contenidoFichero;
	}
	*/
    /** Nombre del fichero que se transmite. Por defecto "" */
    public String nombreFichero="";

    /** Si este es el �ltimo mensaje del fichero en cuesti�n o hay m�s despu�s */
    public boolean ultimoMensaje=true;

    /** Cuantos bytes son v�lidos en el array de bytes */
    public int bytesValidos=0;

    /** Array con bytes leidos del fichero */
    public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];
    
    /** N�mero m�ximo de bytes que se envia�n en cada mensaje */
    public final static int LONGITUD_MAXIMA=10;
}
