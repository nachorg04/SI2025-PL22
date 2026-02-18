package app.controller;

import javax.swing.JOptionPane;

import app.view.AppMainView;
import giis.demo.util.Database; // Asumo que esta es la ruta de tu BD según mensajes anteriores
import giis.demo.util.SwingUtil;

public class AppMainController {

    private AppMainView view;

    /**
     * Constructor del Controlador. Recibe la vista como parámetro.
     */
    public AppMainController(AppMainView v) {
        this.view = v;
        
        // Inicializamos los "escuchadores" de eventos
        this.initView();
        this.initController();
    }

    /**
     * Este método enlaza los botones de la Vista con la lógica del Controlador
     */
    private void initController() {
        
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
        
        // Empresa
        this.view.addAccionEmpresa1Listener(e -> ejecutarAccionEmpresa1());
    }
    
    public void initView() {
		view.getFrame().setVisible(true);
		// Agregar artículos a la lista.
	}

    // LÓGICA DE NEGOCIO (Los métodos que hacen el trabajo de verdad)
    private void crearBaseDeDatos() {
        System.out.println("Controlador: Creando base de datos en blanco...");
        Database db = new Database();
        db.createDatabase(false);
    }

    private void cargarBaseDeDatos() {
        System.out.println("Controlador: Cargando datos de prueba...");
        Database db = new Database();
        db.loadDatabase();
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
    	//Mensaje de error si no se ha seleccionado ningún agente en el combobox
    	if (view.getComboAgente().getSelectedItem() == null) {
			SwingUtil.showMessage("Debes seleccionar un agente", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
    	}
        String agenteSeleccionado = (String) view.getComboAgente().getSelectedItem();
        System.out.println("Agente seleccionado: " + agenteSeleccionado);
        System.out.println("Controlador: Ejecutando acción 1 del Agente.");
    }

    // --- Lógica de la Empresa ---
    private void ejecutarAccionEmpresa1() {
    	//Mensaje de error si no se ha seleccionado ninguna empresa en el combobox
    	if (view.getComboEmpresa().getSelectedItem() == null) {
			SwingUtil.showMessage("Debes seleccionar una empresa", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
    	}
        String empresaSeleccionada = (String) view.getComboEmpresa().getSelectedItem();
        System.out.println("Empresa seleccionada: " + empresaSeleccionada);
        System.out.println("Controlador: Ejecutando acción 1 de la Empresa.");
    }
}