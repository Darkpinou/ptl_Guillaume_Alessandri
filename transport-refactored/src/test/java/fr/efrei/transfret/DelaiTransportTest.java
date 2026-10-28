package fr.efrei.transfret;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelaiTransportTest {

    @Test
    void route_9_heures_par_jour() {
        assertEquals(2, new DelaiRoute().calculer(1260, false));
    }

    @Test
    void route_urgente_18_heures_par_jour() {
        assertEquals(1, new DelaiRoute().calculer(1260, true));
    }

    @Test
    void rail_un_jour_de_plus() {
        assertEquals(2, new DelaiRail().calculer(1440, false));
    }

    @Test
    void rail_urgent_un_jour_de_moins() {
        assertEquals(1, new DelaiRail().calculer(1440, true));
    }

    @Test
    void mer_deux_jours_de_plus() {
        assertEquals(11, new DelaiMer().calculer(5850, false));
    }

    @Test
    void air_un_jour() {
        assertEquals(1, new DelaiAir().calculer(465, false));
    }

    @Test
    void air_long_courrier_deux_jours() {
        assertEquals(2, new DelaiAir().calculer(5840, false));
    }

    @Test
    void air_long_courrier_urgent_un_jour() {
        assertEquals(1, new DelaiAir().calculer(5840, true));
    }
}
