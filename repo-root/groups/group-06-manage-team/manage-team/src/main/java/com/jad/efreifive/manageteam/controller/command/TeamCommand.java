package com.jad.efreifive.manageteam.controller.command;

import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.valueobject.Label;

import java.time.LocalDate;
import java.util.UUID;

public sealed interface TeamCommand
        permits TeamCommand.TeamCreateCommand,
                TeamCommand.TeamUpdateLabelCommand,
                TeamCommand.TeamDissolveCommand,
                TeamCommand.TeamRestoreCommand,
                TeamCommand.TeamUpdateTagCommand {

    static String getLabel(TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> TeamDto.getLabel(createCommand.teamDto());
            case TeamUpdateLabelCommand updateLabelCommand -> updateLabelCommand.newLabel();
            default -> throw new IllegalStateException("No label for this command");
        };
    }

    static String getTag(TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> TeamDto.getTag(createCommand.teamDto());
            case TeamUpdateTagCommand updateTagCommand -> updateTagCommand.newTag();
            default -> throw new IllegalStateException("No tag for this command");
        };
    }

    static LocalDate getCreationDate(final TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> TeamDto.getCreationDate(createCommand.teamDto());
            default -> throw new IllegalStateException("No creation date for this command");
        };
    }

    static UUID getId(TeamCommand command) {
        return switch (command) {
            case TeamCreateCommand createCommand -> throw new IllegalStateException("No ID for this command");
            case TeamUpdateLabelCommand updateLabelCommand -> updateLabelCommand.id();
            case TeamUpdateTagCommand updateTagCommand -> updateTagCommand.id();
            case TeamDissolveCommand dissolveCommand -> dissolveCommand.id();
            case TeamRestoreCommand restoreCommand -> restoreCommand.id();
        };
    }

    static LocalDate getDissolutionDate(TeamCommand command) {
        return switch (command) {
            case TeamDissolveCommand dissolveCommand -> dissolveCommand.dissolutionDate();
            default -> throw new IllegalStateException("No dissolution date for this command");
        };
    }

    record TeamCreateCommand(TeamDto teamDto) implements TeamCommand {
    }

    record TeamDissolveCommand(UUID id, LocalDate dissolutionDate) implements TeamCommand {
    }

    record TeamRestoreCommand(UUID id) implements TeamCommand {
    }

    record TeamUpdateLabelCommand(UUID id, String newLabel) implements TeamCommand {
    }

    record TeamUpdateTagCommand(UUID id, String newTag) implements TeamCommand {
    }
}
