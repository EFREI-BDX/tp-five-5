package org.efrei.five.apimanagematch.domain.external;

import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.MatchState;

import java.util.Optional;

public interface IMatchRepository {
    Optional<Match> findById(Id id);


    void updateMatchStatus(Id id, MatchState state);

    void save(Match match);

}
