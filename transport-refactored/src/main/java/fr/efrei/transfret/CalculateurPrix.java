package fr.efrei.transfret;

import java.util.List;

public class CalculateurPrix {

    // la remise porte sur le prix après suppléments et urgence, l'assurance s'ajoute après la remise
    public double calculerPrixHT(List<Colis> colis, int distance, ModeTransport mode, boolean urgent, boolean assurance, TypeClient typeClient) {
        Prix prix = new PrixDeBase(mode.getTarif(), distance);
        prix = new SupplementFragile(prix);
        prix = new SupplementDangereux(prix);
        prix = new SupplementRefrigere(prix);
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
