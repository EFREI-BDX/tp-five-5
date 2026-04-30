package org.efrei.five.apimanagematch.domain.external;

import org.efrei.five.apimanagematch.domain.valueobjects.Id;

import java.util.List;

public interface IMatchByTeamRepository {
    List<Id> findNotStartedMatchIdsByTeamId(Id teamId);
}