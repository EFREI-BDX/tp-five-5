package fr.efreifive.manageplayer.service;

import fr.efreifive.manageplayer.dto.StatisticDto;
import fr.efreifive.manageplayer.dto.StatisticRequest;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StatisticService {
    private final ConcurrentHashMap<UUID, StatisticDto> statistics = new ConcurrentHashMap<>();
    private final PlayerService playerService;

    public StatisticService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public List<StatisticDto> findAll() {
        return statistics.values().stream()
            .sorted(Comparator.comparing(statistic -> statistic.playerId().toString()))
            .toList();
    }

    public StatisticDto findByPlayerId(UUID playerId) {
        StatisticDto statistic = statistics.get(playerId);
        if (statistic == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Statistic for player id " + playerId + " not found");
        }
        return statistic;
    }

    public StatisticDto create(UUID playerId, StatisticRequest request) {
        ensurePlayerExists(playerId);
        if (statistics.containsKey(playerId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Statistic for player id " + playerId + " already exists");
        }
        StatisticDto statistic = buildStatistic(playerId, request);
        statistics.put(playerId, statistic);
        return statistic;
    }

    public StatisticDto update(UUID playerId, StatisticRequest request) {
        ensurePlayerExists(playerId);
        findByPlayerId(playerId);
        StatisticDto statistic = buildStatistic(playerId, request);
        statistics.put(playerId, statistic);
        return statistic;
    }

    public void delete(UUID playerId) {
        findByPlayerId(playerId);
        statistics.remove(playerId);
    }

    private void ensurePlayerExists(UUID playerId) {
        playerService.findById(playerId);
    }

    private StatisticDto buildStatistic(UUID playerId, StatisticRequest request) {
        int matchesPlayed = requireNonNegative(request.matchesPlayed(), "Matches played");
        int goalsScored = requireNonNegative(request.goalsScored(), "Goals scored");
        int assists = requireNonNegative(request.assists(), "Assists");
        int wins = requireNonNegative(request.wins(), "Wins");
        int draws = requireNonNegative(request.draws(), "Draws");
        int mvps = requireNonNegative(request.mvps(), "Mvps");

        if (wins > matchesPlayed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wins cannot be greater than matches played");
        }
        if (draws > matchesPlayed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Draws cannot be greater than matches played");
        }
        if (mvps > matchesPlayed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mvps cannot be greater than matches played");
        }

        return new StatisticDto(playerId, matchesPlayed, goalsScored, assists, wins, draws, mvps);
    }

    private int requireNonNegative(Integer value, String fieldLabel) {
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldLabel + " must not be null");
        }
        if (value < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldLabel + " must be greater than or equal to 0");
        }
        return value;
    }
}
