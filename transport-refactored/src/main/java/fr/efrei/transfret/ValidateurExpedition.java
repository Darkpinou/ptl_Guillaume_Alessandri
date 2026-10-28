package fr.efrei.transfret;

import java.util.List;

public class ValidateurExpedition {

    public boolean estValide(Client client, List<Colis> colis, String villeDepart, String villeArrivee, ModeTransport mode, boolean urgent) {
        if (client == null) {
            System.out.println("ERREUR : client obligatoire");
            return false;
        }
        if (colis == null || colis.isEmpty()) {
            System.out.println("ERREUR : au moins un colis est nécessaire");
            return false;
        }
        for (Colis c : colis) {
            if (c.getPoids() <= 0) {
                System.out.println("ERREUR : poids invalide pour " + c.getDescription());
                return false;
            }
        }
        for (Interdiction interdiction : mode.getInterdictions()) {
            if (interdiction.concerne(colis)) {
                System.out.println("ERREUR : " + interdiction.getMessage());
                return false;
            }
        }
        if (urgent && !mode.isUrgenceAutorisee()) {
            System.out.println("ERREUR : " + mode.getRefusUrgence());
            return false;
        }
        if (Utils.getDistance(villeDepart, villeArrivee) == Utils.DISTANCE_INCONNUE) {
            System.out.println("ERREUR : trajet inconnu " + villeDepart + " -> " + villeArrivee);
            return false;
        }
        return true;
    }
}
