package fr.efrei.managefield.service

import fr.efrei.managefield.service.dto.ChangeReservationStatusCommandDto
import fr.efrei.managefield.service.dto.CreateReservationCommandDto
import fr.efrei.managefield.service.dto.ReservationViewResultDto
import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull

/**
 * Defines reservation operations exposed by the application layer.
 */
interface ReservationService {
    /**
     * Creates a reservation through the stored procedure.
     */
    fun create(@Valid @NotNull command: CreateReservationCommandDto): ReservationViewResultDto

    /**
     * Changes a reservation status through the stored procedure.
     */
    fun changeStatus(@Valid @NotNull command: ChangeReservationStatusCommandDto): ReservationViewResultDto
}
