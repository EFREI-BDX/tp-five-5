package fr.player.valueobject;

import java.util.Objects;

/**
 * Représente la taille d'un joueur exprimée en centimètres.
 */
public class Height {
    private final double value;

    public Height(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Height value must be strictly positive");
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
