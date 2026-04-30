package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.external.ITeamManagementService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamManagementService implements ITeamManagementService {


    private final TeamManagementClient teamManagementClient;

    public TeamManagementService(TeamManagementClient teamManagementClient) {
        this.teamManagementClient = teamManagementClient;
    }

    @Override
    public Optional<Team> getTeam(Id teamId) {
        return teamManagementClient.getTeam(teamId);
    }
}
