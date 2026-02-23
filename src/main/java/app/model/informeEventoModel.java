package app.model;

import java.util.List;
import app.dto.AppMainDTO; // Reutilizamos este para sacar listas solo con "nombre"
import app.dto.informeEventoDTO;
import giis.demo.util.Database;

public class informeEventoModel {
    private Database db = new Database();

    // 1. Obtener los eventos que pertenecen a la agencia seleccionada
    public List<informeEventoDTO> getEventosAgencia(String nombreAgencia) {
        String sql = "SELECT e.id_evento AS idEvento, e.fecha AS fecha, e.descripcion AS descripcion " +
                     "FROM Evento e JOIN Agencia a ON e.id_agencia = a.id_agencia " +
                     "WHERE a.nombre = ?";
        return db.executeQueryPojo(informeEventoDTO.class, sql, nombreAgencia);
    }

    // 2. Obtener lista de reporteros asignados al evento
    public List<AppMainDTO> getReporterosAsignados(String idEvento) {
        String sql = "SELECT r.nombre AS nombre " +
                     "FROM Reportero r JOIN Asignacion a ON r.id_reportero = a.id_reportero " +
                     "WHERE a.id_evento = ?";
        return db.executeQueryPojo(AppMainDTO.class, sql, idEvento);
    }

    // 3. Obtener si el reportaje está entregado y quién lo entregó
    public List<informeEventoDTO> getEstadoReportaje(String idEvento) {
        String sql = "SELECT CASE WHEN rep.id_reportaje IS NOT NULL THEN 'SÍ' ELSE 'NO' END AS entregado, " +
                     "r.nombre AS autor " +
                     "FROM Evento e " +
                     "LEFT JOIN Reportaje rep ON e.id_evento = rep.id_evento " +
                     "LEFT JOIN Reportero r ON rep.id_reportero = r.id_reportero " +
                     "WHERE e.id_evento = ?";
        return db.executeQueryPojo(informeEventoDTO.class, sql, idEvento);
    }

    // 4. Obtener las empresas que tienen acceso = 1 (true)
    public List<AppMainDTO> getEmpresasConAcceso(String idEvento) {
        String sql = "SELECT emp.nombre AS nombre " +
                     "FROM Empresa_Comunicacion emp JOIN Ofrecimiento o ON emp.id_empresa = o.id_empresa " +
                     "WHERE o.id_evento = ? AND o.tiene_acceso = 1";
        return db.executeQueryPojo(AppMainDTO.class, sql, idEvento);
    }
}