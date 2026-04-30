package org.efrei.five.apimanagematch.external;


import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Label;
import org.efrei.five.apimanagematch.domain.valueobjects.Tag;
import org.efrei.five.apimanagematch.external.dto.TeamResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Component
public class TeamManagmentClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public TeamManagmentClient(
            RestTemplate restTemplate,
            @Value("${service.team.url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Optional<Team> getTeam(Id teamId) {
        try {
            String url = baseUrl + "/teams/" + teamId.value();

            TeamResponse team = restTemplate.getForObject(url, TeamResponse.class);

            return Optional.ofNullable(team)
                    .map(t -> new Team(
                            teamId,
                            new Tag(t.tag()),
                            new Label(t.label())
                    ));

        } catch (RestClientException e) {
            return Optional.empty();
        }
    }
}