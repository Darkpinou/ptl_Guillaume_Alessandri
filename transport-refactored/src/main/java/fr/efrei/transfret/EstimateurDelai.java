package fr.efrei.transfret;

public class EstimateurDelai {

    private static final int DELAI_MINIMUM = 1;

    // retourne le délai de livraison en jours
    public int calculerDelai(int distance, ModeTransport mode, boolean urgent) {
        return Math.max(mode.getDelai().calculer(distance, urgent), DELAI_MINIMUM);
    }
}
