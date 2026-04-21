package app.controller;

import java.util.List;

import javax.swing.JOptionPane;

import app.dto.RevisarReportajeDTO;
import app.model.RevisarReportajeModel;
import app.view.RevisarReportajesView;
import giis.demo.util.SwingUtil;

public class RevisarReportajeController {

    private RevisarReportajeModel model;
    private RevisarReportajesView view;
    private String reportero;

    public RevisarReportajeController(RevisarReportajeModel model, RevisarReportajesView view, String reportero) {
        this.model = model;
        this.view = view;
        this.reportero = reportero;
        initController();
        cargarReportajes();
        this.view.setVisible(true);
    }

    
    private void initController() {
        view.addSeleccionReportajeListener(e -> {
            if (view.getComboReportajes().getSelectedItem() != null) {
                view.getComboReportajesRevisados().setSelectedIndex(-1);
            }
            mostrarDetalleSeleccionado();
        });
        view.addSeleccionReportajeRevisadoListener(e -> {
            if (view.getComboReportajesRevisados().getSelectedItem() != null) {
                view.getComboReportajes().setSelectedIndex(-1);
            }
            mostrarDetalleSeleccionado();
        });
        view.addAgregarComentarioListener(e -> agregarComentario());
        view.addMarcarRevisadoListener(e -> marcarRevisado());
    }

    private void cargarReportajes() {
        view.getComboReportajes().removeAllItems();
        view.getComboReportajesRevisados().removeAllItems();

        List<RevisarReportajeDTO> reportajes = model.getReportajesParaRevision(reportero);
        for (RevisarReportajeDTO r : reportajes) {
            view.getComboReportajes().addItem(r);
        }

        List<RevisarReportajeDTO> reportajesRevisados = model.getReportajesRevisadosPorReportero(reportero);
        for (RevisarReportajeDTO r : reportajesRevisados) {
            view.getComboReportajesRevisados().addItem(r);
        }

        if (view.getComboReportajes().getItemCount() > 0) {
            view.getComboReportajes().setSelectedIndex(0);
            view.getComboReportajesRevisados().setSelectedIndex(-1);
        } else if (view.getComboReportajesRevisados().getItemCount() > 0) {
            view.getComboReportajesRevisados().setSelectedIndex(0);
            view.getComboReportajes().setSelectedIndex(-1);
        }

        mostrarDetalleSeleccionado();
    }

    private void mostrarDetalleSeleccionado() {
        RevisarReportajeDTO seleccionado = getReportajeSeleccionado();
        if (seleccionado == null) {
            limpiarVista();
            return;
        }

        view.setTitulo(seleccionado.getTitulo());
        view.setSubtitulo(seleccionado.getSubtitulo());
        view.setCuerpo(seleccionado.getCuerpo());

        view.setImagenesDefinitivas(formatearLista(model.getMultimediaDefinitiva(seleccionado.getId_reportaje(), "IMAGEN"),
                "No hay imágenes definitivas."));
        view.setVideosDefinitivos(formatearLista(model.getMultimediaDefinitiva(seleccionado.getId_reportaje(), "VIDEO"),
                "No hay vídeos definitivos."));

        recargarComentarios(seleccionado.getId_reportaje());
    }

    private void recargarComentarios(int idReportaje) {
        List<RevisarReportajeDTO> comentarios = model.getComentariosRevision(idReportaje);
        if (comentarios.isEmpty()) {
            view.setComentarios("No hay comentarios de revisión.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (RevisarReportajeDTO c : comentarios) {
            sb.append("[").append(c.getFecha_hora()).append("] ")
              .append(c.getAutor_comentario()).append(":\n")
              .append(c.getComentario()).append("\n\n");
        }
        view.setComentarios(sb.toString());
 
    }

    private void agregarComentario() {
        RevisarReportajeDTO seleccionado = getReportajeSeleccionado();
        if (seleccionado == null) {
            SwingUtil.showMessage("Debes seleccionar un reportaje.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String comentario = view.getNuevoComentario();
        if (comentario == null || comentario.isBlank()) {
            SwingUtil.showMessage("Debes escribir un comentario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.guardarComentario(seleccionado.getId_reportaje(), reportero, comentario.trim());
        view.limpiarNuevoComentario();
        recargarComentarios(seleccionado.getId_reportaje());
   
    }

    private void marcarRevisado() {
        RevisarReportajeDTO seleccionado = (RevisarReportajeDTO) view.getComboReportajes().getSelectedItem();
        if (seleccionado == null) {
            SwingUtil.showMessage("Debes seleccionar un reportaje.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

       
        boolean reportajeRevisado = model.marcarRevisado(seleccionado.getId_reportaje(), reportero);
        if (reportajeRevisado) {
            SwingUtil.showMessage("Reportaje marcado como REVISADO.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            SwingUtil.showMessage("Tu revisión se ha guardado. El reportaje se marcará como REVISADO cuando terminen todos los reporteros asignados.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        }
        cargarReportajes();
    }

    private RevisarReportajeDTO getReportajeSeleccionado() {
        RevisarReportajeDTO pendiente = (RevisarReportajeDTO) view.getComboReportajes().getSelectedItem();
        if (pendiente != null) {
            return pendiente;
        }
        return (RevisarReportajeDTO) view.getComboReportajesRevisados().getSelectedItem();
    }

    private void limpiarVista() {
        view.setTitulo("");
        view.setSubtitulo("");
        view.setCuerpo("");
        view.setImagenesDefinitivas("");
        view.setVideosDefinitivos("");
        view.setComentarios("");
    }

    private String formatearLista(List<String> valores, String mensajeVacio) {
        if (valores == null || valores.isEmpty()) {
            return mensajeVacio;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valores.size(); i++) {
            sb.append(i + 1).append(". ").append(valores.get(i));
            if (i < valores.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
