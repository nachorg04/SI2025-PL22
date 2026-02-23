package app.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

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

	/**
	 * Create the frame.
	 */
	public OfrecerReportajesView() {
		setTitle("Gestión de Ofrecimientos de Reportajes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Mejor DISPOSE que EXIT para no cerrar toda la app
		setBounds(100, 100, 1110, 568);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Etiquetas
		JLabel lblEventos = new JLabel("Eventos con reporteros asignados");
		lblEventos.setBounds(10, 22, 240, 24);
		contentPane.add(lblEventos);
		
		JLabel lblEmpresas = new JLabel("Empresas comunicación (Sin ofertas)");
		lblEmpresas.setBounds(391, 27, 249, 14);
		contentPane.add(lblEmpresas);
		
		JLabel lblOfertas = new JLabel("Ofertas en curso");
		lblOfertas.setBounds(761, 27, 155, 14);
		contentPane.add(lblOfertas);
		
		// Columna 1: Eventos
		JScrollPane scrollPaneEventos = new JScrollPane();
		scrollPaneEventos.setBounds(10, 52, 313, 407);
		contentPane.add(scrollPaneEventos);
		
		tableEventos = new JTable();
		scrollPaneEventos.setViewportView(tableEventos);
		
		// Columna 2: Empresas
		JScrollPane scrollPaneEmpresas = new JScrollPane();
		scrollPaneEmpresas.setBounds(391, 52, 313, 407);
		contentPane.add(scrollPaneEmpresas);
		
		tableEmpresas = new JTable();
		scrollPaneEmpresas.setViewportView(tableEmpresas);
		
		// Columna 3: Lista Derecha
		JScrollPane scrollPaneOfertas = new JScrollPane();
		scrollPaneOfertas.setBounds(761, 52, 313, 407);
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