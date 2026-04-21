package app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import app.dto.CalculoDietasDTO;

public class CalculoDietasView extends JFrame {

    private JComboBox<CalculoDietasDTO> comboReportajes;
    private JButton btnCalcular;

    private JTextField txtCosteDia;
    private JTextField txtDietaAlojamiento;
    private JTextField txtDietaManutencion;
    private JTextField txtNumeroDias;
    private JTextField txtTotal;

    public CalculoDietasView() {
        setTitle("Cálculo total de dietas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 650, 360);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(contentPane);

        JPanel panelSuperior = new JPanel(new BorderLayout(8, 0));
        panelSuperior.add(new JLabel("Reportaje asignado:"), BorderLayout.WEST);
        comboReportajes = new JComboBox<>();
        panelSuperior.add(comboReportajes, BorderLayout.CENTER);
        btnCalcular = new JButton("Calcular total dietas");
        panelSuperior.add(btnCalcular, BorderLayout.EAST);

        contentPane.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 8, 8));

        panelCampos.add(new JLabel("Coste por día:"));
        txtCosteDia = crearCampoSoloLectura();
        panelCampos.add(txtCosteDia);

        panelCampos.add(new JLabel("Dieta alojamiento:"));
        txtDietaAlojamiento = crearCampoSoloLectura();
        panelCampos.add(txtDietaAlojamiento);

        panelCampos.add(new JLabel("Dieta manutención:"));
        txtDietaManutencion = crearCampoSoloLectura();
        panelCampos.add(txtDietaManutencion);

        panelCampos.add(new JLabel("Número de días:"));
        txtNumeroDias = crearCampoSoloLectura();
        panelCampos.add(txtNumeroDias);

        panelCampos.add(new JLabel("TOTAL:"));
        txtTotal = crearCampoSoloLectura();
        panelCampos.add(txtTotal);

        contentPane.add(panelCampos, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSur.add(new JLabel("Fórmula: (alojamiento + manutención + coste por día) × número de días"));
        contentPane.add(panelSur, BorderLayout.SOUTH);
    }

    private JTextField crearCampoSoloLectura() {
        JTextField txt = new JTextField();
        txt.setEditable(false);
        return txt;
    }

    public JComboBox<CalculoDietasDTO> getComboReportajes() {
        return comboReportajes;
    }

    public void addCalcularListener(ActionListener listener) {
        btnCalcular.addActionListener(listener);
    }

    public void setCosteDia(String valor) {
        txtCosteDia.setText(valor);
    }

    public void setDietaAlojamiento(String valor) {
        txtDietaAlojamiento.setText(valor);
    }

    public void setDietaManutencion(String valor) {
        txtDietaManutencion.setText(valor);
    }

    public void setNumeroDias(String valor) {
        txtNumeroDias.setText(valor);
    }

    public void setTotal(String valor) {
        txtTotal.setText(valor);
    }
}