package app.dto;
import lombok.*;


public class EventoDisplayDTO {
	private Integer idEvento;
	private String descripcion;
	private String fecha;

	// Puerta para la tabla de Java
	public Integer getIdEvento() { return idEvento; }
	public void setIdEvento(Integer idEvento) { this.idEvento = idEvento; }

	// TRUCO: Puerta secreta para que la Base de Datos meta el ID sin quejarse
	public void setId_evento(Integer id_evento) { this.idEvento = id_evento; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getFecha() { return fecha; }
	public void setFecha(String fecha) { this.fecha = fecha; }
}