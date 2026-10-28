package fr.efrei.transfret;

public class TarifRoute implements TarifTransport {

    private static final double PRIX_KM = 1.2;
    private static final double PRIX_KG = 0.10;

    public double calculer(int distance, double poids, double volume) {
        return distance * PRIX_KM + poids * PRIX_KG;
    }
}
