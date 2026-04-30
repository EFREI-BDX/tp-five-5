package org.efrei.five.apimanagematch.external.dto;

import java.util.UUID;

public record FieldReservationResponce(
        UUID fieldId, String status
) {
}
