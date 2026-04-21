package app.model;

import java.util.ArrayList;
import java.util.List;

import app.dto.AccederReportajeDTO;
import giis.demo.util.Database;

public class AccederReportajeModel {

    private Database db = new Database();

    /**
     * Obtiene los eventos a los que una empresa especifica tiene acceso concedido.
     */
    public List<AccederReportajeDTO> getEventosConAcceso(String nombreEmpresa) {
        String sql = "SELECT e.id_evento, e.descripcion AS nombre_evento "
                + "FROM Evento e "
                + "JOIN Ofrecimiento o ON e.id_evento = o.id_evento "
                + "JOIN Empresa_Comunicacion emp ON o.id_empresa = emp.id_empresa "
                + "WHERE emp.nombre = ? AND ("
                + "o.tiene_acceso = 1 "
                + "OR o.acceso_especial_embargo = 1 "
                + "OR (UPPER(o.estado) = 'ACEPTADO' AND EXISTS ("
                + "SELECT 1 FROM Reportaje r "
                + "WHERE r.id_evento = e.id_evento "
                + "AND r.fecha_fin_embargo IS NOT NULL "
                + "AND datetime(r.fecha_fin_embargo) > CURRENT_TIMESTAMP"
                + ")))";

        return db.executeQueryPojo(AccederReportajeDTO.class, sql, nombreEmpresa);
    }

    /**
     * Recupera el contenido de la ultima version del reportaje junto con la informacion
     * de embargo y acceso especial para la empresa.
     */
    public AccederReportajeDTO getDetalleUltimaVersion(int idEvento, String nombreEmpresa) {
        String sql = "SELECT r.id_reportaje, r.titulo, "
                + "COALESCE(v.subtitulo_guardado, r.subtitulo) AS subtitulo, "
                + "COALESCE(v.cuerpo_guardado, r.cuerpo) AS cuerpo, "
                + "COALESCE(v.fecha_cambio, date(r.fecha_entrega)) AS fecha, "
                + "COALESCE(v.hora_cambio, time(r.fecha_entrega)) AS hora, "
                + "r.fecha_fin_embargo, "
                + "COALESCE(o.acceso_especial_embargo, 0) AS acceso_especial_embargo, "
                + "COALESCE(o.tiene_acceso, 0) AS tiene_acceso "
                + "FROM Reportaje r "
                + "JOIN Ofrecimiento o ON o.id_evento = r.id_evento "
                + "JOIN Empresa_Comunicacion emp ON emp.id_empresa = o.id_empresa "
                + "LEFT JOIN Version_Reportaje v ON r.id_reportaje = v.id_reportaje "
                + "WHERE r.id_evento = ? AND emp.nombre = ? "
                + "ORDER BY v.fecha_cambio DESC, v.hora_cambio DESC, v.id_version DESC "
                + "LIMIT 1";

        List<AccederReportajeDTO> res = db.executeQueryPojo(AccederReportajeDTO.class, sql, idEvento, nombreEmpresa);
        if (res.isEmpty()) {
            return null;
        }

        AccederReportajeDTO detalle = res.get(0);
        if (!detalle.isEmbargoActivo()) {
            detalle.setFotos(getRutasMultimediaDefinitiva(detalle.getId_reportaje(), "IMAGEN"));
            detalle.setVideos(getRutasMultimediaDefinitiva(detalle.getId_reportaje(), "VIDEO"));
        }
        return detalle;
    }

    private List<String> getRutasMultimediaDefinitiva(int idReportaje, String tipo) {
        String sql = "SELECT ruta "
                + "FROM Multimedia "
                + "WHERE id_reportaje = ? AND tipo = ? AND estado = 'DEFINITIVO' "
                + "ORDER BY id_multimedia";

        List<AccederReportajeDTO> rows = db.executeQueryPojo(AccederReportajeDTO.class, sql, idReportaje, tipo);
        List<String> rutas = new ArrayList<>();
        for (AccederReportajeDTO row : rows) {
            rutas.add(row.getRuta());
        }
        return rutas;
    }

    public void marcarReportajeComoDescargado(int idEvento, String nombreEmpresa) {
        String sql = "UPDATE Ofrecimiento SET descargado = true "
                + "WHERE id_evento = ? "
                + "AND id_empresa = (SELECT id_empresa FROM Empresa_Comunicacion WHERE nombre = ?)";
        db.executeUpdate(sql, idEvento, nombreEmpresa);
    }
}
