package fr.efrei.transfret;

public enum TypeClient {
    PARTICULIER(new RemiseParticulier(), false, false),
    ENTREPRISE(new RemiseEntreprise(), true, false),
    PREMIUM(new RemisePremium(), false, true);

    private final PolitiqueRemise remise;
    private final boolean siretObligatoire;
    private final boolean recoitSms;

    TypeClient(PolitiqueRemise remise, boolean siretObligatoire, boolean recoitSms) {
        this.remise = remise;
        this.siretObligatoire = siretObligatoire;
        this.recoitSms = recoitSms;
    }

    public PolitiqueRemise getRemise() {
        return remise;
    }

    public boolean isSiretObligatoire() {
        return siretObligatoire;
    }

    public boolean recoitSms() {
        return recoitSms;
    }
}
