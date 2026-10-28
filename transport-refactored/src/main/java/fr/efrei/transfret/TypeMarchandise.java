package fr.efrei.transfret;

import java.util.List;

public enum TypeMarchandise {
    STANDARD("Standard"),
    FRAGILE("Fragile"),
    DANGEREUX("Dangereux"),
    REFRIGERE("Réfrigéré");

    private final String libelle;

    TypeMarchandise(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public boolean estPresentDans(List<Colis> colis) {
        for (Colis c : colis) {
            if (c.getType() == this) {
                return true;
            }
        }
        return false;
    }
}
