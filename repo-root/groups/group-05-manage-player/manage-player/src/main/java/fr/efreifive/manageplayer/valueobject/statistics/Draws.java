package fr.player.valueobject.statistics;

/**
 * Représente le nombre d'égalités d'un joueur.
 */
public class Draws {
    private final int value;

    public Draws(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Draws value must be greater than or equal to 0");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
