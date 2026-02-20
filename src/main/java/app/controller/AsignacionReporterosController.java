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
		List<EventoDisplayDTO> eventos = model.getEventosSinAsignar(nombreAgencia);
		// Usamos los nombres exactos de tu DTO original: id_evento
		TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[]{"id_evento", "descripcion", "fecha"});
		view.getTabEventos().setModel(tmodel);
		SwingUtil.autoAdjustColumns(view.getTabEventos());
	}

	private void cargarReporterosDisponibles() {
		int filaSeleccionada = view.getTabEventos().getSelectedRow();
		if (filaSeleccionada >= 0) {
			reporterosAsignadosVisualmente.clear();
			actualizarTablaAsignadosVisualmente();

			String fecha = (String) view.getTabEventos().getValueAt(filaSeleccionada, 2);

			List<ReporteroDisplayDTO> disponibles = model.getReporterosDisponibles(fecha, nombreAgencia);
			// Usamos los nombres exactos de tu DTO original: id_reportero
			TableModel tmodel = SwingUtil.getTableModelFromPojos(disponibles, new String[]{"id_reportero", "nombre"});
			view.getTabDisponibles().setModel(tmodel);
			SwingUtil.autoAdjustColumns(view.getTabDisponibles());
		}
	}

	private void moverReporteroAAsignados() {
		int[] filasSeleccionadas = view.getTabDisponibles().getSelectedRows();
		for (int fila : filasSeleccionadas) {
			ReporteroDisplayDTO rep = new ReporteroDisplayDTO();
			// Usamos el setter exacto generado por Lombok para tu variable original
			rep.setId_reportero((Integer) view.getTabDisponibles().getValueAt(fila, 0));
			rep.setNombre((String) view.getTabDisponibles().getValueAt(fila, 1));

			// Usamos el getter exacto generado por Lombok
			boolean yaEsta = reporterosAsignadosVisualmente.stream()
					.anyMatch(r -> r.getId_reportero().equals(rep.getId_reportero()));
			if (!yaEsta) {
				reporterosAsignadosVisualmente.add(rep);
			}
		}
		actualizarTablaAsignadosVisualmente();
	}

	private void actualizarTablaAsignadosVisualmente() {
		TableModel tmodel = SwingUtil.getTableModelFromPojos(reporterosAsignadosVisualmente, new String[]{"id_reportero", "nombre"});
		view.getTabAsignados().setModel(tmodel);
		SwingUtil.autoAdjustColumns(view.getTabAsignados());
	}

	private void confirmarAsignacion() {
		int filaEvento = view.getTabEventos().getSelectedRow();
		if (filaEvento >= 0 && !reporterosAsignadosVisualmente.isEmpty()) {
			Integer idEvento = (Integer) view.getTabEventos().getValueAt(filaEvento, 0);

			for (ReporteroDisplayDTO rep : reporterosAsignadosVisualmente) {
				model.guardarAsignacion(idEvento, rep.getId_reportero());
			}

			JOptionPane.showMessageDialog(null, "¡Reporteros asignados correctamente!");

			reporterosAsignadosVisualmente.clear();
			actualizarTablaAsignadosVisualmente();
			view.getTabDisponibles().setModel(new javax.swing.table.DefaultTableModel());

			initView();
		} else {
			JOptionPane.showMessageDialog(null, "Selecciona un evento y al menos un reportero para asignar.");
		}
	}
}