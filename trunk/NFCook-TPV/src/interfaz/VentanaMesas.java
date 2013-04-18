package interfaz;

import sockets.ClienteFichero;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import sockets.EscuchaCliente;
import tpv.Mesa;
import tpv.Restaurante;

public class VentanaMesas extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private static GraphicsDevice grafica = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private boolean esPantallaCompleta;
	private Restaurante unRestaurante;
	private String idCamarero;	
	private JPanel panelMesasCamarero;
	private JScrollPane scrollpanelMesasCamarero;
	private final static int puerto = 5000;
	private final static String servidor = "192.168.1.54";

	public VentanaMesas(Restaurante unRestaurante, String idCamarero){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		//Eliminamos los bordes de la ventana.
		setUndecorated(true);
		
		//Calculamos el tama�o de la pantalla
		Dimension dimenionesPantalla = getToolkit().getScreenSize();
				
		this.unRestaurante = unRestaurante;
		this.idCamarero = idCamarero;
		
		/*
		 * JPanel principal (contiene el JPanel de las mesas del restaurante y el JPanel de las mesas del camarero ).
		 */
		
		// Panel principal, es el que contendra los paneles de las mesas y las mesas del camarero.
		JPanel panelContenedorMesasYcamarero = new JPanel();
		// Ponemos un borde al panel por estetica.
		panelContenedorMesasYcamarero.setBorder(new EmptyBorder(5, 5, 5, 5));
		// Para poder decidir el tama�o de los paneles de dentro.
		panelContenedorMesasYcamarero.setLayout(null);
		setContentPane(panelContenedorMesasYcamarero);
		
		/*
		 * JPanel izquierdo ( contiene todas las mesas del restaurante ).
		 */
		
		// Creamos un JScrollPane por si no entrasen todas las mesas.
		JScrollPane scrollpanelMesas = new JScrollPane();
		// Ponemos un borde al ScrollPanel por estetica. Efecto hundido.
		scrollpanelMesas.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		// Ajustamos el tama�o del panel a partir del tama�o de la ventana.
		scrollpanelMesas.setBounds(10, 10, (int) dimenionesPantalla.getWidth()-300, (int) dimenionesPantalla.getHeight()-20); //(x, y, w, z) -> X = despazamiento a derecha; Y = despazamiento encima; W = ancho ; Z = largo.
		// Impedimos que tenga barra horizontal.
		scrollpanelMesas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Creamos el panel contenedor de las mesas,GridLayout permite a�adir los elementos en forma de matriz, estan obligados a tener el mismo tama�o todos los componentes.
		JPanel panelMesas = new JPanel(new GridLayout(0, 7, -17, 0));
		// Ponemos un borde al panel por estetica, asi las mesas no saldran pegadas al techo.
		panelMesas.setBorder(new EmptyBorder(17, 0, 17, 0));
		// Para que si pulsamos sobre el panel los botones se activen, esto solo es necesario cuando se han desplegado las opciones de la mesa.
		panelMesas.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				BotonMesa.activarTodosLosBotones();
			}
			
		});
		// A�adimos el panel al JScrollPane.
		scrollpanelMesas.setViewportView(panelMesas);
		// A�adimos el panel al panel principal.
		panelContenedorMesasYcamarero.add(scrollpanelMesas);
		
		/*
		 *  JPanel derecho ( contiene todas las mesas que lleva el camarero que ha iniciado sesi�n).
		 */
		
		// Creamos un JScrollPane por si no entrasen todas las mesas.
		JScrollPane scrollpanelMesasCamarero = new JScrollPane();
		// Ponemos un borde al ScrollPanel por estetica. Efecto hundido.
		scrollpanelMesasCamarero.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		// Ajustamos el tama�o del panel a partir del tama�o de la ventana.
		scrollpanelMesasCamarero.setBounds((int) dimenionesPantalla.getWidth()-280, 10, (int) dimenionesPantalla.getWidth()-1010, (int) dimenionesPantalla.getHeight()-20); //(x, y, w, z) -> X = despazamiento a derecha; Y = despazamiento encima; W = ancho ; Z = largo.
		// Impedimos que tenga barra horizontal.
		scrollpanelMesasCamarero.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Creamos el panel contenedor de las mesas del camarero, GridLayout permite a�adir los elementos en forma de matriz, estan obligados a tener el mismo tama�o todos los componentes.
		panelMesasCamarero = new JPanel(new GridLayout(0, 2, -17, 0));
		// Ponemos un borde al panel por estetica, asi las mesas no saldran pegadas al techo.
		panelMesasCamarero.setBorder(new EmptyBorder(17, 0, 17, 0));
		// Para que si pulsamos sobre el panel los botones se activen, esto solo es necesario cuando se han desplegado las opciones de la mesa.
		panelMesasCamarero.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				BotonMesa.activarTodosLosBotones();
			}
			
		});
		// A�adimos el panel al JScrollPane.
		scrollpanelMesasCamarero.setViewportView(panelMesasCamarero);
		// A�adimos el panel al panel principal.
		panelContenedorMesasYcamarero.add(scrollpanelMesasCamarero);
		
	    Iterator<Mesa> iteradorMesas = unRestaurante.getIteratorMesas();
		
		while(iteradorMesas.hasNext()){
			Mesa unaMesa = iteradorMesas.next();
			BotonMesa botonMesa = new BotonMesa(this, unRestaurante, unaMesa.getNumeroPersonas(), unaMesa.getIdMesa(), idCamarero);
			panelMesas.add(botonMesa, null);
		}
		
		cargarMesasCamarero();

	}
	
	public void cargarMesasCamarero(){
		
		panelMesasCamarero.removeAll();
		Iterator<Mesa> iteradorMesasCamarero = unRestaurante.getIteratorMesas();
		
		while(iteradorMesasCamarero.hasNext()){
			Mesa unaMesa = iteradorMesasCamarero.next();
			if(unaMesa.getIdCamarero()!=null && unaMesa.getIdCamarero().equals(idCamarero)){
				BotonMesa botonMesa = new BotonMesa(this, unRestaurante, unaMesa.getNumeroPersonas(), unaMesa.getIdMesa(), idCamarero);
				panelMesasCamarero.add(botonMesa, null);
				panelMesasCamarero.validate();
				panelMesasCamarero.repaint();
			}
		}
		
	}
	
	public static void main(String args[]){
		
		// cargamos las bases de datos desde el Servidor
		//ClienteFichero.pide("MesasRestaurante.db", servidor, puerto);
		//ClienteFichero.pide("MiBase.db", servidor, puerto);
		/*ClienteFichero.pide("MesasRestaurante.db", servidor, puerto);
		ClienteFichero.pide("MiBase.db", servidor, puerto);
		
		/* Consulta de prueba de insercion enviada al servidor*/
		 ClienteFichero.enviaConsulta("MiBase.db", servidor, puerto, "INSERT INTO Restaurantes " +
        		"VALUES ('fh102', 'Foster', 'Bebidas', 'null', 'CervecitaRica', 'MuyRica', 'Riquisima', 'null', 'null', '10.0')");
        
		// Consulta de prueba de insercion enviada al servidor
		/*  ClienteFichero.enviaConsulta("MiBase.db", servidor, puerto, "INSERT INTO Restaurantes " +
        		"VALUES ('fh103', 'Foster', 'Bebidas', 'null', 'CervecitaRica', 'MuyRica', 'Riquisima', 'null', 'null', '10.0')");
        */
		VentanaMesas ventanaMesas = new VentanaMesas(new Restaurante(), null);
		ventanaMesas.pack();
		ventanaMesas.setVisible(true);
		
		EscuchaCliente thread = new EscuchaCliente(); // lanzamos el thread de escucha
        thread.start();
        
        ClienteFichero.enviaConsulta("MiBase.db", servidor, puerto, "INSERT INTO Restaurantes " +
        		"VALUES ('fh104', 'Foster', 'Bebidas', 'null', 'CervecitaRica', 'MuyRica', 'Riquisima', 'null', 'null', '10.0')");
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(!esPantallaCompleta){
            grafica.setFullScreenWindow(this);
		}
		else{
            grafica.setFullScreenWindow(null);
		}
		esPantallaCompleta = !esPantallaCompleta;
	}
	
}