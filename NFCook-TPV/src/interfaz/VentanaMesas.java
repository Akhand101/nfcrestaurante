package interfaz;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import tpv.Mesa;
import tpv.Restaurante;

public class VentanaMesas extends JFrame implements ActionListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	private static GraphicsDevice grafica = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private boolean esPantallaCompleta;
	private Restaurante unRestaurante;
	private String idCamarero;
	
	private JPanel panelMesasCamarero;
	private JPanel panelMesas;
	
	private JScrollPane scrollpanelMesasCamarero;
	private JScrollPane scrollpanelMesas;
	
	private final Lock mutexMesasRestaurante = new ReentrantLock(true);
	private final Lock mutexMesasCamarero = new ReentrantLock(true);

	public VentanaMesas(Restaurante unRestaurante, String idCamarero){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		//Eliminamos los bordes de la ventana.
		setUndecorated(true);
		this.addMouseMotionListener(this);
		
		//Calculamos el tama�o de la pantalla
		Dimension dimenionesPantalla = getToolkit().getScreenSize();
				
		this.unRestaurante = unRestaurante;
		this.unRestaurante.setVentanaMesas(this);
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
		scrollpanelMesas = new JScrollPane();
		// Ponemos un borde al ScrollPanel por estetica. Efecto hundido.
		scrollpanelMesas.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		// Ajustamos el tama�o del panel a partir del tama�o de la ventana.
		scrollpanelMesas.setBounds(10, 10, (int) (dimenionesPantalla.getWidth()-(dimenionesPantalla.getWidth()/4)), (int) dimenionesPantalla.getHeight()-20); //(x, y, w, z) -> X = despazamiento a derecha; Y = despazamiento encima; W = ancho ; Z = largo.
		// Impedimos que tenga barra horizontal.
		scrollpanelMesas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// Creamos el panel contenedor de las mesas,GridLayout permite a�adir los elementos en forma de matriz, estan obligados a tener el mismo tama�o todos los componentes.
		panelMesas = new JPanel(new GridLayout(0, 7, -17, 0));
		// Ponemos un borde al panel por estetica, asi las mesas no saldran pegadas al techo.
		panelMesas.setBorder(new EmptyBorder(17, 0, 17, 0));
		// Para que si pulsamos sobre el panel los botones se activen, esto solo es necesario cuando se han desplegado las opciones de la mesa.
		panelMesas.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				BotonMesa.despulsarImagen();
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
		scrollpanelMesasCamarero = new JScrollPane();
		// Ponemos un borde al ScrollPanel por estetica. Efecto hundido.
		scrollpanelMesasCamarero.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		// Ajustamos el tama�o del panel a partir del tama�o de la ventana.
		scrollpanelMesasCamarero.setBounds((int) (dimenionesPantalla.getWidth()-(dimenionesPantalla.getWidth()/4))+20, 10, (int) ((dimenionesPantalla.getWidth()/4)-30), (int) dimenionesPantalla.getHeight()-20); //(x, y, w, z) -> X = despazamiento a derecha; Y = despazamiento encima; W = ancho ; Z = largo.
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
				BotonMesa.despulsarImagen();
				BotonMesa.activarTodosLosBotones();
			}
			
		});
		// A�adimos el panel al JScrollPane.
		scrollpanelMesasCamarero.setViewportView(panelMesasCamarero);
		// A�adimos el panel al panel principal.
		panelContenedorMesasYcamarero.add(scrollpanelMesasCamarero);
		
		cargarMesasRestaurante();
		
		cargarMesasCamarero();

	}
	
	/* Incluimos los mutex porque al tener m�s de un thread en ejecuci�n, 
	* si los dos acceden a este m�todo nos pintar�n las mismas mesas m�s de una vez
	*/
	public void cargarMesasRestaurante(){
		if (mutexMesasRestaurante.tryLock()){ // si el mutex no est� bloqueado, lo bloqueo
			
			panelMesas.removeAll();
			Iterator<Mesa> iteradorMesas = unRestaurante.getIteratorMesas();
			
			while(iteradorMesas.hasNext()){
				Mesa unaMesa = iteradorMesas.next();
				BotonMesa botonMesa = new BotonMesa(this, unRestaurante, unaMesa.getNumeroPersonas(), unaMesa.getIdMesa(), idCamarero);
				panelMesas.add(botonMesa, null);
			}
			panelMesasCamarero.validate();
			panelMesasCamarero.repaint();
			
			scrollpanelMesas.validate();
			scrollpanelMesas.repaint();
			
			mutexMesasRestaurante.unlock(); // desbloqueamos el mutex
		} else{
			System.err.println("Mutex ocupado");
			cargarMesasRestaurante(); // si el mutex esta cogido, rellamamos al metodo hasta que se desbloquee
		}
	}
	
	/* Incluimos los mutex porque al tener m�s de un thread en ejecuci�n, 
	* si los dos acceden a este m�todo nos pintar�n las mismas mesas m�s de una vez
	*/
	public void cargarMesasCamarero(){
		if (mutexMesasCamarero.tryLock()){ // si el mutex no est� bloqueado, lo bloqueo
			
			panelMesasCamarero.removeAll();
			Iterator<Mesa> iteradorMesasCamarero = unRestaurante.getIteratorMesas();
			
			while(iteradorMesasCamarero.hasNext()){
				Mesa unaMesa = iteradorMesasCamarero.next();
				if(unaMesa.getIdCamarero()!=null && unaMesa.getIdCamarero().equals(idCamarero) && !unaMesa.esMesaCerrada()){
					BotonMesa botonMesa = new BotonMesa(this, unRestaurante, unaMesa.getNumeroPersonas(), unaMesa.getIdMesa(), idCamarero);
					panelMesasCamarero.add(botonMesa, null);
				}
			}
			panelMesasCamarero.validate();
			panelMesasCamarero.repaint();
			
			scrollpanelMesasCamarero.validate();
			scrollpanelMesasCamarero.repaint();
			mutexMesasCamarero.unlock(); // desbloqueamos el mutex
			
		}else
			cargarMesasCamarero(); // si el mutex esta cogido, rellamamos al metodo hasta que se desbloquee
	}
	
	
	public void refrescarMesasPanel(){
		cargarMesasRestaurante();
		cargarMesasCamarero();
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

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Raton Drag");
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("Raton moviendose");
	}
	
}
