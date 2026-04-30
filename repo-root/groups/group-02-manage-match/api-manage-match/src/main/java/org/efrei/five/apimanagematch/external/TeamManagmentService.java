package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.external.ITeamManagmentService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamManagmentService implements ITeamManagmentService {


    private final TeamManagmentClient teamManagmentClient;

    public TeamManagmentService(TeamManagmentClient teamManagmentClient) {
        this.teamManagmentClient = teamManagmentClient;
    }

    @Override
    public Optional<Team> getTeam(Id teamId) {
        return teamManagmentClient.getTeam(teamId);
    }
}
