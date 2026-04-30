package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.efrei.five.apimanagematch.external.dto.EndMatchRequest;
import org.efrei.five.apimanagematch.external.dto.StartMatchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class RecordManagmentClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RecordManagmentClient(
            RestTemplate restTemplate,
            @Value("${service.record.url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void notifyStart(Id id, Period period, Id teamA, Id teamB) {
        String url = baseUrl + "/matchs/event/start";

        StartMatchRequest request = new StartMatchRequest(
                id.value(),
                teamA.value(),
                teamB.value(),
                period.start(),
                Duration.between(period.start(), period.end()).toMinutes()
        );

        restTemplate.postForObject(url, request, Void.class);
    }

    public void notifyEnd(Id id, Period period) {
        String url = baseUrl + "/matchs/event/end";

        EndMatchRequest request = new EndMatchRequest(
                id.value(),
                period.end()
        );

        restTemplate.postForObject(url, request, Void.class);
    }
}
