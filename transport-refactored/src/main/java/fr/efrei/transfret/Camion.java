package fr.efrei.transfret;

import java.util.List;

public class Camion extends Vehicule {

    protected boolean adr; // certification matières dangereuses

    public Camion(String immatriculation, double capaciteMax, boolean adr) {
        super(immatriculation, capaciteMax, ModeTransport.ROUTE);
        this.adr = adr;
    }

    // les colis réfrigérés partent en camion frigorifique
    protected boolean accepte(List<Colis> colis) {
        if (TypeMarchandise.REFRIGERE.estPresentDans(colis)) {
            return false;
        }
        return adr || !TypeMarchandise.DANGEREUX.estPresentDans(colis);
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("Le camion " + immatriculation + " roule sur " + distance + " km");
    }
}
