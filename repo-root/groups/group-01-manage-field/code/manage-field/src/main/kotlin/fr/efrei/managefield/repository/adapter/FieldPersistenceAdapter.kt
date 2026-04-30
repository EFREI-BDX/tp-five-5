package fr.efrei.managefield.repository.adapter

import fr.efrei.managefield.domain.entity.Field
import fr.efrei.managefield.domain.enums.FieldStatusCode
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.FieldName
import fr.efrei.managefield.entity.ActiveFieldEntity
import fr.efrei.managefield.entity.FieldEntity
import fr.efrei.managefield.repository.ActiveFieldRepository
import fr.efrei.managefield.repository.FieldRepository
import fr.efrei.managefield.repository.procedural.requireSuccess
import fr.efrei.managefield.service.exception.ApplicationInternalException
import fr.efrei.managefield.service.port.FieldReadPort
import fr.efrei.managefield.service.port.FieldWritePort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

/**
 * Persistence adapter implementing field application ports with JPA views and stored procedures.
 */
@Component
class FieldPersistenceAdapter(
    private val fieldRepository: FieldRepository,
    private val activeFieldRepository: ActiveFieldRepository
) : FieldReadPort, FieldWritePort {
    override fun findById(fieldId: DomainId): Field? {
        return fieldRepository.findByIdOrNull(fieldId.value)?.toDomain()
    }

    override fun listActiveFields(): List<Field> {
        return activeFieldRepository.findAllByOrderByNameAsc().map { it.toDomain() }
    }

    override fun createField(fieldId: DomainId?, name: FieldName, status: FieldStatusCode): DomainId {
        val response = fieldRepository.createField(fieldId?.value, name.value, status.id).requireSuccess()
        val createdFieldId = response.getFieldId() ?: fieldId?.value
            ?: throw ApplicationInternalException("create field procedure did not return a field_id")

        return DomainId.from(createdFieldId)
    }

    override fun changeFieldStatus(fieldId: DomainId, status: FieldStatusCode) {
        fieldRepository.changeFieldStatus(fieldId.value, status.id).requireSuccess()
    }

    private fun FieldEntity.toDomain(): Field {
        return Field.restore(
            id = DomainId.from(id),
            name = FieldName.from(name),
            status = FieldStatusCode.fromId(statusId)
                ?: throw ApplicationInternalException("unknown field status id: $statusId")
        )
    }

    private fun ActiveFieldEntity.toDomain(): Field {
        return Field.restore(
            id = DomainId.from(id),
            name = FieldName.from(name),
            status = FieldStatusCode.fromId(statusId)
                ?: throw ApplicationInternalException("unknown field status id: $statusId")
        )
    }
}
