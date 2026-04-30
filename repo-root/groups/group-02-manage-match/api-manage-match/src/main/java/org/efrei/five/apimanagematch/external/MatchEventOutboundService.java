package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.external.dto.EndMatchRequest;
import org.efrei.five.apimanagematch.external.dto.StartMatchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Service
public class MatchEventOutboundService {

    private static final Logger log = LoggerFactory.getLogger(MatchEventOutboundService.class);

    private final RestTemplate restTemplate;
    private final MatchOutboundProperties properties;

    public MatchEventOutboundService(RestTemplate restTemplate, MatchOutboundProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public void notifyMatchStarted(Match match) {
        StartMatchRequest startMatchRequest = new StartMatchRequest(
                Match.getIdValue(match),
                Team.getIdValue(match.teamA()),
                Team.getIdValue(match.teamB()),
                Match.getStartValue(match),
                Duration.between(Match.getStartValue(match), Match.getEndValue(match)).toMinutes());
        notify("started", startMatchRequest, StartMatchRequest.class);
    }

    public void notifyMatchEnded(Match match) {
        EndMatchRequest endMatchRequest = new EndMatchRequest(Match.getIdValue(match), Match.getEndValue(match));
        notify("ended", endMatchRequest, EndMatchRequest.class);
    }

    private <T, R> void notify(String eventType, T payload, Class<R> responseType) {
        List<String> urls = properties.getNotifyUrls().getOrDefault(eventType, List.of());
        for (String url : urls) {
            try {
                restTemplate.postForObject(url, payload, responseType);
                log.info("Event {} sent to {}", eventType, url);
                return;
            } catch (Exception e) {
                log.warn("Event {} failed to send to {}: {}", eventType, url, e.getMessage());
            }
        }
    }
}