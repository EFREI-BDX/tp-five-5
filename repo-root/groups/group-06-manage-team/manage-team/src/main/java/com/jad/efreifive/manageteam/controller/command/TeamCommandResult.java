package com.jad.efreifive.manageteam.controller.command;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.service.DomainErrorCode;

public sealed interface TeamCommandResult
        permits TeamCommandResult.SuccessWithTeamDtoPayLoad,
                TeamCommandResult.SuccessWithPlayerDtoPayLoad,
                TeamCommandResult.SuccessNoPayLoad,
                TeamCommandResult.Failure {

    static Failure error(String message, DomainErrorCode domainErrorCode) {
        return new Failure(message, domainErrorCode);
    }

    static TeamCommandResult successWithPayLoad(final TeamDto payLoad) {
        return new SuccessWithTeamDtoPayLoad(payLoad);
    }


    static TeamCommandResult successWithPayLoad(final PlayerDto payLoad) {
        return new SuccessWithPlayerDtoPayLoad(payLoad);
    }

    static TeamCommandResult successNoPayLoad() {
        return new SuccessNoPayLoad();
    }

    static String getMessage(final TeamCommandResult teamCommandResult) {
        return switch (teamCommandResult) {
            case SuccessWithTeamDtoPayLoad success -> "Success";
            case SuccessWithPlayerDtoPayLoad success -> "Success";
            case SuccessNoPayLoad success -> "Success";
            case Failure failure -> failure.message();
        };
    }

    static DomainErrorCode getErrorCode(final TeamCommandResult teamCommandResult) {
        return switch (teamCommandResult) {
            case Failure failure -> failure.domainErrorCode();
            default -> throw new IllegalStateException(
                    "Cannot get error code from a successful result");
        };
    }

    static TeamDto getTeamDtoPayload(final TeamCommandResult teamCommandResult) {
        return switch (teamCommandResult) {
            case SuccessWithTeamDtoPayLoad success -> success.payload();
            default -> throw new IllegalStateException("Cannot get payload from a result without TeamDto payload");
        };
    }

    static PlayerDto getPlayerDtoPayload(final TeamCommandResult teamCommandResult) {
        return switch (teamCommandResult) {
            case SuccessWithPlayerDtoPayLoad success -> success.payload();
            default -> throw new IllegalStateException("Cannot get payload from a result without PlayerDto payload");
        };
    }

    record SuccessWithTeamDtoPayLoad(TeamDto payload) implements TeamCommandResult {
    }

    record SuccessWithPlayerDtoPayLoad(PlayerDto payload) implements TeamCommandResult {
    }

    record SuccessNoPayLoad() implements TeamCommandResult {
    }

    record Failure(String message, DomainErrorCode domainErrorCode) implements TeamCommandResult {
    }
}
