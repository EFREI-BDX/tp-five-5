package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;

import java.util.Optional;

public interface ITeamDomainService {

    Optional<Team> getTeamById(Id id);
}
