package app.ut;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import app.dto.ReporteroDisplayDTO;
import app.model.AsignacionReporterosModel;
import giis.demo.util.Database;

@DisplayName("HU Asignación de reporteros - pruebas unitarias de lógica de negocio")
public class TestAsignacionReporterosModel {

    private Database db = new Database();
    private AsignacionReporterosModel model = new AsignacionReporterosModel();

    @BeforeEach
    public void setUp() {
        db.loadDatabase();
    }

    @Nested
    @DisplayName("CP-AR-01...05 Disponibilidad y filtros de selección")
    class DisponibilidadYFiltros {

        @Test
        @DisplayName("CP-AR-01 Reportero ya asignado al evento no está disponible y se respeta tipo BASE")
        public void cpAr01DebeExcluirYaAsignadosYRespetarTipo() {
            List<ReporteroDisplayDTO> disponibles = model.getReporterosDisponibles(
                    "2026-10-25", "2026-10-27",
                    "Agencia Central de Noticias",
                    20,
                    false, "BASE", false);

            Set<Integer> ids = disponibles.stream()
                    .map(ReporteroDisplayDTO::getIdReportero)
                    .collect(Collectors.toSet());

            assertAll(
                () -> assertFalse(ids.contains(1)),   // ya asignado al evento 20
                () -> assertTrue(ids.contains(9)),    // BASE válido de agencia
                () -> assertFalse(ids.contains(11))   // no es BASE
            );
        }

        @Test
        @DisplayName("CP-AR-02 Solo especializados devuelve reporteros con temática compatible")
        public void cpAr02SoloEspecializadosFiltraPorTematica() {
            List<ReporteroDisplayDTO> especializados = model.getReporterosDisponibles(
                    "2026-10-20", "2026-10-21",
                    "Agencia Central de Noticias",
                    11,
                    true, "TODOS", false);

            assertAll(
                () -> assertTrue(especializados.stream().anyMatch(r -> r.getIdReportero() == 14)), // especializado e interesado
                () -> assertFalse(especializados.stream().anyMatch(r -> r.getIdReportero() == 1)), // no especializado
                () -> assertFalse(especializados.stream().anyMatch(r -> r.getIdReportero() == 11)) // ya asignado al evento 11
            );
        }

        @Test
        @DisplayName("CP-AR-03 Solo freelance aplica reglas de preferencia")
        public void cpAr03SoloFreelanceAplicaPreferenciasValidas() {
            List<ReporteroDisplayDTO> freelancers = model.getReporterosDisponibles(
                    "2026-10-25", "2026-10-27",
                    "Agencia Central de Noticias",
                    20,
                    false, "TODOS", true);

            Set<Integer> ids = freelancers.stream()
                    .map(ReporteroDisplayDTO::getIdReportero)
                    .collect(Collectors.toSet());

            assertAll(
                () -> assertFalse(ids.contains(14)), // NO_INTERESADO en evento 20
                () -> assertFalse(ids.contains(13))  // sin preferencia en evento 20
            );
        }

        @Test
        @DisplayName("CP-AR-04 Sin reporteros disponibles devuelve lista vacía")
        void cpAr06SinDisponiblesDevuelveListaVacia() {
            List<ReporteroDisplayDTO> res = model.getReporterosDisponibles(
                    "2026-10-25", "2026-10-27",
                    "Agencia Central de Noticias",
                    20,
                    true,                // solo especializados
                    "CAMARÓGRAFO",       // filtro restrictivo
                    true                 // solo freelance
            );

            assertTrue(res.isEmpty());
        }

        @Test
        @DisplayName("CP-AR-05 Reportero con solape de fechas no está disponible")
        void cpAr07SolapeFechasExcluyeReportero() {
            List<ReporteroDisplayDTO> disponibles = model.getReporterosDisponibles(
                    "2026-03-10", "2026-03-10",
                    "Agencia Central de Noticias",
                    99,
                    false, "TODOS", false
            );

            Set<Integer> ids = disponibles.stream()
                    .map(ReporteroDisplayDTO::getIdReportero)
                    .collect(Collectors.toSet());

            assertFalse(ids.contains(1)); // reportero 1 está asignado a evento 1 con solape
        }
    }

    @Nested
    @DisplayName("CP-AR-06...07 Gestión y cierre de asignaciones")
    class GestionYCierre {

        @Test
        @DisplayName("CP-AR-06 Guardar asignación persiste y marca responsable")
        public void cpAr04GuardarAsignacionPersisteEstado() {
            model.guardarAsignacion(99, 9, true, "ABIERTA");

            List<ReporteroDisplayDTO> asignados = model.getReporterosAsignados(99);

            assertAll(
                () -> assertEquals(1, asignados.size()),
                () -> assertEquals(9, asignados.get(0).getIdReportero()),
                () -> assertEquals(1, asignados.get(0).getEsResponsable())
            );
        }

        @Test
        @DisplayName("CP-AR-07 Finalizar evento actualiza estado de asignación")
        public void cpAr05FinalizacionMarcaEventoFinalizado() {
            // Arrange
            assertFalse(model.isAsignacionFinalizada(99));

            // Act
            model.actualizarFinalizacionEvento(99, 999); // parámetro reportero no interviene en este método

            // Assert
            assertTrue(model.isAsignacionFinalizada(99));
        }
    }
}