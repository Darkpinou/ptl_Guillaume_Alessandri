package fr.efrei.transfret;

import java.util.List;

public class Avion extends Vehicule {

    private static final int ALTITUDE_PIEDS = 35000;

    public Avion(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, ModeTransport.AIR);
    }

    protected boolean accepte(List<Colis> colis) {
        return true;
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("L'avion " + immatriculation + " roule sur la piste");
        System.out.println("L'avion " + immatriculation + " vole à " + ALTITUDE_PIEDS + " pieds");
    }
}
