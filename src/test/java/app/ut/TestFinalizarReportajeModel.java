package app.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.dto.FinalizarReportajeDTO;
import app.model.FinalizarReportajeModel;
import giis.demo.util.Database;

public class TestFinalizarReportajeModel {

    private Database db = new Database();
    private FinalizarReportajeModel model = new FinalizarReportajeModel();

    @BeforeEach
    public void setUp() {
        db.loadDatabase();
    }

    @Test
    public void getReportajesPorFinalizarDebeDevolverSoloLosAbiertosDelResponsable() {
        List<FinalizarReportajeDTO> reportajes = model.getReportajesPorFinalizar("Sara Carbonero");

        assertEquals(1, reportajes.size());
        assertEquals(3, reportajes.get(0).getId_reportaje());
        assertEquals("Empate sin goles en el derbi", reportajes.get(0).getTitulo());
    }

    @Test
    public void getDetalleReportajeDebeDevolverNullSiElReporteroNoEsResponsable() {
        FinalizarReportajeDTO detalle = model.getDetalleReportaje(3, "Ana Blanco");

        assertNull(detalle);
    }

    @Test
    public void getMultimediaDebeRecuperarLasPiezasDelTipoSolicitado() {
        List<FinalizarReportajeDTO> multimedia = model.getMultimedia(4, "IMAGEN");

        assertEquals(1, multimedia.size());
        assertEquals("/media/reportaje4/fidma-publico.jpg", multimedia.get(0).getRuta());
        assertEquals("IMAGEN", multimedia.get(0).getTipo());
    }

    @Test
    public void insertarYEliminarMultimediaDebeActualizarElListadoYLaRutaExistente() {
        model.insertarMultimedia(3, "Sara Carbonero", "VIDEO", "/media/reportaje3/resumen-final.mp4");

        assertTrue(model.existeRuta("/MEDIA/REPORTAJE3/RESUMEN-FINAL.MP4"));

        List<FinalizarReportajeDTO> multimedia = model.getMultimedia(3, "VIDEO");
        assertEquals(1, multimedia.size());

        model.eliminarMultimedia(multimedia.get(0).getId_multimedia());

        assertFalse(model.existeRuta("/media/reportaje3/resumen-final.mp4"));
        assertTrue(model.getMultimedia(3, "VIDEO").isEmpty());
    }

    @Test
    public void getReporterosRevisionDebeSepararPendientesYFinalizados() {
        List<FinalizarReportajeDTO> finalizados = model.getReporterosRevision(1, true);
        List<FinalizarReportajeDTO> pendientes = model.getReporterosRevision(3, false);

        assertEquals(2, finalizados.size());
        assertTrue(finalizados.stream().allMatch(FinalizarReportajeDTO::isRevisionFinalizada));

        assertEquals(2, pendientes.size());
        assertTrue(pendientes.stream().noneMatch(FinalizarReportajeDTO::isRevisionFinalizada));
    }

    @Test
    public void getComentariosAsignadosDebeRecuperarSoloComentariosDeAsignadosAlEvento() {
        List<FinalizarReportajeDTO> comentarios = model.getComentariosAsignados(1);

        assertEquals(1, comentarios.size());
        assertEquals("Julia Otero", comentarios.get(0).getAutor_comentario());
    }

    @Test
    public void puedeFinalizarDebeSerFalseSiQuedaAlgunaRevisionPendienteYTrueSiEstanTodasCerradas() {
        assertFalse(model.puedeFinalizar(2));
        assertTrue(model.puedeFinalizar(1));

        db.executeUpdate("INSERT INTO Revision_Reportero (id_reportaje, id_reportero, revision_finalizada, fecha_revision) "
                + "VALUES (3, 7, 1, '2026-04-16 09:00:00')");
        db.executeUpdate("INSERT INTO Revision_Reportero (id_reportaje, id_reportero, revision_finalizada, fecha_revision) "
                + "VALUES (3, 8, 1, '2026-04-16 09:05:00')");

        assertTrue(model.puedeFinalizar(3));
    }

    @Test
    public void existeTituloEnOtroReportajeDebeDetectarDuplicadosPeroPermitirElMismoRegistro() {
        assertTrue(model.existeTituloEnOtroReportaje("Multitudinaria marcha ecologista", 3));
        assertFalse(model.existeTituloEnOtroReportaje("Empate sin goles en el derbi", 3));
    }

    @Test
    public void actualizarContenidoYFinalizarReportajeDebePersistirCambiosYCerrarAsignaciones() {
        db.executeUpdate("INSERT INTO Revision_Reportero (id_reportaje, id_reportero, revision_finalizada, fecha_revision) "
                + "VALUES (3, 7, 1, '2026-04-16 09:00:00')");
        db.executeUpdate("INSERT INTO Revision_Reportero (id_reportaje, id_reportero, revision_finalizada, fecha_revision) "
                + "VALUES (3, 8, 1, '2026-04-16 09:05:00')");

        model.actualizarContenido(3, "Derbi resuelto desde la defensa", "Cronica final cerrada",
                "Texto consolidado y listo para publicar.");
        model.finalizarReportaje(3);

        FinalizarReportajeDTO detalle = model.getDetalleReportaje(3, "Sara Carbonero");
        assertNotNull(detalle);
        assertEquals("Derbi resuelto desde la defensa", detalle.getTitulo());
        assertEquals("Cronica final cerrada", detalle.getSubtitulo());
        assertEquals("Texto consolidado y listo para publicar.", detalle.getCuerpo());

        List<Object[]> reportaje = db.executeQueryArray(
                "SELECT estado_entrega, fecha_fin_entrega FROM Reportaje WHERE id_reportaje = 3");
        assertEquals("FINALIZADA", reportaje.get(0)[0]);
        assertNotNull(reportaje.get(0)[1]);

        List<Object[]> asignaciones = db.executeQueryArray(
                "SELECT COUNT(*) FROM Asignacion WHERE id_evento = 4 AND estado_asignacion = 'FINALIZADA' "
                        + "AND fecha_fin_asignacion IS NOT NULL");
        assertEquals(2, ((Number) asignaciones.get(0)[0]).intValue());
    }
}
