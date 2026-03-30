package fr.player.valueobject.statistics;

import java.util.Objects;

/**
 * Représente le nombre de passes décisives réalisées par un joueur.
 */
public class Assists {
    private final int value;

    public Assists(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Assists value must be greater than or equal to 0");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
