package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;

public interface IMatchTimerService {
    void scheduleMatchTimers(Match match);

    void cancelMatchTimers(Id matchId);
}