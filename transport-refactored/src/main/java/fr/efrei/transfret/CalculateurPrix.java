package fr.efrei.transfret;

import java.util.List;

public class CalculateurPrix {

    private static final double MAJORATION_FRAGILE = 1.15;
    private static final double MAJORATION_DANGEREUX = 1.30;
    private static final double MAJORATION_REFRIGERE = 1.20;
    private static final double MAJORATION_URGENCE = 1.5;

    private static final double TAUX_ASSURANCE = 0.02;
    private static final double ASSURANCE_MINIMUM = 15;

    public double calculerPrixHT(List<Colis> colis, int distance, ModeTransport mode, boolean urgent, boolean assurance, TypeClient typeClient) {
        double poids = 0;
        double volume = 0;
        double valeur = 0;
        boolean fragile = false;
        boolean dangereux = false;
        boolean refrigere = false;
        for (Colis c : colis) {
            poids = poids + c.getPoids();
            volume = volume + c.getVolume();
            valeur = valeur + c.getValeurDeclaree();
            if (c.getType() == TypeMarchandise.FRAGILE) fragile = true;
            if (c.getType() == TypeMarchandise.DANGEREUX) dangereux = true;
            if (c.getType() == TypeMarchandise.REFRIGERE) refrigere = true;
        }

        double prix = mode.getTarif().calculer(distance, poids, volume);

        // suppléments
        if (fragile) prix = prix * MAJORATION_FRAGILE;
        if (dangereux) prix = prix * MAJORATION_DANGEREUX;
        if (refrigere) prix = prix * MAJORATION_REFRIGERE;
        if (urgent) prix = prix * MAJORATION_URGENCE;

        prix = typeClient.getRemise().appliquer(prix);

        // assurance
        if (assurance) {
            double montantAssurance = valeur * TAUX_ASSURANCE;
            if (montantAssurance < ASSURANCE_MINIMUM) montantAssurance = ASSURANCE_MINIMUM;
            prix = prix + montantAssurance;
        }
        return Utils.arrondir(prix);
    }
}
