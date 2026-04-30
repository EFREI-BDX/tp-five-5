package fr.efrei.managefield.service.implementation

import fr.efrei.managefield.domain.entity.Field
import fr.efrei.managefield.domain.entity.Reservation
import fr.efrei.managefield.domain.enums.ReservationStatusCode
import fr.efrei.managefield.domain.service.ReservationPolicy
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.TimeSlot
import fr.efrei.managefield.mapper.ReservationServiceMapper
import fr.efrei.managefield.service.ReservationService
import fr.efrei.managefield.service.dto.request.ChangeReservationStatusCommandDto
import fr.efrei.managefield.service.dto.request.CreateReservationCommandDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import fr.efrei.managefield.service.exception.ApplicationConflictException
import fr.efrei.managefield.service.exception.ApplicationNotFoundException
import fr.efrei.managefield.service.exception.ApplicationValidationException
import fr.efrei.managefield.service.port.FieldReadPort
import fr.efrei.managefield.service.port.ReservationReadPort
import fr.efrei.managefield.service.port.ReservationWritePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.UUID

/**
 * Default [ReservationService] implementation.
 *
 * Reads go through JPA view projections; writes go through repository
 * `@Procedure` methods.
 */
@Service
@Validated
class ReservationServiceImpl(
    private val fieldReadPort: FieldReadPort,
    private val reservationReadPort: ReservationReadPort,
    private val reservationWritePort: ReservationWritePort,
    private val reservationServiceMapper: ReservationServiceMapper
) : ReservationService {
    private val reservationPolicy = ReservationPolicy()

    @Transactional(readOnly = true)
    override fun listByFieldId(fieldId: String): List<ReservationViewResultDto> {
        val id = parseId(fieldId, "field_id")
        findFieldOrThrow(id)

        return reservationServiceMapper.toReservationViews(
            reservationReadPort.listByFieldId(id)
        )
    }

    @Transactional
    override fun create(command: CreateReservationCommandDto): ReservationViewResultDto {
        val fieldId = parseId(command.fieldId, "field_id")
        val status = parseReservationStatus(command.statusId)
        val slot = parseSlot(command.date, command.startTime, command.endTime)
        val reservationId = UUID.randomUUID().toString()
        val field = findFieldOrThrow(fieldId)
        val existingReservations = reservationReadPort.listByFieldId(fieldId)
        requireCreationAllowed(field, status, slot, existingReservations)
        val createdReservationId = reservationWritePort.createReservation(
            reservationId = DomainId.from(reservationId),
            fieldId = fieldId,
            status = status,
            slot = slot
        )

        return reservationServiceMapper.toReservationView(findReservationForFieldOrThrow(createdReservationId, fieldId))
    }

    @Transactional
    override fun changeStatus(command: ChangeReservationStatusCommandDto): ReservationViewResultDto {
        val fieldId = parseId(command.fieldId, "field_id")
        val reservationId = parseId(command.reservationId, "reservation_id")
        val status = parseReservationStatus(command.statusId)
        reservationWritePort.changeReservationStatus(fieldId, reservationId, status)

        return reservationServiceMapper.toReservationView(findReservationForFieldOrThrow(reservationId, fieldId))
    }

    private fun findReservationForFieldOrThrow(reservationId: DomainId, fieldId: DomainId): Reservation {
        return reservationReadPort.findByIdForField(reservationId, fieldId)
            ?: throw ApplicationNotFoundException("reservation not found")
    }

    private fun findFieldOrThrow(fieldId: DomainId): Field {
        return fieldReadPort.findById(fieldId)
            ?: throw ApplicationNotFoundException("field not found")
    }

    private fun parseId(raw: String, field: String): DomainId {
        return try {
            DomainId.from(raw)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException("${field}: ${exception.message}")
        }
    }

    private fun parseSlot(date: String, startTime: String, endTime: String): TimeSlot {
        return try {
            TimeSlot.from(date, startTime, endTime)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException(exception.message ?: "slot is invalid")
        }
    }

    private fun parseReservationStatus(raw: String): ReservationStatusCode {
        val id = parseId(raw, "status_id")
        return ReservationStatusCode.fromId(id.value)
            ?: throw ApplicationNotFoundException("reservation status not found")
    }

    private fun requireCreationAllowed(
        field: Field,
        status: ReservationStatusCode,
        slot: TimeSlot,
        existingReservations: Collection<Reservation>
    ) {
        try {
            reservationPolicy.requireCreationAllowed(field, status, slot, existingReservations)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationConflictException(exception.message ?: "reservation cannot be created")
        }
    }
}
