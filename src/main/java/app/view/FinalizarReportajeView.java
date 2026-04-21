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

import app.dto.FinalizarReportajeDTO;

public class FinalizarReportajeView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<FinalizarReportajeDTO> comboReportajes;
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea txtCuerpo;

    private JComboBox<FinalizarReportajeDTO> comboImagenes;
    private JComboBox<FinalizarReportajeDTO> comboVideos;
    private JTextField txtRutaImagen;
    private JTextField txtRutaVideo;

    private JTextArea txtReporterosPendientes;
    private JTextArea txtReporterosRevisados;
    private JTextArea txtComentarios;

    private JButton btnEliminarImagen;
    private JButton btnEliminarVideo;
    private JButton btnAnadirImagen;
    private JButton btnAnadirVideo;
    private JButton btnGuardarCambios;
    private JButton btnFinalizar;

    public FinalizarReportajeView() {
        setTitle("Finalizar reportaje");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1200, 760);
        setMinimumSize(new Dimension(1100, 700));

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel panelTop = new JPanel(new BorderLayout(8, 0));
        panelTop.add(new JLabel("Reportajes por finalizar:"), BorderLayout.WEST);
        comboReportajes = new JComboBox<>();
        panelTop.add(comboReportajes, BorderLayout.CENTER);
        contentPane.add(panelTop, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearPanelEntrega(), crearPanelRevision());
        split.setResizeWeight(0.58);
        contentPane.add(split, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardarCambios = new JButton("Guardar cambios");
        btnFinalizar = new JButton("Finalizar reportaje");
        panelBottom.add(btnGuardarCambios);
        panelBottom.add(btnFinalizar);
        contentPane.add(panelBottom, BorderLayout.SOUTH);
    }

    private JPanel crearPanelEntrega() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Entrega del reportaje"));

        JPanel panelCabecera = new JPanel(new GridLayout(2, 1, 0, 6));

        JPanel pTitulo = new JPanel(new BorderLayout(0, 4));
        pTitulo.add(new JLabel("Título"), BorderLayout.NORTH);
        txtTitulo = new JTextField();
        pTitulo.add(txtTitulo, BorderLayout.CENTER);
        panelCabecera.add(pTitulo);

        JPanel pSubtitulo = new JPanel(new BorderLayout(0, 4));
        pSubtitulo.add(new JLabel("Subtítulo"), BorderLayout.NORTH);
        txtSubtitulo = new JTextField();
        pSubtitulo.add(txtSubtitulo, BorderLayout.CENTER);
        panelCabecera.add(pSubtitulo);

        panel.add(panelCabecera, BorderLayout.NORTH);

        txtCuerpo = new JTextArea();
        txtCuerpo.setLineWrap(true);
        txtCuerpo.setWrapStyleWord(true);
        JScrollPane scrollCuerpo = new JScrollPane(txtCuerpo);

        JPanel panelMultimedia = new JPanel(new GridLayout(2, 1, 0, 8));
        panelMultimedia.add(crearPanelImagenes());
        panelMultimedia.add(crearPanelVideos());

        JSplitPane splitContenido = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCuerpo, panelMultimedia);
        splitContenido.setResizeWeight(0.58);
        splitContenido.setDividerLocation(300);
        panel.add(splitContenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelImagenes() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBorder(BorderFactory.createTitledBorder("Imágenes"));

        JPanel filaSeleccion = new JPanel(new BorderLayout(6, 0));
        comboImagenes = new JComboBox<>();
        btnEliminarImagen = new JButton("Eliminar seleccionada");
        filaSeleccion.add(comboImagenes, BorderLayout.CENTER);
        filaSeleccion.add(btnEliminarImagen, BorderLayout.EAST);

        JPanel filaAlta = new JPanel(new BorderLayout(6, 0));
        txtRutaImagen = new JTextField();
        btnAnadirImagen = new JButton("Añadir imagen");
        filaAlta.add(txtRutaImagen, BorderLayout.CENTER);
        filaAlta.add(btnAnadirImagen, BorderLayout.EAST);

        p.add(filaSeleccion, BorderLayout.NORTH);
        p.add(filaAlta, BorderLayout.SOUTH);
        return p;
    }

    private JPanel crearPanelVideos() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBorder(BorderFactory.createTitledBorder("Vídeos"));

        JPanel filaSeleccion = new JPanel(new BorderLayout(6, 0));
        comboVideos = new JComboBox<>();
        btnEliminarVideo = new JButton("Eliminar seleccionado");
        filaSeleccion.add(comboVideos, BorderLayout.CENTER);
        filaSeleccion.add(btnEliminarVideo, BorderLayout.EAST);

        JPanel filaAlta = new JPanel(new BorderLayout(6, 0));
        txtRutaVideo = new JTextField();
        btnAnadirVideo = new JButton("Añadir vídeo");
        filaAlta.add(txtRutaVideo, BorderLayout.CENTER);
        filaAlta.add(btnAnadirVideo, BorderLayout.EAST);

        p.add(filaSeleccion, BorderLayout.NORTH);
        p.add(filaAlta, BorderLayout.SOUTH);
        return p;
    }

    private JPanel crearPanelRevision() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Seguimiento de revisión"));

        JPanel panelEstado = new JPanel(new GridLayout(2, 1, 0, 8));

        JPanel pPendientes = new JPanel(new BorderLayout(0, 4));
        pPendientes.setBorder(BorderFactory.createTitledBorder("Reporteros pendientes"));
        txtReporterosPendientes = new JTextArea();
        txtReporterosPendientes.setEditable(false);
        pPendientes.add(new JScrollPane(txtReporterosPendientes), BorderLayout.CENTER);

        JPanel pRevisados = new JPanel(new BorderLayout(0, 4));
        pRevisados.setBorder(BorderFactory.createTitledBorder("Reporteros revisados"));
        txtReporterosRevisados = new JTextArea();
        txtReporterosRevisados.setEditable(false);
        pRevisados.add(new JScrollPane(txtReporterosRevisados), BorderLayout.CENTER);

        panelEstado.add(pPendientes);
        panelEstado.add(pRevisados);

        JPanel pComentarios = new JPanel(new BorderLayout(0, 4));
        pComentarios.setBorder(BorderFactory.createTitledBorder("Comentarios de reporteros asignados"));
        txtComentarios = new JTextArea();
        txtComentarios.setEditable(false);
        txtComentarios.setLineWrap(true);
        txtComentarios.setWrapStyleWord(true);
        pComentarios.add(new JScrollPane(txtComentarios), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelEstado, pComentarios);
        split.setResizeWeight(0.45);
        split.setDividerLocation(290);
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    public JComboBox<FinalizarReportajeDTO> getComboReportajes() {
        return comboReportajes;
    }

    public JComboBox<FinalizarReportajeDTO> getComboImagenes() {
        return comboImagenes;
    }

    public JComboBox<FinalizarReportajeDTO> getComboVideos() {
        return comboVideos;
    }

    public String getTitulo() {
        return txtTitulo.getText();
    }

    public String getSubtitulo() {
        return txtSubtitulo.getText();
    }

    public String getCuerpo() {
        return txtCuerpo.getText();
    }

    public String getRutaImagen() {
        return txtRutaImagen.getText();
    }

    public String getRutaVideo() {
        return txtRutaVideo.getText();
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

    public void setReporterosPendientes(String texto) {
        txtReporterosPendientes.setText(texto == null ? "" : texto);
    }

    public void setReporterosRevisados(String texto) {
        txtReporterosRevisados.setText(texto == null ? "" : texto);
    }

    public void setComentarios(String texto) {
        txtComentarios.setText(texto == null ? "" : texto);
    }

    public void setFinalizarEnabled(boolean enabled) {
        btnFinalizar.setEnabled(enabled);
    }

    public void limpiarCamposMultimedia() {
        txtRutaImagen.setText("");
        txtRutaVideo.setText("");
    }

    public void addSeleccionReportajeListener(ActionListener listener) {
        comboReportajes.addActionListener(listener);
    }

    public void addEliminarImagenListener(ActionListener listener) {
        btnEliminarImagen.addActionListener(listener);
    }

    public void addEliminarVideoListener(ActionListener listener) {
        btnEliminarVideo.addActionListener(listener);
    }

    public void addAnadirImagenListener(ActionListener listener) {
        btnAnadirImagen.addActionListener(listener);
    }

    public void addAnadirVideoListener(ActionListener listener) {
        btnAnadirVideo.addActionListener(listener);
    }

    public void addGuardarCambiosListener(ActionListener listener) {
        btnGuardarCambios.addActionListener(listener);
    }

    public void addFinalizarListener(ActionListener listener) {
        btnFinalizar.addActionListener(listener);
    }
}