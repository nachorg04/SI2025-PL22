package app.model;

import java.util.List;
import giis.demo.util.Database;
import app.dto.AccederReportajeDTO;

public class AccederReportajeModel {

    private Database db = new Database();

    /**
     * Obtiene los eventos a los que una empresa específica tiene acceso concedido.
     */
    public List<AccederReportajeDTO> getEventosConAcceso(String nombreEmpresa) {
        // SQL: Filtramos por la empresa seleccionada y que tenga el permiso (tiene_acceso = 1)
        String sql = "SELECT e.id_evento, e.descripcion AS nombre_evento " +
                     "FROM Evento e " +
                     "JOIN Ofrecimiento o ON e.id_evento = o.id_evento " +
                     "JOIN Empresa_Comunicacion emp ON o.id_empresa = emp.id_empresa " +
                     "WHERE emp.nombre = ? AND o.tiene_acceso = 1";
        
        return db.executeQueryPojo(AccederReportajeDTO.class, sql, nombreEmpresa);
    }

    /**
     * Recupera el contenido de la última versión del reportaje para un evento dado.
     */
    public AccederReportajeDTO getDetalleUltimaVersion(int idEvento) {
        // SQL: Unimos Reportaje con sus Versiones, ordenamos por tiempo descendente y tomamos la primera
        String sql = "SELECT r.titulo, v.subtitulo_guardado AS subtitulo, v.cuerpo_guardado AS cuerpo, " +
                     "v.fecha_cambio AS fecha, v.hora_cambio AS hora " +
                     "FROM Reportaje r " +
                     "JOIN Version_Reportaje v ON r.id_reportaje = v.id_reportaje " +
                     "WHERE r.id_evento = ? " +
                     "ORDER BY v.fecha_cambio DESC, v.hora_cambio DESC LIMIT 1";
        
        List<AccederReportajeDTO> res = db.executeQueryPojo(AccederReportajeDTO.class, sql, idEvento);
        return res.isEmpty() ? null : res.get(0);
    }
}