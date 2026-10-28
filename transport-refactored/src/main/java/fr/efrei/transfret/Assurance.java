package fr.efrei.transfret;

import java.util.List;

public class Assurance extends DecorateurPrix {

    private static final double TAUX = 0.02;
    private static final double MINIMUM = 15;

    public Assurance(Prix prix) {
        super(prix);
    }

    public double calculer(List<Colis> colis) {
        double valeur = 0;
        for (Colis c : colis) {
            valeur = valeur + c.getValeurDeclaree();
        }
        return prix.calculer(colis) + Math.max(valeur * TAUX, MINIMUM);
    }
}
