package nfcook.mensajes;

public class MensajeFichero extends Mensaje{
	
	
	/** Nombre del fichero que se transmite. Por defecto "" */
    public String nombreFichero = "";

    /** Nombre de la ruta del fichero **/
    private String rutaFichero = "";
    
    /** Si este es el �ltimo mensaje del fichero en cuesti�n o hay m�s despu�s */
    public boolean ultimoMensaje = true;

    /** Cuantos bytes son v�lidos en el array de bytes */
    public int bytesValidos = 0;

    /** Array con bytes leidos del fichero */
    public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];
    
    /** N�mero m�ximo de bytes que se envia�n en cada mensaje */
    public final static int LONGITUD_MAXIMA = 10;
	
	public MensajeFichero(String nombreFichero, String rutaFichero) {
		super(Asunto.FICHERO);
		this.nombreFichero = nombreFichero;
		this.rutaFichero = rutaFichero;
	}

	public String getNombreFichero() {
		return nombreFichero;
	}

	public String getRutaFichero() {
		return rutaFichero;
	}

	public boolean isUltimoMensaje() {
		return ultimoMensaje;
	}

	public int getBytesValidos() {
		return bytesValidos;
	}

	public byte[] getContenidoFichero() {
		return contenidoFichero;
	}	
	
	
	
}
