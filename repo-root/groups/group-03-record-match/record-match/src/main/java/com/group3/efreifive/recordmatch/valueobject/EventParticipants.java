package com.group3.efreifive.recordmatch.valueobject;

import java.util.Optional;

public record EventParticipants(PlayerId player1, PlayerId player2) {

    public EventParticipants {
        if (player1 == null && player2 != null) {
            throw new IllegalArgumentException("EventParticipants.player2 cannot be set without player1");
        }
        if (player1 != null && player2 != null && player1.equals(player2)) {
            throw new IllegalArgumentException("EventParticipants.player1 and player2 must be different");
        }
    }

    public static EventParticipants none() {
        return new EventParticipants(null, null);
    }

    public static EventParticipants ofOne(final PlayerId player1) {
        if (player1 == null) {
            throw new IllegalArgumentException("EventParticipants.player1 must not be null");
        }
        return new EventParticipants(player1, null);
    }

    public static EventParticipants ofTwo(final PlayerId player1, final PlayerId player2) {
        if (player1 == null) {
            throw new IllegalArgumentException("EventParticipants.player1 must not be null");
        }
        if (player2 == null) {
            throw new IllegalArgumentException("EventParticipants.player2 must not be null");
        }
        return new EventParticipants(player1, player2);
    }

    public static EventParticipants forCount(final PlayerCount count, final PlayerId player1, final PlayerId player2) {
        if (count.requiresNoPlayer()) {
            return EventParticipants.none();
        }
        if (count.requiresOnePlayer()) {
            return EventParticipants.ofOne(player1);
        }
        return EventParticipants.ofTwo(player1, player2);
    }

    public boolean hasNoPlayer() {
        return this.player1 == null && this.player2 == null;
    }

    public boolean hasOnePlayer() {
        return this.player1 != null && this.player2 == null;
    }

    public boolean hasTwoPlayers() {
        return this.player1 != null && this.player2 != null;
    }

    public Optional<PlayerId> getPlayer1() {
        return Optional.ofNullable(this.player1);
    }

    public Optional<PlayerId> getPlayer2() {
        return Optional.ofNullable(this.player2);
    }
}
