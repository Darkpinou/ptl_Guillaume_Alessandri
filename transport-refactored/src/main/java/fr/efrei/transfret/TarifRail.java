package fr.efrei.transfret;

public class TarifRail implements TarifTransport {

    private static final double PRIX_KM = 0.8;
    private static final double PRIX_KG = 0.07;
    private static final double MINIMUM = 150;

    public double calculer(int distance, double poids, double volume) {
        return Math.max(distance * PRIX_KM + poids * PRIX_KG, MINIMUM);
    }
}
