package org.efrei.five.apimanagematch.external.dto;

import java.time.LocalDateTime;

public record FieldReservationRequest(LocalDateTime startTime, LocalDateTime endTime) {
}