package fr.efreifive.manageplayer.service;

import fr.efreifive.manageplayer.dto.TeamDto;
import fr.efreifive.manageplayer.dto.TeamRequest;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeamService {
    private final ConcurrentHashMap<UUID, TeamDto> teams = new ConcurrentHashMap<>();

    public List<TeamDto> findAll() {
        return teams.values().stream()
            .sorted(Comparator.comparing(TeamDto::name))
            .toList();
    }

    public TeamDto findById(UUID id) {
        TeamDto team = teams.get(id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with id " + id + " not found");
        }
        return team;
    }

    public TeamDto create(TeamRequest request) {
        TeamDto team = new TeamDto(UUID.randomUUID(), validateName(request.name()));
        teams.put(team.id(), team);
        return team;
    }

    public TeamDto update(UUID id, TeamRequest request) {
        findById(id);
        TeamDto team = new TeamDto(id, validateName(request.name()));
        teams.put(id, team);
        return team;
    }

    public void delete(UUID id) {
        findById(id);
        teams.remove(id);
    }

    public boolean exists(UUID id) {
        return teams.containsKey(id);
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team name must not be empty");
        }
        String normalizedName = name.trim();
        if (normalizedName.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team name must not exceed 100 characters");
        }
        return normalizedName;
    }
}
