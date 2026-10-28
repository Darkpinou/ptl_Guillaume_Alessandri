package fr.efrei.transfret;

import java.util.List;

public class Peniche extends Vehicule {

    public Peniche(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, ModeTransport.FLUVIAL);
    }

    protected boolean accepte(List<Colis> colis) {
        return true;
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("La péniche " + immatriculation + " navigue sur " + distance + " km");
    }
}
