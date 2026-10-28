package fr.efrei.transfret;

import java.util.List;

public class SupplementDangereux extends DecorateurPrix {

    private static final double MAJORATION = 1.30;

    public SupplementDangereux(Prix prix) {
        super(prix);
    }

    public double calculer(List<Colis> colis) {
        if (contient(colis, TypeMarchandise.DANGEREUX)) {
            return prix.calculer(colis) * MAJORATION;
        }
        return prix.calculer(colis);
    }
}
