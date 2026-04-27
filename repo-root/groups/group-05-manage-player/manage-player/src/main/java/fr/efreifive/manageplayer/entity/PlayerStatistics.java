package fr.player.valueobject.statistics;

/**
 * Représente les statistiques de performance d'un joueur dans le système.
 */
public class PlayerStatistics {
    private final MatchesPlayed matchesPlayed;
    private final GoalsScored goalsScored;
    private final Assists assists;
    private final Wins wins;
    private final Losses losses;
    private final Draws draws;
    private final Mvps mvps;

    public PlayerStatistics(
        MatchesPlayed matchesPlayed,
        GoalsScored goalsScored,
        Assists assists,
        Wins wins,
        Losses losses,
        Draws draws,
        Mvps mvps
    ) {
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
        if (losses == null) {
            throw new IllegalArgumentException("Losses must not be null");
        }
        if (draws == null) {
            throw new IllegalArgumentException("Draws must not be null");
        }
        if (mvps == null) {
            throw new IllegalArgumentException("Mvps must not be null");
        }
        if (wins.getValue() + losses.getValue() + draws.getValue() > matchesPlayed.getValue()) {
            throw new IllegalArgumentException("Wins, losses and draws total cannot be greater than MatchesPlayed");
        }
        if (mvps.getValue() > matchesPlayed.getValue()) {
            throw new IllegalArgumentException("Mvps cannot be greater than MatchesPlayed");
        }

        this.matchesPlayed = matchesPlayed;
        this.goalsScored = goalsScored;
        this.assists = assists;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
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

    public Losses getLosses() {
        return losses;
    }

    public Draws getDraws() {
        return draws;
    }

    public Mvps getMvps() {
        return mvps;
    }
}
