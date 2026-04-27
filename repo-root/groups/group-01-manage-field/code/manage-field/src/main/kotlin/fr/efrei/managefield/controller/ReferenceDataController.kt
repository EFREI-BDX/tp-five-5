package fr.efrei.managefield.controller

import fr.efrei.managefield.controller.dto.response.FieldStatusResponseDto
import fr.efrei.managefield.controller.dto.response.ReservationStatusResponseDto
import fr.efrei.managefield.mapper.ReferenceDataApiMapper
import fr.efrei.managefield.service.ReferenceDataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Exposes HTTP endpoints for status reference data.
 */
@RestController
class ReferenceDataController(
    private val referenceDataService: ReferenceDataService,
    private val referenceDataApiMapper: ReferenceDataApiMapper
) {
    @GetMapping("/v1/field-statuses")
    fun listFieldStatuses(): List<FieldStatusResponseDto> {
        return referenceDataApiMapper.toFieldStatusResponses(referenceDataService.listFieldStatuses())
    }

    @GetMapping("/v1/reservation-statuses")
    fun listReservationStatuses(): List<ReservationStatusResponseDto> {
        return referenceDataApiMapper.toReservationStatusResponses(referenceDataService.listReservationStatuses())
    }
}
