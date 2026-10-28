package fr.efrei.transfret;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FluvialTest {

    private static final Client CLIENT = new Client("A", "a@b.fr", "06", "x", TypeClient.PARTICULIER, null);

    private static List<Colis> colis(TypeMarchandise type) {
        return List.of(new Colis("colis", 1000, 10, 10, 10, type, 100));
    }

    private static boolean accepte(TypeMarchandise type, boolean urgent) {
        return new ValidateurExpedition().estValide(CLIENT, colis(type), "Paris", "Le Havre", ModeTransport.FLUVIAL, urgent);
    }

    @Test
    void peniche_prend_le_fluvial_seulement() {
        Peniche peniche = new Peniche("PEN-0001", 2500000);
        assertTrue(peniche.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD), ModeTransport.FLUVIAL));
        assertFalse(peniche.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD), ModeTransport.MER));
    }

    @Test
    void la_flotte_affecte_la_peniche() {
        Vehicule v = new AffectationVehicule(new Flotte()).trouverVehicule(colis(TypeMarchandise.STANDARD), ModeTransport.FLUVIAL);
        assertEquals("PEN-0001", v.getImmatriculation());
    }

    @Test
    void paris_le_havre_accepte() {
        assertTrue(accepte(TypeMarchandise.STANDARD, false));
    }

    @Test
    void refrigere_interdit() {
        assertFalse(accepte(TypeMarchandise.REFRIGERE, false));
    }

    @Test
    void pas_d_urgence() {
        assertFalse(accepte(TypeMarchandise.STANDARD, true));
    }
}
