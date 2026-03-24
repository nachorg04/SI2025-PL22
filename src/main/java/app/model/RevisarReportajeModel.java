package app.model;

import java.util.ArrayList;
import java.util.List;

import app.dto.RevisarReportajeDTO;
import giis.demo.util.Database;

public class RevisarReportajeModel {

    private Database db = new Database();

    /**
     * Reportajes que el reportero puede revisar.
     * En este caso: asignados al reportero y en estado EN_REVISION.
     */
    public List<RevisarReportajeDTO> getReportajesParaRevision(String nombreReportero) {
        String sql = "SELECT r.id_reportaje, e.descripcion AS evento, r.titulo, r.subtitulo, r.cuerpo, r.estado_revision "
                + "FROM Reportaje r "
                + "JOIN Evento e ON e.id_evento = r.id_evento "
                + "JOIN Asignacion a ON a.id_evento = r.id_evento "
                + "JOIN Reportero rep ON rep.id_reportero = a.id_reportero "
                + "WHERE rep.nombre = ? AND r.estado_revision = 'EN_REVISION' "
                + "ORDER BY r.id_reportaje";

        return db.executeQueryPojo(RevisarReportajeDTO.class, sql, nombreReportero);
    }

    public List<String> getMultimediaDefinitiva(int idReportaje, String tipo) {
        String sql = "SELECT ruta FROM Multimedia WHERE id_reportaje = ? AND tipo = ? AND estado = 'DEFINITIVO' ORDER BY id_multimedia";
        List<RevisarReportajeDTO> rows = db.executeQueryPojo(RevisarReportajeDTO.class, sql, idReportaje, tipo);

        List<String> res = new ArrayList<>();
        for (RevisarReportajeDTO row : rows) {
            res.add(row.getRuta());
        }
        return res;
    }

    public List<RevisarReportajeDTO> getComentariosRevision(int idReportaje) {
        String sql = "SELECT c.id_comentario, c.id_reportaje, rep.nombre AS autor_comentario, c.comentario, c.fecha_hora "
                + "FROM Comentario_Revision c "
                + "JOIN Reportero rep ON rep.id_reportero = c.id_reportero "
                + "WHERE c.id_reportaje = ? "
                + "ORDER BY c.fecha_hora, c.id_comentario";
        return db.executeQueryPojo(RevisarReportajeDTO.class, sql, idReportaje);
    }

    public void guardarComentario(int idReportaje, String nombreReportero, String comentario) {
        String sql = "INSERT INTO Comentario_Revision(id_reportaje, id_reportero, comentario) "
                + "VALUES (?, (SELECT id_reportero FROM Reportero WHERE nombre = ?), ?)";
        db.executeUpdate(sql, idReportaje, nombreReportero, comentario);
    }

    public void marcarRevisado(int idReportaje) {
        String sql = "UPDATE Reportaje SET estado_revision = 'REVISADO' WHERE id_reportaje = ?";
        db.executeUpdate(sql, idReportaje);
    }
}