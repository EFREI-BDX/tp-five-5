package fr.efrei.managefield.service

import fr.efrei.managefield.service.dto.request.ChangeReservationStatusCommandDto
import fr.efrei.managefield.service.dto.request.CreateReservationCommandDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

/**
 * Defines reservation operations exposed by the application layer.
 */
interface ReservationService {
    /**
     * Lists reservations for a field.
     */
    fun listByFieldId(@NotBlank fieldId: String): List<ReservationViewResultDto>

    /**
     * Creates a reservation through the stored procedure.
     */
    fun create(@Valid @NotNull command: CreateReservationCommandDto): ReservationViewResultDto

    /**
     * Changes a reservation status through the stored procedure.
     */
    fun changeStatus(@Valid @NotNull command: ChangeReservationStatusCommandDto): ReservationViewResultDto
}
