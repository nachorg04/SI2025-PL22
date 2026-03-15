package app.model;

import java.util.List;
import app.dto.gestionarOfrecimientosReportajesDTO;
import giis.demo.util.Database;

public class gestionarOfrecimientosReportajesModel {
    private Database db = new Database();

    // Consultas puras (Devuelven Object[] para que el controlador haga la lógica)
    public List<Object[]> obtenerTodasLasTematicas() {
        String sql = "SELECT nombre FROM Tematica ORDER BY nombre"; 
        return db.executeQueryArray(sql); 
    }

    public List<Object[]> obtenerTematicasEmpresa(String nombreEmpresa) {
        String sql = "SELECT t.nombre FROM Tematica t " +
                     "JOIN Empresa_Tematica et ON t.id_tematica = et.id_tematica " +
                     "JOIN Empresa_Comunicacion emp ON et.id_empresa = emp.id_empresa " +
                     "WHERE emp.nombre = ? ORDER BY t.nombre";
        return db.executeQueryArray(sql, nombreEmpresa);
    }

    // Modificado para aceptar el filtro de temática
    public List<gestionarOfrecimientosReportajesDTO> getOfrecimientosPendientes(String nombreEmpresa, String tematicaFiltro) {
        String sql = "SELECT e.id_evento AS idEvento, " +
                     "e.descripcion AS descripcionEvento, " +
                     "e.fecha AS fechaEvento, " +
                     "a.nombre AS nombreAgencia, " +
                     "o.estado AS estado, " +
                     "o.tiene_acceso AS tieneAcceso " + 
                     "FROM Ofrecimiento o " +
                     "JOIN Evento e ON o.id_evento = e.id_evento " +
                     "JOIN Agencia a ON e.id_agencia = a.id_agencia " +
                     "JOIN Empresa_Comunicacion emp ON o.id_empresa = emp.id_empresa " +
                     "WHERE emp.nombre = ? AND o.estado = 'PENDIENTE'";
                     
        if (tematicaFiltro != null && !tematicaFiltro.equals("Todas las temáticas")) {
            sql += " AND e.id_evento IN (SELECT id_evento FROM Evento_Tematica evt " +
                   " JOIN Tematica t ON evt.id_tematica = t.id_tematica " +
                   " WHERE t.nombre = '" + tematicaFiltro + "') ";
        }
                     
        return db.executeQueryPojo(gestionarOfrecimientosReportajesDTO.class, sql, nombreEmpresa);
    }

    // Modificado para aceptar el filtro de temática
    public List<gestionarOfrecimientosReportajesDTO> getOfrecimientosConDecision(String nombreEmpresa, String tematicaFiltro) {
        String sql = "SELECT e.id_evento AS idEvento, " +
                     "e.descripcion AS descripcionEvento, " +
                     "e.fecha AS fechaEvento, " +
                     "a.nombre AS nombreAgencia, " +
                     "o.estado AS estado, " +
                     "o.tiene_acceso AS tieneAcceso " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento e ON o.id_evento = e.id_evento " +
                     "JOIN Agencia a ON e.id_agencia = a.id_agencia " +
                     "JOIN Empresa_Comunicacion emp ON o.id_empresa = emp.id_empresa " +
                     "WHERE emp.nombre = ? AND o.estado IN ('ACEPTADO', 'RECHAZADO')";
                     
        if (tematicaFiltro != null && !tematicaFiltro.equals("Todas las temáticas")) {
            sql += " AND e.id_evento IN (SELECT id_evento FROM Evento_Tematica evt " +
                   " JOIN Tematica t ON evt.id_tematica = t.id_tematica " +
                   " WHERE t.nombre = '" + tematicaFiltro + "') ";
        }
                     
        return db.executeQueryPojo(gestionarOfrecimientosReportajesDTO.class, sql, nombreEmpresa);
    }

    public void actualizarEstadoOfrecimiento(String idEvento, String nombreEmpresa, String nuevoEstado) {
        String sql = "UPDATE Ofrecimiento " +
                     "SET estado = ? " +
                     "WHERE id_evento = ? AND id_empresa = (SELECT id_empresa FROM Empresa_Comunicacion WHERE nombre = ?)";
        db.executeUpdate(sql, nuevoEstado, idEvento, nombreEmpresa);
    }
}