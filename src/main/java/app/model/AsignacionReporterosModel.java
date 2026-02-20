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
	public List<EventoDisplayDTO> getEventosSinAsignar(String nombreAgencia) {
		String sql = "SELECT e.id_evento AS idEvento, e.descripcion, e.fecha FROM Evento e " +
				"JOIN Agencia a ON e.id_agencia = a.id_agencia " +
				"WHERE a.nombre = ? AND e.id_evento NOT IN (SELECT id_evento FROM Asignacion)";
		return db.executeQueryPojo(EventoDisplayDTO.class, sql, nombreAgencia);
	}

	/**
	 * HU 33524: Un reportero está disponible si no está asignado a un evento 
	 * cuya fecha coincida con la del evento seleccionado.
	 */
	public List<ReporteroDisplayDTO> getReporterosDisponibles(String fechaEvento, String nombreAgencia) {
	    String sql = "SELECT r.id_reportero AS idReportero, r.nombre FROM Reportero r " +
	                 "JOIN Agencia a ON r.id_agencia = a.id_agencia " +
	                 "WHERE a.nombre = ? AND r.id_reportero NOT IN (" +
	                 "    SELECT a.id_reportero FROM Asignacion a " +
	                 "    INNER JOIN Evento e ON a.id_evento = e.id_evento " +
	                 "    WHERE e.fecha = ?" +
	                 ")";
	    return db.executeQueryPojo(ReporteroDisplayDTO.class, sql, nombreAgencia, fechaEvento);
	}

	/**
	 * Método para guardar la asignación final en la base de datos cuando se pulse "Aceptar".
	 */
	public void guardarAsignacion(Integer idEvento, Integer idReportero) {
		String sql = "INSERT INTO Asignacion (id_evento, id_reportero) VALUES (?, ?)";
		db.executeUpdate(sql, idEvento, idReportero);
	}
}