package fr.efrei.managefield.service.implementation

import fr.efrei.managefield.domain.enums.FieldStatusCode
import fr.efrei.managefield.domain.enums.ReservationStatusCode
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.FieldName
import fr.efrei.managefield.domain.valueobject.TimeSlot
import fr.efrei.managefield.entity.FieldEntity
import fr.efrei.managefield.mapper.FieldServiceMapper
import fr.efrei.managefield.repository.FieldRepository
import fr.efrei.managefield.repository.ReservationRepository
import fr.efrei.managefield.service.FieldService
import fr.efrei.managefield.service.dto.ChangeFieldStatusCommandDto
import fr.efrei.managefield.service.dto.CreateFieldCommandDto
import fr.efrei.managefield.service.dto.FieldViewResultDto
import fr.efrei.managefield.service.dto.ListAvailableFieldsCommandDto
import fr.efrei.managefield.service.exception.ApplicationInternalException
import fr.efrei.managefield.service.exception.ApplicationNotFoundException
import fr.efrei.managefield.service.exception.ApplicationValidationException
import fr.efrei.managefield.service.requireSuccess
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

/**
 * Default [FieldService] implementation.
 *
 * Reads go through JPA view projections; writes go through repository
 * `@Procedure` methods.
 */
@Service
@Validated
class FieldServiceImpl(
    private val fieldRepository: FieldRepository,
    private val reservationRepository: ReservationRepository,
    private val fieldServiceMapper: FieldServiceMapper
) : FieldService {
    @Transactional(readOnly = true)
    override fun listAvailableFields(command: ListAvailableFieldsCommandDto): List<FieldViewResultDto> {
        val slot = parseSlot(command.date, command.startTime, command.endTime)
        val activeFields = fieldRepository.findAllByStatusIdOrderByNameAsc(FieldStatusCode.ACTIVE.id)
        val blockingStatusIds = listOf(ReservationStatusCode.PENDING.id, ReservationStatusCode.CONFIRMED.id)

        if (activeFields.isEmpty()) {
            return fieldServiceMapper.toFieldViews(activeFields)
        }

        val blockedFieldIds = reservationRepository
            .findAllByDateAndStatusIdIn(slot.date, blockingStatusIds)
            .filter { reservation ->
                val start = reservation.startTime ?: throw ApplicationInternalException("reservation start_time is missing")
                val end = reservation.endTime ?: throw ApplicationInternalException("reservation end_time is missing")
                slot.overlaps(start, end)
            }
            .map { it.fieldId }
            .toSet()

        return fieldServiceMapper.toFieldViews(activeFields.filterNot { it.id in blockedFieldIds })
    }

    @Transactional(readOnly = true)
    override fun findById(fieldId: String): FieldViewResultDto {
        val id = parseId(fieldId, "field_id")
        return fieldServiceMapper.toFieldView(findFieldOrThrow(id.value))
    }

    @Transactional
    override fun create(command: CreateFieldCommandDto): FieldViewResultDto {
        val fieldId = command.fieldId?.let { parseId(it, "field_id").value }
        val name = parseFieldName(command.name).value
        val statusId = parseId(command.statusId, "status_id").value
        val response = fieldRepository.createField(fieldId, name, statusId).requireSuccess()
        val createdFieldId = response.getFieldId() ?: fieldId
            ?: throw ApplicationInternalException("create field procedure did not return a field_id")

        return fieldServiceMapper.toFieldView(findFieldOrThrow(createdFieldId))
    }

    @Transactional
    override fun changeStatus(command: ChangeFieldStatusCommandDto): FieldViewResultDto {
        val fieldId = parseId(command.fieldId, "field_id")
        val statusId = parseId(command.statusId, "status_id")
        fieldRepository.changeFieldStatus(fieldId.value, statusId.value).requireSuccess()

        return fieldServiceMapper.toFieldView(findFieldOrThrow(fieldId.value))
    }

    private fun findFieldOrThrow(fieldId: String): FieldEntity {
        return fieldRepository.findByIdOrNull(fieldId)
            ?: throw ApplicationNotFoundException("field not found")
    }

    private fun parseId(raw: String, field: String): DomainId {
        return try {
            DomainId.from(raw)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException("${field}: ${exception.message}")
        }
    }

    private fun parseFieldName(raw: String): FieldName {
        return try {
            FieldName.from(raw)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException(exception.message ?: "field name is invalid")
        }
    }

    private fun parseSlot(date: String, startTime: String, endTime: String): TimeSlot {
        return try {
            TimeSlot.from(date, startTime, endTime)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException(exception.message ?: "slot is invalid")
        }
    }
}
