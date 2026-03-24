package app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import app.dto.RevisarReportajeDTO;

public class RevisarReportajesView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<RevisarReportajeDTO> comboReportajes;
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea txtCuerpo;

    private JTextArea txtImagenesDefinitivas;
    private JTextArea txtVideosDefinitivos;

    private JTextArea txtComentarios;
    private JTextArea txtNuevoComentario;

    private JButton btnAgregarComentario;
    private JButton btnMarcarRevisado;

    public RevisarReportajesView() {
        setTitle("Revisar reportajes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1100, 700);
        setMinimumSize(new Dimension(1000, 650));

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel panelTop = new JPanel(new BorderLayout(8, 0));
        panelTop.add(new JLabel("Reportaje a revisar:"), BorderLayout.WEST);
        comboReportajes = new JComboBox<>();
        panelTop.add(comboReportajes, BorderLayout.CENTER);
        contentPane.add(panelTop, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCenter.add(crearPanelDetalle());
        panelCenter.add(crearPanelRevision());
        contentPane.add(panelCenter, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnMarcarRevisado = new JButton("Marcar como revisado");
        panelBottom.add(btnMarcarRevisado);
        contentPane.add(panelBottom, BorderLayout.SOUTH);
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Contenido del reportaje"));

        JPanel panelCabecera = new JPanel(new GridLayout(2, 1, 0, 6));

        JPanel pTitulo = new JPanel(new BorderLayout(0, 4));
        pTitulo.add(new JLabel("Título"), BorderLayout.NORTH);
        txtTitulo = new JTextField();
        txtTitulo.setEditable(false);
        pTitulo.add(txtTitulo, BorderLayout.CENTER);
        panelCabecera.add(pTitulo);

        JPanel pSubtitulo = new JPanel(new BorderLayout(0, 4));
        pSubtitulo.add(new JLabel("Subtítulo"), BorderLayout.NORTH);
        txtSubtitulo = new JTextField();
        txtSubtitulo.setEditable(false);
        pSubtitulo.add(txtSubtitulo, BorderLayout.CENTER);
        panelCabecera.add(pSubtitulo);

        panel.add(panelCabecera, BorderLayout.NORTH);

        txtCuerpo = new JTextArea();
        txtCuerpo.setLineWrap(true);
        txtCuerpo.setWrapStyleWord(true);
        txtCuerpo.setEditable(false);
        JScrollPane scrollCuerpo = new JScrollPane(txtCuerpo);

        JPanel panelMultimedia = new JPanel(new GridLayout(1, 2, 8, 8));

        JPanel pImg = new JPanel(new BorderLayout(0, 4));
        pImg.setBorder(BorderFactory.createTitledBorder("Imágenes definitivas (texto)"));
        txtImagenesDefinitivas = new JTextArea();
        txtImagenesDefinitivas.setEditable(false);
        txtImagenesDefinitivas.setLineWrap(true);
        txtImagenesDefinitivas.setWrapStyleWord(true);
        pImg.add(new JScrollPane(txtImagenesDefinitivas), BorderLayout.CENTER);

        JPanel pVid = new JPanel(new BorderLayout(0, 4));
        pVid.setBorder(BorderFactory.createTitledBorder("Vídeos definitivos (texto)"));
        txtVideosDefinitivos = new JTextArea();
        txtVideosDefinitivos.setEditable(false);
        txtVideosDefinitivos.setLineWrap(true);
        txtVideosDefinitivos.setWrapStyleWord(true);
        pVid.add(new JScrollPane(txtVideosDefinitivos), BorderLayout.CENTER);

        panelMultimedia.add(pImg);
        panelMultimedia.add(pVid);

        JSplitPane splitContenido = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCuerpo, panelMultimedia);
        splitContenido.setResizeWeight(0.32); // reduce altura de cuerpo y da más espacio a multimedia
        splitContenido.setDividerLocation(150);
        panel.add(splitContenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelRevision() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Revisión"));

        txtComentarios = new JTextArea();
        txtComentarios.setEditable(false);
        txtComentarios.setLineWrap(true);
        txtComentarios.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtComentarios), BorderLayout.CENTER);

        JPanel panelNuevo = new JPanel(new BorderLayout(0, 6));
        panelNuevo.setBorder(BorderFactory.createTitledBorder("Nuevo comentario"));
        txtNuevoComentario = new JTextArea(4, 20);
        txtNuevoComentario.setLineWrap(true);
        txtNuevoComentario.setWrapStyleWord(true);
        panelNuevo.add(new JScrollPane(txtNuevoComentario), BorderLayout.CENTER);

        btnAgregarComentario = new JButton("Agregar comentario");
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBtn.add(btnAgregarComentario);
        panelNuevo.add(pBtn, BorderLayout.SOUTH);

        panel.add(panelNuevo, BorderLayout.SOUTH);

        return panel;
    }

    public JComboBox<RevisarReportajeDTO> getComboReportajes() {
        return comboReportajes;
    }

    public void setTitulo(String titulo) {
        txtTitulo.setText(titulo == null ? "" : titulo);
    }

    public void setSubtitulo(String subtitulo) {
        txtSubtitulo.setText(subtitulo == null ? "" : subtitulo);
    }

    public void setCuerpo(String cuerpo) {
        txtCuerpo.setText(cuerpo == null ? "" : cuerpo);
    }

    public void setImagenesDefinitivas(String texto) {
        txtImagenesDefinitivas.setText(texto == null ? "" : texto);
    }

    public void setVideosDefinitivos(String texto) {
        txtVideosDefinitivos.setText(texto == null ? "" : texto);
    }

    public void setComentarios(String texto) {
        txtComentarios.setText(texto == null ? "" : texto);
    }

    public String getNuevoComentario() {
        return txtNuevoComentario.getText();
    }

    public void limpiarNuevoComentario() {
        txtNuevoComentario.setText("");
    }

    public void addSeleccionReportajeListener(ActionListener listener) {
        comboReportajes.addActionListener(listener);
    }

    public void addAgregarComentarioListener(ActionListener listener) {
        btnAgregarComentario.addActionListener(listener);
    }

    public void addMarcarRevisadoListener(ActionListener listener) {
        btnMarcarRevisado.addActionListener(listener);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new RevisarReportajesView().setVisible(true));
    }
}