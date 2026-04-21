package app.controller;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import app.dto.FinalizarReportajeDTO;
import app.model.FinalizarReportajeModel;
import app.view.FinalizarReportajeView;
import giis.demo.util.SwingUtil;

public class FinalizarReportajeController {

    private static final String TIPO_IMAGEN = "IMAGEN";
    private static final String TIPO_VIDEO = "VIDEO";

    private FinalizarReportajeModel model;
    private FinalizarReportajeView view;
    private String reporteroResponsable;

    public FinalizarReportajeController(FinalizarReportajeModel model, FinalizarReportajeView view, String reporteroResponsable) {
        this.model = model;
        this.view = view;
        this.reporteroResponsable = reporteroResponsable;
        initController();
        cargarReportajes();
        this.view.setVisible(true);
    }

    private void initController() {
        view.addSeleccionReportajeListener(e -> cargarDetalle());
        view.addGuardarCambiosListener(e -> guardarCambios());
        view.addAnadirImagenListener(e -> anadirMultimedia(TIPO_IMAGEN));
        view.addAnadirVideoListener(e -> anadirMultimedia(TIPO_VIDEO));
        view.addEliminarImagenListener(e -> eliminarMultimediaSeleccionada(view.getComboImagenes(), "imagen"));
        view.addEliminarVideoListener(e -> eliminarMultimediaSeleccionada(view.getComboVideos(), "vídeo"));
        view.addFinalizarListener(e -> finalizarReportaje());
    }

    private void cargarReportajes() {
        view.getComboReportajes().removeAllItems();
        List<FinalizarReportajeDTO> reportajes = model.getReportajesPorFinalizar(reporteroResponsable);
        for (FinalizarReportajeDTO reportaje : reportajes) {
            view.getComboReportajes().addItem(reportaje);
        }
        cargarDetalle();
    }

    private void cargarDetalle() {
        FinalizarReportajeDTO seleccionado = (FinalizarReportajeDTO) view.getComboReportajes().getSelectedItem();
        if (seleccionado == null || seleccionado.getId_reportaje() == null) {
            limpiarDetalle();
            return;
        }

        FinalizarReportajeDTO detalle = model.getDetalleReportaje(seleccionado.getId_reportaje(), reporteroResponsable);
        if (detalle == null) {
            limpiarDetalle();
            return;
        }

        view.setTitulo(detalle.getTitulo());
        view.setSubtitulo(detalle.getSubtitulo());
        view.setCuerpo(detalle.getCuerpo());

        recargarCombo(view.getComboImagenes(), model.getMultimedia(detalle.getId_reportaje(), TIPO_IMAGEN));
        recargarCombo(view.getComboVideos(), model.getMultimedia(detalle.getId_reportaje(), TIPO_VIDEO));

        List<FinalizarReportajeDTO> pendientes = model.getReporterosRevision(detalle.getId_reportaje(), false);
        List<FinalizarReportajeDTO> revisados = model.getReporterosRevision(detalle.getId_reportaje(), true);
        view.setReporterosPendientes(formatearReporteros(pendientes, "Todos han revisado."));
        view.setReporterosRevisados(formatearReporteros(revisados, "Ningún reportero ha revisado todavía."));

        List<FinalizarReportajeDTO> comentarios = model.getComentariosAsignados(detalle.getId_reportaje());
        view.setComentarios(formatearComentarios(comentarios));

        view.setFinalizarEnabled(model.puedeFinalizar(detalle.getId_reportaje()));
    }

