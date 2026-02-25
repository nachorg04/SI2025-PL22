package app.model;

import giis.demo.util.Database;
import app.dto.EventoDisplayDTO;
import app.dto.EmpresaDisplayDTO;
import java.util.List;


public class DarAccesoEmpresaModel {

	private Database db = new Database();

	// 1. Eventos de la agencia que SÍ tienen reportaje entregado
	public List<EventoDisplayDTO> getEventosConReportaje(String nombreAgencia) {
		String sql = "SELECT e.id_evento, e.descripcion, e.fecha FROM Evento e " +
				"JOIN Agencia a ON e.id_agencia = a.id_agencia " +
				"JOIN Reportaje r ON e.id_evento = r.id_evento " +
				"WHERE a.nombre = ?";
		return db.executeQueryPojo(EventoDisplayDTO.class, sql, nombreAgencia);
	}

	// 2. Empresas que han aceptado pero AÚN NO tienen acceso (tiene_acceso = 0)
	public List<EmpresaDisplayDTO> getEmpresasAceptadasSinAcceso(Integer idEvento) {
		String sql = "SELECT emp.id_empresa, emp.nombre FROM Empresa_Comunicacion emp " +
				"JOIN Ofrecimiento o ON emp.id_empresa = o.id_empresa " +
				"WHERE o.id_evento = ? AND o.estado = 'ACEPTADO' AND o.tiene_acceso = 0";
		return db.executeQueryPojo(EmpresaDisplayDTO.class, sql, idEvento);
	}

	// 3. Dar acceso (hacemos un UPDATE en la BD)
	public void concederAcceso(Integer idEvento, Integer idEmpresa) {
		if (idEvento == null || idEmpresa == null) {
			throw new giis.demo.util.ApplicationException("Error interno: Los IDs están vacíos.");
		}
		String sql = "UPDATE Ofrecimiento SET tiene_acceso = 1 WHERE id_evento = ? AND id_empresa = ?";
		db.executeUpdate(sql, idEvento, idEmpresa);
	}
}
