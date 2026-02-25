package app.model;

import java.util.List;
import giis.demo.util.Database;
import app.dto.OfrecerReportajesDTO;

public class OfrecerReportajesModel {

    private Database db = new Database();

    /**
     * Obtiene los eventos que tienen reportero asignado, 
     * filtrados por la AGENCIA seleccionada en el menú principal.
     */
    public List<OfrecerReportajesDTO> getEventosConReportero(String nombreAgencia) {
        // SQL con JOIN para filtrar por el nombre de la agencia
        String sql = "SELECT e.id_evento, e.descripcion AS nombre_evento, r.nombre AS reportero_asignado " +
                     "FROM Evento e " +
                     "JOIN Asignacion a ON e.id_evento = a.id_evento " +
                     "JOIN Reportero r ON a.id_reportero = r.id_reportero " +
                     "JOIN Agencia ag ON e.id_agencia = ag.id_agencia " + 
                     "WHERE ag.nombre = ?"; 
        
        return db.executeQueryPojo(OfrecerReportajesDTO.class, sql, nombreAgencia);
    }

    /**
     * Obtiene las empresas que aún no han recibido una oferta para un evento concreto.
     */
    public List<OfrecerReportajesDTO> getEmpresasSinOferta(int idEvento) {
        String sql = "SELECT id_empresa, nombre AS nombre_empresa " + 
                     "FROM Empresa_Comunicacion " +
                     "WHERE id_empresa NOT IN (" +
                     "  SELECT id_empresa FROM Ofrecimiento WHERE id_evento = ?" +
                     ")";
        return db.executeQueryPojo(OfrecerReportajesDTO.class, sql, idEvento);
    }

    /**
     * Inserta un nuevo ofrecimiento en la tabla Ofrecimiento.
     */
    public void insertarOfrecimientos(int idEvento, int idEmpresa) {
        String sql = "INSERT INTO Ofrecimiento (id_evento, id_empresa, estado, tiene_acceso) VALUES (?, ?, 'PENDIENTE', 0)";
        db.executeUpdate(sql, idEvento, idEmpresa);
    }

    /**
     * Elimina un ofrecimiento (Lógica para el botón Cancelar Todo).
     */
    public void eliminarOfrecimiento(int idEvento, int idEmpresa) {
        String sql = "DELETE FROM Ofrecimiento WHERE id_evento = ? AND id_empresa = ?";
        db.executeUpdate(sql, idEvento, idEmpresa);
    }
}