package fr.efrei.managefield.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.time.LocalDateTime

/**
 * Immutable JPA projection backed by the `v_field_status` database view.
 */
@Entity
@Immutable
@Table(name = "v_field_status")
class FieldStatusEntity(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    var id: String = "",

    @Column(name = "code", nullable = false, length = 32)
    var code: String = "",

    @Column(name = "label", nullable = false, length = 100)
    var label: String = "",

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
)
