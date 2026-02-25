package app.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableModel;
import giis.demo.util.SwingUtil;

public class DarAccesoEmpresaController {
	private app.model.DarAccesoEmpresaModel model;
	private app.view.DarAccesoEmpresaView view;
	private String nombreAgencia;

	// Listas en memoria para gestionar el movimiento entre las dos tablas
	private List<app.dto.EmpresaDisplayDTO> empresasDisponiblesMemoria;
	private List<app.dto.EmpresaDisplayDTO> empresasSeleccionadasMemoria;

	public DarAccesoEmpresaController(app.model.DarAccesoEmpresaModel m, app.view.DarAccesoEmpresaView v, String nombreAgencia) {
		this.model = m;
		this.view = v;
		this.nombreAgencia = nombreAgencia;
		this.empresasSeleccionadasMemoria = new ArrayList<>();
		this.initView();
		this.initController();
	}

	public void initController() {
		// Escuchador para cuando se hace clic en un evento de la tabla izquierda
		view.getTabEventos().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				cargarEmpresasSinAcceso();
			}
		});

		// Escuchadores de los botones
		view.getBtnDarAcceso().addActionListener(e -> moverEmpresasASeleccionadas());
		view.getBtnAceptar().addActionListener(e -> confirmarAccesos());
		view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
	}

	public void initView() {
		// Ponemos el nombre de la agencia en el título
		view.getLblTituloAgencia().setText("Agencia de Prensa: " + nombreAgencia);

		// Cargamos los eventos con reportaje
		List<app.dto.EventoDisplayDTO> eventos = model.getEventosConReportaje(nombreAgencia);

		TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[]{"idEvento", "descripcion", "fecha"});
		view.getTabEventos().setModel(tmodel);
		SwingUtil.autoAdjustColumns(view.getTabEventos());

		// Ocultamos la columna del ID del evento (Columna 0)
		view.getTabEventos().getColumnModel().getColumn(0).setMinWidth(0);
		view.getTabEventos().getColumnModel().getColumn(0).setMaxWidth(0);
		view.getTabEventos().getColumnModel().getColumn(0).setWidth(0);

		// Ajustamos la fecha para que no ocupe demasiado
		view.getTabEventos().getColumnModel().getColumn(2).setMinWidth(80);
		view.getTabEventos().getColumnModel().getColumn(2).setMaxWidth(100);

		view.getTabEventos().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		view.getTabEventos().setRowHeight(25);
		view.getFrame().setVisible(true);
	}

	private void cargarEmpresasSinAcceso() {
		int fila = view.getTabEventos().getSelectedRow();
		if (fila >= 0) {
			Integer idEvento = (Integer) view.getTabEventos().getValueAt(fila, 0);

			// Pedimos a la BD las empresas que aceptaron pero no tienen acceso
			empresasDisponiblesMemoria = model.getEmpresasAceptadasSinAcceso(idEvento);
			empresasSeleccionadasMemoria.clear(); // Limpiamos la derecha al cambiar de evento

			actualizarTablaDisponibles();
			actualizarTablaSeleccionadas();
		}
	}

	private void moverEmpresasASeleccionadas() {
		int filaEvento = view.getTabEventos().getSelectedRow();
		int[] filasSeleccionadas = view.getTabEmpresasDisponibles().getSelectedRows();

		// FRENO DE EMERGENCIA: Validamos que haya algo seleccionado
		if (filaEvento == -1 || filasSeleccionadas.length == 0) {
			SwingUtil.showMessage(
					"No se puede dar acceso: Debes seleccionar un evento y al menos una empresa.", 
					"Aviso", 
					javax.swing.JOptionPane.WARNING_MESSAGE
					);
			return;
		}

		// Movemos las empresas de abajo hacia arriba para no romper los índices
		for (int i = filasSeleccionadas.length - 1; i >= 0; i--) {
			int fila = filasSeleccionadas[i];
			app.dto.EmpresaDisplayDTO emp = empresasDisponiblesMemoria.get(fila);
			empresasSeleccionadasMemoria.add(emp);
			empresasDisponiblesMemoria.remove(fila);
		}

		actualizarTablaDisponibles();
		actualizarTablaSeleccionadas();
	}

	private void confirmarAccesos() {
		int filaEvento = view.getTabEventos().getSelectedRow();

		if (filaEvento >= 0 && !empresasSeleccionadasMemoria.isEmpty()) {
			Integer idEvento = (Integer) view.getTabEventos().getValueAt(filaEvento, 0);

			// Hacemos el UPDATE en la base de datos para cada empresa de la tabla derecha
			for (app.dto.EmpresaDisplayDTO emp : empresasSeleccionadasMemoria) {
				model.concederAcceso(idEvento, emp.getIdEmpresa());
			}

			javax.swing.JOptionPane.showMessageDialog(null, "¡Accesos al reportaje concedidos correctamente!");
			view.getFrame().dispose(); // Cerramos la ventana
		} else {
			SwingUtil.showMessage("No hay empresas seleccionadas para dar acceso.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
		}
	}

	private void actualizarTablaDisponibles() {
		TableModel tm = SwingUtil.getTableModelFromPojos(empresasDisponiblesMemoria, new String[]{"idEmpresa", "nombre"});
		view.getTabEmpresasDisponibles().setModel(tm);
		view.getTabEmpresasDisponibles().getColumnModel().getColumn(0).setMinWidth(0);
		view.getTabEmpresasDisponibles().getColumnModel().getColumn(0).setMaxWidth(0);
		view.getTabEmpresasDisponibles().setRowHeight(25);
	}

	private void actualizarTablaSeleccionadas() {
		TableModel tm = SwingUtil.getTableModelFromPojos(empresasSeleccionadasMemoria, new String[]{"idEmpresa", "nombre"});
		view.getTabEmpresasSeleccionadas().setModel(tm);
		view.getTabEmpresasSeleccionadas().getColumnModel().getColumn(0).setMinWidth(0);
		view.getTabEmpresasSeleccionadas().getColumnModel().getColumn(0).setMaxWidth(0);
		view.getTabEmpresasSeleccionadas().setRowHeight(25);
	}
}
