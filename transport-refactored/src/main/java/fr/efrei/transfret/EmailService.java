package fr.efrei.transfret;

import java.util.ArrayList;
import java.util.List;

public class EmailService implements CanalNotification {

    public static final String SMTP_HOST = "smtp.transfret.fr";
    public static final String EXPEDITEUR = "noreply@transfret.fr";

    // garde une trace des emails envoyés (demandé par le support client)
    private final List<String[]> journal = new ArrayList<String[]>();

    public void envoyer(String destinataire, String sujet, String corps) {
        if (destinataire == null || destinataire.indexOf("@") < 0) {
            // on ignore, ce n'est pas grave si l'email ne part pas
            return;
        }
        System.out.println("[EMAIL via " + SMTP_HOST + "] " + EXPEDITEUR + " -> " + destinataire + " | " + sujet);
        System.out.println("        " + corps);
        journal.add(new String[]{destinataire, sujet, corps});
    }

    public List<String[]> getJournal() {
        return journal;
    }
}
