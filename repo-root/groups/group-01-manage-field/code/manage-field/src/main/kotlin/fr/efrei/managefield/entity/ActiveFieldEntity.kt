package fr.efrei.managefield.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.time.LocalDateTime

/**
 * Immutable JPA projection backed by the `v_active_field` availability read view.
 */
@Entity
@Immutable
@Table(name = "v_active_field")
class ActiveFieldEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    var id: String = "",

    @Column(name = "name", nullable = false, length = 100)
    var name: String = "",

    @Column(name = "status_id", nullable = false, length = 36)
    var statusId: String = "",

    @Column(name = "status_code", nullable = false, length = 32)
    var statusCode: String = "",

    @Column(name = "status_label", nullable = false, length = 100)
    var statusLabel: String = "",

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
)
