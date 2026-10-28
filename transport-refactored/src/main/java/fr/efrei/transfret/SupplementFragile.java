package fr.efrei.transfret;

import java.util.List;

public class SupplementFragile extends DecorateurPrix {

    private static final double MAJORATION = 1.15;

    public SupplementFragile(Prix prix) {
        super(prix);
    }

    public double calculer(List<Colis> colis) {
        if (contient(colis, TypeMarchandise.FRAGILE)) {
            return prix.calculer(colis) * MAJORATION;
        }
        return prix.calculer(colis);
    }
}
