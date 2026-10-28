package fr.efrei.transfret;

public class RemiseParticulier implements PolitiqueRemise {

    public double appliquer(double prix) {
        return prix;
    }
}
