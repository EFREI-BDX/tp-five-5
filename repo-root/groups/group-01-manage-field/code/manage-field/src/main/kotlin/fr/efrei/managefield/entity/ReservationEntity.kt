package fr.efrei.managefield.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Immutable JPA projection backed by the `v_reservation_details` database view.
 */
@Entity
@Immutable
@Table(name = "v_reservation_details")
class ReservationEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    var id: String = "",

    @Column(name = "field_id", nullable = false, length = 36)
    var fieldId: String = "",

    @Column(name = "status_id", nullable = false, length = 36)
    var statusId: String = "",

    @Column(name = "status_code", nullable = false, length = 32)
    var statusCode: String = "",

    @Column(name = "status_label", nullable = false, length = 100)
    var statusLabel: String = "",

    @Column(name = "date", nullable = false)
    var date: LocalDate? = null,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalTime? = null,

    @Column(name = "end_time", nullable = false)
    var endTime: LocalTime? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
)
