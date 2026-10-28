package fr.efrei.transfret;

import java.util.EnumSet;

public enum ModeTransport {
    ROUTE(new TarifRoute(), new DelaiRoute(), EnumSet.noneOf(Interdiction.class), null),
    RAIL(new TarifRail(), new DelaiRail(), EnumSet.of(Interdiction.REFRIGERE_HORS_ROUTE_ET_MER), null),
    MER(new TarifMer(), new DelaiMer(), EnumSet.noneOf(Interdiction.class), "pas d'option urgente pour le transport maritime"),
    AIR(new TarifAir(), new DelaiAir(), EnumSet.of(Interdiction.DANGEREUX_EN_AERIEN, Interdiction.REFRIGERE_HORS_ROUTE_ET_MER), null),
    FLUVIAL(new TarifFluvial(), new DelaiFluvial(), EnumSet.of(Interdiction.REFRIGERE_HORS_ROUTE_ET_MER), "pas d'option urgente pour le transport fluvial");

    private final TarifTransport tarif;
    private final DelaiTransport delai;
    // EnumSet parcourt les interdictions dans leur ordre de déclaration, celui des contrôles du legacy
    private final EnumSet<Interdiction> interdictions;
    // null quand le mode accepte l'urgence
    private final String refusUrgence;

    ModeTransport(TarifTransport tarif, DelaiTransport delai, EnumSet<Interdiction> interdictions, String refusUrgence) {
        this.tarif = tarif;
        this.delai = delai;
        this.interdictions = interdictions;
        this.refusUrgence = refusUrgence;
    }

    public TarifTransport getTarif() {
        return tarif;
    }

    public DelaiTransport getDelai() {
        return delai;
    }

    public EnumSet<Interdiction> getInterdictions() {
        return interdictions;
    }

    public boolean isUrgenceAutorisee() {
        return refusUrgence == null;
    }

    public String getRefusUrgence() {
        return refusUrgence;
    }
}
