package app.dto;

public class gestionarOfrecimientosReportajesDTO {
    // Estos nombres coincidirán con los alias (AS) de la consulta SQL
    private String idEvento;
    private String descripcionEvento;
    private String fechaEvento;
    private String nombreAgencia;
    private String estado;

    // Getters y Setters necesarios para que SwingUtil y DbUtil hagan su magia
    public String getIdEvento() { return idEvento; }
    public void setIdEvento(String idEvento) { this.idEvento = idEvento; }

    public String getDescripcionEvento() { return descripcionEvento; }
    public void setDescripcionEvento(String descripcionEvento) { this.descripcionEvento = descripcionEvento; }

    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getNombreAgencia() { return nombreAgencia; }
    public void setNombreAgencia(String nombreAgencia) { this.nombreAgencia = nombreAgencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}