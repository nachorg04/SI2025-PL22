package app.model;

import giis.demo.util.Database;
import java.util.List;
import app.dto.EventoDisplayDTO;      
import app.dto.ReporteroDisplayDTO;    

public class AsignacionReporterosModel {

	
	private Database db = new Database();

	/**
	 * HU 33524: Visualizar la lista de eventos pertenecientes a la agencia 
	 * que no tienen ningún reportero asignado.
	 */
	public List<EventoDisplayDTO> getEventosSinAsignar() {
		// Buscamos eventos cuyo id_evento no exista en la tabla Asignacion
		String sql = "SELECT id_evento, descripcion, fecha FROM Evento " +
				"WHERE id_evento NOT IN (SELECT id_evento FROM Asignacion)";

		return db.executeQueryPojo(EventoDisplayDTO.class, sql);
	}

	/**
	 * HU 33524: Un reportero está disponible si no está asignado a un evento 
	 * cuya fecha coincida con la del evento seleccionado.
	 */
	public List<ReporteroDisplayDTO> getReporterosDisponibles(String fechaEvento) {
		String sql = "SELECT id_reportero, nombre FROM Reportero " +
				"WHERE id_reportero NOT IN (" +
				"    SELECT a.id_reportero FROM Asignacion a " +
				"    INNER JOIN Evento e ON a.id_evento = e.id_evento " +
				"    WHERE e.fecha = ?" +
				")";

		return db.executeQueryPojo(ReporteroDisplayDTO.class, sql, fechaEvento);
	}

	/**
	 * Método para guardar la asignación final en la base de datos cuando se pulse "Aceptar".
	 */
	public void guardarAsignacion(Integer idEvento, Integer idReportero) {
		String sql = "INSERT INTO Asignacion (id_evento, id_reportero) VALUES (?, ?)";
		db.executeUpdate(sql, idEvento, idReportero);
	}
}