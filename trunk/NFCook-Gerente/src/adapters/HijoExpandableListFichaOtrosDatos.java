package adapters;

public class HijoExpandableListFichaOtrosDatos extends HijoExpandableListFicha{
	
	private String otrosDatos;

	public HijoExpandableListFichaOtrosDatos(String otrosDatos) {
//		this.otrosDatos = otrosDatos;
		
		this.otrosDatos = "\u2022 Idiomas: Ingl�s Nivel Alto\n\u2022 Posee carnet de conducir (Permiso B)\n\u2022 Conocimiento avanzado de inform�tica\n\u2022 Buena presencia de cara al p�blico";
	}

	public String getOtrosDatos() {
		return otrosDatos;
	}

}
