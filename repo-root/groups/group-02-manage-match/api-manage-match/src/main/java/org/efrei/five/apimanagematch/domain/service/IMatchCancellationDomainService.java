package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.valueobjects.Id;

public interface IMatchCancellationDomainService {
    void cancelMatchesForTeam(Id teamId);
}