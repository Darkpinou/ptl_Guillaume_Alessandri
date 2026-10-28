package fr.efrei.transfret;

public interface ClientRepository {

    void sauvegarderClient(Client c);

    Client trouverClient(String email);
}
