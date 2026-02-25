package app.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;

public class AccederReportajeView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// Componentes públicos para que el controlador los manipule
	public JTable tableEventos;
	public JTextField txtTitulo;
	public JTextField txtSubtitulo;
	public JTextArea txtCuerpo;
	public JLabel lblFechaVersion;
	public JLabel lblHoraVersion;

	public AccederReportajeView() {
		setTitle("Visualización de Reportajes Autorizados");
		// Al cerrar esta ventana, solo se libera su memoria sin cerrar la app
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		setBounds(100, 100, 900, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// --- PANEL IZQUIERDO: SELECCIÓN DE EVENTO ---
		JLabel lblLista = new JLabel("Eventos con acceso concedido:");
		lblLista.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLista.setBounds(20, 20, 250, 14);
		contentPane.add(lblLista);

		JScrollPane scrollTable = new JScrollPane();
		scrollTable.setBounds(20, 45, 300, 450);
		contentPane.add(scrollTable);
		
		tableEventos = new JTable();
		scrollTable.setViewportView(tableEventos);

		// --- PANEL DERECHO: VISOR DE CONTENIDO (SOLO LECTURA) ---
		JLabel lblDetalle = new JLabel("Contenido del Reportaje (Última Versión)");
		lblDetalle.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDetalle.setBounds(350, 20, 300, 14);
		contentPane.add(lblDetalle);

		// Etiquetas para los metadatos de la última versión
		lblFechaVersion = new JLabel("Fecha: --/--/--");
		lblFechaVersion.setBounds(650, 20, 100, 14);
		contentPane.add(lblFechaVersion);

		lblHoraVersion = new JLabel("Hora: --:--");
		lblHoraVersion.setBounds(760, 20, 100, 14);
		contentPane.add(lblHoraVersion);

		JLabel lblTit = new JLabel("Título:");
		lblTit.setBounds(350, 50, 46, 14);
		contentPane.add(lblTit);

		txtTitulo = new JTextField();
		txtTitulo.setEditable(false); // Requisito: solo lectura
		txtTitulo.setBounds(350, 70, 500, 25);
		contentPane.add(txtTitulo);

		JLabel lblSub = new JLabel("Subtítulo:");
		lblSub.setBounds(350, 110, 80, 14);
		contentPane.add(lblSub);

		txtSubtitulo = new JTextField();
		txtSubtitulo.setEditable(false); // Requisito: solo lectura
		txtSubtitulo.setBounds(350, 130, 500, 25);
		contentPane.add(txtSubtitulo);

		JLabel lblCuerpo = new JLabel("Cuerpo:");
		lblCuerpo.setBounds(350, 170, 46, 14);
		contentPane.add(lblCuerpo);

		JScrollPane scrollCuerpo = new JScrollPane();
		scrollCuerpo.setBounds(350, 190, 500, 305);
		contentPane.add(scrollCuerpo);

		txtCuerpo = new JTextArea();
		txtCuerpo.setEditable(false); // Requisito: solo lectura
		txtCuerpo.setLineWrap(true);
		txtCuerpo.setWrapStyleWord(true);
		scrollCuerpo.setViewportView(txtCuerpo);
	}
}