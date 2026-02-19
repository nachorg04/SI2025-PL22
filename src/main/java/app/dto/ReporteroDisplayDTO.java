package app.dto;
import lombok.*;

public class ReporteroDisplayDTO {
	private Integer id_reportero;
	private String nombre;

	// Getters y Setters manuales
	public Integer getId_reportero() { return id_reportero; }
	public void setId_reportero(Integer id_reportero) { this.id_reportero = id_reportero; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
}