package fr.efrei.transfret;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TarifTransportTest {

    private static final double PRECISION = 0.001;

    @Test
    void route_au_km_et_au_kg() {
        assertEquals(568, new TarifRoute().calculer(465, 100, 1), PRECISION);
    }

    @Test
    void rail_au_km_et_au_kg() {
        assertEquals(442, new TarifRail().calculer(465, 1000, 1), PRECISION);
    }

    @Test
    void rail_minimum_150() {
        assertEquals(150, new TarifRail().calculer(100, 10, 1), PRECISION);
    }

    @Test
    void mer_au_km_et_au_kg() {
        assertEquals(3375, new TarifMer().calculer(5850, 15000, 1), PRECISION);
    }

    @Test
    void mer_minimum_500() {
        assertEquals(500, new TarifMer().calculer(760, 10, 1), PRECISION);
    }

    @Test
    void air_poids_reel() {
        assertEquals(2850, new TarifAir().calculer(1000, 500, 1), PRECISION);
    }

    @Test
    void air_poids_volumetrique_si_plus_grand() {
        assertEquals(1351.5, new TarifAir().calculer(1000, 10, 1), PRECISION);
    }
}
