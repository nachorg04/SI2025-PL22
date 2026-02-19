package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JButton;

public class AsignarReporterosView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabEventos;
	private JTable tabDisponibles;
	private JTable tabAsignados;
	private JButton btnAsignar;
	private JButton btnCancelar;
	private JButton btnAceptar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AsignarReporterosView frame = new AsignarReporterosView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AsignarReporterosView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 52, 130, 142);
		contentPane.add(scrollPane);

		tabEventos = new JTable();
		scrollPane.setViewportView(tabEventos);

		JLabel lblNewLabel = new JLabel("Agencia de Prensa : Mi Agencia");
		lblNewLabel.setBounds(10, 9, 253, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Eventos");
		lblNewLabel_1.setBounds(10, 34, 81, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Reporteros Disponibles");
		lblNewLabel_2.setBounds(147, 34, 116, 14);
		contentPane.add(lblNewLabel_2);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(147, 52, 116, 142);
		contentPane.add(scrollPane_1);

		tabDisponibles = new JTable();
		scrollPane_1.setViewportView(tabDisponibles);

		btnAsignar = new JButton("Asignar");
		btnAsignar.setBounds(147, 199, 116, 23);
		contentPane.add(btnAsignar);

		JLabel lblNewLabel_3 = new JLabel("Reporteros Asignados");
		lblNewLabel_3.setBounds(273, 34, 130, 14);
		contentPane.add(lblNewLabel_3);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(273, 52, 116, 142);
		contentPane.add(scrollPane_2);

		tabAsignados = new JTable();
		scrollPane_2.setViewportView(tabAsignados);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(345, 238, 89, 23);
		contentPane.add(btnCancelar);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(246, 238, 89, 23);
		contentPane.add(btnAceptar);

	}
	public JTable getTabEventos() {
		return tabEventos;
	}
	public JTable getTabDisponibles() {
		return tabDisponibles;
	}
	public JTable getTabAsignados() {
		return tabAsignados;
	}
	public JButton getBtnAsignar() {
		return btnAsignar;
	}
	public JButton getBtnCancelar() {
		return btnCancelar;
	}
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	public javax.swing.JFrame getFrame() {
		return this; 
	}
}
