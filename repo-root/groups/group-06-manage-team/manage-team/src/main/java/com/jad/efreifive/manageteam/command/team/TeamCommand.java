package com.jad.efreifive.manageteam.command.team;

import com.jad.efreifive.manageteam.dto.TeamDto;

import java.time.LocalDate;

public sealed interface TeamCommand
        permits TeamCreateCommand, TeamUpdateCommand, TeamDeleteCommand, TeamUndeleteCommand {

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
}
