package app.dto;
import lombok.*;

public class EventoDisplayDTO {
	private Integer id_evento;
	private String descripcion;
	private String fecha;

	// Getters y Setters manuales
	public Integer getId_evento() { return id_evento; }
	public void setId_evento(Integer id_evento) { this.id_evento = id_evento; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getFecha() { return fecha; }
	public void setFecha(String fecha) { this.fecha = fecha; }
}