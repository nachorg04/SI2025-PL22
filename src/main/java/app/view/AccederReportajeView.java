package app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AccederReportajeView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	// Componentes publicos para que el controlador los gestione
	public JTable tableEventos;
	public JTextField txtTitulo;
	public JTextField txtSubtitulo;
	public JTextArea txtCuerpo;
	public JLabel lblFechaVersion;
	public JLabel lblHoraVersion;
	public JLabel lblNombreEmpresa;
	public JLabel lblEstadoAcceso;
	public JLabel lblTipoAcceso;
	public JLabel lblFechaAccesoEmbargo;
	public JLabel lblPreviewFotos;
	public JLabel lblPreviewVideos;
	public JButton btnDescargarJson;

	public AccederReportajeView() {
		setTitle("Visualizacion de Reportajes Autorizados");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1180, 720);
		setMinimumSize(new Dimension(1080, 680));

		contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JPanel panelCabecera = new JPanel(new BorderLayout());
		lblNombreEmpresa = new JLabel("EMPRESA: ");
		lblNombreEmpresa.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelCabecera.add(lblNombreEmpresa, BorderLayout.WEST);
		contentPane.add(panelCabecera, BorderLayout.NORTH);

		JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPrincipal.setResizeWeight(0.28);
		splitPrincipal.setDividerLocation(320);
		contentPane.add(splitPrincipal, BorderLayout.CENTER);

		splitPrincipal.setLeftComponent(crearPanelEventos());
		splitPrincipal.setRightComponent(crearPanelDetalle());
	}

	private JPanel crearPanelEventos() {
		JPanel panelEventos = new JPanel(new BorderLayout(0, 8));

		JLabel lblLista = new JLabel("REPORTAJES AUTORIZADOS");
		lblLista.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelEventos.add(lblLista, BorderLayout.NORTH);

		tableEventos = new JTable();
		tableEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableEventos.setDefaultEditor(Object.class, null);
		panelEventos.add(new JScrollPane(tableEventos), BorderLayout.CENTER);

		return panelEventos;
	}

	private JPanel crearPanelDetalle() {
		JPanel panelDetalle = new JPanel(new BorderLayout(0, 10));

		JPanel panelTitulo = new JPanel(new BorderLayout(10, 0));
		JLabel lblDetalle = new JLabel("ACCESO AL REPORTAJE");
		lblDetalle.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelTitulo.add(lblDetalle, BorderLayout.WEST);

		JPanel panelMetadatos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		lblFechaVersion = new JLabel("Fecha: --/--/----");
		lblHoraVersion = new JLabel("Hora: --:--");
		panelMetadatos.add(lblFechaVersion);
		panelMetadatos.add(lblHoraVersion);
		panelTitulo.add(panelMetadatos, BorderLayout.EAST);

		panelDetalle.add(panelTitulo, BorderLayout.NORTH);

		JSplitPane splitDetalle = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitDetalle.setResizeWeight(0.58);
		splitDetalle.setDividerLocation(320);
		splitDetalle.setTopComponent(crearPanelTexto());
		splitDetalle.setBottomComponent(crearPanelMultimedia());

		panelDetalle.add(splitDetalle, BorderLayout.CENTER);

		JPanel panelInferior = new JPanel(new BorderLayout(0, 8));
		panelInferior.add(crearPanelEstadoAcceso(), BorderLayout.CENTER);
		panelInferior.add(crearPanelAcciones(), BorderLayout.SOUTH);
		panelDetalle.add(panelInferior, BorderLayout.SOUTH);

		return panelDetalle;
	}

	private JPanel crearPanelEstadoAcceso() {
		JPanel panelEstado = new JPanel(new GridLayout(3, 1, 0, 4));
		panelEstado.setBorder(BorderFactory.createTitledBorder("Estado de acceso por embargo"));

		lblEstadoAcceso = new JLabel("Estado de acceso: Acceso completo disponible");
		lblTipoAcceso = new JLabel("Tipo de acceso: Normal");
		lblFechaAccesoEmbargo = new JLabel("Fecha de acceso por embargo: -");

		panelEstado.add(lblEstadoAcceso);
		panelEstado.add(lblTipoAcceso);
		panelEstado.add(lblFechaAccesoEmbargo);
		return panelEstado;
	}

	private JPanel crearPanelTexto() {
		JPanel panelTexto = new JPanel(new BorderLayout(0, 8));
		panelTexto.setBorder(BorderFactory.createTitledBorder("Texto visible del reportaje"));

		JPanel panelCampos = new JPanel(new GridLayout(2, 1, 0, 6));

		JPanel panelTitulo = new JPanel(new BorderLayout(0, 4));
		panelTitulo.add(new JLabel("TITULO:"), BorderLayout.NORTH);
		txtTitulo = new JTextField();
		txtTitulo.setEditable(false);
		panelTitulo.add(txtTitulo, BorderLayout.CENTER);
		panelCampos.add(panelTitulo);

		JPanel panelSubtitulo = new JPanel(new BorderLayout(0, 4));
		panelSubtitulo.add(new JLabel("SUBTITULO:"), BorderLayout.NORTH);
		txtSubtitulo = new JTextField();
		txtSubtitulo.setEditable(false);
		panelSubtitulo.add(txtSubtitulo, BorderLayout.CENTER);
		panelCampos.add(panelSubtitulo);

		panelTexto.add(panelCampos, BorderLayout.NORTH);

		JPanel panelCuerpo = new JPanel(new BorderLayout(0, 4));
		panelCuerpo.add(new JLabel("CUERPO:"), BorderLayout.NORTH);
		txtCuerpo = new JTextArea();
		txtCuerpo.setEditable(false);
		txtCuerpo.setLineWrap(true);
		txtCuerpo.setWrapStyleWord(true);
		panelCuerpo.add(new JScrollPane(txtCuerpo), BorderLayout.CENTER);
		panelTexto.add(panelCuerpo, BorderLayout.CENTER);

		return panelTexto;
	}

	private JPanel crearPanelMultimedia() {
		JPanel panelMultimedia = new JPanel(new BorderLayout());
		panelMultimedia.setBorder(BorderFactory.createTitledBorder("Multimedia asociado"));

		JPanel panelPreviews = new JPanel(new GridLayout(1, 2, 10, 0));
		panelPreviews.add(crearPanelPreviewFotos());
		panelPreviews.add(crearPanelPreviewVideos());
		panelMultimedia.add(panelPreviews, BorderLayout.CENTER);

		return panelMultimedia;
	}

	private JPanel crearPanelPreviewFotos() {
		JPanel panelFotos = new JPanel(new BorderLayout());
		panelFotos.setBorder(BorderFactory.createTitledBorder("Fotos"));

		lblPreviewFotos = new JLabel(
			"<html><div style='text-align:center; padding:12px;'>"
				+ "Aqui se mostraran las fotos<br/>"
				+ "si el acceso lo permite."
				+ "</div></html>",
			SwingConstants.CENTER);
		lblPreviewFotos.setBorder(BorderFactory.createEtchedBorder());
		panelFotos.add(lblPreviewFotos, BorderLayout.CENTER);

		return panelFotos;
	}

	private JPanel crearPanelPreviewVideos() {
		JPanel panelVideos = new JPanel(new BorderLayout());
		panelVideos.setBorder(BorderFactory.createTitledBorder("Videos"));

		lblPreviewVideos = new JLabel(
			"<html><div style='text-align:center; padding:12px;'>"
				+ "Aqui se mostraran los videos<br/>"
				+ "si el acceso lo permite."
				+ "</div></html>",
			SwingConstants.CENTER);
		lblPreviewVideos.setBorder(BorderFactory.createEtchedBorder());
		panelVideos.add(lblPreviewVideos, BorderLayout.CENTER);

		return panelVideos;
	}

	private JPanel crearPanelAcciones() {
		JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnDescargarJson = new JButton("Descargar reportaje JSON");
		panelAcciones.add(btnDescargarJson);
		return panelAcciones;
	}
}