    private void guardarCambios() {
        FinalizarReportajeDTO seleccionado = (FinalizarReportajeDTO) view.getComboReportajes().getSelectedItem();
        if (seleccionado == null || seleccionado.getId_reportaje() == null) {
            SwingUtil.showMessage("Debes seleccionar un reportaje.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = view.getTitulo().trim();
        String subtitulo = view.getSubtitulo().trim();
        String cuerpo = view.getCuerpo().trim();

        if (titulo.isEmpty() || subtitulo.isEmpty() || cuerpo.isEmpty()) {
            SwingUtil.showMessage("Título, subtítulo y cuerpo son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (model.existeTituloEnOtroReportaje(titulo, seleccionado.getId_reportaje())) {
            SwingUtil.showMessage("Ya existe otro reportaje con ese título.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.actualizarContenido(seleccionado.getId_reportaje(), titulo, subtitulo, cuerpo);
        SwingUtil.showMessage("Cambios guardados.", "Información", JOptionPane.INFORMATION_MESSAGE);
        cargarReportajes();
    }

    private void anadirMultimedia(String tipo) {
        FinalizarReportajeDTO seleccionado = (FinalizarReportajeDTO) view.getComboReportajes().getSelectedItem();
        if (seleccionado == null || seleccionado.getId_reportaje() == null) {
            SwingUtil.showMessage("Debes seleccionar un reportaje.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ruta = TIPO_IMAGEN.equals(tipo) ? view.getRutaImagen().trim() : view.getRutaVideo().trim();
        if (ruta.isEmpty()) {
            SwingUtil.showMessage("La ruta no puede estar vacía.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (model.existeRuta(ruta)) {
            SwingUtil.showMessage("La ruta indicada ya existe en otro archivo multimedia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.insertarMultimedia(seleccionado.getId_reportaje(), reporteroResponsable, tipo, ruta);
        view.limpiarCamposMultimedia();
        cargarDetalle();
    }

    private void eliminarMultimediaSeleccionada(JComboBox<FinalizarReportajeDTO> combo, String etiqueta) {
        FinalizarReportajeDTO multimedia = (FinalizarReportajeDTO) combo.getSelectedItem();
        if (multimedia == null || multimedia.getId_multimedia() == null) {
            SwingUtil.showMessage("Debes seleccionar un " + etiqueta + ".", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.eliminarMultimedia(multimedia.getId_multimedia());
        cargarDetalle();
    }

    private void finalizarReportaje() {
        FinalizarReportajeDTO seleccionado = (FinalizarReportajeDTO) view.getComboReportajes().getSelectedItem();
        if (seleccionado == null || seleccionado.getId_reportaje() == null) {
            SwingUtil.showMessage("Debes seleccionar un reportaje.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!model.puedeFinalizar(seleccionado.getId_reportaje())) {
            SwingUtil.showMessage("No se puede finalizar: faltan revisiones pendientes.", "Aviso", JOptionPane.WARNING_MESSAGE);
            cargarDetalle();
            return;
        }

        model.finalizarReportaje(seleccionado.getId_reportaje());
        SwingUtil.showMessage("Reportaje finalizado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        cargarReportajes();
    }

    private void limpiarDetalle() {
        view.setTitulo("");
        view.setSubtitulo("");
        view.setCuerpo("");
        view.getComboImagenes().removeAllItems();
        view.getComboVideos().removeAllItems();
        view.setReporterosPendientes("");
        view.setReporterosRevisados("");
        view.setComentarios("");
        view.setFinalizarEnabled(false);
    }

    private void recargarCombo(JComboBox<FinalizarReportajeDTO> combo, List<FinalizarReportajeDTO> elementos) {
        combo.removeAllItems();
        for (FinalizarReportajeDTO e : elementos) {
            combo.addItem(e);
        }
    }

    private String formatearReporteros(List<FinalizarReportajeDTO> reporteros, String mensajeVacio) {
        if (reporteros == null || reporteros.isEmpty()) {
            return mensajeVacio;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reporteros.size(); i++) {
            sb.append(i + 1).append(". ").append(reporteros.get(i).getNombre_reportero());
            if (i < reporteros.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String formatearComentarios(List<FinalizarReportajeDTO> comentarios) {
        if (comentarios == null || comentarios.isEmpty()) {
            return "No hay comentarios de revisión de reporteros asignados.";
        }

        StringBuilder sb = new StringBuilder();
        for (FinalizarReportajeDTO c : comentarios) {
            sb.append("[").append(c.getFecha_hora()).append("] ")
              .append(c.getAutor_comentario()).append(":\n")
              .append(c.getComentario()).append("\n\n");
        }
        return sb.toString();
    }
}