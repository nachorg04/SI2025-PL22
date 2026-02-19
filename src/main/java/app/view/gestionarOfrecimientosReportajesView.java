package app.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

public class gestionarOfrecimientosReportajesView {

    private JFrame frame;
    private JTable tabOfrecimientos;
    
    // --- NUEVOS COMPONENTES PARA EL HISTORIAL ---
    private JTable tabHistorial;
    private DefaultTableModel modeloHistorial; 
    
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JButton btnVolver;
    private JLabel lblEmpresaActual;

    public gestionarOfrecimientosReportajesView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión de Ofrecimientos");
        frame.setBounds(100, 100, 750, 500); // Un poco más grande para que quepan bien las dos tablas
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        frame.setContentPane(contentPane);

        // --- CABECERA ---
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEmpresaActual = new JLabel("Ofrecimientos pendientes para: ");
        panelNorte.add(lblEmpresaActual);
        contentPane.add(panelNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL DIVIDIDO EN DOS (Pendientes arriba, Historial abajo) ---
        JPanel panelDivisor = new JPanel(new GridLayout(2, 1, 0, 15)); // 2 filas, 1 columna
        contentPane.add(panelDivisor, BorderLayout.CENTER);

        // 1. ZONA SUPERIOR: Tabla de Pendientes y Botones
        JPanel panelPendientes = new JPanel(new BorderLayout());
        panelPendientes.setBorder(BorderFactory.createTitledBorder("⏳ Ofrecimientos PENDIENTES"));
        
        tabOfrecimientos = new JTable();
        tabOfrecimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabOfrecimientos.setDefaultEditor(Object.class, null);
        panelPendientes.add(new JScrollPane(tabOfrecimientos), BorderLayout.CENTER);

        // Botones debajo de la tabla de pendientes
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnVolver = new JButton("Volver");
        btnRechazar = new JButton("Rechazar Ofrecimiento");
        btnAceptar = new JButton("Aceptar Ofrecimiento");
        panelBotones.add(btnVolver);
        panelBotones.add(btnRechazar);
        panelBotones.add(btnAceptar);
        panelPendientes.add(panelBotones, BorderLayout.SOUTH);

        panelDivisor.add(panelPendientes);

        // 2. ZONA INFERIOR: Tabla de Historial
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBorder(BorderFactory.createTitledBorder("Decisiones tomadas:"));
        
        tabHistorial = new JTable();
        tabHistorial.setDefaultEditor(Object.class, null);
        
        // Creamos las columnas para la tabla del historial
        modeloHistorial = new DefaultTableModel(new String[]{"Decisión", "Evento", "Agencia"}, 0);
        tabHistorial.setModel(modeloHistorial);
        
        panelHistorial.add(new JScrollPane(tabHistorial), BorderLayout.CENTER);
        
        panelDivisor.add(panelHistorial);
    }

    // --- MÉTODOS PARA EL HISTORIAL ---
    public void agregarAlHistorial(String decision, String evento, String agencia) {
        // Añade una nueva fila a la tabla de historial
        modeloHistorial.addRow(new Object[]{decision, evento, agencia});
    }

    // --- GETTERS Y SETTERS ---
    public JFrame getFrame() { return frame; }
    public JTable getTabOfrecimientos() { return tabOfrecimientos; }
    public void setNombreEmpresa(String nombre) { lblEmpresaActual.setText("Ofrecimientos pendientes para: " + nombre); }

    // --- LISTENERS ---
    public void addAceptarListener(ActionListener listener) { btnAceptar.addActionListener(listener); }
    public void addRechazarListener(ActionListener listener) { btnRechazar.addActionListener(listener); }
    public void addVolverListener(ActionListener listener) { btnVolver.addActionListener(listener); }
}