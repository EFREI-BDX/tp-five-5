package fr.player.valueobject.statistics;

import java.util.Objects;

/**
 * Représente le nombre de victoires d'un joueur.
 */
public class Wins {
    private final int value;

    public Wins(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Wins value must be greater than or equal to 0");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
