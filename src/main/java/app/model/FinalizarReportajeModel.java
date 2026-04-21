package app.model;

import java.util.List;

import app.dto.FinalizarReportajeDTO;
import giis.demo.util.Database;

public class FinalizarReportajeModel {

    private Database db = new Database();

    public List<FinalizarReportajeDTO> getReportajesPorFinalizar(String nombreResponsable) {
        String sql = "SELECT rep.id_reportaje, rep.id_evento, e.descripcion, rep.titulo, rep.subtitulo, rep.cuerpo "
                + "FROM Reportaje rep "
                + "JOIN Evento e ON e.id_evento = rep.id_evento "
                + "JOIN Asignacion a ON a.id_evento = rep.id_evento AND a.es_responsable = true "
                + "JOIN Reportero r ON r.id_reportero = a.id_reportero "
                + "WHERE r.nombre = ? AND rep.fecha_entrega IS NOT NULL AND rep.estado_entrega = 'ABIERTA' "
                + "ORDER BY rep.id_reportaje DESC";
        return db.executeQueryPojo(FinalizarReportajeDTO.class, sql, nombreResponsable);
    }

    public FinalizarReportajeDTO getDetalleReportaje(int idReportaje, String nombreResponsable) {
        String sql = "SELECT rep.id_reportaje, rep.id_evento, e.descripcion, rep.titulo, rep.subtitulo, rep.cuerpo "
                + "FROM Reportaje rep "
                + "JOIN Evento e ON e.id_evento = rep.id_evento "
                + "JOIN Asignacion a ON a.id_evento = rep.id_evento AND a.es_responsable = true "
                + "JOIN Reportero r ON r.id_reportero = a.id_reportero "
                + "WHERE rep.id_reportaje = ? AND r.nombre = ?";
        List<FinalizarReportajeDTO> rows = db.executeQueryPojo(FinalizarReportajeDTO.class, sql, idReportaje, nombreResponsable);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public List<FinalizarReportajeDTO> getMultimedia(int idReportaje, String tipo) {
        String sql = "SELECT id_multimedia, ruta, tipo FROM Multimedia "
                + "WHERE id_reportaje = ? AND tipo = ? ORDER BY id_multimedia DESC";
        return db.executeQueryPojo(FinalizarReportajeDTO.class, sql, idReportaje, tipo);
    }

    public boolean existeRuta(String ruta) {
        String sql = "SELECT id_multimedia FROM Multimedia WHERE UPPER(ruta) = UPPER(?)";
        return !db.executeQueryPojo(FinalizarReportajeDTO.class, sql, ruta).isEmpty();
    }

    public void insertarMultimedia(int idReportaje, String nombreResponsable, String tipo, String ruta) {
        String sql = "INSERT INTO Multimedia(id_reportaje, id_reportero, tipo, ruta, estado) "
                + "VALUES (?, (SELECT id_reportero FROM Reportero WHERE nombre = ?), ?, ?, 'DEFINITIVO')";
        db.executeUpdate(sql, idReportaje, nombreResponsable, tipo, ruta);
    }

    public void eliminarMultimedia(int idMultimedia) {
        String sql = "DELETE FROM Multimedia WHERE id_multimedia = ?";
        db.executeUpdate(sql, idMultimedia);
    }

    public List<FinalizarReportajeDTO> getReporterosRevision(int idReportaje, boolean finalizada) {
        String sql = "SELECT rep.id_reportero, rep.nombre AS nombre_reportero, "
                + "CASE WHEN rr.revision_finalizada = true THEN 1 ELSE 0 END AS revision_finalizada "
                + "FROM Reportaje r "
                + "JOIN Asignacion a ON a.id_evento = r.id_evento "
                + "JOIN Reportero rep ON rep.id_reportero = a.id_reportero "
                + "LEFT JOIN Revision_Reportero rr ON rr.id_reportaje = r.id_reportaje AND rr.id_reportero = rep.id_reportero "
                + "WHERE r.id_reportaje = ? "
                + "AND (CASE WHEN rr.revision_finalizada = true THEN 1 ELSE 0 END) = ? "
                + "ORDER BY rep.nombre";
        return db.executeQueryPojo(FinalizarReportajeDTO.class, sql, idReportaje, finalizada ? 1 : 0);
    }

    public List<FinalizarReportajeDTO> getComentariosAsignados(int idReportaje) {
        String sql = "SELECT c.id_comentario, rep.nombre AS autor_comentario, c.comentario, c.fecha_hora "
                + "FROM Comentario_Revision c "
                + "JOIN Reportero rep ON rep.id_reportero = c.id_reportero "
                + "JOIN Reportaje r ON r.id_reportaje = c.id_reportaje "
                + "JOIN Asignacion a ON a.id_evento = r.id_evento AND a.id_reportero = c.id_reportero "
                + "WHERE c.id_reportaje = ? "
                + "ORDER BY c.fecha_hora, c.id_comentario";
        return db.executeQueryPojo(FinalizarReportajeDTO.class, sql, idReportaje);
    }

    public boolean puedeFinalizar(int idReportaje) {
        String sql = "SELECT 1 "
                + "FROM Reportaje r "
                + "WHERE r.id_reportaje = ? "
                + "AND NOT EXISTS ("
                + "  SELECT 1 FROM Asignacion a "
                + "  LEFT JOIN Revision_Reportero rr ON rr.id_reportaje = r.id_reportaje AND rr.id_reportero = a.id_reportero "
                + "  WHERE a.id_evento = r.id_evento "
                + "  AND (rr.revision_finalizada IS NULL OR rr.revision_finalizada = false)"
                + ")";
        return !db.executeQueryArray(sql, idReportaje).isEmpty();
    }

    public boolean existeTituloEnOtroReportaje(String titulo, int idReportaje) {
        String sql = "SELECT id_reportaje FROM Reportaje WHERE UPPER(titulo)=UPPER(?) AND id_reportaje <> ?";
        return !db.executeQueryPojo(FinalizarReportajeDTO.class, sql, titulo, idReportaje).isEmpty();
    }

    public void actualizarContenido(int idReportaje, String titulo, String subtitulo, String cuerpo) {
        String sql = "UPDATE Reportaje SET titulo = ?, subtitulo = ?, cuerpo = ? WHERE id_reportaje = ?";
        db.executeUpdate(sql, titulo, subtitulo, cuerpo, idReportaje);
    }

    public void finalizarReportaje(int idReportaje) {
        String sqlReportaje = "UPDATE Reportaje SET estado_entrega = 'FINALIZADA', fecha_fin_entrega = datetime('now') "
                + "WHERE id_reportaje = ?";
        db.executeUpdate(sqlReportaje, idReportaje);

        String sqlAsignacion = "UPDATE Asignacion SET estado_asignacion = 'FINALIZADA', fecha_fin_asignacion = datetime('now') "
                + "WHERE id_evento = (SELECT id_evento FROM Reportaje WHERE id_reportaje = ?)";
        db.executeUpdate(sqlAsignacion, idReportaje);
    }
}