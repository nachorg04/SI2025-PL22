package app.controller;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import app.dto.InformeIngresosDTO;
import app.model.InformeIngresosModel;
import app.view.InformeIngresosView;
import giis.demo.util.SwingUtil;

public class InformeIngresosController {

    private InformeIngresosModel model;
    private InformeIngresosView view;
    
    private List<InformeIngresosDTO> datosBD;
    private String nombreAgenciaLogueada;

    public InformeIngresosController(InformeIngresosModel model, InformeIngresosView view, String nombreAgencia) {
        this.model = model;
        this.view = view;
        this.nombreAgenciaLogueada = nombreAgencia;
        this.initController();
    }

    public void initController() {
        view.getLblAgenciaNombre().setText("🏢 Agencia: " + nombreAgenciaLogueada);

        // 1. Cargamos todos los datos de la base de datos a memoria
        datosBD = model.obtenerIngresosPorAgencia(nombreAgenciaLogueada);

        // 2. Rellenamos el desplegable con las temáticas disponibles
        cargarComboTematicas();

        // 3. Cargamos la tabla superior por primera vez
        cargarTablaEventos();

        // 4. Escuchador: Cuando seleccionas un Evento en la tabla de arriba -> actualiza abajo
        view.getTableEventos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarTablasDetalle();
                }
            }
        });

        // 5. Escuchador NUEVO: Cuando cambias la Temática en el desplegable -> actualiza arriba y limpia abajo
        view.getCbTematica().addActionListener(e -> {
            cargarTablaEventos();
            limpiarTablasDetalle();
        });

        // Botón Volver
        view.getBtnVolver().addActionListener(e -> view.getFrame().dispose());
    }

    /**
     * Rellena el JComboBox extrayendo las temáticas únicas de la lista de datos
     */
    private void cargarComboTematicas() {
        // Usamos un Set para no meter temáticas repetidas
        Set<String> tematicasUnicas = new LinkedHashSet<>();
        tematicasUnicas.add("Todas las temáticas"); // Añadimos la opción de ver todo
        
        for (InformeIngresosDTO dto : datosBD) {
            if (dto.getTematica() != null) {
                tematicasUnicas.add(dto.getTematica());
            }
        }
        
        for (String t : tematicasUnicas) {
            view.getCbTematica().addItem(t);
        }
    }

    /**
     * Carga la tabla de arriba filtrando por la temática seleccionada en el combo
     */
    private void cargarTablaEventos() {
        String tematicaSeleccionada = (String) view.getCbTematica().getSelectedItem();
        if (tematicaSeleccionada == null) return;

        DefaultTableModel modeloEventos = new DefaultTableModel(new Object[]{"Temática", "ID Evento", "Nombre del evento", "Total Ingresos (€)"}, 0);
        
        Map<Integer, Double> totalPorEvento = new LinkedHashMap<>();
        Map<Integer, InformeIngresosDTO> infoEvento = new LinkedHashMap<>();

        for (InformeIngresosDTO dto : datosBD) {
            // --- FILTRO MÁGICO ---
            // Si el combo no dice "Todas" y la temática de esta fila no coincide, la saltamos
            if (!"Todas las temáticas".equals(tematicaSeleccionada) && !tematicaSeleccionada.equals(dto.getTematica())) {
                continue; 
            }
            
            if (!infoEvento.containsKey(dto.getId_evento())) {
                infoEvento.put(dto.getId_evento(), dto);
                totalPorEvento.put(dto.getId_evento(), 0.0);
            }
            double ingreso = (dto.getTiene_tarifa() == 1) ? dto.getCuota_mensual() : dto.getPrecio_evento();
            totalPorEvento.put(dto.getId_evento(), totalPorEvento.get(dto.getId_evento()) + ingreso);
        }

        for (Integer id : infoEvento.keySet()) {
            InformeIngresosDTO dto = infoEvento.get(id);
            modeloEventos.addRow(new Object[]{
                dto.getTematica(), 
                dto.getId_evento(), 
                dto.getNombre_evento(), 
                String.format("%.2f", totalPorEvento.get(id))
            });
        }

        view.getTableEventos().setModel(modeloEventos);
        SwingUtil.autoAdjustColumns(view.getTableEventos());
    }

    private void actualizarTablasDetalle() {
        int filaSeleccionada = view.getTableEventos().getSelectedRow();
        if (filaSeleccionada == -1) return; 

        int idEventoSeleccionado = (int) view.getTableEventos().getValueAt(filaSeleccionada, 1);
        String nombreEvento = (String) view.getTableEventos().getValueAt(filaSeleccionada, 2);
        
        view.getLblEventoSeleccionado().setText("Detalles del evento seleccionado: [" + nombreEvento + "]");

        DefaultTableModel modeloTarifa = new DefaultTableModel(new Object[]{"Empresa", "Acceso", "Cuota mensual"}, 0);
        DefaultTableModel modeloSinTarifa = new DefaultTableModel(new Object[]{"Empresa", "Acceso", "Pagado"}, 0);

        double totalTarifa = 0.0;
        double totalSinTarifa = 0.0;

        for (InformeIngresosDTO dto : datosBD) {
            if (dto.getId_evento() == idEventoSeleccionado) {
                if (dto.getTiene_tarifa() == 1) { 
                    modeloTarifa.addRow(new Object[]{dto.getNombre_empresa(), dto.getAccesoPantalla(), String.format("%.2f €", dto.getCuota_mensual())});
                    totalTarifa += dto.getCuota_mensual();
                } else { 
                    modeloSinTarifa.addRow(new Object[]{dto.getNombre_empresa(), dto.getAccesoPantalla(), String.format("%.2f €", dto.getPrecio_evento())});
                    totalSinTarifa += dto.getPrecio_evento();
                }
            }
        }

        view.getTableTarifaPlana().setModel(modeloTarifa);
        view.getTableSinTarifa().setModel(modeloSinTarifa);
        
        view.getLblSumatorioTarifa().setText(String.format("Sumatorio con tarifa plana: %.2f €", totalTarifa));
        view.getLblSumatorioSinTarifa().setText(String.format("Sumatorio sin tarifa plana: %.2f €", totalSinTarifa));
    }
    
    /**
     * Limpia las tablas de detalle cuando se cambia el filtro de arriba
     */
    private void limpiarTablasDetalle() {
        view.getLblEventoSeleccionado().setText("Detalles del evento seleccionado: [Ninguno]");
        view.getTableTarifaPlana().setModel(new DefaultTableModel(new Object[]{"Empresa", "Acceso", "Cuota mensual"}, 0));
        view.getTableSinTarifa().setModel(new DefaultTableModel(new Object[]{"Empresa", "Acceso", "Pagado"}, 0));
        view.getLblSumatorioTarifa().setText("Sumatorio con tarifa plana: 0.00 €");
        view.getLblSumatorioSinTarifa().setText("Sumatorio sin tarifa plana: 0.00 €");
    }
    
    public void mostrarVista() {
        view.getFrame().setVisible(true);
    }
}