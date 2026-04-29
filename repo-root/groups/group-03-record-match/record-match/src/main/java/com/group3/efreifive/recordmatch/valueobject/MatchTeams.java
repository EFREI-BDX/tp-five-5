package com.group3.efreifive.recordmatch.valueobject;

public record MatchTeams(TeamId team1, TeamId team2) {

    public MatchTeams {
        if (team1 == null) {
            throw new IllegalArgumentException("MatchTeams.team1 must not be null");
        }
        if (team2 == null) {
            throw new IllegalArgumentException("MatchTeams.team2 must not be null");
        }
        if (team1.equals(team2)) {
            throw new IllegalArgumentException("MatchTeams.team1 and team2 must be different");
        }
    }

    public static MatchTeams of(final TeamId team1, final TeamId team2) {
        return new MatchTeams(team1, team2);
    }

    public boolean containsTeam(final TeamId teamId) {
        return this.team1.equals(teamId) || this.team2.equals(teamId);
    }
}
