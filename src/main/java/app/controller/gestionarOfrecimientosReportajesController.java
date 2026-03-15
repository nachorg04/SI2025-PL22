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
    
    private List<gestionarOfrecimientosReportajesDTO> listaMostrada; 
    
    // Bandera para evitar que la tabla se refresque a medias mientras rellenamos el ComboBox
    private boolean ajustandoDesplegable = false;

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
        view.addEliminarDecisionListener(e -> tomarDecision("PENDIENTE")); 
        view.addVolverListener(e -> view.getFrame().dispose());
        
        // Listeners de Filtros
        view.addFiltroEstadoListener(e -> cargarTabla());
        view.addChkEspecializacionListener(e -> cargarComboTematicas()); // Recarga el combo
        view.addComboTematicasListener(e -> cargarTabla()); // Filtra la tabla
        
        // Listener de la tabla para bloqueos
        view.getTabOfrecimientos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    evaluarBloqueoBotones();
                }
            }
        });
    }

    public void initView() {
        view.setNombreEmpresa(nombreEmpresaActual);
        cargarComboTematicas(); // Cargamos el combo por primera vez (esto ya llama a cargarTabla)
        view.getFrame().setVisible(true);
    }

    private void cargarComboTematicas() {
        ajustandoDesplegable = true; // Bloqueamos recargas de tabla innecesarias
        view.getComboTematicas().removeAllItems();
        view.getComboTematicas().addItem("Todas las temáticas");

        // Obtenemos los datos crudos del Modelo
        List<Object[]> filasBD;
        if (view.getChkEspecializacion().isSelected()) {
            filasBD = model.obtenerTematicasEmpresa(nombreEmpresaActual);
        } else {
            filasBD = model.obtenerTodasLasTematicas();
        }

        // LÓGICA MVC: El Controlador transforma Object[] a String
        for (Object[] fila : filasBD) {
            String nombreTematica = (String) fila[0];
            view.getComboTematicas().addItem(nombreTematica);
        }
        
        ajustandoDesplegable = false; // Desbloqueamos
        cargarTabla(); // Refrescamos la tabla con las nuevas opciones
    }

    private void cargarTabla() {
        if (ajustandoDesplegable) return; // Evita errores si el combo está vacío temporalmente
        
        String tematicaSeleccionada = (String) view.getComboTematicas().getSelectedItem();
        if (tematicaSeleccionada == null) tematicaSeleccionada = "Todas las temáticas";

        // 1. Miramos qué filtro de estado está seleccionado
        if (view.getRdbtnPendientes().isSelected()) {
            listaMostrada = model.getOfrecimientosPendientes(nombreEmpresaActual, tematicaSeleccionada);
            view.getBtnEliminarDecision().setVisible(false);
        } else {
            listaMostrada = model.getOfrecimientosConDecision(nombreEmpresaActual, tematicaSeleccionada);
            view.getBtnEliminarDecision().setVisible(true);
        }
        
        // 2. Cargamos la tabla
        TableModel tmodel = SwingUtil.getTableModelFromPojos(listaMostrada, 
                new String[] {"idEvento", "descripcionEvento", "fechaEvento", "nombreAgencia", "estado", "accesoVisible"});
        
        view.getTabOfrecimientos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabOfrecimientos());
        
        // 3. Reseteamos los botones
        evaluarBloqueoBotones(); 
    }

    private void evaluarBloqueoBotones() {
        int filaSel = view.getTabOfrecimientos().getSelectedRow();
        
        if (filaSel == -1 || listaMostrada == null || listaMostrada.size() <= filaSel) {
            view.getBtnAceptar().setEnabled(false);
            view.getBtnRechazar().setEnabled(false);
            view.getBtnEliminarDecision().setEnabled(false);
            return;
        }

        gestionarOfrecimientosReportajesDTO dtoSeleccionado = listaMostrada.get(filaSel);

        boolean estaAceptado = "ACEPTADO".equals(dtoSeleccionado.getEstado());
        boolean tieneAcceso = dtoSeleccionado.getTieneAcceso() != null && dtoSeleccionado.getTieneAcceso() == 1;

        if (estaAceptado && tieneAcceso) {
            view.getBtnAceptar().setEnabled(false);
            view.getBtnRechazar().setEnabled(false);
            view.getBtnEliminarDecision().setEnabled(false);
        } else {
            view.getBtnAceptar().setEnabled(true);
            view.getBtnRechazar().setEnabled(true);
            view.getBtnEliminarDecision().setEnabled(true);
        }
    }

    private void tomarDecision(String nuevaDecision) {
        String idEventoSeleccionado = SwingUtil.getSelectedKey(view.getTabOfrecimientos());
        if (idEventoSeleccionado.isEmpty()) return;

        model.actualizarEstadoOfrecimiento(idEventoSeleccionado, nombreEmpresaActual, nuevaDecision);
        
        if (nuevaDecision.equals("PENDIENTE")) {
            SwingUtil.showMessage("La decisión ha sido eliminada. El ofrecimiento vuelve a estar PENDIENTE.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            SwingUtil.showMessage("El ofrecimiento ha sido marcado como: " + nuevaDecision, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
        
        cargarTabla();
    }
}