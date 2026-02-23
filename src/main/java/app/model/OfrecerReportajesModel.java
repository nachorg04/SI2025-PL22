package app.model;

import java.util.List;
import giis.demo.util.Database;
import app.dto.OfrecerReportajesDTO;

public class OfrecerReportajesModel {

    private Database db = new Database();

    /**
     * Consulta 1: Obtiene eventos con su reportero asignado.
     * Hacemos JOIN con Asignacion y Reportero para sacar los nombres reales.
     */
    public List<OfrecerReportajesDTO> getEventosConReportero() {
        String sql = "SELECT e.id_evento, e.descripcion AS nombre_evento, r.nombre AS reportero_asignado " +
                     "FROM Evento e " +
                     "JOIN Asignacion a ON e.id_evento = a.id_evento " +
                     "JOIN Reportero r ON a.id_reportero = r.id_reportero";
        return db.executeQueryPojo(OfrecerReportajesDTO.class, sql);
    }

    /**
     * Consulta 2: Obtiene empresas que NO tienen aún una oferta para ese evento.
     */
    public List<OfrecerReportajesDTO> getEmpresasSinOferta(int idEvento) {
        // En tu DB la columna es 'nombre', usamos 'AS nombre_empresa' para el DTO
        String sql = "SELECT id_empresa, nombre AS nombre_empresa " + 
                     "FROM Empresa_Comunicacion " +
                     "WHERE id_empresa NOT IN (" +
                     "  SELECT id_empresa FROM Ofrecimiento WHERE id_evento = ?" +
                     ")";
        return db.executeQueryPojo(OfrecerReportajesDTO.class, sql, idEvento);
    }

    /**
     * Consulta 3: Insertar el ofrecimiento.
     */
    public void insertarOfrecimientos(int idEvento, int idEmpresa) {
        String sql = "INSERT INTO Ofrecimiento (id_evento, id_empresa, estado) VALUES (?, ?, 'PENDIENTE')";
        db.executeUpdate(sql, idEvento, idEmpresa);
    }
    
    public void eliminarOfrecimiento(int idEvento, int idEmpresa) {
        String sql = "DELETE FROM Ofrecimiento WHERE id_evento = ? AND id_empresa = ?";
        db.executeUpdate(sql, idEvento, idEmpresa); 
    }
}