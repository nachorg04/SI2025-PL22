package app.controller;

import giis.demo.util.SwingUtil;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import app.dto.EventoDisplayDTO;
import app.dto.ReporteroDisplayDTO;
import app.model.AsignacionReporterosModel;
import app.view.AsignarReporterosView;

public class AsignacionReporterosController {

	private String nombreAgencia;
	private AsignacionReporterosModel model;
	private AsignarReporterosView view;
	private List<ReporteroDisplayDTO> reporterosAsignadosVisualmente;
	private List<ReporteroDisplayDTO> reporterosDisponiblesVisualmente;

	public AsignacionReporterosController(AsignacionReporterosModel m, AsignarReporterosView v, String nombreAgencia) {
		this.model = m;
		this.view = v;
		this.nombreAgencia = nombreAgencia; // Guardamos el nombre de la agencia seleccionada
		this.reporterosAsignadosVisualmente = new ArrayList<>();
		this.initView();
	}

	public void initController() {
		// Añade el manejador de eventos usando el exceptionWrapper del profesor
		view.getTabEventos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> cargarReporterosDisponibles());
			}
		});

		view.getBtnAsignar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> moverReporteroAAsignados()));
		view.getBtnAceptar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> confirmarAsignacion()));
		view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose()); 
	}

	public void initView() {
		List<app.dto.EventoDisplayDTO> eventos = model.getEventosSinAsignar(nombreAgencia);


		javax.swing.table.TableModel tmodel = giis.demo.util.SwingUtil.getTableModelFromPojos(eventos, new String[]{"idEvento", "descripcion", "fecha"});
		view.getTabEventos().setModel(tmodel);
		giis.demo.util.SwingUtil.autoAdjustColumns(view.getTabEventos());

		// Ocultamos el ID de los eventos para que la tabla quede limpia
		view.getTabEventos().getColumnModel().getColumn(0).setMinWidth(0);
		view.getTabEventos().getColumnModel().getColumn(0).setMaxWidth(0);
		view.getTabEventos().getColumnModel().getColumn(0).setWidth(0);

		// Mejoras visuales (opcional)
		view.getTabEventos().getColumnModel().getColumn(2).setMinWidth(80);
		view.getTabEventos().getColumnModel().getColumn(2).setMaxWidth(100); // No pasará de

		view.getTabEventos().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		view.getTabEventos().setRowHeight(25);
	}


	private void cargarReporterosDisponibles() {
		int filaSeleccionada = view.getTabEventos().getSelectedRow();
		if (filaSeleccionada >= 0) {
			reporterosAsignadosVisualmente.clear();
			actualizarTablaAsignadosVisualmente();

			String fecha = (String) view.getTabEventos().getValueAt(filaSeleccionada, 2);

			// Guardamos en memoria los disponibles y actualizamos su tabla
			reporterosDisponiblesVisualmente = model.getReporterosDisponibles(fecha, nombreAgencia);
			actualizarTablaDisponiblesVisualmente();
		}
	}

	private void moverReporteroAAsignados() {
		int[] filasSeleccionadas = view.getTabDisponibles().getSelectedRows();

		// 1. Recogemos los reporteros seleccionados de la tabla central
		List<ReporteroDisplayDTO> aMover = new ArrayList<>();
		for (int fila : filasSeleccionadas) {
			aMover.add(reporterosDisponiblesVisualmente.get(fila));
		}

		// 2. Los añadimos a la derecha y los quitamos de la izquierda
		reporterosAsignadosVisualmente.addAll(aMover);
		reporterosDisponiblesVisualmente.removeAll(aMover);

		// 3. Refrescamos ambas tablas para ver la "magia"
		actualizarTablaDisponiblesVisualmente();
		actualizarTablaAsignadosVisualmente();
	}

	private void actualizarTablaAsignadosVisualmente() {

		javax.swing.table.TableModel tmodel = giis.demo.util.SwingUtil.getTableModelFromPojos(reporterosAsignadosVisualmente, new String[]{"idReportero", "nombre"});
		view.getTabAsignados().setModel(tmodel);
		giis.demo.util.SwingUtil.autoAdjustColumns(view.getTabAsignados());

		// Ocultar ID visualmente
		view.getTabAsignados().getColumnModel().getColumn(0).setMinWidth(0);
		view.getTabAsignados().getColumnModel().getColumn(0).setMaxWidth(0);
		view.getTabAsignados().getColumnModel().getColumn(0).setWidth(0);

		// Hacer que el nombre ocupe todo el ancho sobrante
		view.getTabAsignados().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
		view.getTabAsignados().setRowHeight(25);
	}
	private void actualizarTablaDisponiblesVisualmente() {
		TableModel tmodel = giis.demo.util.SwingUtil.getTableModelFromPojos(reporterosDisponiblesVisualmente, new String[]{"idReportero", "nombre"});
		view.getTabDisponibles().setModel(tmodel);
		giis.demo.util.SwingUtil.autoAdjustColumns(view.getTabDisponibles());

		// Ocultar ID y expandir
		view.getTabDisponibles().getColumnModel().getColumn(0).setMinWidth(0);
		view.getTabDisponibles().getColumnModel().getColumn(0).setMaxWidth(0);
		view.getTabDisponibles().getColumnModel().getColumn(0).setWidth(0);
		view.getTabDisponibles().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
		view.getTabDisponibles().setRowHeight(25);
	}

	private void confirmarAsignacion() {
		int filaEvento = view.getTabEventos().getSelectedRow();

		if (filaEvento >= 0 && !reporterosAsignadosVisualmente.isEmpty()) {
			Integer idEvento = (Integer) view.getTabEventos().getValueAt(filaEvento, 0);

			// 1. Guardamos cada reportero asignado en la base de datos
			for (app.dto.ReporteroDisplayDTO rep : reporterosAsignadosVisualmente) {
				model.guardarAsignacion(idEvento, rep.getIdReportero());
			}

			// 2. Mostramos el mensaje de éxito
			javax.swing.JOptionPane.showMessageDialog(null, "¡Reporteros asignados correctamente!");

			// 3. ¡CERRAR LA VENTANA!
			view.getFrame().dispose();

		} else {
			javax.swing.JOptionPane.showMessageDialog(null, "Selecciona un evento y al menos un reportero para asignar.");
		}
	}
}