package com.jad.efreifive.manageteam.controller.command;

import com.jad.efreifive.manageteam.dto.TeamDto;

import java.time.LocalDate;
import java.util.UUID;

public sealed interface TeamCommand
        permits TeamCommand.TeamCreateCommand,
                TeamCommand.TeamUpdateLabelCommand,
                TeamCommand.TeamDissolveCommand,
                TeamCommand.TeamRestoreCommand,
                TeamCommand.TeamUpdateTagCommand,
                TeamCommand.TeamAssignPlayerCommand,
                TeamCommand.TeamRemovePlayerCommand {

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
            case TeamUpdateLabelCommand updateLabelCommand -> updateLabelCommand.id();
            case TeamUpdateTagCommand updateTagCommand -> updateTagCommand.id();
            case TeamDissolveCommand dissolveCommand -> dissolveCommand.id();
            case TeamRestoreCommand restoreCommand -> restoreCommand.id();
            case TeamAssignPlayerCommand assignPlayerCommand -> assignPlayerCommand.teamId();
            case TeamRemovePlayerCommand removePlayerCommand -> removePlayerCommand.teamId();
            default -> throw new IllegalStateException("No ID for this command");
        };
    }

    static UUID getPlayerId(TeamCommand command) {
        return switch (command) {
            case TeamAssignPlayerCommand assignPlayerCommand -> assignPlayerCommand.playerId();
            case TeamRemovePlayerCommand removePlayerCommand -> removePlayerCommand.playerId();
            default -> throw new IllegalStateException("No player ID for this command");
        };
    }

    static LocalDate getDissolutionDate(TeamCommand command) {
        return switch (command) {
            case TeamDissolveCommand dissolveCommand -> dissolveCommand.dissolutionDate();
            default -> throw new IllegalStateException("No dissolution date for this command");
        };
    }

    String toLogString();

    record TeamCreateCommand(TeamDto teamDto) implements TeamCommand {
        @Override
        public String toLogString() {
            return String.format("TeamCreateCommand{label=%s, tag=%s, creationDate=%s}",
                                 TeamDto.getLabel(this.teamDto()),
                                 TeamDto.getTag(this.teamDto()),
                                 TeamDto.getCreationDate(this.teamDto()));
        }
    }

    record TeamDissolveCommand(UUID id, LocalDate dissolutionDate) implements TeamCommand {
        @Override
        public String toLogString() {
            return String.format("TeamDissolveCommand{id=%s, dissolutionDate=%s}",
                                 this.id(),
                                 this.dissolutionDate());
        }
    }

    record TeamRestoreCommand(UUID id) implements TeamCommand {
        @Override
        public String toLogString() {
            return String.format("TeamRestoreCommand{id=%s}", this.id());
        }
    }

    record TeamUpdateLabelCommand(UUID id, String newLabel) implements TeamCommand {
        @Override
        public String toLogString() {
            return String.format("TeamUpdateLabelCommand{id=%s, newLabel=%s}", this.id(), this.newLabel());
        }
    }

    record TeamUpdateTagCommand(UUID id, String newTag) implements TeamCommand {
        @Override
        public String toLogString() {
            return String.format("TeamUpdateTagCommand{id=%s, newTag=%s}", this.id(), this.newTag());
        }
    }

    record TeamAssignPlayerCommand(UUID teamId, UUID playerId) implements TeamCommand {
        @Override
        public String toLogString() {
            return String.format("TeamAssignPlayerCommand{teamId=%s, playerId=%s}", this.teamId(), this.playerId());
        }
    }

    record TeamRemovePlayerCommand(UUID teamId, UUID playerId) implements TeamCommand {
        @Override
        public String toLogString() {
            return String.format("TeamRemovePlayerCommand{teamId=%s, playerId=%s}", this.teamId(), this.playerId());
        }
    }
}
