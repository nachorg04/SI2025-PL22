package app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class gestionarOfrecimientosReportajesView {

    private JFrame frame;
    private JTable tabOfrecimientos;
    
    // Filtros de Estado
    private JRadioButton rdbtnPendientes;
    private JRadioButton rdbtnConDecision;
    
    // NUEVOS Filtros de Temática (HU 34084)
    private JCheckBox chkEspecializacion;
    private JComboBox<String> comboTematicas;
    
    // Botones
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JButton btnEliminarDecision; 
    private JButton btnVolver;
    
    private JLabel lblEmpresaActual;

    public gestionarOfrecimientosReportajesView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión de Ofrecimientos");
        frame.setBounds(100, 100, 850, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        frame.setContentPane(contentPane);

        // --- CABECERA Y FILTROS ---
        JPanel panelNorte = new JPanel(new GridLayout(3, 1)); 
        
        lblEmpresaActual = new JLabel("🏢 Empresa: ");
        panelNorte.add(lblEmpresaActual);
        
        // Fila de filtros de estado
        JPanel panelFiltrosEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltrosEstado.add(new JLabel("Estado de Decisión: "));
        rdbtnPendientes = new JRadioButton("Ver PENDIENTES de decisión");
        rdbtnConDecision = new JRadioButton("Ver con DECISIÓN TOMADA");
        rdbtnPendientes.setSelected(true); 
        
        ButtonGroup grupoFiltros = new ButtonGroup();
        grupoFiltros.add(rdbtnPendientes);
        grupoFiltros.add(rdbtnConDecision);
        
        panelFiltrosEstado.add(rdbtnPendientes);
        panelFiltrosEstado.add(rdbtnConDecision);
        panelNorte.add(panelFiltrosEstado);

        // NUEVA Fila de filtros de Temáticas (HU 34084)
        JPanel panelFiltrosTematicas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkEspecializacion = new JCheckBox("Mostrar solo eventos de mis especializaciones");
        comboTematicas = new JComboBox<String>();
        comboTematicas.setPreferredSize(new Dimension(200, 25)); 
        
        panelFiltrosTematicas.add(chkEspecializacion);
        panelFiltrosTematicas.add(new JLabel("  Filtrar por temática: "));
        panelFiltrosTematicas.add(comboTematicas);
        panelNorte.add(panelFiltrosTematicas);
        
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
        btnEliminarDecision = new JButton("Eliminar Decisión");
        
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
    public JCheckBox getChkEspecializacion() { return chkEspecializacion; }
    public JComboBox<String> getComboTematicas() { return comboTematicas; }
    
    // Getters de botones
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public JButton getBtnEliminarDecision() { return btnEliminarDecision; }

    public void setNombreEmpresa(String nombre) { lblEmpresaActual.setText("🏢 Empresa: " + nombre); }

    // --- LISTENERS ---
    public void addAceptarListener(ActionListener listener) { btnAceptar.addActionListener(listener); }
    public void addRechazarListener(ActionListener listener) { btnRechazar.addActionListener(listener); }
    public void addEliminarDecisionListener(ActionListener listener) { btnEliminarDecision.addActionListener(listener); }
    public void addVolverListener(ActionListener listener) { btnVolver.addActionListener(listener); }
    
    // Listeners de Filtros
    public void addFiltroEstadoListener(ActionListener listener) { 
        rdbtnPendientes.addActionListener(listener);
        rdbtnConDecision.addActionListener(listener);
    }
    public void addChkEspecializacionListener(ActionListener listener) { chkEspecializacion.addActionListener(listener); }
    public void addComboTematicasListener(ActionListener listener) { comboTematicas.addActionListener(listener); }
}