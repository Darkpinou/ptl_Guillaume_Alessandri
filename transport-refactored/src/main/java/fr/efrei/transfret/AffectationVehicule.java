package fr.efrei.transfret;

import java.util.List;

public class AffectationVehicule {

    private final Flotte flotte;

    public AffectationVehicule(Flotte flotte) {
        this.flotte = flotte;
    }

    // retourne le premier véhicule disponible et compatible, null si aucun
    public Vehicule trouverVehicule(List<Colis> colis, ModeTransport mode) {
        double poids = 0;
        for (Colis c : colis) {
            poids = poids + c.getPoids();
        }
        for (Vehicule v : flotte.getVehicules()) {
            if (v.estDisponible() && v.getCapaciteMax() - v.getChargeActuelle() >= poids && v.peutPrendreEnCharge(colis, mode)) {
                return v;
            }
        }
        return null;
    }
}
