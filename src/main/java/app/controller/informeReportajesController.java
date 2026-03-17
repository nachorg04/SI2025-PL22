package app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import app.dto.informeReportajesDTO;
import app.model.informeReportajesModel;
import app.view.informeReportajesView;
import giis.demo.util.SwingUtil;

public class informeReportajesController {

    private informeReportajesModel model;
    private informeReportajesView view;
    private String nombreEmpresaActual;
    
    // Formateadores de fecha
    private SimpleDateFormat formatoUI = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");

    public informeReportajesController(informeReportajesModel m, informeReportajesView v, String nombreEmpresa) {
        this.model = m;
        this.view = v;
        this.nombreEmpresaActual = nombreEmpresa;
        
        // Hacemos que sea obligatorio meter la fecha con el formato exacto
        formatoUI.setLenient(false); 
        
        this.initView();
        this.initController();
    }

    private void initController() {
        view.addGenerarInformeListener(e -> generarInforme());
    }

    public void initView() {
        view.setNombreEmpresa(nombreEmpresaActual);
        view.getFrame().setVisible(true);
    }

    private void generarInforme() {
        String txtInicio = view.getTxtFechaInicio().getText().trim();
        String txtFin = view.getTxtFechaFin().getText().trim();
        
        if (txtInicio.isEmpty() || txtFin.isEmpty()) {
            SwingUtil.showMessage("Por favor, rellene ambas fechas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fechaInicioBD = "";
        String fechaFinBD = "";

        // 1. Transformar las fechas de DD/MM/YYYY a YYYY-MM-DD
        try {
            Date dateInicio = formatoUI.parse(txtInicio);
            Date dateFin = formatoUI.parse(txtFin);
            
            if (dateInicio.after(dateFin)) {
                SwingUtil.showMessage("La fecha de inicio no puede ser posterior a la fecha de fin.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            fechaInicioBD = formatoBD.format(dateInicio);
            fechaFinBD = formatoBD.format(dateFin);
            
        } catch (ParseException ex) {
            SwingUtil.showMessage("Formato de fecha incorrecto. Use DD/MM/AAAA.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Ejecutar la consulta en el modelo
        List<informeReportajesDTO> listaInforme = model.obtenerInforme(nombreEmpresaActual, fechaInicioBD, fechaFinBD);
        
        if (listaInforme.isEmpty()) {
            SwingUtil.showMessage("No se encontraron reportajes con acceso en ese rango de fechas.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }

        // 3. Cargar la tabla
        TableModel tmodel = SwingUtil.getTableModelFromPojos(listaInforme, 
                new String[] {"tituloReportaje", "evento", "fechaEvento", "precio"});
        view.getTabInforme().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabInforme());
        
        // 4. Calcular el PRECIO TOTAL SUMADO (Criterio de Raquel)
        double precioTotal = 0.0;
        for (informeReportajesDTO dto : listaInforme) {
            if (dto.getPrecio() != null) {
                precioTotal += dto.getPrecio();
            }
        }
        
        // 5. Actualizar la etiqueta del total con 2 decimales
        view.setPrecioTotal(String.format("💰 PRECIO TOTAL DEL INFORME: %.2f €", precioTotal));
    }
}