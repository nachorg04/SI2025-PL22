package app.controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import app.dto.gestionarOfrecimientosReportajesDTO;
import app.model.gestionarOfrecimientosReportajesModel;
import app.view.gestionarOfrecimientosReportajesView;
import giis.demo.util.SwingUtil;

public class gestionarOfrecimientosReportajesController {

    private gestionarOfrecimientosReportajesModel model;
    private gestionarOfrecimientosReportajesView view;
    private String nombreEmpresaActual;

    public gestionarOfrecimientosReportajesController(gestionarOfrecimientosReportajesModel m, gestionarOfrecimientosReportajesView v, String nombreEmpresa) {
        this.model = m;
        this.view = v;
        this.nombreEmpresaActual = nombreEmpresa;
        
        this.initView();
        this.initController();
    }

    private void initController() {
        view.addAceptarListener(e -> tomarDecision("ACEPTADO"));
        view.addRechazarListener(e -> tomarDecision("RECHAZADO"));
        view.addVolverListener(e -> view.getFrame().dispose());
    }

    public void initView() {
        view.setNombreEmpresa(nombreEmpresaActual);
        cargarTabla();
        view.getFrame().setVisible(true);
    }

    private void cargarTabla() {
        List<gestionarOfrecimientosReportajesDTO> pendientes = model.getOfrecimientosPendientes(nombreEmpresaActual);
        
        TableModel tmodel = SwingUtil.getTableModelFromPojos(pendientes, 
                new String[] {"idEvento", "descripcionEvento", "fechaEvento", "nombreAgencia"});
        
        view.getTabOfrecimientos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabOfrecimientos());
    }

    private void tomarDecision(String decision) {
        // 1. Obtenemos qué fila ha seleccionado el usuario
        int filaSeleccionada = view.getTabOfrecimientos().getSelectedRow();
        String idEventoSeleccionado = SwingUtil.getSelectedKey(view.getTabOfrecimientos());
        
        if (idEventoSeleccionado.isEmpty() || filaSeleccionada == -1) {
            SwingUtil.showMessage("Por favor, selecciona un ofrecimiento de la tabla superior primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. EXTRAER DATOS PARA EL HISTORIAL (Antes de recargar la tabla)
        // Sabemos que la columna 1 es la 'descripcionEvento' y la 3 es el 'nombreAgencia' según el array de cargarTabla()
        String descEvento = (String) view.getTabOfrecimientos().getValueAt(filaSeleccionada, 1);
        String agencia = (String) view.getTabOfrecimientos().getValueAt(filaSeleccionada, 3);

        // 3. Actualizamos en base de datos
        model.actualizarEstadoOfrecimiento(idEventoSeleccionado, nombreEmpresaActual, decision);
        
        // 4. AÑADIMOS LA DECISIÓN AL HISTORIAL VISUAL
        String etiquetaDecision = decision.equals("ACEPTADO") ? "✅ ACEPTADO" : "❌ RECHAZADO";
        view.agregarAlHistorial(etiquetaDecision, descEvento, agencia);
        
        // 5. Refrescamos la tabla superior (el ofrecimiento desaparecerá porque ya no es PENDIENTE)
        cargarTabla();
    }
}