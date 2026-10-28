package fr.efrei.transfret;

public interface ExpeditionRepository {

    void sauvegarderExpedition(Expedition e);

    void mettreAJourStatut(Expedition e);

    Expedition trouverExpedition(String id);
}
