package app.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.dto.CalculoDietasDTO;
import app.model.CalculoDietasModel;
import giis.demo.util.Database;

public class TestCalculoDietasModel {

    private Database db = new Database();
    private CalculoDietasModel model = new CalculoDietasModel();

    @BeforeEach
    public void setUp() {
        db.loadDatabase();
    }

    @Test
    public void testDietaAlojamientoEsCeroSiMismaProvincia() {
        CalculoDietasDTO detalle = model.getDetalleDietaReportaje(1, "Ana Blanco");

        assertNotNull(detalle);
        assertEquals(0.0, detalle.getDieta_alojamiento(), 0.001);
        assertEquals(162.50, model.calcularTotal(detalle), 0.001);
    }

    @Test
    public void testTotalIncluyeAlojamientoSiEsProvinciaDistinta() {
        CalculoDietasDTO detalle = model.getDetalleDietaReportaje(2, "Lois Lane");

        assertNotNull(detalle);
        assertEquals(95.0, detalle.getDieta_alojamiento(), 0.001);
        assertEquals(137.0, model.calcularTotal(detalle), 0.001);
    }
}