package com.jad.efreifive.manageteam.command.team;

import com.jad.efreifive.manageteam.dto.TeamDto;

import java.time.LocalDate;
import java.util.UUID;

public sealed interface TeamCommand
        permits TeamCommand.TeamCreateCommand, TeamCommand.TeamUpdateCommand, TeamCommand.TeamDissolveCommand,
                TeamCommand.TreamRestoreCommand {

    static String getLabel(TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> TeamDto.getLabel(createCommand.teamDto());
            case TeamUpdateCommand updateCommand -> TeamDto.getLabel(updateCommand.teamDto());
            default -> throw new IllegalStateException("No label for this command");
        };
    }

    static String getTag(TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> TeamDto.getTag(createCommand.teamDto());
            case TeamUpdateCommand updateCommand -> TeamDto.getTag(updateCommand.teamDto());
            default -> throw new IllegalStateException("No tag for this command");
        };
    }

    static LocalDate getCreationDate(final TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> TeamDto.getCreationDate(createCommand.teamDto());
            case TeamUpdateCommand updateCommand -> TeamDto.getCreationDate(updateCommand.teamDto());
            default -> throw new IllegalStateException("No creation date for this command");
        };
    }

    static UUID getId(TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> throw new IllegalStateException("No ID for this command");
            case TeamUpdateCommand updateCommand -> TeamDto.getId(updateCommand.teamDto());
            case TeamDissolveCommand dissolveCommand -> dissolveCommand.id();
            case TreamRestoreCommand restoreCommand -> restoreCommand.id();
        };
    }

    static LocalDate getDissolutionDate(TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> TeamDto.getDissolutionDate(createCommand.teamDto());
            case TeamUpdateCommand updateCommand -> TeamDto.getDissolutionDate(updateCommand.teamDto());
            case TeamDissolveCommand dissolveCommand -> dissolveCommand.dissolutionDate();
            default -> throw new IllegalStateException("No dissolution date for this command");
        };
    }

    record TeamCreateCommand(TeamDto teamDto) implements TeamCommand {
    }

    record TeamDissolveCommand(UUID id, LocalDate dissolutionDate) implements TeamCommand {
    }

    record TreamRestoreCommand(UUID id) implements TeamCommand {
    }

    record TeamUpdateCommand(TeamDto teamDto) implements TeamCommand {
    }
}
