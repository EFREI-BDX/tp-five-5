package fr.player.valueobject.statistics;

import java.util.Objects;

/**
 * Représente le nombre de buts marqués par un joueur.
 */
public class GoalsScored {
    private final int value;

    public GoalsScored(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("GoalsScored value must be greater than or equal to 0");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
