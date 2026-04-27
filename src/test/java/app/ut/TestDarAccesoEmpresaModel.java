package app.ut;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.dto.EmpresaDisplayDTO;
import app.model.DarAccesoEmpresaModel;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class TestDarAccesoEmpresaModel {

    private Database db = new Database();
    private DarAccesoEmpresaModel model = new DarAccesoEmpresaModel();

    @BeforeEach
    public void setUp() {
        db.loadDatabase();
    }

    // CP-01: Verificar cálculo de elegibilidad de pago (tarifa/pago por reportaje)
    @Test
    public void getEmpresasAceptadasDebeCalcularElegiblePagoCorrectamente() {
        var empresas = model.getEmpresasAceptadasByAcceso(20, false);

        Map<Integer, EmpresaDisplayDTO> porId = empresas.stream()
                .collect(Collectors.toMap(EmpresaDisplayDTO::getIdEmpresa, e -> e, (a, b) -> a));

        assertEquals(1, porId.get(7).getElegiblePago());   // tarifa + al corriente
        assertEquals(0, porId.get(8).getElegiblePago());   // tarifa sin pago al corriente
        assertEquals(1, porId.get(9).getElegiblePago());   // sin tarifa pero reportaje pagado
        assertEquals(0, porId.get(10).getElegiblePago());  // sin tarifa y sin pagar
    }

    // CP-02: Conceder acceso cuando empresa elegible y evento finalizado
    @Test
    public void concederAccesoDebePermitirEmpresaElegible() {
        assertDoesNotThrow(() -> model.concederAcceso(20, 7));

        boolean sigueSinAcceso = model.getEmpresasAceptadasByAcceso(20, false).stream()
                .anyMatch(e -> e.getIdEmpresa() == 7);
        assertFalse(sigueSinAcceso);
    }

    // CP-03: No conceder acceso cuando empresa NO elegible por pago
    @Test
    public void concederAccesoDebeFallarSiEmpresaNoElegible() {
        assertThrows(ApplicationException.class, () -> model.concederAcceso(20, 8));
    }

    // CP-04: No conceder acceso si evento no finalizado
    @Test
    public void concederAccesoDebeFallarSiEventoNoFinalizado() {
        assertThrows(ApplicationException.class, () -> model.concederAcceso(4, 4));
    }

    // CP-05: No revocar acceso si el contenido ya fue descargado
    @Test
    public void revocarAccesoNoDebeCambiarSiYaDescargado() {
        model.revocarAcceso(1, 1);

        boolean tieneAcceso = model.getEmpresasAceptadasByAcceso(1, true).stream()
                .anyMatch(e -> e.getIdEmpresa() == 1);
        assertTrue(tieneAcceso);
    }

    // CP-06: Revocar acceso cuando NO está descargado
    @Test
    public void revocarAccesoDebeQuitarAccesoSiNoEstaDescargado() {
        model.concederAcceso(20, 7);
        model.revocarAcceso(20, 7);

        boolean sigueConAcceso = model.getEmpresasAceptadasByAcceso(20, true).stream()
                .anyMatch(e -> e.getIdEmpresa() == 7);
        assertFalse(sigueConAcceso);
    }

    // CP-07: Conceder acceso especial por embargo activo a empresa elegible
    @Test
    public void concederAccesoEspecialEmbargoDebePermitirSiEmbargoActivoYElegible() {
        assertDoesNotThrow(() -> model.concederAccesoEspecialEmbargo(20, 7));
    }

    // CP-08: No conceder acceso especial si el embargo está caducado
    @Test
    public void concederAccesoEspecialEmbargoDebeFallarSiEmbargoCaducado() {
        db.executeUpdate("UPDATE Evento SET finalizado = 1 WHERE id_evento = 200");
        assertThrows(ApplicationException.class, () -> model.concederAccesoEspecialEmbargo(200, 1));
    }
}