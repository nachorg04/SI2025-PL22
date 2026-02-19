package app.controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import app.dto.gestionarOfrecimientosReportajesDTO;
import app.model.gestionarOfrecimientosReportajesModel;
import app.view.gestionarOfrecimientosReportajesView;
import giis.demo.util.SwingUtil;

public class gestionarOfrecimientosReportajesController {

    private gestionarOfrecimientosReportajesModel model;
    private gestionarOfrecimientosReportajesView view;
    private String nombreEmpresaActual;
    
    // Guardamos la lista actual para poder consultar el DTO cuando seleccionen una fila
    private List<gestionarOfrecimientosReportajesDTO> listaMostrada; 

    public gestionarOfrecimientosReportajesController(gestionarOfrecimientosReportajesModel m, gestionarOfrecimientosReportajesView v, String nombreEmpresa) {
        this.model = m;
        this.view = v;
        this.nombreEmpresaActual = nombreEmpresa;
        
        this.initView();
        this.initController();
    }

    private void initController() {
        // Botones de acción
        view.addAceptarListener(e -> tomarDecision("ACEPTADO"));
        view.addRechazarListener(e -> tomarDecision("RECHAZADO"));
        view.addEliminarDecisionListener(e -> tomarDecision("PENDIENTE")); // Volver a pendiente es eliminarla
        view.addVolverListener(e -> view.getFrame().dispose());
        
        // Listener para los Radio Buttons (Filtro)
        view.addFiltroListener(e -> cargarTabla());
        
        // --- LA MAGIA DEL BLOQUEO (Cumple el criterio de la HU) ---
        view.getTabOfrecimientos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // Solo ejecutamos si el usuario ha terminado de hacer clic
                if (!event.getValueIsAdjusting()) {
                    evaluarBloqueoBotones();
                }
            }
        });
    }

    public void initView() {
        view.setNombreEmpresa(nombreEmpresaActual);
        cargarTabla();
        view.getFrame().setVisible(true);
    }

    private void cargarTabla() {
        // 1. Miramos qué filtro está seleccionado
        if (view.getRdbtnPendientes().isSelected()) {
            listaMostrada = model.getOfrecimientosPendientes(nombreEmpresaActual);
            // Si estamos en PENDIENTES, el botón de eliminar no tiene sentido
            view.getBtnEliminarDecision().setVisible(false);
        } else {
            listaMostrada = model.getOfrecimientosConDecision(nombreEmpresaActual);
            // Si estamos viendo el historial, el botón de eliminar aparece
            view.getBtnEliminarDecision().setVisible(true);
        }
        
        // 2. Cargamos la tabla incluyendo el estado y la columna "accesoVisible" que fabricamos en el DTO
        TableModel tmodel = SwingUtil.getTableModelFromPojos(listaMostrada, 
                new String[] {"idEvento", "descripcionEvento", "fechaEvento", "nombreAgencia", "estado", "accesoVisible"});
        
        view.getTabOfrecimientos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabOfrecimientos());
        
        // 3. Al recargar la tabla, deseleccionamos todo y reseteamos los botones
        evaluarBloqueoBotones(); 
    }

    /**
     * Este método se ejecuta cada vez que el usuario hace clic en una fila.
     * Es el responsable de encender/apagar los botones según las reglas del profesor.
     */
    private void evaluarBloqueoBotones() {
        int filaSel = view.getTabOfrecimientos().getSelectedRow();
        
        // Si no hay nada seleccionado, apagamos todo y salimos
        if (filaSel == -1) {
            view.getBtnAceptar().setEnabled(false);
            view.getBtnRechazar().setEnabled(false);
            view.getBtnEliminarDecision().setEnabled(false);
            return;
        }

        // Recuperamos el DTO de la fila seleccionada usando nuestra lista guardada
        gestionarOfrecimientosReportajesDTO dtoSeleccionado = listaMostrada.get(filaSel);

        // CRITERIO DE ACEPTACIÓN: "Si se aceptó y ya se tiene acceso, no se podrá cambiar la decisión"
        boolean estaAceptado = "ACEPTADO".equals(dtoSeleccionado.getEstado());
        boolean tieneAcceso = dtoSeleccionado.getTieneAcceso() != null && dtoSeleccionado.getTieneAcceso() == 1;

        if (estaAceptado && tieneAcceso) {
            // ¡BLOQUEO TOTAL!
            view.getBtnAceptar().setEnabled(false);
            view.getBtnRechazar().setEnabled(false);
            view.getBtnEliminarDecision().setEnabled(false);
        } else {
            // Vía libre: El usuario puede usar los botones
            view.getBtnAceptar().setEnabled(true);
            view.getBtnRechazar().setEnabled(true);
            view.getBtnEliminarDecision().setEnabled(true);
        }
    }

    private void tomarDecision(String nuevaDecision) {
        String idEventoSeleccionado = SwingUtil.getSelectedKey(view.getTabOfrecimientos());
        if (idEventoSeleccionado.isEmpty()) return;

        // Actualizamos en la BD
        model.actualizarEstadoOfrecimiento(idEventoSeleccionado, nombreEmpresaActual, nuevaDecision);
        
        // Feedback al usuario (Excepto si fue "Eliminar decisión", que cambiamos el texto)
        if (nuevaDecision.equals("PENDIENTE")) {
            SwingUtil.showMessage("La decisión ha sido eliminada. El ofrecimiento vuelve a estar PENDIENTE.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            SwingUtil.showMessage("El ofrecimiento ha sido marcado como: " + nuevaDecision, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Refrescamos la tabla para que la fila desaparezca (o cambie de estado)
        cargarTabla();
    }
}