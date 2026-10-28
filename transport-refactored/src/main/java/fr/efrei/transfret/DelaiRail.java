package fr.efrei.transfret;

public class DelaiRail implements DelaiTransport {

    private static final double KM_PAR_JOUR = 1440.0;
    private static final int JOURS_EN_PLUS = 1;
    private static final int JOURS_GAGNES_URGENT = 1;

    public int calculer(int distance, boolean urgent) {
        int jours = (int) Math.ceil(distance / KM_PAR_JOUR) + JOURS_EN_PLUS;
        if (urgent) {
            jours = jours - JOURS_GAGNES_URGENT;
        }
        return jours;
    }
}
