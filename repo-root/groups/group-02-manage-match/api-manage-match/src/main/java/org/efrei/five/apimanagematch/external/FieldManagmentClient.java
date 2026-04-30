package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Label;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.efrei.five.apimanagematch.external.dto.FieldReservationRequest;
import org.efrei.five.apimanagematch.external.dto.FieldReservationResponce;
import org.efrei.five.apimanagematch.external.dto.FieldResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class FieldManagmentClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FieldManagmentClient(
            RestTemplate restTemplate,
            @Value("${service.field.url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Optional<Field> getField(Id id) {
        try {
            String url = baseUrl + "/v1//fields/" + id.value();
            FieldResponse response = restTemplate.getForObject(url, FieldResponse.class);

            if (response == null) {
                return Optional.empty();
            }

            return Optional.of(new Field(id, new Label(response.label())));

        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    public Boolean createReservation(Id id, Period period) {
        try {
            String url = baseUrl + "/v1/fields/" + id.value() + "/reservations";

            FieldReservationRequest payload = new FieldReservationRequest(
                    period.start(),
                    period.end()
            );

            ResponseEntity<FieldReservationResponce> response =
                    restTemplate.postForEntity(url, payload, FieldReservationResponce.class);

            return response.getStatusCode().is2xxSuccessful();

        } catch (RestClientException e) {
            return false;
        }
    }
}
