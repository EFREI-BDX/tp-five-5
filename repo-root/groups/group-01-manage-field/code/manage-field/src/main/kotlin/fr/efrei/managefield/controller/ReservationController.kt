package fr.efrei.managefield.controller

import fr.efrei.managefield.controller.dto.request.CreateReservationRequestDto
import fr.efrei.managefield.controller.dto.request.UpdateReservationStatusRequestDto
import fr.efrei.managefield.controller.dto.response.ReservationResponseDto
import fr.efrei.managefield.mapper.ReservationApiMapper
import fr.efrei.managefield.service.ReservationService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

/**
 * Exposes HTTP endpoints for reservation creation and status changes.
 */
@Validated
@RestController
@RequestMapping("/v1/fields/{field_id}/reservations")
class ReservationController(
    private val reservationService: ReservationService,
    private val reservationApiMapper: ReservationApiMapper
) {
    @PostMapping
    fun create(
        @PathVariable("field_id") @NotBlank fieldId: String,
        @Valid @RequestBody request: CreateReservationRequestDto
    ): ResponseEntity<ReservationResponseDto> {
        val command = reservationApiMapper.toCreateCommand(fieldId, request)
        val response = reservationApiMapper.toReservationResponse(reservationService.create(command))

        return ResponseEntity
            .created(URI.create("/v1/fields/${response.fieldId}/reservations/${response.id}"))
            .body(response)
    }

    @PatchMapping("/{reservation_id}/status")
    fun changeStatus(
        @PathVariable("field_id") @NotBlank fieldId: String,
        @PathVariable("reservation_id") @NotBlank reservationId: String,
        @Valid @RequestBody request: UpdateReservationStatusRequestDto
    ): ReservationResponseDto {
        val command = reservationApiMapper.toChangeStatusCommand(fieldId, reservationId, request)
        return reservationApiMapper.toReservationResponse(reservationService.changeStatus(command))
    }
}
