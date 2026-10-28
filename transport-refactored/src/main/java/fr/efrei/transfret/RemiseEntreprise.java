package fr.efrei.transfret;

public class RemiseEntreprise implements PolitiqueRemise {

    private static final double SEUIL_GROSSE_COMMANDE = 5000;
    private static final double COEF_GROSSE_COMMANDE = 0.85;
    private static final double COEF = 0.90;

    public double appliquer(double prix) {
        if (prix > SEUIL_GROSSE_COMMANDE) {
            return prix * COEF_GROSSE_COMMANDE;
        }
        return prix * COEF;
    }
}
