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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AsignarReporterosView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabEventos;
	private JTable tabDisponibles;
	private JTable tabAsignados;
	private JButton btnAsignar;
	private JButton btnCancelar;
	private JButton btnAceptar;
	
	private JLabel lblNombreAgencia;

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
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 89, 362, 257);
		contentPane.add(scrollPane);

		tabEventos = new JTable();
		scrollPane.setViewportView(tabEventos);

		lblNombreAgencia = new JLabel("Agencia de Prensa : Mi Agencia");
		lblNombreAgencia.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNombreAgencia.setBounds(10, 9, 633, 32);
		contentPane.add(lblNombreAgencia);

		JLabel lblNewLabel_1 = new JLabel("Eventos");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(10, 64, 81, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Reporteros Disponibles");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(382, 64, 208, 14);
		contentPane.add(lblNewLabel_2);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(382, 89, 244, 257);
		contentPane.add(scrollPane_1);

		tabDisponibles = new JTable();
		scrollPane_1.setViewportView(tabDisponibles);

		btnAsignar = new JButton("Asignar");
		btnAsignar.setBounds(382, 357, 244, 23);
		contentPane.add(btnAsignar);

		JLabel lblNewLabel_3 = new JLabel("Reporteros Asignados");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(632, 64, 242, 14);
		contentPane.add(lblNewLabel_3);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(636, 89, 238, 257);
		contentPane.add(scrollPane_2);

		tabAsignados = new JTable();
		scrollPane_2.setViewportView(tabAsignados);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancelar.setBounds(763, 527, 111, 23);
		contentPane.add(btnCancelar);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(636, 527, 117, 23);
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
	  public javax.swing.JLabel getLblTituloAgencia() { 
	        return this.lblNombreAgencia; 
	    }
}
