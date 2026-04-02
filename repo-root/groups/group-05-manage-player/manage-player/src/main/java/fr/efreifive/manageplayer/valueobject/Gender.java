package fr.player.valueobject;

/**
 * Représente le genre déclaré d'un joueur.
 */
public enum Gender {
    HOMME("homme"),
    FEMME("femme"),
    NON_BINAIRE("non binaire"),
    NON_SPECIFIE("non spécifié");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender fromValue(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Gender value must not be empty");
        }
        for (Gender gender : Gender.values()) {
            if (gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender value: " + value);
    }
}
