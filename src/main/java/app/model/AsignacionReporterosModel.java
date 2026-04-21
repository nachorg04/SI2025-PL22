package app.model;

import java.util.ArrayList;
import java.util.List;

import app.dto.EventoDisplayDTO;
import app.dto.ReporteroDisplayDTO;
import giis.demo.util.Database;

public class AsignacionReporterosModel {

    private Database db = new Database();

    public List<EventoDisplayDTO> getEventosSinAsignar(String nombreAgencia) {
        String sql = "SELECT e.id_evento, e.descripcion, "
                + "COALESCE(e.fecha_inicio, e.fecha) AS fecha_inicio, "
                + "COALESCE(e.fecha_fin, e.fecha) AS fecha_fin, "
                + "COALESCE(GROUP_CONCAT(DISTINCT t.nombre), 'Sin tematica') AS tematicas "
                + "FROM Evento e "
                + "JOIN Agencia a ON e.id_agencia = a.id_agencia "
                + "LEFT JOIN Evento_Tematica et ON et.id_evento = e.id_evento "
                + "LEFT JOIN Tematica t ON t.id_tematica = et.id_tematica "
                + "WHERE a.nombre = ? "
                + "AND NOT EXISTS ("
                + "   SELECT 1 FROM Asignacion asig WHERE asig.id_evento = e.id_evento"
                + ") "
                + "GROUP BY e.id_evento, e.descripcion, e.fecha_inicio, e.fecha_fin, e.fecha";
        return db.executeQueryPojo(EventoDisplayDTO.class, sql, nombreAgencia);
    }

    public List<EventoDisplayDTO> getEventosConAsignacion(String nombreAgencia) {
        String sql = "SELECT e.id_evento, e.descripcion, "
                + "COALESCE(e.fecha_inicio, e.fecha) AS fecha_inicio, "
                + "COALESCE(e.fecha_fin, e.fecha) AS fecha_fin, "
                + "COALESCE(GROUP_CONCAT(DISTINCT t.nombre), 'Sin tematica') AS tematicas "
                + "FROM Evento e "
                + "JOIN Agencia a ON e.id_agencia = a.id_agencia "
                + "JOIN Asignacion asig ON e.id_evento = asig.id_evento "
                + "LEFT JOIN Evento_Tematica et ON et.id_evento = e.id_evento "
                + "LEFT JOIN Tematica t ON t.id_tematica = et.id_tematica "
                + "WHERE a.nombre = ? "
                + "GROUP BY e.id_evento, e.descripcion, e.fecha_inicio, e.fecha_fin, e.fecha";
        return db.executeQueryPojo(EventoDisplayDTO.class, sql, nombreAgencia);
    }

    public List<ReporteroDisplayDTO> getReporterosDisponibles(String fechaInicioEvento, String fechaFinEvento,
            String nombreAgencia, Integer idEvento, boolean soloEspecializados, String tipoReportero,
            boolean soloFreelance) {
        StringBuilder sql = new StringBuilder(
                "SELECT r.id_reportero, r.nombre, "
                + "CASE WHEN COALESCE(r.es_freelance, 0) = 1 THEN 'FREELANCE' "
                + "ELSE UPPER(COALESCE(r.tipo_reportero, 'BASE')) END AS tipo_reportero, "
                + "COALESCE(pf.estado_preferencia, '-') AS estado_preferencia, "
                + "COALESCE(r.es_freelance, 0) AS es_freelance, "
                + "COALESCE(GROUP_CONCAT(DISTINCT t.nombre), 'Sin especializacion') AS tematicas "
                + "FROM Reportero r "
                + "LEFT JOIN Reportero_Tematica rt ON rt.id_reportero = r.id_reportero "
                + "LEFT JOIN Tematica t ON t.id_tematica = rt.id_tematica "
                + "LEFT JOIN Preferencia_Freelance pf ON pf.id_reportero = r.id_reportero AND pf.id_evento = ? "
                + "WHERE NOT EXISTS ("
                + "   SELECT 1 FROM Asignacion a0 "
                + "   WHERE a0.id_evento = ? AND a0.id_reportero = r.id_reportero"
                + ") "
                + "AND NOT EXISTS ("
                + "   SELECT 1 FROM Asignacion asig "
                + "   JOIN Evento e2 ON asig.id_evento = e2.id_evento "
                + "   WHERE asig.id_reportero = r.id_reportero "
                + "   AND e2.id_evento <> ? "
                + "   AND NOT (COALESCE(e2.fecha_fin, e2.fecha) < ? OR COALESCE(e2.fecha_inicio, e2.fecha) > ?)"
                + ") "
                + "AND ("
                + "   (COALESCE(r.es_freelance, 0) = 0 AND EXISTS ("
                + "       SELECT 1 FROM Agencia a WHERE a.id_agencia = r.id_agencia AND a.nombre = ?"
                + "   )) "
                + "   OR (COALESCE(r.es_freelance, 0) = 1 AND COALESCE(pf.estado_preferencia, '') IN ('INTERESADO', 'DUDANDO'))"
                + ") ");

        List<Object> params = new ArrayList<>();
        params.add(idEvento);
        params.add(idEvento);
        params.add(idEvento);
        params.add(fechaInicioEvento);
        params.add(fechaFinEvento);
        params.add(nombreAgencia);

        if (soloFreelance) {
            sql.append("AND COALESCE(r.es_freelance, 0) = 1 ");
        }

        boolean filtrarPorTipo = tipoReportero != null && !"TODOS".equalsIgnoreCase(tipoReportero);
        if (filtrarPorTipo) {
            sql.append("AND UPPER(COALESCE(r.tipo_reportero, 'BASE')) = UPPER(?) ");
            params.add(tipoReportero);
        }

        if (soloEspecializados) {
            sql.append("AND EXISTS ("
                    + "   SELECT 1 FROM Reportero_Tematica rt2 "
                    + "   JOIN Evento_Tematica et2 ON rt2.id_tematica = et2.id_tematica "
                    + "   WHERE rt2.id_reportero = r.id_reportero AND et2.id_evento = ?"
                    + ") ");
            params.add(idEvento);
        }

        sql.append("GROUP BY r.id_reportero, r.nombre, r.tipo_reportero, pf.estado_preferencia, r.es_freelance");

        return db.executeQueryPojo(ReporteroDisplayDTO.class, sql.toString(), params.toArray());
    }

