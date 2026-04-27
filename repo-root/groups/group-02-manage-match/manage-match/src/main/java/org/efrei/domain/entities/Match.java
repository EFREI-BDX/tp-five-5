package org.efrei.domain.entities;
import org.efrei.domain.valueobjects.*;
public record Match(Id id, Team teamA, Team teamB, Field field, Period period, MatchState status) {}
