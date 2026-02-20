package app.model;

import giis.demo.util.Database;
import java.util.List;
import app.dto.EventoDisplayDTO;      
import app.dto.ReporteroDisplayDTO;    

public class AsignacionReporterosModel {
	private Database db = new Database();

	public List<EventoDisplayDTO> getEventosSinAsignar(String nombreAgencia) {
		// SQL limpio, sin alias ni trucos matemáticos. Java eliminará el guión bajo solo.
		String sql = "SELECT e.id_evento, e.descripcion, e.fecha FROM Evento e " +
				"JOIN Agencia a ON e.id_agencia = a.id_agencia " +
				"WHERE a.nombre = ? AND NOT EXISTS (" +
				"    SELECT 1 FROM Asignacion asig WHERE asig.id_evento = e.id_evento" +
				")";
		return db.executeQueryPojo(EventoDisplayDTO.class, sql, nombreAgencia);
	}

	public List<ReporteroDisplayDTO> getReporterosDisponibles(String fechaEvento, String nombreAgencia) {
		// SQL limpio, sin alias
		String sql = "SELECT r.id_reportero, r.nombre FROM Reportero r " +
				"JOIN Agencia a ON r.id_agencia = a.id_agencia " +
				"WHERE a.nombre = ? AND NOT EXISTS (" +
				"    SELECT 1 FROM Asignacion asig " +
				"    JOIN Evento e ON asig.id_evento = e.id_evento " +
				"    WHERE e.fecha = ? AND asig.id_reportero = r.id_reportero" +
				")";
		return db.executeQueryPojo(ReporteroDisplayDTO.class, sql, nombreAgencia, fechaEvento);
	}

	public void guardarAsignacion(Integer idEvento, Integer idReportero) {
		// Si el ID llega nulo, LANZAMOS ERROR para que se bloquee y la pantalla NO nos diga que ha habido éxito.
		if (idEvento == null || idReportero == null) {
			throw new giis.demo.util.ApplicationException("Error interno: El ID del evento o reportero está llegando vacío.");
		}

		String sql = "INSERT INTO Asignacion (id_evento, id_reportero) VALUES (?, ?)";
		db.executeUpdate(sql, idEvento, idReportero);
	}
}