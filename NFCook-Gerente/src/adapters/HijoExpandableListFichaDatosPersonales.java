package adapters;

/**
 * Esta clase almacenar� todos los campos que queremos guardar en la categor�a de datos personales, dentro
 * de la ficha con la infomraci�n de cada empleado.
 * 
 * @author Abel
 *
 */
public class HijoExpandableListFichaDatosPersonales extends HijoExpandableListFicha{
	
	private String foto;
	private String sexo;
	private String nombre;
	private String fechaNacimiento;
	private String dni;
	private String estadoCivil;
	private String nacionalidad;
	
	public HijoExpandableListFichaDatosPersonales(String foto, String sexo, String nombre, String fechaNacimiento, String dni, 
			String estadoCivil, String nacionalidad) {
//		this.sexo = sexo;
//		this.nombre = nombre;
//		this.fechaNacimiento = fechaNacimiento;
//		this.dni = dni;
//		this.estadoCivil = estadoCivil;
//		this.nacionalidad = nacionalidad;
		
		this.foto = "abelchocano";
		this.sexo = "Var�n";
		this.nombre = "Abel Chocano G�mez";
		this.fechaNacimiento = "22/07/1991";
		this.dni = "05305671P";
		this.estadoCivil = "Soltero";
		this.nacionalidad = "Almor�bide";
	}

	public String getFoto() {
		return foto;
	}

	public String getSexo() {
		return sexo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public String getDni() {
		return dni;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

}
