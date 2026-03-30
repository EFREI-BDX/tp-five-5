package fr.player.valueobject.statistics;

import java.util.Objects;

/**
 * Représente le nombre de MVP (Meilleur joueur du match) d'un joueur.
 */
public class Mvps {
    private final int value;

    public Mvps(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Mvps value must be greater than or equal to 0");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
