package app.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import app.dto.AppMainDTO;
import app.dto.informeEventoDTO;
import app.model.informeEventoModel;
import app.view.informeEventoView;
import giis.demo.util.SwingUtil;

public class informeEventoController {

    private informeEventoModel model;
    private informeEventoView view;
    private String nombreAgencia;

    // Guardamos los datos generados para poder exportarlos al CSV
    private String infoEntregado = "";
    private String infoAutor = "";
    private List<AppMainDTO> reporterosActuales;
    private List<AppMainDTO> empresasActuales;
    private String descEventoActual = "";

    public informeEventoController(informeEventoModel m, informeEventoView v, String nombreAgencia) {
        this.model = m;
        this.view = v;
        this.nombreAgencia = nombreAgencia;
        this.initView();
        this.initController();
    }

    private void initController() {
        view.addVolverListener(e -> view.getFrame().dispose());
        view.addGenerarInformeListener(e -> generarInforme());
        view.addExportarCSVListener(e -> exportarCSV());
    }

    public void initView() {
        view.setNombreAgencia(nombreAgencia);
        cargarEventos();
        view.getFrame().setVisible(true);
    }

    private void cargarEventos() {
        List<informeEventoDTO> eventos = model.getEventosAgencia(nombreAgencia);
        TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] {"idEvento", "fecha", "descripcion"});
        view.getTabEventos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabEventos());
    }

    private void generarInforme() {
        int fila = view.getTabEventos().getSelectedRow();
        String idEvento = SwingUtil.getSelectedKey(view.getTabEventos());

        if (idEvento.isEmpty() || fila == -1) {
            SwingUtil.showMessage("Por favor, selecciona un evento de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        descEventoActual = (String) view.getTabEventos().getValueAt(fila, 2);

        // 1. Datos básicos
        List<informeEventoDTO> estado = model.getEstadoReportaje(idEvento);
        if (!estado.isEmpty()) {
            infoEntregado = estado.get(0).getEntregado();
            infoAutor = estado.get(0).getAutor();
            view.setDatosReportaje(infoEntregado, infoAutor);
        }

        // 2. Tabla de Reporteros asignados
        reporterosActuales = model.getReporterosAsignados(idEvento);
        view.getTabReporteros().setModel(SwingUtil.getTableModelFromPojos(reporterosActuales, new String[]{"nombre"}));
        
        // 3. Tabla de Empresas con acceso
        empresasActuales = model.getEmpresasConAcceso(idEvento);
        view.getTabEmpresas().setModel(SwingUtil.getTableModelFromPojos(empresasActuales, new String[]{"nombre"}));

        // Hacemos visible el panel inferior del informe
        view.getPanelInforme().setVisible(true);
        view.getFrame().revalidate();
    }

    private void exportarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Informe CSV");
        fileChooser.setSelectedFile(new java.io.File("Informe_" + descEventoActual.replace(" ", "_") + ".csv"));

        int userSelection = fileChooser.showSaveDialog(view.getFrame());

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write("INFORME DEL EVENTO: " + descEventoActual + "\n\n");
                writer.write("Reportaje entregado:," + infoEntregado + "\n");
                writer.write("Entregado por:," + (infoAutor != null ? infoAutor : "") + "\n\n");
                
                writer.write("REPORTEROS ASIGNADOS\n");
                if (reporterosActuales.isEmpty()) writer.write("Ninguno\n");
                for (AppMainDTO r : reporterosActuales) writer.write(r.getNombre() + "\n");
                
                writer.write("\nEMPRESAS CON ACCESO\n");
                if (empresasActuales.isEmpty()) writer.write("Ninguna\n");
                for (AppMainDTO e : empresasActuales) writer.write(e.getNombre() + "\n");

                SwingUtil.showMessage("Informe guardado en:\n" + fileToSave.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                SwingUtil.showMessage("Error al guardar el archivo CSV.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}