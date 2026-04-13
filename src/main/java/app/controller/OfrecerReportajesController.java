package app.controller;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import app.dto.OfrecerReportajesDTO;
import app.model.OfrecerReportajesModel;
import app.view.OfrecerReportajesView;
import giis.demo.util.SwingUtil;

public class OfrecerReportajesController {

	private OfrecerReportajesModel model;
	private OfrecerReportajesView view;
	private String agenciaActual;
	private int operacionesRealizadas = 0;
	private List<OfrecerReportajesDTO> eventosCargados;

	public OfrecerReportajesController(OfrecerReportajesModel m, OfrecerReportajesView v, String agencia) {
		this.model = m;
		this.view = v;
		this.agenciaActual = agencia;
		this.initView();
	}

	public void initView() {
		view.lblAgenciaSeleccionada.setText("Agencia: " + agenciaActual);
		cargarEventos();
		configurarEstadoFiltrosOferta(false);

		view.tableEventos.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				actualizarTablasDependientes();
			}
		});

		view.comboFiltroEmpresas.addActionListener(e -> {
			int fila = view.tableEventos.getSelectedRow();
			if (fila != -1) {
				actualizarTablasDependientes();
			}
		});

		view.chkCoincidenciaTematicas.addActionListener(e -> {
			int fila = view.tableEventos.getSelectedRow();
			if (fila != -1) {
				actualizarTablasDependientes();
			}
		});

		view.chkSoloTarifaPlana.addActionListener(e -> {
			int fila = view.tableEventos.getSelectedRow();
			if (fila != -1) {
				actualizarTablasDependientes();
			}
		});

		view.btnOfertar.addActionListener(e -> ejecutarOferta());
		view.btnQuitarOfrecimiento.addActionListener(e -> ejecutarQuitarOfrecimiento());

		view.btnAceptarTodo.addActionListener(e -> {
			if (operacionesRealizadas == 0) {
				SwingUtil.showMessage("No se han realizado cambios en esta sesion.", "Aviso", 1);
			} else {
				String mensaje = "Sesion finalizada con exito.\nSe han registrado "
						+ operacionesRealizadas + " cambios en la base de datos.";
				SwingUtil.showMessage(mensaje, "Exito", 1);
				operacionesRealizadas = 0;
			}
		});

		view.btnCancelar.addActionListener(e -> view.dispose());

		view.btnLimpiarSeleccion.addActionListener(e -> {
			view.tableEventos.clearSelection();
			view.chkCoincidenciaTematicas.setSelected(false);
			view.chkSoloTarifaPlana.setSelected(false);
			configurarEstadoFiltrosOferta(false);
			limpiarTablas();
			operacionesRealizadas = 0;
		});

		view.setVisible(true);
	}

	private void actualizarTablasDependientes() {
		int fila = view.tableEventos.getSelectedRow();
		if (fila == -1) {
			limpiarTablas();
			return;
		}

		int idEvento = (int) view.tableEventos.getValueAt(fila, 0);
		OfrecerReportajesDTO eventoSeleccionado = eventosCargados.get(fila);
		view.lblTematicasEvento.setText("Tematicas del evento: " + eventoSeleccionado.getTematicas_evento());

		boolean mostrandoSinOferta = view.comboFiltroEmpresas.getSelectedIndex() == 0;
		configurarEstadoFiltrosOferta(mostrandoSinOferta);

		cargarEmpresasSegunFiltro(idEvento);
		cargarOfertasEnCurso(idEvento);
	}

	private void configurarEstadoFiltrosOferta(boolean habilitado) {
		view.chkCoincidenciaTematicas.setEnabled(habilitado);
		view.chkSoloTarifaPlana.setEnabled(habilitado);
		if (!habilitado) {
			view.chkCoincidenciaTematicas.setSelected(false);
			view.chkSoloTarifaPlana.setSelected(false);
		}
	}

	private void ejecutarQuitarOfrecimiento() {
		int filaEvento = view.tableEventos.getSelectedRow();
		int filaOferta = view.tableOfertasEnCurso.getSelectedRow();

		if (filaOferta == -1) {
			SwingUtil.showMessage("Seleccione una empresa de 'OFERTAS EN CURSO' para quitarla.", "Aviso", 1);
			return;
		}

		int idEvento = (int) view.tableEventos.getValueAt(filaEvento, 0);
		int idEmpresa = (int) view.tableOfertasEnCurso.getValueAt(filaOferta, 0);

		OfrecerReportajesDTO detalle = model.getDetalleOfrecimiento(idEvento, idEmpresa);
		if (detalle.getTiene_acceso() == 1) {
			SwingUtil.showMessage("No se puede quitar: Acceso ya concedido.", "Error", 0);
			return;
		}

		if ("ACEPTADO".equals(detalle.getEstado())) {
			SwingUtil.showMessage("Se ha notificado por email a " + detalle.getEmail() + " la cancelacion.",
					"Email enviado", 1);
		}

		model.eliminarOfrecimiento(idEvento, idEmpresa);
		operacionesRealizadas++;
		actualizarTablasDependientes();
	}

	private void ejecutarOferta() {
		int filaEvento = view.tableEventos.getSelectedRow();
		if (filaEvento == -1) {
			return;
		}

		int idEvento = (int) view.tableEventos.getValueAt(filaEvento, 0);
		boolean cambios = false;
		StringBuilder noPermitidasPago = new StringBuilder();

		for (int r = 0; r < view.tableEmpresas.getRowCount(); r++) {
			Boolean check = (Boolean) view.tableEmpresas.getValueAt(r, 4);
			if (check != null && check) {
				if (!estaAlCorrienteLaFila(r)) {
					if (noPermitidasPago.length() > 0) {
						noPermitidasPago.append(", ");
					}
					noPermitidasPago.append(view.tableEmpresas.getValueAt(r, 1));
					continue;
				}
				model.insertarOfrecimientos(idEvento, (int) view.tableEmpresas.getValueAt(r, 0));
				operacionesRealizadas++;
				cambios = true;
			}
		}

		if (noPermitidasPago.length() > 0) {
			SwingUtil.showMessage(
					"No se puede ofertar a empresas que no estan al corriente de pago: " + noPermitidasPago,
					"Aviso", 1);
		}

		if (cambios) {
			actualizarTablasDependientes();
		} else if (noPermitidasPago.length() == 0) {
			SwingUtil.showMessage("No hay empresas seleccionadas.", "Aviso", 1);
		}
	}

	private void cargarEmpresasSegunFiltro(int idEvento) {
		int filtro = view.comboFiltroEmpresas.getSelectedIndex();
		if (filtro == 0) {
			boolean soloCoincidentes = view.chkCoincidenciaTematicas.isSelected();
			boolean soloTarifaPlana = view.chkSoloTarifaPlana.isSelected();
			configurarTablaConChecks(model.getEmpresasSinOferta(idEvento, agenciaActual, soloCoincidentes, soloTarifaPlana));
			view.btnOfertar.setEnabled(true);
		} else {
			String[] cols = { "id_empresa", "nombre_empresa", "tarifa_plana_info", "pago_info", "estado" };
			view.tableEmpresas.setModel(SwingUtil.getTableModelFromPojos(model.getEmpresasConOferta(idEvento, agenciaActual), cols));
			view.btnOfertar.setEnabled(false);
		}
		SwingUtil.autoAdjustColumns(view.tableEmpresas);
	}

	private void cargarOfertasEnCurso(int idEvento) {
		List<OfrecerReportajesDTO> ofertas = model.getEmpresasConOferta(idEvento, agenciaActual);
		String[] columnas = { "id_empresa", "nombre_empresa", "tarifa_plana_info", "pago_info", "estado" };
		view.tableOfertasEnCurso.setModel(SwingUtil.getTableModelFromPojos(ofertas, columnas));
		SwingUtil.autoAdjustColumns(view.tableOfertasEnCurso);
	}

	private void cargarEventos() {
		eventosCargados = model.getEventosConReportero(agenciaActual);
		// NUEVO: Añadimos la columna "estado_embargo" a la visualizacion
		String[] columnas = { "id_evento", "nombre_evento", "reportero_asignado", "estado_embargo" };
		view.tableEventos.setModel(SwingUtil.getTableModelFromPojos(eventosCargados, columnas));
		SwingUtil.autoAdjustColumns(view.tableEventos);
	}

	private void configurarTablaConChecks(List<OfrecerReportajesDTO> empresas) {
		String[] columnas = { "id_empresa", "nombre_empresa", "tarifa_plana_info", "pago_info" };
		DefaultTableModel tm = (DefaultTableModel) SwingUtil.getTableModelFromPojos(empresas, columnas);
		tm.addColumn("Seleccionar");

		// NUEVO: Evaluamos si el evento actual tiene el embargo activo
		boolean embargoActivo = false;
		int filaEvento = view.tableEventos.getSelectedRow();
		if (filaEvento != -1) {
			String estadoEmbargo = (String) view.tableEventos.getValueAt(filaEvento, 3);
			if ("ACTIVO".equals(estadoEmbargo)) {
				embargoActivo = true;
			}
		}
		
		final boolean hayEmbargo = embargoActivo;

		view.tableEmpresas.setModel(new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override public int getRowCount() { return tm.getRowCount(); }
			@Override public int getColumnCount() { return tm.getColumnCount(); }
			
			@Override public Object getValueAt(int r, int c) { 
				if (c == 4) {
					// Si hay embargo y la empresa NO los acepta, desmarcamos y bloqueamos
					if (hayEmbargo && !empresas.get(r).isAcepta_embargos()) {
						return false; 
					}
				}
				return tm.getValueAt(r, c); 
			}
			
			@Override public void setValueAt(Object v, int r, int c) { 
				tm.setValueAt(v, r, c); 
				fireTableCellUpdated(r, c); 
			}
			
			@Override public Class<?> getColumnClass(int c) { return c == 4 ? Boolean.class : Object.class; }
			
			@Override public boolean isCellEditable(int r, int c) { 
				if (c == 4) {
					// Magia: Bloqueamos la edicion del checkbox si rechaza embargos
					if (hayEmbargo && !empresas.get(r).isAcepta_embargos()) {
						return false; 
					}
					return estaAlCorrienteLaFila(r);
				}
				return false; 
			}
			
			@Override public String getColumnName(int c) { 
				// Cambiamos el titulo para que el agente sepa por que no puede pulsar
				return c == 4 ? (hayEmbargo ? "Sel. (Bloqueos)" : "Seleccionar") : tm.getColumnName(c); 
			}
		});
	}

	private boolean estaAlCorrienteLaFila(int fila) {
		Object valor = view.tableEmpresas.getValueAt(fila, 3);
		return valor != null && "SI".equalsIgnoreCase(valor.toString());
	}

	private void limpiarTablas() {
		view.tableEmpresas.setModel(new DefaultTableModel());
		view.tableOfertasEnCurso.setModel(new DefaultTableModel());
		view.lblTematicasEvento.setText("Tematicas del evento: -");
	}
}