    public List<ReporteroDisplayDTO> getReporterosAsignados(Integer idEvento) {
        String sql = "SELECT r.id_reportero, r.nombre, "
                + "CASE WHEN COALESCE(r.es_freelance, 0) = 1 THEN 'FREELANCE' "
                + "ELSE UPPER(COALESCE(r.tipo_reportero, 'BASE')) END AS tipo_reportero, "
                + "COALESCE(a.es_responsable, 0) AS es_responsable, "
                + "COALESCE(a.estado_asignacion, 'ABIERTA') AS estado_asignacion, "
                + "COALESCE(pf.estado_preferencia, '-') AS estado_preferencia, "
                + "COALESCE(r.es_freelance, 0) AS es_freelance, "
                + "COALESCE(GROUP_CONCAT(DISTINCT t.nombre), 'Sin especializacion') AS tematicas "
                + "FROM Reportero r "
                + "JOIN Asignacion a ON r.id_reportero = a.id_reportero "
                + "LEFT JOIN Preferencia_Freelance pf ON pf.id_evento = a.id_evento AND pf.id_reportero = r.id_reportero "
                + "LEFT JOIN Reportero_Tematica rt ON rt.id_reportero = r.id_reportero "
                + "LEFT JOIN Tematica t ON t.id_tematica = rt.id_tematica "
                + "WHERE a.id_evento = ? "
                + "GROUP BY r.id_reportero, r.nombre, r.tipo_reportero, a.es_responsable, a.estado_asignacion, "
                + "pf.estado_preferencia, r.es_freelance";
        return db.executeQueryPojo(ReporteroDisplayDTO.class, sql, idEvento);
    }

    public void eliminarAsignacionesPorEvento(Integer idEvento) {
        String sql = "DELETE FROM Asignacion WHERE id_evento = ?";
        db.executeUpdate(sql, idEvento);
    }

    public void guardarAsignacion(Integer idEvento, Integer idReportero) {
        guardarAsignacion(idEvento, idReportero, false, "ABIERTA");
    }

    public void guardarAsignacion(Integer idEvento, Integer idReportero, boolean esResponsable, String estadoAsignacion) {
        if (idEvento == null || idReportero == null) {
            throw new giis.demo.util.ApplicationException(
                    "Error interno: El ID del evento o reportero esta llegando vacio.");
        }
        String sql = "INSERT INTO Asignacion (id_evento, id_reportero, es_responsable, estado_asignacion, fecha_fin_asignacion) "
                + "VALUES (?, ?, ?, ?, NULL)";
        db.executeUpdate(sql, idEvento, idReportero, esResponsable ? 1 : 0, estadoAsignacion);
    }

    public void actualizarFinalizacionEvento(Integer idEvento, Integer idReporteroResponsable) {
        String sql = "UPDATE Evento SET finalizado = 1 WHERE id_evento = ?";
        db.executeUpdate(sql, idEvento);
    }

    public boolean isAsignacionFinalizada(Integer idEvento) {
        String sql = "SELECT COALESCE(finalizado, 0) FROM Evento WHERE id_evento = ?";
        List<Object[]> rows = db.executeQueryArray(sql, idEvento);
        return rows != null && !rows.isEmpty() && ((Number) rows.get(0)[0]).intValue() == 1;
    }
}
