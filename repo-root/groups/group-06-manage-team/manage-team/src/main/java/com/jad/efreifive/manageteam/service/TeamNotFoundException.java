package com.jad.efreifive.manageteam.service;

public class TeamNotFoundException extends TeamServiceException {

    public TeamNotFoundException(String message) {
        super(DomainErrorCode.TEAM_NOT_FOUND, message);
    }
}

