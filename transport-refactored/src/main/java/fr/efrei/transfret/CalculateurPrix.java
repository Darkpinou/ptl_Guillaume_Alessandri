package fr.efrei.transfret;

import java.util.List;
import java.util.function.UnaryOperator;

public class CalculateurPrix {

    private final List<UnaryOperator<Prix>> supplementsMarchandise;

    public CalculateurPrix(List<UnaryOperator<Prix>> supplementsMarchandise) {
        this.supplementsMarchandise = supplementsMarchandise;
    }

    // la remise porte sur le prix après suppléments et urgence, l'assurance s'ajoute après la remise
    public double calculerPrixHT(List<Colis> colis, int distance, ModeTransport mode, boolean urgent, boolean assurance, TypeClient typeClient) {
        Prix prix = new PrixDeBase(mode.getTarif(), distance);
        for (UnaryOperator<Prix> supplement : supplementsMarchandise) {
            prix = supplement.apply(prix);
        }
        if (urgent) {
            prix = new SupplementUrgence(prix);
        }
        prix = new Remise(prix, typeClient.getRemise());
        if (assurance) {
            prix = new Assurance(prix);
        }
        return Utils.arrondir(prix.calculer(colis));
    }
}
