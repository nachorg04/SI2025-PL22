package app.dto;

public class informeReportajesDTO {
    private String tituloReportaje;
    private String evento;
    private String fechaEvento;
    private Double precio;

    public String getTituloReportaje() { return tituloReportaje; }
    public void setTituloReportaje(String tituloReportaje) { this.tituloReportaje = tituloReportaje; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}