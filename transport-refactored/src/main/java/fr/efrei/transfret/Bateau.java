package fr.efrei.transfret;

import java.util.List;

public class Bateau extends Vehicule {

    private static final double KM_PAR_MILLE_NAUTIQUE = 1.852;

    private boolean conteneursRefrigeres;

    public Bateau(String immatriculation, double capaciteMax, boolean conteneursRefrigeres) {
        super(immatriculation, capaciteMax, ModeTransport.MER);
        this.conteneursRefrigeres = conteneursRefrigeres;
    }

    protected boolean accepte(List<Colis> colis) {
        return conteneursRefrigeres || !TypeMarchandise.REFRIGERE.estPresentDans(colis);
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("Le navire " + immatriculation + " navigue sur " + (int) (distance / KM_PAR_MILLE_NAUTIQUE) + " milles nautiques");
        if (TypeMarchandise.REFRIGERE.estPresentDans(chargement)) {
            System.out.println("Conteneurs frigorifiques réglés à " + TEMPERATURE_REFRIGERATION + "°C");
        }
    }
}
