package fr.player.valueobject.statistics;

import java.util.Objects;

/**
 * Représente le nombre de matchs joués par un joueur.
 */
public class MatchesPlayed {
    private final int value;

    public MatchesPlayed(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("MatchesPlayed value must be greater than or equal to 0");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
