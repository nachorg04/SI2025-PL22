package app.controller;



import javax.swing.JOptionPane;


import app.model.AppMainModel;
import app.view.AppMainView;
import giis.demo.util.Database; // Asumo que esta es la ruta de tu BD según mensajes anteriores
import giis.demo.util.SwingUtil;

public class AppMainController {

	private AppMainView view;
	private AppMainModel model;

	/**
	 * Constructor del Controlador. Recibe la vista como parámetro.
	 */
	public AppMainController(AppMainView v, AppMainModel m) {
		this.view = v;
		this.model = m;

		// Inicializamos los "escuchadores" de eventos
		this.initView();
		this.initController();
	}

	/**
	 * Este método enlaza los botones de la Vista con la lógica del Controlador
	 */
	private void initController() {
		// Hacemos visible la ventana
		view.getFrame().setVisible(true);
		// ==========================================
		// EVENTOS DE LA BASE DE DATOS
		// ==========================================
		this.view.addCrearBDListener(e -> crearBaseDeDatos());
		this.view.addCargarBDListener(e -> cargarBaseDeDatos());

		// ==========================================
		// EVENTOS DE LOS ROLES
		// ==========================================

		// Reportero
		this.view.addAccionReportero1Listener(e -> ejecutarAccionReportero1());

		// Agente
		this.view.addAccionAgente1Listener(e -> ejecutarAccionAgente1());
		this.view.addAccionAgente2Listener(e -> ejecutarAccionAgente2());
		this.view.addAccionInformeEventosListener(e -> accionInformeEventos());

		// Empresa
		this.view.addAccionEmpresa1Listener(e -> ejecutarAccionEmpresa1());
		this.view.addOfrecerReportajesListener(e -> ejecutarOfrecerReportajes()); // 
	}

	public void initView() {
		actualizarComboBoxes();
		view.getFrame().setVisible(true);
		// Agregar artículos a la lista.
	}

	// LÓGICA DE NEGOCIO (Los métodos que hacen el trabajo de verdad)
	private void crearBaseDeDatos() {
		System.out.println("Controlador: Creando base de datos en blanco...");
		Database db = new Database();
		db.createDatabase(false);

		actualizarComboBoxes();
	}

	private void cargarBaseDeDatos() {
		System.out.println("Controlador: Cargando datos de prueba...");
		Database db = new Database();
		db.loadDatabase();

		actualizarComboBoxes();
	}

