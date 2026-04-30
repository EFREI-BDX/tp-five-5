package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.external.ITeamManagmentService;
import org.efrei.five.apimanagematch.domain.external.ITeamRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamDomainService implements ITeamDomainService {

    private final ITeamManagmentService service;
    private final ITeamRepository repository;

    public TeamDomainService(ITeamManagmentService service, ITeamRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    public Optional<Team> getTeamById(Id id) {
        service.getTeam(id).ifPresent(repository::upsert);
        return repository.findById(id);
    }
}
