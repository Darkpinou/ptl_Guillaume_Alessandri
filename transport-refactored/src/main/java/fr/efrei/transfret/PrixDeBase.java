package fr.efrei.transfret;

import java.util.List;

public class PrixDeBase implements Prix {

    private final TarifTransport tarif;
    private final int distance;

    public PrixDeBase(TarifTransport tarif, int distance) {
        this.tarif = tarif;
        this.distance = distance;
    }

    public double calculer(List<Colis> colis) {
        double poids = 0;
        double volume = 0;
        for (Colis c : colis) {
            poids = poids + c.getPoids();
            volume = volume + c.getVolume();
        }
        return tarif.calculer(distance, poids, volume);
    }
}
