package com.jad.efreifive.manageteam.controller.command;

import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.service.DomainErrorCode;

public sealed interface TeamCommandResult
        permits TeamCommandResult.SuccessWithPayLoad,
                TeamCommandResult.SuccessNoPayLoad,
                TeamCommandResult.Failure {

    static Failure error(String message, DomainErrorCode domainErrorCode) {
        return new Failure(message, domainErrorCode);
    }

    static TeamCommandResult successWithPayLoad(final TeamDto payLoad) {
        return new SuccessWithPayLoad(payLoad);
    }

    static TeamCommandResult successNoPayLoad() {
        return new SuccessNoPayLoad();
    }

    static String getMessage(final TeamCommandResult teamCommandResult) {
        return switch (teamCommandResult) {
            case SuccessWithPayLoad success -> "Success";
            case SuccessNoPayLoad success -> "Success";
            case Failure failure -> failure.message();
        };
    }

    static DomainErrorCode getErrorCode(final TeamCommandResult teamCommandResult) {
        return switch (teamCommandResult) {
            case SuccessWithPayLoad success -> throw new IllegalStateException(
                    "Cannot get error code from a successful result");
            case SuccessNoPayLoad success -> throw new IllegalStateException(
                    "Cannot get error code from a successful result");
            case Failure failure -> failure.domainErrorCode();
        };
    }

    static TeamDto getPayload(final TeamCommandResult teamCommandResult) {
        return switch (teamCommandResult) {
            case SuccessWithPayLoad success -> success.payload();
            case SuccessNoPayLoad success -> throw new IllegalStateException(
                    "Cannot get payload from a success result without payload");
            case Failure failure -> throw new IllegalStateException("Cannot get payload from a failure result");
        };
    }

    record SuccessWithPayLoad(TeamDto payload) implements TeamCommandResult {
    }

    record SuccessNoPayLoad() implements TeamCommandResult {
    }

    record Failure(String message, DomainErrorCode domainErrorCode) implements TeamCommandResult {
    }
}
