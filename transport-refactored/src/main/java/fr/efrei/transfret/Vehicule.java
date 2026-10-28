package fr.efrei.transfret;

import java.util.ArrayList;
import java.util.List;

public abstract class Vehicule implements IVehicule {

    protected static final double TEMPERATURE_REFRIGERATION = 4;

    protected String immatriculation;
    protected double capaciteMax;
    protected double chargeActuelle;
    protected boolean disponible = true;
    protected List<Colis> chargement = new ArrayList<Colis>();
    private final ModeTransport mode;

    public Vehicule(String immatriculation, double capaciteMax, ModeTransport mode) {
        this.immatriculation = immatriculation;
        this.capaciteMax = capaciteMax;
        this.mode = mode;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public double getCapaciteMax() {
        return capaciteMax;
    }

    public double getChargeActuelle() {
        return chargeActuelle;
    }

    public boolean estDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void charger(Colis colis) {
        if (chargeActuelle + colis.getPoids() > capaciteMax) {
            System.out.println("ERREUR : capacité dépassée pour " + immatriculation);
            return;
        }
        chargement.add(colis);
        chargeActuelle = chargeActuelle + colis.getPoids();
    }

    public void decharger(Colis colis) {
        if (chargement.remove(colis)) {
            chargeActuelle = chargeActuelle - colis.getPoids();
        }
    }

    public boolean peutPrendreEnCharge(List<Colis> colis, ModeTransport mode) {
        return mode == this.mode && accepte(colis);
    }

    protected abstract boolean accepte(List<Colis> colis);

    protected void faireLePlein() {
        System.out.println("Plein effectué pour " + immatriculation);
    }
}
