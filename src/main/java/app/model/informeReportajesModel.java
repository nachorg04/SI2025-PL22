package app.model;

import java.util.List;
import app.dto.informeReportajesDTO;
import giis.demo.util.Database;

public class informeReportajesModel {
    private Database db = new Database();

    public List<informeReportajesDTO> obtenerInforme(String nombreEmpresa, String fechaInicioBD, String fechaFinBD) {
        String sql = "SELECT r.titulo AS tituloReportaje, " +
                     "e.descripcion AS evento, " +
                     "e.fecha AS fechaEvento, " +
                     "e.precio AS precio " +
                     "FROM Reportaje r " +
                     "JOIN Evento e ON r.id_evento = e.id_evento " +
                     "JOIN Ofrecimiento o ON e.id_evento = o.id_evento " +
                     "JOIN Empresa_Comunicacion emp ON o.id_empresa = emp.id_empresa " +
                     "WHERE emp.nombre = ? " +
                     "AND o.tiene_acceso = 1 " + // 1 es 'true' en SQLite
                     "AND e.fecha >= ? AND e.fecha <= ? " +
                     "ORDER BY e.fecha ASC";
                     
        return db.executeQueryPojo(informeReportajesDTO.class, sql, nombreEmpresa, fechaInicioBD, fechaFinBD);
    }
}