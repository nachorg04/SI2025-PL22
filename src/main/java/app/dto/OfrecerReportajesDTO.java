package app.dto;

public class OfrecerReportajesDTO {
    // Atributos de los Eventos
    private int id_evento;
    private String nombre_evento;
    private String reportero_asignado;
    
    // Atributos de las Empresas
    private int id_empresa;
    private String nombre_empresa;

    // Constructor vacío (obligatorio para que la base de datos lo rellene solo)
    public OfrecerReportajesDTO() {}

    // Getters y Setters (Eclipse los usa para pintar las tablas)
    public int getId_evento() { return id_evento; }
    public void setId_evento(int id_evento) { this.id_evento = id_evento; }

    public String getNombre_evento() { return nombre_evento; }
    public void setNombre_evento(String nombre_evento) { this.nombre_evento = nombre_evento; }

    public String getReportero_asignado() { return reportero_asignado; }
    public void setReportero_asignado(String reportero_asignado) { this.reportero_asignado = reportero_asignado; }

    public int getId_empresa() { return id_empresa; }
    public void setId_empresa(int id_empresa) { this.id_empresa = id_empresa; }

    public String getNombre_empresa() { return nombre_empresa; }
    public void setNombre_empresa(String nombre_empresa) { this.nombre_empresa = nombre_empresa; }

    // Este método es útil para mostrar el nombre en el JList de la derecha
    @Override
    public String toString() {
        return nombre_empresa;
    }
}