package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.external.ITeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamSyncDomainService implements ITeamSyncDomainService {

    private final ITeamRepository teamRepository;

    public TeamSyncDomainService(ITeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void syncTeam(Team team) {
        teamRepository.upsert(team);
    }
}