package app.model;

import java.util.List;

import app.dto.CalculoDietasDTO;
import giis.demo.util.Database;

public class CalculoDietasModel {

    private Database db = new Database();

    public List<CalculoDietasDTO> getReportajesAsignados(String nombreReportero) {
        String sql = "SELECT rep.id_reportaje, rep.id_evento, rep.titulo, e.descripcion, "
                + "e.provincia AS provincia_evento, e.pais AS pais_evento, r.provincia_residencia "
                + "FROM Asignacion a "
                + "JOIN Reportero r ON r.id_reportero = a.id_reportero "
                + "JOIN Reportaje rep ON rep.id_evento = a.id_evento "
                + "JOIN Evento e ON e.id_evento = rep.id_evento "
                + "WHERE r.nombre = ? "
                + "ORDER BY e.fecha_inicio DESC, rep.id_reportaje DESC";
        return db.executeQueryPojo(CalculoDietasDTO.class, sql, nombreReportero);
    }

    public CalculoDietasDTO getDetalleDietaReportaje(int idReportaje, String nombreReportero) {
        String sql = "SELECT rep.id_reportaje, rep.id_evento, rep.titulo, e.descripcion, "
                + "COALESCE(e.provincia, '') AS provincia_evento, COALESCE(e.pais, '') AS pais_evento, "
                + "COALESCE(r.provincia_residencia, '') AS provincia_residencia, "
                + "COALESCE(e.precio, 0) AS coste_dia, "
                + "CASE WHEN UPPER(COALESCE(r.provincia_residencia, '')) = UPPER(COALESCE(e.provincia, '')) "
                + "THEN 0 ELSE COALESCE(da.importe_diario, 0) END AS dieta_alojamiento, "
                + "COALESCE(dm.importe_diario, 0) AS dieta_manutencion, "
                + "CASE "
                + "WHEN e.fecha_inicio IS NULL OR e.fecha_fin IS NULL THEN 1 "
                + "WHEN julianday(e.fecha_fin) - julianday(e.fecha_inicio) + 1 < 1 THEN 1 "
                + "ELSE CAST(julianday(e.fecha_fin) - julianday(e.fecha_inicio) + 1 AS INTEGER) "
                + "END AS numero_dias "
                + "FROM Asignacion a "
                + "JOIN Reportero r ON r.id_reportero = a.id_reportero "
                + "JOIN Reportaje rep ON rep.id_evento = a.id_evento "
                + "JOIN Evento e ON e.id_evento = rep.id_evento "
                + "LEFT JOIN Dieta_Alojamiento da ON da.pais = e.pais AND da.provincia = e.provincia "
                + "LEFT JOIN Dieta_Manutencion dm ON dm.pais = e.pais "
                + "WHERE rep.id_reportaje = ? AND r.nombre = ?";

        List<CalculoDietasDTO> rows = db.executeQueryPojo(CalculoDietasDTO.class, sql, idReportaje, nombreReportero);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public double calcularTotal(CalculoDietasDTO dieta) {
        if (dieta == null) {
            return 0;
        }
        double alojamiento = dieta.getDieta_alojamiento() == null ? 0 : dieta.getDieta_alojamiento();
        double manutencion = dieta.getDieta_manutencion() == null ? 0 : dieta.getDieta_manutencion();
        double costeDia = dieta.getCoste_dia() == null ? 0 : dieta.getCoste_dia();
        int numeroDias = dieta.getNumero_dias() == null ? 1 : dieta.getNumero_dias();
        return (alojamiento + manutencion + costeDia) * numeroDias;
    }
}