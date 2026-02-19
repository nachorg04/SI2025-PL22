package app.model;

import java.util.List;
import app.dto.gestionarOfrecimientosReportajesDTO;
import giis.demo.util.Database;

public class gestionarOfrecimientosReportajesModel {
    private Database db = new Database();

    // Cumple el criterio: "Solo visualizar los eventos para los que aún no hay decisión tomada"
    public List<gestionarOfrecimientosReportajesDTO> getOfrecimientosPendientes(String nombreEmpresa) {
        String sql = "SELECT e.id_evento AS idEvento, " +
                     "e.descripcion AS descripcionEvento, " +
                     "e.fecha AS fechaEvento, " +
                     "a.nombre AS nombreAgencia, " +
                     "o.estado AS estado " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento e ON o.id_evento = e.id_evento " +
                     "JOIN Agencia a ON e.id_agencia = a.id_agencia " +
                     "JOIN Empresa_Comunicacion emp ON o.id_empresa = emp.id_empresa " +
                     "WHERE emp.nombre = ? AND o.estado = 'PENDIENTE'";
                     
        return db.executeQueryPojo(gestionarOfrecimientosReportajesDTO.class, sql, nombreEmpresa);
    }

    // Cumple el criterio: "aceptar o rechazar el ofrecimiento"
    public void actualizarEstadoOfrecimiento(String idEvento, String nombreEmpresa, String nuevoEstado) {
        String sql = "UPDATE Ofrecimiento " +
                     "SET estado = ? " +
                     "WHERE id_evento = ? AND id_empresa = (SELECT id_empresa FROM Empresa_Comunicacion WHERE nombre = ?)";
                     
        db.executeUpdate(sql, nuevoEstado, idEvento, nombreEmpresa);
    }
}