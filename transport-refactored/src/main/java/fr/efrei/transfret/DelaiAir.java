package fr.efrei.transfret;

public class DelaiAir implements DelaiTransport {

    private static final int JOURS = 1;
    private static final int SEUIL_LONG_COURRIER_KM = 3000;
    private static final int JOURS_LONG_COURRIER = 2;

    public int calculer(int distance, boolean urgent) {
        if (distance > SEUIL_LONG_COURRIER_KM && !urgent) {
            return JOURS_LONG_COURRIER;
        }
        return JOURS;
    }
}
