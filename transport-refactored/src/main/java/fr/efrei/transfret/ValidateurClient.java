package fr.efrei.transfret;

public class ValidateurClient {

    private static final int LONGUEUR_SIRET = 14;

    public boolean estValide(String nom, String email, TypeClient type, String siret) {
        if (nom == null || nom.trim().length() == 0) {
            System.out.println("ERREUR : nom obligatoire");
            return false;
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            System.out.println("ERREUR : email invalide (" + email + ")");
            return false;
        }
        if (type == TypeClient.ENTREPRISE && (siret == null || siret.length() != LONGUEUR_SIRET)) {
            System.out.println("ERREUR : SIRET obligatoire (14 chiffres) pour une entreprise");
            return false;
        }
        return true;
    }
}
