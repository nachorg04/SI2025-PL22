package app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class gestionarOfrecimientosReportajesView {

    private JFrame frame;
    private JTable tabOfrecimientos;
    
    // Filtros
    private JRadioButton rdbtnPendientes;
    private JRadioButton rdbtnConDecision;
    
    // Botones
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JButton btnEliminarDecision; // NUEVO BOTÓN
    private JButton btnVolver;
    
    private JLabel lblEmpresaActual;

    public gestionarOfrecimientosReportajesView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión de Ofrecimientos");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        frame.setContentPane(contentPane);

        // --- CABECERA Y FILTROS ---
        JPanel panelNorte = new JPanel(new GridLayout(2, 1));
        
        lblEmpresaActual = new JLabel("🏢 Empresa: ");
        panelNorte.add(lblEmpresaActual);
        
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Filtro de visualización: "));
        
        rdbtnPendientes = new JRadioButton("Ver PENDIENTES de decisión");
        rdbtnConDecision = new JRadioButton("Ver con DECISIÓN TOMADA");
        rdbtnPendientes.setSelected(true); // Por defecto, marcamos Pendientes
        
        ButtonGroup grupoFiltros = new ButtonGroup();
        grupoFiltros.add(rdbtnPendientes);
        grupoFiltros.add(rdbtnConDecision);
        
        panelFiltros.add(rdbtnPendientes);
        panelFiltros.add(rdbtnConDecision);
        panelNorte.add(panelFiltros);
        
        contentPane.add(panelNorte, BorderLayout.NORTH);

        // --- TABLA ÚNICA CENTRAL ---
        tabOfrecimientos = new JTable();
        tabOfrecimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabOfrecimientos.setDefaultEditor(Object.class, null);
        contentPane.add(new JScrollPane(tabOfrecimientos), BorderLayout.CENTER);

        // --- BOTONES SUR ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnVolver = new JButton("Volver");
        btnRechazar = new JButton("Rechazar");
        btnAceptar = new JButton("Aceptar");
        btnEliminarDecision = new JButton("Eliminar Decisión"); // NUEVO
        
        panelSur.add(btnVolver);
        panelSur.add(btnRechazar);
        panelSur.add(btnAceptar);
        panelSur.add(btnEliminarDecision);
        contentPane.add(panelSur, BorderLayout.SOUTH);
    }

    // --- GETTERS ---
    public JFrame getFrame() { return frame; }
    public JTable getTabOfrecimientos() { return tabOfrecimientos; }
    public JRadioButton getRdbtnPendientes() { return rdbtnPendientes; }
    public JRadioButton getRdbtnConDecision() { return rdbtnConDecision; }
    
    // Getters de botones para que el Controller los pueda habilitar/deshabilitar
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public JButton getBtnEliminarDecision() { return btnEliminarDecision; }

    public void setNombreEmpresa(String nombre) { lblEmpresaActual.setText("🏢 Empresa: " + nombre); }

    // --- LISTENERS ---
    public void addAceptarListener(ActionListener listener) { btnAceptar.addActionListener(listener); }
    public void addRechazarListener(ActionListener listener) { btnRechazar.addActionListener(listener); }
    public void addEliminarDecisionListener(ActionListener listener) { btnEliminarDecision.addActionListener(listener); }
    public void addVolverListener(ActionListener listener) { btnVolver.addActionListener(listener); }
    public void addFiltroListener(ActionListener listener) { 
        rdbtnPendientes.addActionListener(listener);
        rdbtnConDecision.addActionListener(listener);
    }
}