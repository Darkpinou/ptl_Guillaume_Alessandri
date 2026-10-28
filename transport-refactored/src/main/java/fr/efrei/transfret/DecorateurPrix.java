package fr.efrei.transfret;

import java.util.List;

public abstract class DecorateurPrix implements Prix {

    protected final Prix prix;

    protected DecorateurPrix(Prix prix) {
        this.prix = prix;
    }

    protected boolean contient(List<Colis> colis, TypeMarchandise type) {
        for (Colis c : colis) {
            if (c.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
