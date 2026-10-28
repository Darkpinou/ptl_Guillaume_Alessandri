package fr.efrei.transfret;

import java.util.List;

public class SupplementRefrigere extends DecorateurPrix {

    private static final double MAJORATION = 1.20;

    public SupplementRefrigere(Prix prix) {
        super(prix);
    }

    public double calculer(List<Colis> colis) {
        if (contient(colis, TypeMarchandise.REFRIGERE)) {
            return prix.calculer(colis) * MAJORATION;
        }
        return prix.calculer(colis);
    }
}
