package nfcook.mensajes;

public class MensajeFichero extends Mensaje{
	
	private static final long serialVersionUID = 1L;

	/** Nombre del fichero que se transmite. Por defecto "" */
    private String nombreFichero = "";

    /** Nombre de la ruta del fichero **/
    private String rutaFichero = "";
    
    /** Si este es el �ltimo mensaje del fichero en cuesti�n o hay m�s despu�s */
    private boolean ultimoMensaje = true;

    /** Cuantos bytes son v�lidos en el array de bytes */
    private int bytesValidos = 0;

    /** Array con bytes leidos del fichero */
    private byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];
    
    /** N�mero m�ximo de bytes que se envia�n en cada mensaje */
    private final static int LONGITUD_MAXIMA = 10;
	
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
	
	public static int getLongitudMaxima() {
		return LONGITUD_MAXIMA;
	}

	public void setUltimoMensaje(boolean ultimoMensaje) {
		this.ultimoMensaje = ultimoMensaje;
	}

	public void setBytesValidos(int bytesValidos) {
		this.bytesValidos = bytesValidos;
	}
	
	
	
	
	
}
