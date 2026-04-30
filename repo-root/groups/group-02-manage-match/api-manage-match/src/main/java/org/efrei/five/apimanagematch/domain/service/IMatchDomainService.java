package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;

import java.util.Optional;

public interface IMatchDomainService {
    Optional<Match> getMatch(Id id);

    void cancelMatch(Id id);

    public Match createMatch(Id teamAUuid, Id teamBUuid, Id fieldUuid, Period period);
}
