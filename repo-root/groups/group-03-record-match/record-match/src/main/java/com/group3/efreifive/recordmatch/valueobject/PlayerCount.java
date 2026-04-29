package com.group3.efreifive.recordmatch.valueobject;

public record PlayerCount(int value) {

    public PlayerCount {
        if (value < 0 || value > 2) {
            throw new IllegalArgumentException("PlayerCount.value must be 0, 1 or 2, got: " + value);
        }
    }

    public static PlayerCount of(final int value) {
        return new PlayerCount(value);
    }

    public boolean requiresNoPlayer() {
        return this.value == 0;
    }

    public boolean requiresOnePlayer() {
        return this.value == 1;
    }

    public boolean requiresTwoPlayers() {
        return this.value == 2;
    }
}
