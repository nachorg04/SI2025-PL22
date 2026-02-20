package app.dto;
import lombok.*;

public class ReporteroDisplayDTO {
	private Integer idReportero;
	private String nombre;

	// Puerta para la tabla de Java
	public Integer getIdReportero() { return idReportero; }
	public void setIdReportero(Integer idReportero) { this.idReportero = idReportero; }

	// TRUCO: Puerta secreta para que la Base de Datos meta el ID sin quejarse
	public void setId_reportero(Integer id_reportero) { this.idReportero = id_reportero; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
}