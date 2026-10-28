package fr.efrei.transfret;

import fr.efrei.transport.contrat.Canal;
import fr.efrei.transport.contrat.ColisDto;
import fr.efrei.transport.contrat.DemandeClient;
import fr.efrei.transport.contrat.DemandeDevis;
import fr.efrei.transport.contrat.DemandeExpedition;
import fr.efrei.transport.contrat.EtatExpedition;
import fr.efrei.transport.contrat.Notification;
import fr.efrei.transport.contrat.Statut;
import fr.efrei.transport.contrat.SystemeTransport;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Branche le code historique sur les tests de contrat.
 * <p>
 * Chaque réinitialisation construit un nouveau graphe d'objets. La conversion des
 * énumérations du contrat est confinée ici.
 */
public class NouveauSystemeAdapter implements SystemeTransport {

    private Database db;
    private EmailService email;
    private SmsService sms;
    private ServiceClient serviceClient;
    private ServiceExpedition serviceExpedition;
    private GenerateurFacture generateurFacture;

    @Override
    public void reinitialiser() {
        db = new Database();
        email = new EmailService();
        sms = new SmsService();
        serviceClient = new ServiceClient(db, new ValidateurClient(), email);
        // le contrat décrit la grille tarifaire d'origine, sans le supplément hors gabarit
        CalculateurPrix calculateur = new CalculateurPrix(List.of(SupplementFragile::new, SupplementDangereux::new,
                SupplementRefrigere::new));
        serviceExpedition = new ServiceExpedition(db, calculateur, new AffectationVehicule(new Flotte()),
                new ValidateurExpedition(), new EstimateurDelai(), new Notificateur(email, sms));
        generateurFacture = new GenerateurFacture();
    }

    @Override
    public Optional<String> creerClient(DemandeClient d) {
        Client client = serviceClient.creerClient(d.nom(), d.email(), d.telephone(), d.adresse(), TypeClient.valueOf(d.type().name()), d.siret());
        return Optional.ofNullable(client).map(Client::getEmail);
    }

    @Override
    public Optional<String> creerExpedition(DemandeExpedition d) {
        Client client = db.trouverClient(d.refClient());
        Expedition expedition = serviceExpedition.creerExpedition(client, versColis(d.colis()), d.villeDepart(),
                d.villeArrivee(), ModeTransport.valueOf(d.mode().name()), d.urgent(), d.assurance());
        return Optional.ofNullable(expedition).map(Expedition::getId);
    }

    @Override
    public Optional<EtatExpedition> consulter(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null) {
            return Optional.empty();
        }
        int delai = (int) ChronoUnit.DAYS.between(jour(e.getDateCreation()), jour(e.getDateLivraisonPrevue()));
        return Optional.of(new EtatExpedition(e.getId(), Statut.valueOf(e.getStatut().name()), e.getPrixHT(), e.getPrixTTC(),
                delai, e.getVehicule().getImmatriculation(), e.getFraisAnnulation()));
    }

    @Override
    public OptionalDouble devisTTC(DemandeDevis d) {
        double devis = serviceExpedition.calculerDevis(versColis(d.colis()), d.villeDepart(), d.villeArrivee(),
                ModeTransport.valueOf(d.mode().name()), d.urgent(), d.assurance(),
                TypeClient.valueOf(d.typeClient().name()));
        return devis == ServiceExpedition.DEVIS_IMPOSSIBLE ? OptionalDouble.empty() : OptionalDouble.of(devis);
    }

    @Override
    public boolean demarrer(String id) {
        return serviceExpedition.demarrerExpedition(id);
    }

    @Override
    public boolean livrer(String id) {
        return serviceExpedition.livrerExpedition(id);
    }

    @Override
    public boolean annuler(String id) {
        return serviceExpedition.annulerExpedition(id);
    }

    @Override
    public Optional<String> facture(String id) {
        return Optional.ofNullable(db.trouverExpedition(id)).map(generateurFacture::generer);
    }

    @Override
    public List<Notification> notifications() {
        List<Notification> notifications = new ArrayList<>();
        for (String[] e : email.getJournal()) {
            notifications.add(new Notification(Canal.EMAIL, e[0], e[1]));
        }
        for (String[] s : sms.getJournal()) {
            notifications.add(new Notification(Canal.SMS, s[0], s[1]));
        }
        return notifications;
    }

    private static java.time.LocalDate jour(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static List<Colis> versColis(List<ColisDto> colis) {
        return colis.stream().map(NouveauSystemeAdapter::versColis).collect(Collectors.toCollection(ArrayList::new));
    }

    private static Colis versColis(ColisDto c) {
        return new Colis(c.description(), c.poidsKg(), c.longueurCm(), c.largeurCm(), c.hauteurCm(),
                TypeMarchandise.valueOf(c.type().name()), c.valeurDeclaree());
    }
}
