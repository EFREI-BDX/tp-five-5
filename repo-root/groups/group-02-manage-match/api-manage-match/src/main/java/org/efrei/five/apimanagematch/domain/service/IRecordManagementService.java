package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;

public interface IRecordManagementService {
    void notifyStart(Id id, Period period, Id teamIdA, Id teamIdB);

    void notifyEnd(Id id, Period period);
}
