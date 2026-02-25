package app.controller;

import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.model.AccederReportajeModel;
import app.view.AccederReportajeView;
import app.dto.AccederReportajeDTO;
import giis.demo.util.SwingUtil;

public class AccederReportajeController {

    private AccederReportajeModel model;
    private AccederReportajeView view;
    private String nombreEmpresa;

    public AccederReportajeController(AccederReportajeModel m, AccederReportajeView v, String empresa) {
        this.model = m;
        this.view = v;
        this.nombreEmpresa = empresa;
        this.initView();
    }

    public void initView() {
        // Cargar la lista inicial de eventos con acceso concedido
        this.cargarEventos();
        
        // Listener para detectar cuándo el usuario selecciona un evento en la tabla
        view.tableEventos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Evita que se ejecute dos veces por clic
                    cargarDetalleReportaje();
                }
            }
        });

        view.setVisible(true);
    }

    private void cargarEventos() {
        // Filtramos por la empresa que viene del login
        List<AccederReportajeDTO> eventos = model.getEventosConAcceso(nombreEmpresa);
        String[] columnas = {"id_evento", "nombre_evento"};
        view.tableEventos.setModel(SwingUtil.getTableModelFromPojos(eventos, columnas));
    }

    private void cargarDetalleReportaje() {
        int row = view.tableEventos.getSelectedRow();
        if (row != -1) {
            // Obtener el ID del evento seleccionado
            int idEvento = (int) view.tableEventos.getValueAt(row, 0);
            
            // Pedir al modelo la ÚLTIMA versión
            AccederReportajeDTO detalle = model.getDetalleUltimaVersion(idEvento);
            
            if (detalle != null) {
                // Rellenar los campos no editables de la vista
                view.txtTitulo.setText(detalle.getTitulo());
                view.txtSubtitulo.setText(detalle.getSubtitulo());
                view.txtCuerpo.setText(detalle.getCuerpo());
                view.lblFechaVersion.setText("Fecha: " + detalle.getFecha());
                view.lblHoraVersion.setText("Hora: " + detalle.getHora());
            }
        }
    }
}