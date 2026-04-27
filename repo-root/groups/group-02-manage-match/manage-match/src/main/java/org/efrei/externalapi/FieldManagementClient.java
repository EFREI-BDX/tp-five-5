package org.efrei.externalapi;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.UUID;

@Client("${field.service.url}")
public interface FieldManagementClient {

    @Post("/v1/fields/{fieldId}/reservations")
    ReservationResponse createReservation(
            UUID fieldId,
            @Body ReservationRequest request
    );

    @Post("")
    void cancelReservation(UUID fieldId);

    record ReservationRequest(String startTime, String endTime) {
    }

    record ReservationResponse(UUID id, String status) {
    }
}