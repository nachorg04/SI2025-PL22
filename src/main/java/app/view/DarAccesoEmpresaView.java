package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;

public class DarAccesoEmpresaView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabEventos;
	private JTable tabEmpresasDisponibles;
	private JTable tabEmpresasSeleccionadas;
	private JButton btnDarAcceso;
	private JButton btnCancelar;
	private JButton btnAceptar;

	// El JLabel arriba para poder cambiarlo desde el Controlador
	private JLabel lblNombreAgencia;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DarAccesoEmpresaView frame = new DarAccesoEmpresaView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DarAccesoEmpresaView() {
		// DISPOSE_ON_CLOSE para no cerrar toda la app al cerrar esta ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// --- COLUMNA 1: EVENTOS CON REPORTAJE ---
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 89, 380, 257);
		contentPane.add(scrollPane);

		tabEventos = new JTable();
		scrollPane.setViewportView(tabEventos);

		lblNombreAgencia = new JLabel("Agencia de Prensa : Mi Agencia");
		lblNombreAgencia.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNombreAgencia.setBounds(10, 9, 450, 22);
		contentPane.add(lblNombreAgencia);

		JLabel lblTituloEventos = new JLabel("Eventos con Reportaje entregado");
		lblTituloEventos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTituloEventos.setBounds(10, 64, 250, 14);
		contentPane.add(lblTituloEventos);

		// --- COLUMNA 2: EMPRESAS SIN ACCESO ---
		JLabel lblTituloEmpresas = new JLabel("Empresas sin acceso");
		lblTituloEmpresas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTituloEmpresas.setBounds(410, 64, 208, 14);
		contentPane.add(lblTituloEmpresas);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(410, 89, 244, 257);
		contentPane.add(scrollPane_1);

		tabEmpresasDisponibles = new JTable();
		scrollPane_1.setViewportView(tabEmpresasDisponibles);

		// --- BOTÓN CENTRAL ---
		btnDarAcceso = new JButton("Dar Acceso ->");
		btnDarAcceso.setBounds(410, 357, 244, 23);
		contentPane.add(btnDarAcceso);

		// --- COLUMNA 3: EMPRESAS SELECCIONADAS ---
		JLabel lblTituloSeleccionadas = new JLabel("Nuevos accesos a otorgar");
		lblTituloSeleccionadas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTituloSeleccionadas.setBounds(670, 64, 242, 14);
		contentPane.add(lblTituloSeleccionadas);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(670, 89, 238, 257);
		contentPane.add(scrollPane_2);

		tabEmpresasSeleccionadas = new JTable();
		scrollPane_2.setViewportView(tabEmpresasSeleccionadas);

		// --- BOTONES INFERIORES ---
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(797, 527, 111, 23);
		contentPane.add(btnCancelar);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(670, 527, 117, 23);
		contentPane.add(btnAceptar);
	}

	// GETTERS para el controlador
	public JTable getTabEventos() { return tabEventos; }
	public JTable getTabEmpresasDisponibles() { return tabEmpresasDisponibles; }
	public JTable getTabEmpresasSeleccionadas() { return tabEmpresasSeleccionadas; }
	public JButton getBtnDarAcceso() { return btnDarAcceso; }
	public JButton getBtnCancelar() { return btnCancelar; }
	public JButton getBtnAceptar() { return btnAceptar; }
	public JFrame getFrame() { return this; }
	public JLabel getLblTituloAgencia() { return this.lblNombreAgencia; }
}