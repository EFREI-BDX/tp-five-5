package fr.efreifive.manageplayer.service;

import java.util.UUID;

public class PlayerNotFoundException extends ServiceOperationException {
    public PlayerNotFoundException(UUID id) {
        super(DomainErrorCode.PLAYER_NOT_FOUND, "Player with id " + id + " not found");
    }
}
