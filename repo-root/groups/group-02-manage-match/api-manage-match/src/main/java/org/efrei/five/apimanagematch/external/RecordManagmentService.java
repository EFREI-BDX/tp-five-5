package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.service.IRecordManagmentService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.springframework.stereotype.Service;

@Service
public class RecordManagmentService implements IRecordManagmentService {

    private final RecordManagmentClient client;

    public RecordManagmentService(RecordManagmentClient client) {
        this.client = client;
    }

    @Override
    public void notifyEnd(Id id, Period period) {
        client.notifyEnd(id, period);
    }

    @Override
    public void notifyStart(Id id, Period period, Id teamIdA, Id teamIdB) {
        client.notifyStart(id, period, teamIdA, teamIdB);
    }
}
