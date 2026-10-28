package fr.efrei.transfret;

import java.util.List;

public enum Interdiction {
    DANGEREUX_EN_AERIEN(TypeMarchandise.DANGEREUX, "les matières dangereuses sont interdites en transport aérien"),
    REFRIGERE_HORS_ROUTE_ET_MER(TypeMarchandise.REFRIGERE, "le transport réfrigéré n'est possible que par route ou par mer");

    private final TypeMarchandise type;
    private final String message;

    Interdiction(TypeMarchandise type, String message) {
        this.type = type;
        this.message = message;
    }

    public boolean concerne(List<Colis> colis) {
        return type.estPresentDans(colis);
    }

    public String getMessage() {
        return message;
    }
}
