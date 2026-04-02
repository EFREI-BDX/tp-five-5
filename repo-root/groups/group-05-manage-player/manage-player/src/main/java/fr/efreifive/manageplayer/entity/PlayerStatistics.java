package fr.player.valueobject.statistics;

import java.util.Objects;

/**
 * Représente les statistiques de performance d'un joueur dans le système.
 */
public class PlayerStatistics {
    private final MatchesPlayed matchesPlayed;
    private final GoalsScored goalsScored;
    private final Assists assists;
    private final Wins wins;
    private final Draws draws;
    private final Mvps mvps;

    public PlayerStatistics(MatchesPlayed matchesPlayed, GoalsScored goalsScored, Assists assists, 
                           Wins wins, Draws draws, Mvps mvps) {
        if (matchesPlayed == null) {
            throw new IllegalArgumentException("MatchesPlayed must not be null");
        }
        if (goalsScored == null) {
            throw new IllegalArgumentException("GoalsScored must not be null");
        }
        if (assists == null) {
            throw new IllegalArgumentException("Assists must not be null");
        }
        if (wins == null) {
            throw new IllegalArgumentException("Wins must not be null");
        }
        if (draws == null) {
            throw new IllegalArgumentException("Draws must not be null");
        }
        if (mvps == null) {
            throw new IllegalArgumentException("Mvps must not be null");
        }
        
        // Validate business rules
        if (wins.getValue() > matchesPlayed.getValue()) {
            throw new IllegalArgumentException("Wins cannot be greater than MatchesPlayed");
        }
        if (draws.getValue() > matchesPlayed.getValue()) {
            throw new IllegalArgumentException("Draws cannot be greater than MatchesPlayed");
        }
        if (mvps.getValue() > matchesPlayed.getValue()) {
            throw new IllegalArgumentException("Mvps cannot be greater than MatchesPlayed");
        }draws = draws;
        this.
        
        this.matchesPlayed = matchesPlayed;
        this.goalsScored = goalsScored;
        this.assists = assists;
        this.wins = wins;
        this.mvps = mvps;
    }

    public MatchesPlayed getMatchesPlayed() {
        return matchesPlayed;
    }

    public GoalsScored getGoalsScored() {
        return goalsScored;
    }

    public Assists getAssists() {
        return assists;
    }

    public Wins getWins() {
        return wins;
    }
Draws getDraws() {
        return draws;
    }

    public 
    public Mvps getMvps() {
        return mvps;
    }
}
