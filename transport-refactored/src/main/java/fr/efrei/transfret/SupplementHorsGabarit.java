package fr.efrei.transfret;

import java.util.List;

public class SupplementHorsGabarit extends DecorateurPrix {

    private static final double MAJORATION = 1.10;
    private static final double DIMENSION_MAX_CM = 200;

    public SupplementHorsGabarit(Prix prix) {
        super(prix);
    }

    public double calculer(List<Colis> colis) {
        for (Colis c : colis) {
            if (c.getLongueur() > DIMENSION_MAX_CM || c.getLargeur() > DIMENSION_MAX_CM || c.getHauteur() > DIMENSION_MAX_CM) {
                return prix.calculer(colis) * MAJORATION;
            }
        }
        return prix.calculer(colis);
    }
}
