package fr.efrei.managefield.repository.adapter

import fr.efrei.managefield.domain.entity.Reservation
import fr.efrei.managefield.domain.enums.ReservationStatusCode
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.TimeSlot
import fr.efrei.managefield.entity.BlockingReservationEntity
import fr.efrei.managefield.entity.ReservationEntity
import fr.efrei.managefield.repository.BlockingReservationRepository
import fr.efrei.managefield.repository.ReservationRepository
import fr.efrei.managefield.repository.procedural.requireSuccess
import fr.efrei.managefield.service.exception.ApplicationInternalException
import fr.efrei.managefield.service.port.ReservationReadPort
import fr.efrei.managefield.service.port.ReservationWritePort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Persistence adapter implementing reservation application ports with JPA views and stored procedures.
 */
@Component
class ReservationPersistenceAdapter(
    private val reservationRepository: ReservationRepository,
    private val blockingReservationRepository: BlockingReservationRepository
) : ReservationReadPort, ReservationWritePort {
    override fun listByFieldId(fieldId: DomainId): List<Reservation> {
        return reservationRepository.findAllByFieldIdOrderByDateAscStartTimeAsc(fieldId.value).map { it.toDomain() }
    }

    override fun listBlockingByDate(date: LocalDate): List<Reservation> {
        return blockingReservationRepository.findAllByDate(date).map { it.toDomain() }
    }

    override fun findByIdForField(reservationId: DomainId, fieldId: DomainId): Reservation? {
        val reservation = reservationRepository.findByIdOrNull(reservationId.value) ?: return null
        if (reservation.fieldId != fieldId.value) {
            return null
        }

        return reservation.toDomain()
    }

    override fun createReservation(
        reservationId: DomainId,
        fieldId: DomainId,
        status: ReservationStatusCode,
        slot: TimeSlot
    ): DomainId {
        val response = reservationRepository
            .createReservation(
                reservationId = reservationId.value,
                fieldId = fieldId.value,
                statusId = status.id,
                date = slot.date,
                startTime = slot.startTime,
                endTime = slot.endTime
            )
            .requireSuccess()

        val createdReservationId = response.getReservationId()
            ?: throw ApplicationInternalException("create reservation procedure did not return a reservation_id")

        return DomainId.from(createdReservationId)
    }

    override fun changeReservationStatus(fieldId: DomainId, reservationId: DomainId, status: ReservationStatusCode) {
        reservationRepository
            .changeReservationStatus(fieldId.value, reservationId.value, status.id)
            .requireSuccess()
    }

    private fun ReservationEntity.toDomain(): Reservation {
        return Reservation.restore(
            id = DomainId.from(id),
            fieldId = DomainId.from(fieldId),
            status = ReservationStatusCode.fromId(statusId)
                ?: throw ApplicationInternalException("unknown reservation status id: $statusId"),
            slot = TimeSlot.from(
                rawDate = requireNotNull(date) { "reservation date must not be null" }.toString(),
                rawStartTime = requireNotNull(startTime) { "reservation start time must not be null" }
                    .format(DateTimeFormatter.ofPattern("HH:mm")),
                rawEndTime = requireNotNull(endTime) { "reservation end time must not be null" }
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
            )
        )
    }

    private fun BlockingReservationEntity.toDomain(): Reservation {
        return Reservation.restore(
            id = DomainId.from(id),
            fieldId = DomainId.from(fieldId),
            status = ReservationStatusCode.fromId(statusId)
                ?: throw ApplicationInternalException("unknown reservation status id: $statusId"),
            slot = TimeSlot.from(
                rawDate = requireNotNull(date) { "reservation date must not be null" }.toString(),
                rawStartTime = requireNotNull(startTime) { "reservation start time must not be null" }
                    .format(DateTimeFormatter.ofPattern("HH:mm")),
                rawEndTime = requireNotNull(endTime) { "reservation end time must not be null" }
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
            )
        )
    }
}
