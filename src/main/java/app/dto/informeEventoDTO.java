package app.dto;

public class informeEventoDTO {
    // Para la lista de eventos
    private String idEvento;
    private String fecha;
    private String descripcion;
    
    // Para el estado del reportaje
    private String entregado;
    private String autor;

    // Getters y Setters necesarios para que DbUtil haga su magia
    public String getIdEvento() { return idEvento; }
    public void setIdEvento(String idEvento) { this.idEvento = idEvento; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEntregado() { return entregado; }
    public void setEntregado(String entregado) { this.entregado = entregado; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
}