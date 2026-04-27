package com.jad.efreifive.manageteam.service;

public class PlayerNotFoundException extends TeamServiceException {

    public PlayerNotFoundException(String message) {
        super(DomainErrorCode.PLAYER_NOT_FOUND, message);
    }
}

