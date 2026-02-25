package app.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Color;

public class OfrecerReportajesView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// Definimos los componentes como public para que el Controller acceda a ellos
	public JTable tableEventos;
	public JTable tableEmpresas;
	public JList<String> listOfertasEnCurso;
	public JButton btnOfertar;
	public JButton btnCancelarEmpresas;
	public JButton btnAceptarTodo;
	public JButton btnCancelarTodo;
	
	// Punto 2: Etiqueta para mostrar el nombre de la agencia seleccionada
	public JLabel lblAgenciaSeleccionada;

	/**
	 * Create the frame.
	 */
	public OfrecerReportajesView() {
		setTitle("Gestión de Ofrecimientos de Reportajes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		setBounds(100, 100, 1110, 568);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Punto 2: Configuración de la etiqueta de la Agencia
		lblAgenciaSeleccionada = new JLabel("Agencia: Seleccione una agencia");
		lblAgenciaSeleccionada.setForeground(Color.BLACK);
		lblAgenciaSeleccionada.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAgenciaSeleccionada.setBounds(10, 5, 600, 24);
		contentPane.add(lblAgenciaSeleccionada);
		
		// Etiquetas de las columnas (ajustadas en Y para dejar espacio al título)
		JLabel lblEventos = new JLabel("Eventos con reporteros asignados");
		lblEventos.setBounds(10, 32, 240, 24);
		contentPane.add(lblEventos);
		
		JLabel lblEmpresas = new JLabel("Empresas comunicación (Sin ofertas)");
		lblEmpresas.setBounds(391, 37, 249, 14);
		contentPane.add(lblEmpresas);
		
		JLabel lblOfertas = new JLabel("Ofertas en curso");
		lblOfertas.setBounds(761, 37, 155, 14);
		contentPane.add(lblOfertas);
		
		// Columna 1: Eventos
		JScrollPane scrollPaneEventos = new JScrollPane();
		scrollPaneEventos.setBounds(10, 62, 313, 397);
		contentPane.add(scrollPaneEventos);
		
		tableEventos = new JTable();
		scrollPaneEventos.setViewportView(tableEventos);
		
		// Columna 2: Empresas
		JScrollPane scrollPaneEmpresas = new JScrollPane();
		scrollPaneEmpresas.setBounds(391, 62, 313, 397);
		contentPane.add(scrollPaneEmpresas);
		
		tableEmpresas = new JTable();
		scrollPaneEmpresas.setViewportView(tableEmpresas);
		
		// Columna 3: Lista Derecha
		JScrollPane scrollPaneOfertas = new JScrollPane();
		scrollPaneOfertas.setBounds(761, 62, 313, 397);
		contentPane.add(scrollPaneOfertas);
		
		listOfertasEnCurso = new JList<String>();
		scrollPaneOfertas.setViewportView(listOfertasEnCurso);
		
		// Botones Columna Central
		btnOfertar = new JButton("Ofertar");
		btnOfertar.setBounds(391, 468, 110, 30);
		contentPane.add(btnOfertar);
		
		btnCancelarEmpresas = new JButton("Cancelar");
		btnCancelarEmpresas.setBounds(594, 468, 110, 30);
		contentPane.add(btnCancelarEmpresas);
		
		// Botones Columna Derecha
		btnAceptarTodo = new JButton("Aceptar todo");
		btnAceptarTodo.setBounds(761, 468, 130, 30);
		contentPane.add(btnAceptarTodo);
		
		btnCancelarTodo = new JButton("Cancelar todo");
		btnCancelarTodo.setBounds(944, 468, 130, 30);
		contentPane.add(btnCancelarTodo);
	}
}