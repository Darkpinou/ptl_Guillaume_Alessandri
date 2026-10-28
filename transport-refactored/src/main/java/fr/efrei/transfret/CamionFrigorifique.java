package fr.efrei.transfret;

import java.util.List;

public class CamionFrigorifique extends Camion {

    public CamionFrigorifique(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, false);
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return TypeMarchandise.REFRIGERE.estPresentDans(colis);
    }

    @Override
    public void partir(int distance) {
        super.partir(distance);
        if (TypeMarchandise.REFRIGERE.estPresentDans(chargement)) {
            System.out.println("Réfrigération activée à " + TEMPERATURE_REFRIGERATION + "°C sur " + immatriculation);
        }
    }
}
