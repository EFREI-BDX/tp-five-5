package org.efrei.five.apimanagematch.domain.command.getmatch;

import org.efrei.five.apimanagematch.domain.command.common.MatchResponseDto;

import java.util.Optional;

public record GetMatchResponse(Optional<MatchResponseDto> match) {
}