package fr.efrei.transfret;

public class Notificateur {

    private final CanalNotification email;
    private final CanalNotification sms;

    public Notificateur(CanalNotification email, CanalNotification sms) {
        this.email = email;
        this.sms = sms;
    }

    public void notifier(Client client, String sujet, String corps, String texteSms) {
        email.envoyer(client.getEmail(), sujet, corps);
        if (client.getType() == TypeClient.PREMIUM) {
            sms.envoyer(client.getTelephone(), sujet, texteSms);
        }
    }
}
