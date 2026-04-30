package org.efrei.five.apimanagematch.domain.command.syncteam;

import java.util.UUID;

public record SyncTeamCommand(UUID id, String tag, String label) {
}