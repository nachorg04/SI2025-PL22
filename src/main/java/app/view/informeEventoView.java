package app.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class informeEventoView {

    private JFrame frame;
    private JLabel lblAgencia;
    private JTable tabEventos;
    private JButton btnGenerarInforme;
    private JButton btnExportarCSV;
    private JButton btnVolver;

    // Componentes del informe que se mostrarán abajo
    private JPanel panelInforme;
    private JLabel lblEntregado;
    private JLabel lblAutor;
    private JTable tabReporteros;
    private JTable tabEmpresas;

    public informeEventoView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Informe de Eventos - Agencia");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- ZONA NORTE: Selección de evento ---
        JPanel panelNorte = new JPanel(new BorderLayout(5, 5));
        panelNorte.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        lblAgencia = new JLabel("Agencia: ");
        lblAgencia.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelNorte.add(lblAgencia, BorderLayout.NORTH);

        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabEventos.setDefaultEditor(Object.class, null);
        JScrollPane scrollEventos = new JScrollPane(tabEventos);
        scrollEventos.setPreferredSize(new Dimension(750, 150));
        panelNorte.add(scrollEventos, BorderLayout.CENTER);

        JPanel panelBotoneraNorte = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        btnGenerarInforme = new JButton("Generar Informe por Pantalla");
        panelBotoneraNorte.add(btnVolver);
        panelBotoneraNorte.add(btnGenerarInforme);
        panelNorte.add(panelBotoneraNorte, BorderLayout.SOUTH);

        frame.getContentPane().add(panelNorte, BorderLayout.NORTH);

        // --- ZONA CENTRO: Panel oculto del Informe ---
        panelInforme = new JPanel(new BorderLayout(10, 10));
        panelInforme.setBorder(BorderFactory.createTitledBorder("INFORME DEL EVENTO"));
        panelInforme.setVisible(false); // Oculto hasta que se pulse el botón

        // Datos básicos (entregado y autor)
        JPanel panelDatosReportaje = new JPanel(new GridLayout(2, 1));
        panelDatosReportaje.setBorder(new EmptyBorder(5, 10, 5, 10));
        lblEntregado = new JLabel("Reportaje entregado: -");
        lblAutor = new JLabel("Entregado por: -");
        panelDatosReportaje.add(lblEntregado);
        panelDatosReportaje.add(lblAutor);
        panelInforme.add(panelDatosReportaje, BorderLayout.NORTH);

        // Tablas gemelas (Reporteros y Empresas)
        JPanel panelTablasInforme = new JPanel(new GridLayout(1, 2, 10, 0));
        
        tabReporteros = new JTable();
        tabReporteros.setDefaultEditor(Object.class, null);
        JPanel panelRep = new JPanel(new BorderLayout());
        panelRep.add(new JLabel("Reporteros asignados:"), BorderLayout.NORTH);
        panelRep.add(new JScrollPane(tabReporteros), BorderLayout.CENTER);
        
        tabEmpresas = new JTable();
        tabEmpresas.setDefaultEditor(Object.class, null);
        JPanel panelEmp = new JPanel(new BorderLayout());
        panelEmp.add(new JLabel("Empresas con acceso:"), BorderLayout.NORTH);
        panelEmp.add(new JScrollPane(tabEmpresas), BorderLayout.CENTER);

        panelTablasInforme.add(panelRep);
        panelTablasInforme.add(panelEmp);
        panelInforme.add(panelTablasInforme, BorderLayout.CENTER);

        // Botón de exportar CSV abajo a la derecha
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExportarCSV = new JButton("Guardar como CSV");
        panelSur.add(btnExportarCSV);
        panelInforme.add(panelSur, BorderLayout.SOUTH);

        frame.getContentPane().add(panelInforme, BorderLayout.CENTER);
    }

    // Getters y Setters
    public JFrame getFrame() { return frame; }
    public JTable getTabEventos() { return tabEventos; }
    public JTable getTabReporteros() { return tabReporteros; }
    public JTable getTabEmpresas() { return tabEmpresas; }
    public JPanel getPanelInforme() { return panelInforme; }
    
    public void setNombreAgencia(String nombre) { lblAgencia.setText("Agencia: " + nombre); }
    public void setDatosReportaje(String entregado, String autor) {
        lblEntregado.setText("Reportaje entregado: " + entregado);
        lblAutor.setText("Entregado por: " + (autor != null ? autor : "(En blanco)"));
    }

    // Listeners para los botones
    public void addGenerarInformeListener(ActionListener listener) { btnGenerarInforme.addActionListener(listener); }
    public void addExportarCSVListener(ActionListener listener) { btnExportarCSV.addActionListener(listener); }
    public void addVolverListener(ActionListener listener) { btnVolver.addActionListener(listener); }
}