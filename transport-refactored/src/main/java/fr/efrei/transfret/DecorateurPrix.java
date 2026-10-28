package fr.efrei.transfret;

public abstract class DecorateurPrix implements Prix {

    protected final Prix prix;

    protected DecorateurPrix(Prix prix) {
        this.prix = prix;
    }
}
