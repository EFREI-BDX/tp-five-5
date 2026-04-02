package fr.player.valueobject;

/**
 * Représente l'état métier d'un joueur dans le système.
 */
public enum Status {
    ACTIF("actif"),
    INACTIF("inactif"),
    SUPPRIME("supprimé");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status fromValue(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Status value must not be empty");
        }
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
