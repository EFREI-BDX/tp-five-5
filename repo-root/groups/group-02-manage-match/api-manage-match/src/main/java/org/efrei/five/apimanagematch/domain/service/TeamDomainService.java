package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.external.ITeamManagementService;
import org.efrei.five.apimanagematch.domain.external.ITeamRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamDomainService implements ITeamDomainService {

    private final ITeamManagementService service;
    private final ITeamRepository repository;

    public TeamDomainService(ITeamManagementService service, ITeamRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    public Optional<Team> getTeamById(Id id) {
        service.getTeam(id).ifPresent(repository::upsert);
        return repository.findById(id);
    }
}
