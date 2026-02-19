package app.dto;

public class gestionarOfrecimientosReportajesDTO {
    private String idEvento;
    private String descripcionEvento;
    private String fechaEvento;
    private String nombreAgencia;
    private String estado;
    
    // --- NUEVOS CAMPOS ---
    private Integer tieneAcceso; // Será 1 (True) o 0 (False) desde la BD
    private String accesoVisible; // Aquí guardaremos "✔️ SÍ" o "❌ NO" para la tabla

    // Getters y Setters anteriores...
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

    // --- GETTERS/SETTERS NUEVOS ---
    public Integer getTieneAcceso() { return tieneAcceso; }
    public void setTieneAcceso(Integer tieneAcceso) { 
        this.tieneAcceso = tieneAcceso;
        // Magia: Cuando Java mete el 0 o 1, autocalculamos el texto bonito para la tabla
        this.accesoVisible = (tieneAcceso != null && tieneAcceso == 1) ? "SÍ" : "NO";
    }

    public String getAccesoVisible() { return accesoVisible; }
    // No necesitamos setter de accesoVisible porque se calcula solo arriba
}