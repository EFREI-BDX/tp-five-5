package com.group3.efreifive.recordmatch.service;

import com.group3.efreifive.recordmatch.dto.MatchDto;

import java.util.UUID;

public interface IMatchSyncService {
    void handleMatchStarted(MatchDto matchDto);
    void handleMatchEnded(UUID matchId);
}
