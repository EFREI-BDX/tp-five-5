package fr.player.entity;

/**
 * Agrégat principal - Représente une équipe dans le système ERP du centre de foot à 5.
 */
public class Team {
    private final TeamId id;
    private final TeamName name;

    public Team(TeamId id, TeamName name) {
        if (id == null) {
            throw new IllegalArgumentException("TeamId must not be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("TeamName must not be null");
        }
        this.id = id;
        this.name = name;
    }

    public TeamId getId() {
        return id;
    }

    public TeamName getName() {
        return name;
    }
}
