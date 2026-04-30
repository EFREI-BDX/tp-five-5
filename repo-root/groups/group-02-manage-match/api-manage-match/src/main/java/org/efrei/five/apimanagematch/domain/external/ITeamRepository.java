package org.efrei.five.apimanagematch.domain.external;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;

import java.util.Optional;

public interface ITeamRepository {
    void upsert(Team team);

    Optional<Team> findById(Id id);
}
