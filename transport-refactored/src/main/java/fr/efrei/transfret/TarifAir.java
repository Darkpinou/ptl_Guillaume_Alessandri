package fr.efrei.transfret;

public class TarifAir implements TarifTransport {

    private static final double PRIX_KM = 0.6;
    private static final double PRIX_KG = 4.5;
    private static final double KG_PAR_M3 = 167;

    public double calculer(int distance, double poids, double volume) {
        double poidsTaxable = Math.max(poids, volume * KG_PAR_M3);
        return distance * PRIX_KM + poidsTaxable * PRIX_KG;
    }
}
