package fr.orleans.univ.miage.interop.transactionservice.modele;

public enum Type {
    IN("in"),
    OUT("out");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
