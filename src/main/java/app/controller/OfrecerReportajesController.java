package app.controller;

import java.util.List;
import java.util.ArrayList; // Necesario para rastrear la sesión
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import app.model.OfrecerReportajesModel;
import app.view.OfrecerReportajesView;
import app.dto.OfrecerReportajesDTO;
import giis.demo.util.SwingUtil;

public class OfrecerReportajesController {

	private OfrecerReportajesModel model;
	private OfrecerReportajesView view;
	private DefaultListModel<String> modeloListaOfertas = new DefaultListModel<>();
	
	// Lista para rastrear qué empresas hemos ofertado en esta sesión
	private List<Integer> empresasOfertadasSesion = new ArrayList<>();

	public OfrecerReportajesController(OfrecerReportajesModel model, OfrecerReportajesView view) {
		this.model = model;
		this.view = view;
		this.initView();
	}

	public void initView() {
		cargarEventos();
		view.listOfertasEnCurso.setModel(modeloListaOfertas);

		view.tableEventos.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = view.tableEventos.getSelectedRow();
				if (fila != -1) {
					int idEvento = (int) view.tableEventos.getValueAt(fila, 0);
					cargarEmpresas(idEvento);
				}
			}
		});

		// --- BOTONES ---
		view.btnOfertar.addActionListener(e -> ejecutarOferta());
		view.btnCancelarEmpresas.addActionListener(e -> gestionarChecks(false));

		view.btnAceptarTodo.addActionListener(e -> {
			if (modeloListaOfertas.isEmpty()) {
				SwingUtil.showMessage("No hay ofrecimientos en curso para confirmar.", "Aviso", 1);
			} else {
				SwingUtil.showMessage("Sesión confirmada con éxito.", "Éxito", 1);
				// Al confirmar, vaciamos el rastreador pero NO borramos de la DB
				empresasOfertadasSesion.clear();
				limpiarInterfazSesion();
			}
		});

		// Este es el botón que ahora sí "cancela todo" de verdad
		view.btnCancelarTodo.addActionListener(e -> ejecutarCancelacionTotal());
		
		view.setVisible(true);
	}

	private void ejecutarCancelacionTotal() {
		int filaEvento = view.tableEventos.getSelectedRow();
		if (filaEvento != -1) {
			int idEvento = (int) view.tableEventos.getValueAt(filaEvento, 0);
			
			// 1. Borramos de la base de datos lo hecho en esta sesión
			for (Integer idEmpresa : empresasOfertadasSesion) {
				model.eliminarOfrecimiento(idEvento, idEmpresa); 
			}
			
			// 2. Limpiamos las listas y refrescamos la tabla central
			empresasOfertadasSesion.clear();
			limpiarInterfazSesion();
			cargarEmpresas(idEvento); // Ahora volverán a aparecer porque ya no están en la DB
		}
	}

	private void limpiarInterfazSesion() {
		modeloListaOfertas.clear();
		gestionarChecks(false);
	}

	private void ejecutarOferta() {
		int filaEvento = view.tableEventos.getSelectedRow();
		if (filaEvento == -1) {
			SwingUtil.showMessage("Seleccione un evento primero", "Aviso", 1);
			return;
		}

		int idEvento = (int) view.tableEventos.getValueAt(filaEvento, 0);
		boolean algunaMarcada = false;
		
		for (int i = 0; i < view.tableEmpresas.getRowCount(); i++) {
			Boolean seleccionado = (Boolean) view.tableEmpresas.getValueAt(i, 2);
			if (seleccionado != null && seleccionado) {
				algunaMarcada = true;
				int idEmpresa = (int) view.tableEmpresas.getValueAt(i, 0);
				String nombreEmpresa = (String) view.tableEmpresas.getValueAt(i, 1);

				model.insertarOfrecimientos(idEvento, idEmpresa); // Persiste en BD [cite: 17]
				empresasOfertadasSesion.add(idEmpresa); // Lo guardamos para poder cancelar luego
				modeloListaOfertas.addElement(nombreEmpresa + " (P)");
			}
		}

		if (algunaMarcada) {
			cargarEmpresas(idEvento);
		} else {
			SwingUtil.showMessage("No hay empresas seleccionadas para ofertar", "Aviso", 1);
		}
	}

	// ... (Resto de métodos cargarEventos, cargarEmpresas y gestionarChecks se mantienen igual) ...
    private void cargarEventos() {
		List<OfrecerReportajesDTO> eventos = model.getEventosConReportero();
		String[] columnas = {"id_evento", "nombre_evento", "reportero_asignado"};
		view.tableEventos.setModel(SwingUtil.getTableModelFromPojos(eventos, columnas));
		SwingUtil.autoAdjustColumns(view.tableEventos);
	}

	private void cargarEmpresas(int idEvento) {
		List<OfrecerReportajesDTO> empresas = model.getEmpresasSinOferta(idEvento);
		String[] columnas = {"id_empresa", "nombre_empresa"};
		
		DefaultTableModel tableModel = (DefaultTableModel) SwingUtil.getTableModelFromPojos(empresas, columnas);
		tableModel.addColumn("Seleccionar"); 

		view.tableEmpresas.setModel(new DefaultTableModel() {
			@Override
			public int getRowCount() { return tableModel.getRowCount(); }
			@Override
			public int getColumnCount() { return tableModel.getColumnCount(); }
			@Override
			public Object getValueAt(int r, int c) { return tableModel.getValueAt(r, c); }
			@Override
			public void setValueAt(Object aValue, int r, int c) { 
				tableModel.setValueAt(aValue, r, c); 
				fireTableCellUpdated(r, c); 
			}
			@Override
			public Class<?> getColumnClass(int c) {
				return c == 2 ? Boolean.class : Object.class;
			}
			@Override
			public String getColumnName(int c) { return tableModel.getColumnName(c); }
		});
		gestionarChecks(false);
	}

	private void gestionarChecks(boolean estado) {
		for (int i = 0; i < view.tableEmpresas.getRowCount(); i++) {
			view.tableEmpresas.setValueAt(estado, i, 2);
		}
	}
}