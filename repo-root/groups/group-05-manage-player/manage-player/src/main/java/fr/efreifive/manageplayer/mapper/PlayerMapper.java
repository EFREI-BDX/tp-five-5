package fr.efreifive.manageplayer.mapper;

import fr.efreifive.manageplayer.dto.CreatePlayerRequest;
import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.PlayerStatisticsDto;
import fr.efreifive.manageplayer.dto.UpdatePlayerRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public PlayerDto fromCreateRequest(CreatePlayerRequest request, PlayerStatisticsDto statistics, String status, String createdAt) {
        return new PlayerDto(
            UUID.randomUUID(),
            request.firstName(),
            request.lastName(),
            request.email(),
            request.phone(),
            request.birthDate(),
            request.gender(),
            request.height(),
            status,
            statistics,
            List.of(),
            createdAt,
            createdAt
        );
    }

    public PlayerDto merge(PlayerDto existingPlayer, UpdatePlayerRequest request, String updatedAt) {
        return new PlayerDto(
            existingPlayer.id(),
            request.firstName() != null ? request.firstName() : existingPlayer.firstName(),
            request.lastName() != null ? request.lastName() : existingPlayer.lastName(),
            request.email() != null ? request.email() : existingPlayer.email(),
            request.phone() != null ? request.phone() : existingPlayer.phone(),
            request.birthDate() != null ? request.birthDate() : existingPlayer.birthDate(),
            request.gender() != null ? request.gender() : existingPlayer.gender(),
            request.height() != null ? request.height() : existingPlayer.height(),
            existingPlayer.status(),
            existingPlayer.statistics(),
            existingPlayer.teamIds(),
            existingPlayer.createdAt(),
            updatedAt
        );
    }

    public PlayerDto withStatus(PlayerDto existingPlayer, String status, String updatedAt) {
        return new PlayerDto(
            existingPlayer.id(),
            existingPlayer.firstName(),
            existingPlayer.lastName(),
            existingPlayer.email(),
            existingPlayer.phone(),
            existingPlayer.birthDate(),
            existingPlayer.gender(),
            existingPlayer.height(),
            status,
            existingPlayer.statistics(),
            existingPlayer.teamIds(),
            existingPlayer.createdAt(),
            updatedAt
        );
    }

    public PlayerDto withStatistics(PlayerDto existingPlayer, PlayerStatisticsDto statistics, String updatedAt) {
        return new PlayerDto(
            existingPlayer.id(),
            existingPlayer.firstName(),
            existingPlayer.lastName(),
            existingPlayer.email(),
            existingPlayer.phone(),
            existingPlayer.birthDate(),
            existingPlayer.gender(),
            existingPlayer.height(),
            existingPlayer.status(),
            statistics,
            existingPlayer.teamIds(),
            existingPlayer.createdAt(),
            updatedAt
        );
    }

    public PlayerDto withTeamIds(PlayerDto existingPlayer, List<String> teamIds, String updatedAt) {
        return new PlayerDto(
            existingPlayer.id(),
            existingPlayer.firstName(),
            existingPlayer.lastName(),
            existingPlayer.email(),
            existingPlayer.phone(),
            existingPlayer.birthDate(),
            existingPlayer.gender(),
            existingPlayer.height(),
            existingPlayer.status(),
            existingPlayer.statistics(),
            List.copyOf(teamIds),
            existingPlayer.createdAt(),
            updatedAt
        );
    }
}
