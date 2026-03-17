package app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class informeReportajesView {

    private JFrame frame;
    private JTable tabInforme;
    
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnGenerarInforme;
    
    private JLabel lblPrecioTotal;
    private JLabel lblEmpresaActual;

    public informeReportajesView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Informe de Reportajes Adquiridos");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        frame.setContentPane(contentPane);

        // --- CABECERA Y FILTROS ---
        JPanel panelNorte = new JPanel(new GridLayout(2, 1)); 
        
        lblEmpresaActual = new JLabel("Empresa: ");
        lblEmpresaActual.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelNorte.add(lblEmpresaActual);
        
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtFechaInicio = new JTextField(10);
        txtFechaFin = new JTextField(10);
        btnGenerarInforme = new JButton("Generar Informe");
        
        panelFiltros.add(new JLabel("Fecha Inicio (DD/MM/AAAA): "));
        panelFiltros.add(txtFechaInicio);
        panelFiltros.add(new JLabel("  Fecha Fin (DD/MM/AAAA): "));
        panelFiltros.add(txtFechaFin);
        panelFiltros.add(new JLabel("   "));
        panelFiltros.add(btnGenerarInforme);
        panelNorte.add(panelFiltros);
        
        contentPane.add(panelNorte, BorderLayout.NORTH);

        // --- TABLA CENTRAL ---
        tabInforme = new JTable();
        tabInforme.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabInforme.setDefaultEditor(Object.class, null);
        contentPane.add(new JScrollPane(tabInforme), BorderLayout.CENTER);

        // --- PIE (PRECIO TOTAL) ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblPrecioTotal = new JLabel("PRECIO TOTAL DEL INFORME: 0.00 €");
        lblPrecioTotal.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPrecioTotal.setForeground(new Color(0, 100, 0)); // Verde oscuro
        panelSur.add(lblPrecioTotal);
        
        contentPane.add(panelSur, BorderLayout.SOUTH);
    }

    // --- GETTERS ---
    public JFrame getFrame() { return frame; }
    public JTable getTabInforme() { return tabInforme; }
    public JTextField getTxtFechaInicio() { return txtFechaInicio; }
    public JTextField getTxtFechaFin() { return txtFechaFin; }
    public JButton getBtnGenerarInforme() { return btnGenerarInforme; }

    public void setNombreEmpresa(String nombre) { lblEmpresaActual.setText("Empresa: " + nombre); }
    public void setPrecioTotal(String texto) { lblPrecioTotal.setText(texto); }

    // --- LISTENERS ---
    public void addGenerarInformeListener(ActionListener listener) { btnGenerarInforme.addActionListener(listener); }
}