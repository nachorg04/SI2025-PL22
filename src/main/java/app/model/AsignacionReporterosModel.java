package app.model;

import giis.demo.util.Database;
import java.util.List;
import app.dto.EventoDisplayDTO;
import app.dto.ReporteroDisplayDTO;

public class AsignacionReporterosModel {

    private Database db = new Database();

    // 1. Eventos SIN asignar 
    public List<EventoDisplayDTO> getEventosSinAsignar(String nombreAgencia) {
        String sql = "SELECT e.id_evento, e.descripcion, e.fecha FROM Evento e " +
                     "JOIN Agencia a ON e.id_agencia = a.id_agencia " +
                     "WHERE a.nombre = ? AND NOT EXISTS (" +
                     "   SELECT 1 FROM Asignacion asig WHERE asig.id_evento = e.id_evento" +
                     ")";
        return db.executeQueryPojo(EventoDisplayDTO.class, sql, nombreAgencia);
    }

    // 2. Eventos CON reporteros asignados
    public List<EventoDisplayDTO> getEventosConAsignacion(String nombreAgencia) {
        String sql = "SELECT DISTINCT e.id_evento, e.descripcion, e.fecha FROM Evento e " +
                     "JOIN Agencia a ON e.id_agencia = a.id_agencia " +
                     "JOIN Asignacion asig ON e.id_evento = asig.id_evento " +
                     "WHERE a.nombre = ?";
        return db.executeQueryPojo(EventoDisplayDTO.class, sql, nombreAgencia);
    }

    // 3. Reporteros Disponibles 
    public List<ReporteroDisplayDTO> getReporterosDisponibles(String fechaEvento, String nombreAgencia) {
        String sql = "SELECT r.id_reportero, r.nombre FROM Reportero r " +
                     "JOIN Agencia a ON r.id_agencia = a.id_agencia " +
                     "WHERE a.nombre = ? AND NOT EXISTS (" +
                     "   SELECT 1 FROM Asignacion asig " +
                     "   JOIN Evento e ON asig.id_evento = e.id_evento " +
                     "   WHERE e.fecha = ? AND asig.id_reportero = r.id_reportero" +
                     ")";
        return db.executeQueryPojo(ReporteroDisplayDTO.class, sql, nombreAgencia, fechaEvento);
    }

    // 4. Reporteros que YA están asignados a este evento
    public List<ReporteroDisplayDTO> getReporterosAsignados(Integer idEvento) {
        String sql = "SELECT r.id_reportero, r.nombre FROM Reportero r " +
                     "JOIN Asignacion a ON r.id_reportero = a.id_reportero " +
                     "WHERE a.id_evento = ?";
        return db.executeQueryPojo(ReporteroDisplayDTO.class, sql, idEvento);
    }

    // 5. Borrar todas las asignaciones de un evento de golpe
    public void eliminarAsignacionesPorEvento(Integer idEvento) {
        String sql = "DELETE FROM Asignacion WHERE id_evento = ?";
        db.executeUpdate(sql, idEvento);
    }

    // 6. Guardar Asignacion 
    public void guardarAsignacion(Integer idEvento, Integer idReportero) {
        if (idEvento == null || idReportero == null) {
            throw new giis.demo.util.ApplicationException("Error interno: El ID del evento o reportero está llegando vacío.");
        }
        String sql = "INSERT INTO Asignacion (id_evento, id_reportero) VALUES (?, ?)";
        db.executeUpdate(sql, idEvento, idReportero);
    }
}