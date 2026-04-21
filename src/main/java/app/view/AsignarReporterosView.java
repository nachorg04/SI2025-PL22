package app.view;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class AsignarReporterosView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tabEventos;
    private JTable tabDisponibles;
    private JTable tabAsignados;
    private JButton btnAsignar;
    private JButton btnEliminar;
    private JButton btnCancelar;
    private JButton btnAceptar;
    private JButton btnFinalizarAsignacion;
    private JButton btnElegirResponsable;
    private JLabel lblNombreAgencia;
    private JLabel lblResponsable;
    private JLabel lblInfoFreelance;
    private JLabel lblInfoFinalizacionFreelance;
    private JComboBox<String> cbFiltroEventos;
    private JComboBox<String> cbFiltroTematicaReporteros;
    private JComboBox<String> cbFiltroTipoReportero;
    private JCheckBox chkEsFreelance;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AsignarReporterosView frame = new AsignarReporterosView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AsignarReporterosView() {
        setTitle("Asignacion de Reporteros con Freelance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1240, 720);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblNombreAgencia = new JLabel("Agencia de Prensa : Mi Agencia");
        lblNombreAgencia.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNombreAgencia.setBounds(10, 9, 500, 22);
        contentPane.add(lblNombreAgencia);

        lblResponsable = new JLabel("Reportero responsable: (sin seleccionar)");
        lblResponsable.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblResponsable.setBounds(640, 20, 530, 22);
        contentPane.add(lblResponsable);

        btnElegirResponsable = new JButton("Elegir responsable");
        btnElegirResponsable.setBounds(640, 50, 210, 26);
        contentPane.add(btnElegirResponsable);

        lblInfoFinalizacionFreelance = new JLabel(
                "No se podra finalizar si hay un freelance asignado en estado DUDANDO.");
        lblInfoFinalizacionFreelance.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblInfoFinalizacionFreelance.setBounds(640, 85, 560, 20);
        contentPane.add(lblInfoFinalizacionFreelance);

        JLabel lblFiltro = new JLabel("Filtro eventos:");
        lblFiltro.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblFiltro.setBounds(10, 55, 130, 20);
        contentPane.add(lblFiltro);

        cbFiltroEventos = new JComboBox<String>();
        cbFiltroEventos.setModel(
                new DefaultComboBoxModel<>(new String[] { "Eventos sin reporteros asignados", "Eventos con reporteros asignados" }));
        cbFiltroEventos.setBounds(160, 55, 300, 22);
        contentPane.add(cbFiltroEventos);

        JLabel lblFiltroTematica = new JLabel("Filtro tematica reportero:");
        lblFiltroTematica.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblFiltroTematica.setBounds(10, 85, 170, 20);
        contentPane.add(lblFiltroTematica);

        cbFiltroTematicaReporteros = new JComboBox<String>();
        cbFiltroTematicaReporteros.setModel(
                new DefaultComboBoxModel<>(new String[] { "Todos los disponibles", "Solo especializados en tematica del evento" }));
        cbFiltroTematicaReporteros.setBounds(190, 85, 300, 22);
        contentPane.add(cbFiltroTematicaReporteros);

        JLabel lblFiltroTipoReportero = new JLabel("Filtro tipo reportero:");
        lblFiltroTipoReportero.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblFiltroTipoReportero.setBounds(10, 115, 150, 20);
        contentPane.add(lblFiltroTipoReportero);

        cbFiltroTipoReportero = new JComboBox<String>();
        cbFiltroTipoReportero.setModel(
                new DefaultComboBoxModel<>(new String[] { "TODOS", "GRAFICO", "CAMAROGRAFO", "BASE" }));
        cbFiltroTipoReportero.setBounds(160, 115, 300, 22);
        contentPane.add(cbFiltroTipoReportero);

        JLabel lblFiltroFreelance = new JLabel("Filtro es freelance:");
        lblFiltroFreelance.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblFiltroFreelance.setBounds(10, 145, 150, 20);
        contentPane.add(lblFiltroFreelance);

        chkEsFreelance = new JCheckBox("Mostrar solo freelance");
        chkEsFreelance.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chkEsFreelance.setBounds(160, 145, 200, 23);
        contentPane.add(chkEsFreelance);

        lblInfoFreelance = new JLabel("Los freelance con estado NO INTERESADO no apareceran.");
        lblInfoFreelance.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblInfoFreelance.setBounds(370, 146, 420, 20);
        contentPane.add(lblInfoFreelance);

        JLabel lblEventos = new JLabel("Eventos");
        lblEventos.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblEventos.setBounds(10, 180, 120, 16);
        contentPane.add(lblEventos);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 205, 500, 410);
        contentPane.add(scrollPane);
        tabEventos = new JTable();
        scrollPane.setViewportView(tabEventos);

        JLabel lblDisponibles = new JLabel("Reporteros Disponibles");
        lblDisponibles.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDisponibles.setBounds(530, 180, 210, 16);
        contentPane.add(lblDisponibles);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(530, 205, 330, 410);
        contentPane.add(scrollPane_1);
        tabDisponibles = new JTable();
        scrollPane_1.setViewportView(tabDisponibles);

        btnAsignar = new JButton("Asignar");
        btnAsignar.setBounds(530, 625, 330, 30);
        contentPane.add(btnAsignar);

        JLabel lblAsignados = new JLabel("Reporteros Asignados");
        lblAsignados.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblAsignados.setBounds(880, 180, 220, 16);
        contentPane.add(lblAsignados);

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(880, 205, 330, 410);
        contentPane.add(scrollPane_2);
        tabAsignados = new JTable();
        scrollPane_2.setViewportView(tabAsignados);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(880, 625, 330, 30);
        contentPane.add(btnEliminar);

        btnFinalizarAsignacion = new JButton("Finalizar asignacion de evento");
        btnFinalizarAsignacion.setBounds(10, 625, 500, 30);
        contentPane.add(btnFinalizarAsignacion);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(1090, 662, 120, 28);
        contentPane.add(btnCancelar);

        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(960, 662, 120, 28);
        contentPane.add(btnAceptar);
    }

    public JTable getTabEventos() { return tabEventos; }
    public JTable getTabDisponibles() { return tabDisponibles; }
    public JTable getTabAsignados() { return tabAsignados; }
    public JButton getBtnAsignar() { return btnAsignar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnFinalizarAsignacion() { return btnFinalizarAsignacion; }
    public JButton getBtnElegirResponsable() { return btnElegirResponsable; }
    public JComboBox<String> getCbFiltroEventos() { return cbFiltroEventos; }
    public JComboBox<String> getCbFiltroTematicaReporteros() { return cbFiltroTematicaReporteros; }
    public JComboBox<String> getCbFiltroTipoReportero() { return cbFiltroTipoReportero; }
    public JCheckBox getChkEsFreelance() { return chkEsFreelance; }
    public JFrame getFrame() { return this; }
    public JLabel getLblTituloAgencia() { return this.lblNombreAgencia; }
    public JLabel getLblResponsable() { return this.lblResponsable; }
    public JLabel getLblInfoFreelance() { return lblInfoFreelance; }
    public JLabel getLblInfoFinalizacionFreelance() { return lblInfoFinalizacionFreelance; }
}
