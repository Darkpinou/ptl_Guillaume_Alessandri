package fr.efrei.transfret;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateurPrixTest {

    private static final double PRECISION = 0.001;
    private static final Prix CENT_EUROS = colis -> 100;

    private static List<Colis> colis(TypeMarchandise type, double poids, double valeur) {
        return List.of(new Colis("colis", poids, 10, 10, 10, type, valeur));
    }

    @Test
    void prix_de_base_delegue_au_tarif_du_mode() {
        assertEquals(568, new PrixDeBase(new TarifRoute(), 465).calculer(colis(TypeMarchandise.STANDARD, 100, 0)), PRECISION);
    }

    @Test
    void fragile_majore_de_15_pourcent() {
        assertEquals(115, new SupplementFragile(CENT_EUROS).calculer(colis(TypeMarchandise.FRAGILE, 1, 0)), PRECISION);
    }

    @Test
    void fragile_sans_colis_fragile() {
        assertEquals(100, new SupplementFragile(CENT_EUROS).calculer(colis(TypeMarchandise.STANDARD, 1, 0)), PRECISION);
    }

    @Test
    void dangereux_majore_de_30_pourcent() {
        assertEquals(130, new SupplementDangereux(CENT_EUROS).calculer(colis(TypeMarchandise.DANGEREUX, 1, 0)), PRECISION);
    }

    @Test
    void dangereux_sans_colis_dangereux() {
        assertEquals(100, new SupplementDangereux(CENT_EUROS).calculer(colis(TypeMarchandise.STANDARD, 1, 0)), PRECISION);
    }

    @Test
    void refrigere_majore_de_20_pourcent() {
        assertEquals(120, new SupplementRefrigere(CENT_EUROS).calculer(colis(TypeMarchandise.REFRIGERE, 1, 0)), PRECISION);
    }

    @Test
    void refrigere_sans_colis_refrigere() {
        assertEquals(100, new SupplementRefrigere(CENT_EUROS).calculer(colis(TypeMarchandise.STANDARD, 1, 0)), PRECISION);
    }

    @Test
    void urgence_majore_de_50_pourcent() {
        assertEquals(150, new SupplementUrgence(CENT_EUROS).calculer(colis(TypeMarchandise.STANDARD, 1, 0)), PRECISION);
    }

    @Test
    void remise_delegue_a_la_politique() {
        assertEquals(90, new Remise(CENT_EUROS, new RemiseEntreprise()).calculer(colis(TypeMarchandise.STANDARD, 1, 0)), PRECISION);
    }

    @Test
    void assurance_2_pourcent_de_la_valeur() {
        assertEquals(140, new Assurance(CENT_EUROS).calculer(colis(TypeMarchandise.STANDARD, 1, 2000)), PRECISION);
    }

    @Test
    void assurance_minimum_15() {
        assertEquals(115, new Assurance(CENT_EUROS).calculer(colis(TypeMarchandise.STANDARD, 1, 300)), PRECISION);
    }

    // base 4000 : seule l'urgence fait passer le seuil de 5000 de la remise entreprise.
    // Remise avant l'urgence : 5600. Assurance avant la remise : 5270.
    @Test
    void remise_apres_urgence_et_assurance_apres_remise() {
        double prix = new CalculateurPrix().calculerPrixHT(colis(TypeMarchandise.STANDARD, 34420, 10000), 465,
                ModeTransport.ROUTE, true, true, TypeClient.ENTREPRISE);
        assertEquals(5300, prix, PRECISION);
    }
}
