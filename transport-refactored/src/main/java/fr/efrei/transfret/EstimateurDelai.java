package fr.efrei.transfret;

public class EstimateurDelai {

    private static final double ROUTE_VITESSE_KMH = 70.0;
    private static final int ROUTE_HEURES_PAR_JOUR = 9;
    private static final int ROUTE_HEURES_PAR_JOUR_URGENT = 18;
    private static final double RAIL_KM_PAR_JOUR = 1440.0;
    private static final int RAIL_JOURS_EN_PLUS = 1;
    private static final int RAIL_JOURS_GAGNES_URGENT = 1;
    private static final double MER_KM_PAR_JOUR = 720.0;
    private static final int MER_JOURS_EN_PLUS = 2;
    private static final int AIR_JOURS = 1;
    private static final int AIR_SEUIL_LONG_COURRIER_KM = 3000;
    private static final int AIR_JOURS_LONG_COURRIER = 2;
    private static final int DELAI_MINIMUM = 1;

    // retourne le délai de livraison en jours
    public int calculerDelai(int distance, ModeTransport mode, boolean urgent) {
        int jours = 0;
        switch (mode) {
            case ROUTE:
                double heures = distance / ROUTE_VITESSE_KMH;
                if (urgent) {
                    jours = (int) Math.ceil(heures / ROUTE_HEURES_PAR_JOUR_URGENT);
                } else {
                    jours = (int) Math.ceil(heures / ROUTE_HEURES_PAR_JOUR);
                }
                break;
            case RAIL:
                jours = (int) Math.ceil(distance / RAIL_KM_PAR_JOUR) + RAIL_JOURS_EN_PLUS;
                if (urgent) jours = jours - RAIL_JOURS_GAGNES_URGENT;
                break;
            case MER:
                jours = (int) Math.ceil(distance / MER_KM_PAR_JOUR) + MER_JOURS_EN_PLUS;
                break;
            case AIR:
                jours = AIR_JOURS;
                if (distance > AIR_SEUIL_LONG_COURRIER_KM) jours = AIR_JOURS_LONG_COURRIER;
                if (urgent) jours = AIR_JOURS;
                break;
        }
        if (jours < DELAI_MINIMUM) jours = DELAI_MINIMUM;
        return jours;
    }
}
