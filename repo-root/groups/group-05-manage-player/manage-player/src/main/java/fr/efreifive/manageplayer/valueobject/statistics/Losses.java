package fr.player.valueobject.statistics;

/**
 * Représente le nombre de défaites d'un joueur.
 */
public class Losses {
    private final int value;

    public Losses(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Losses value must be greater than or equal to 0");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