	// --- Lógica del Reportero ---
	private void ejecutarAccionReportero1() {
		//Mensaje de error si no se ha seleccionado ningún reportero en el combobox
		if (view.getComboReportero().getSelectedItem() == null) {
			SwingUtil.showMessage("Debes seleccionar un reportero", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String reporteroSeleccionado = (String) view.getComboReportero().getSelectedItem();

		if (reporteroSeleccionado != null) {
			System.out.println("Reportero seleccionado: " + reporteroSeleccionado);
			System.out.println("Ejecutando acción 1 para el reportero...");
			// Aquí iría el código para abrir otra ventana, guardar algo, etc.
		} else {
			System.out.println("Aviso: No se ha seleccionado ningún reportero en el combo.");
		}
	}

	// --- Lógica del Agente ---
	private void ejecutarAccionAgente1() {
		// Mensaje de error si no se ha seleccionado ningún agente en el combobox
		if (view.getComboAgente().getSelectedItem() == null) {
			SwingUtil.showMessage("Debes seleccionar un agente", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String agenteSeleccionado = (String) view.getComboAgente().getSelectedItem();
		System.out.println("Agente seleccionado: " + agenteSeleccionado);
		System.out.println("Controlador: Ejecutando acción 1 del Agente.");

		// ==========================================
		// CÓDIGO NUEVO: AQUÍ SE ABRE TU PANTALLA
		// ==========================================

		// 1. Instanciamos tu modelo y tu vista (usando tu paquete unificado app.asignacion)
		app.model.AsignacionReporterosModel asigModel = new app.model.AsignacionReporterosModel();
		app.view.AsignarReporterosView asigView = new app.view.AsignarReporterosView();

		// 2. Instanciamos tu controlador inyectándole el modelo y la vista
		app.controller.AsignacionReporterosController asigController = new app.controller.AsignacionReporterosController(asigModel, asigView, agenteSeleccionado);
		// 3. Inicializamos tu controlador (activa tus botones)
		asigController.initController();

		// 4. Hacemos visible tu ventana
		asigView.getFrame().setVisible(true);
	}

	private void ejecutarAccionAgente2() {
		// Mensaje de error si no se ha seleccionado ningún agente en el combobox
		if (view.getComboAgente().getSelectedItem() == null) {
			giis.demo.util.SwingUtil.showMessage("Debes seleccionar un agente", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Obtenemos el nombre de la agencia seleccionada
		String agenteSeleccionado = (String) view.getComboAgente().getSelectedItem();
		System.out.println("Agente seleccionado para distribuir reportaje: " + agenteSeleccionado);

		// Arrancamos el MVC de la historia 33528
		app.model.DarAccesoEmpresaModel modeloAcceso = new app.model.DarAccesoEmpresaModel();
		app.view.DarAccesoEmpresaView vistaAcceso = new app.view.DarAccesoEmpresaView();
		new app.controller.DarAccesoEmpresaController(modeloAcceso, vistaAcceso, agenteSeleccionado);
	}


	// --- Lógica de la Empresa ---
	private void ejecutarAccionEmpresa1() {
		if (view.getComboEmpresa().getSelectedItem() == null) {
			SwingUtil.showMessage("Debes seleccionar una empresa", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String empresaSeleccionada = (String) view.getComboEmpresa().getSelectedItem();

		// ¡Magia! Creamos el Modelo, la Vista y el Controlador de la nueva pantalla
		app.model.gestionarOfrecimientosReportajesModel ofreModel = new app.model.gestionarOfrecimientosReportajesModel();
		app.view.gestionarOfrecimientosReportajesView ofreView = new app.view.gestionarOfrecimientosReportajesView();

		// Le pasamos el nombre de la empresa para que sepa de quién cargar los datos
		new app.controller.gestionarOfrecimientosReportajesController(ofreModel, ofreView, empresaSeleccionada);
	}

	private void ejecutarOfrecerReportajes() {
	    // Validamos que haya una empresa seleccionada en el combo
	    if (view.getComboEmpresa().getSelectedItem() == null) {
	        SwingUtil.showMessage("Debes seleccionar una empresa", "ERROR", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    
	    String empresaSeleccionada = (String) view.getComboEmpresa().getSelectedItem();
	    System.out.println("Empresa seleccionada para ofrecer reportajes: " + empresaSeleccionada);

	    // 1. Instanciamos TU modelo y TU vista
	    app.model.OfrecerReportajesModel miModelo = new app.model.OfrecerReportajesModel();
	    app.view.OfrecerReportajesView miVista = new app.view.OfrecerReportajesView();

	    // 2. Instanciamos TU controlador (él se encarga de mostrar la ventana)
	    // Le pasamos el nombre de la empresa por si necesitas filtrar por ella
	    new app.controller.OfrecerReportajesController(miModelo, miVista);
	}
	
	
	/**
	 * Limpia y vuelve a cargar los datos de los ComboBoxes desde la base de datos.
	 */
	/**
	 * Limpia y vuelve a cargar los datos de los ComboBoxes desde la base de datos.
	 */
	private void actualizarComboBoxes() {
		try {
			// 1. Limpiamos los comboboxes por si tenían datos viejos
			view.getComboReportero().removeAllItems();
			view.getComboAgente().removeAllItems();
			view.getComboEmpresa().removeAllItems();

			// 2. Pedimos los datos al modelo y rellenamos Reporteros
			java.util.List<app.dto.AppMainDTO> reporteros = model.getListaReporteros();
			for (app.dto.AppMainDTO rep : reporteros) {
				view.getComboReportero().addItem(rep.getNombre());
			}

			// 3. Rellenamos Agencias (Para el combo del Agente)
			java.util.List<app.dto.AppMainDTO> agencias = model.getListaAgencias();
			for (app.dto.AppMainDTO agencia : agencias) {
				view.getComboAgente().addItem(agencia.getNombre());
			}

			// 4. Rellenamos Empresas
			java.util.List<app.dto.AppMainDTO> empresas = model.getListaEmpresas();
			for (app.dto.AppMainDTO emp : empresas) {
				view.getComboEmpresa().addItem(emp.getNombre());
			}

		} catch (giis.demo.util.UnexpectedException e) {
			// Si da error (por ejemplo, porque la tabla Reportero no existe aún), 
			// atrapamos el error para que la aplicación NO crashee.
			System.out.println("Aviso: Base de datos no inicializada. Los combos aparecerán vacíos.");
		}
	}
	
	private void accionInformeEventos() {
        //Mensaje de error si no se ha seleccionado ninguna agencia en el combobox
        if (view.getComboAgente().getSelectedItem() == null) {
            SwingUtil.showMessage("Debes seleccionar una agencia", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String agenciaSeleccionada = (String) view.getComboAgente().getSelectedItem();
        
        // Instanciamos el MVC del informe del evento
        app.model.informeEventoModel model = new app.model.informeEventoModel();
        app.view.informeEventoView vista = new app.view.informeEventoView();
        
        new app.controller.informeEventoController(model, vista, agenciaSeleccionada);
    }
}