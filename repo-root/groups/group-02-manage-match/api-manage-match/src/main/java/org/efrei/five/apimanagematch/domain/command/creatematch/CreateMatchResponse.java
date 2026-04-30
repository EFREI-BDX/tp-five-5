package org.efrei.five.apimanagematch.domain.command.creatematch;

import org.efrei.five.apimanagematch.domain.command.common.MatchResponseDto;

public record CreateMatchResponse(
        MatchResponseDto match) {
}
