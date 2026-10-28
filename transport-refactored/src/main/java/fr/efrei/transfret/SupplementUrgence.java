package fr.efrei.transfret;

import java.util.List;

public class SupplementUrgence extends DecorateurPrix {

    private static final double MAJORATION = 1.5;

    public SupplementUrgence(Prix prix) {
        super(prix);
    }

    public double calculer(List<Colis> colis) {
        return prix.calculer(colis) * MAJORATION;
    }
}
