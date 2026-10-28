package fr.efrei.transfret;

public class DelaiRoute implements DelaiTransport {

    private static final double VITESSE_KMH = 70.0;
    private static final int HEURES_PAR_JOUR = 9;
    private static final int HEURES_PAR_JOUR_URGENT = 18;

    public int calculer(int distance, boolean urgent) {
        double heures = distance / VITESSE_KMH;
        if (urgent) {
            return (int) Math.ceil(heures / HEURES_PAR_JOUR_URGENT);
        }
        return (int) Math.ceil(heures / HEURES_PAR_JOUR);
    }
}
