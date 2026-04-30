package org.efrei.five.apimanagematch.domain.entities;


import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.MatchState;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;

import java.time.LocalDateTime;
import java.util.UUID;

public record Match(Id id, Team teamA, Team teamB, Field field, Period period, MatchState status) {

    public static UUID getIdValue(Match match) {
        return match.id().value();
    }

    public static LocalDateTime getStartValue(Match match) {
        return match.period().start();
    }

    public static LocalDateTime getEndValue(Match match) {
        return match.period().end();
    }

    public static String getMatchState(Match match) {
        return match.status.toString();
    }

}