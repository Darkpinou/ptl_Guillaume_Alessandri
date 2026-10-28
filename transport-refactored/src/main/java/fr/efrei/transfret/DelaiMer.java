package fr.efrei.transfret;

public class DelaiMer implements DelaiTransport {

    private static final double KM_PAR_JOUR = 720.0;
    private static final int JOURS_EN_PLUS = 2;

    public int calculer(int distance, boolean urgent) {
        return (int) Math.ceil(distance / KM_PAR_JOUR) + JOURS_EN_PLUS;
    }
}
