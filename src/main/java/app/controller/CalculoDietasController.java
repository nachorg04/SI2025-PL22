package app.controller;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JOptionPane;

import app.dto.CalculoDietasDTO;
import app.model.CalculoDietasModel;
import app.view.CalculoDietasView;
import giis.demo.util.SwingUtil;

public class CalculoDietasController {

    private CalculoDietasModel model;
    private CalculoDietasView view;
    private String nombreReportero;
    private DecimalFormat df = new DecimalFormat("0.00");

    public CalculoDietasController(CalculoDietasModel model, CalculoDietasView view, String nombreReportero) {
        this.model = model;
        this.view = view;
        this.nombreReportero = nombreReportero;

        initController();
        cargarReportajes();
        this.view.setVisible(true);
    }

    private void initController() {
        view.addCalcularListener(e -> calcularTotalDietas());
    }

    private void cargarReportajes() {
        view.getComboReportajes().removeAllItems();
        List<CalculoDietasDTO> reportajes = model.getReportajesAsignados(nombreReportero);
        for (CalculoDietasDTO reportaje : reportajes) {
            view.getComboReportajes().addItem(reportaje);
        }

        if (view.getComboReportajes().getItemCount() == 0) {
            SwingUtil.showMessage("No hay reportajes asignados para el reportero seleccionado.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void calcularTotalDietas() {
        CalculoDietasDTO seleccionado = (CalculoDietasDTO) view.getComboReportajes().getSelectedItem();
        if (seleccionado == null || seleccionado.getId_reportaje() == null) {
            SwingUtil.showMessage("Debes seleccionar un reportaje asignado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CalculoDietasDTO detalle = model.getDetalleDietaReportaje(seleccionado.getId_reportaje(), nombreReportero);
        if (detalle == null) {
            SwingUtil.showMessage("No se pudieron recuperar los datos de dietas para ese reportaje.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = model.calcularTotal(detalle);

        view.setCosteDia(df.format(detalle.getCoste_dia() == null ? 0 : detalle.getCoste_dia()));
        view.setDietaAlojamiento(df.format(detalle.getDieta_alojamiento() == null ? 0 : detalle.getDieta_alojamiento()));
        view.setDietaManutencion(df.format(detalle.getDieta_manutencion() == null ? 0 : detalle.getDieta_manutencion()));
        view.setNumeroDias(String.valueOf(detalle.getNumero_dias() == null ? 1 : detalle.getNumero_dias()));
        view.setTotal(df.format(total));
    }
}