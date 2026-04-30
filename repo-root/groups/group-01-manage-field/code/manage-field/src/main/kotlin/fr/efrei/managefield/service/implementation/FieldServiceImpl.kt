package fr.efrei.managefield.service.implementation

import fr.efrei.managefield.domain.entity.Field
import fr.efrei.managefield.domain.enums.FieldStatusCode
import fr.efrei.managefield.domain.service.FieldAvailabilityDomainService
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.FieldName
import fr.efrei.managefield.domain.valueobject.TimeSlot
import fr.efrei.managefield.mapper.FieldServiceMapper
import fr.efrei.managefield.mapper.ReservationServiceMapper
import fr.efrei.managefield.service.FieldService
import fr.efrei.managefield.service.dto.request.ChangeFieldStatusCommandDto
import fr.efrei.managefield.service.dto.request.CreateFieldCommandDto
import fr.efrei.managefield.service.dto.request.ListAvailableFieldsCommandDto
import fr.efrei.managefield.service.dto.response.FieldDetailsViewResultDto
import fr.efrei.managefield.service.dto.response.FieldViewResultDto
import fr.efrei.managefield.service.exception.ApplicationNotFoundException
import fr.efrei.managefield.service.exception.ApplicationValidationException
import fr.efrei.managefield.service.port.FieldReadPort
import fr.efrei.managefield.service.port.FieldWritePort
import fr.efrei.managefield.service.port.ReservationReadPort
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
    private val fieldReadPort: FieldReadPort,
    private val fieldWritePort: FieldWritePort,
    private val reservationReadPort: ReservationReadPort,
    private val fieldServiceMapper: FieldServiceMapper,
    private val reservationServiceMapper: ReservationServiceMapper
) : FieldService {
    private val availabilityDomainService = FieldAvailabilityDomainService()

    @Transactional(readOnly = true)
    override fun listAvailableFields(command: ListAvailableFieldsCommandDto): List<FieldViewResultDto> {
        val slot = parseSlot(command.date, command.startTime, command.endTime)
        val activeFields = fieldReadPort.listActiveFields()
        val blockingReservations = reservationReadPort.listBlockingByDate(slot.date)
        val availableFields = availabilityDomainService.listAvailableFields(activeFields, blockingReservations, slot)

        return fieldServiceMapper.toFieldViews(availableFields)
    }

    @Transactional(readOnly = true)
    override fun findById(fieldId: String): FieldDetailsViewResultDto {
        val id = parseId(fieldId, "field_id")
        val field = findFieldOrThrow(id.value)
        val reservations = reservationServiceMapper.toReservationViews(
            reservationReadPort.listByFieldId(id)
        )

        return fieldServiceMapper.toFieldDetails(field, reservations)
    }

    @Transactional
    override fun create(command: CreateFieldCommandDto): FieldViewResultDto {
        val fieldId = command.fieldId?.let { parseId(it, "field_id").value }
        val name = parseFieldName(command.name).value
        val status = parseFieldStatus(command.statusId)
        val createdFieldId = fieldWritePort.createField(
            fieldId = fieldId?.let { DomainId.from(it) },
            name = FieldName.from(name),
            status = status
        )

        return fieldServiceMapper.toFieldView(findFieldOrThrow(createdFieldId.value))
    }

    @Transactional
    override fun changeStatus(command: ChangeFieldStatusCommandDto): FieldViewResultDto {
        val fieldId = parseId(command.fieldId, "field_id")
        val status = parseFieldStatus(command.statusId)
        fieldWritePort.changeFieldStatus(fieldId, status)

        return fieldServiceMapper.toFieldView(findFieldOrThrow(fieldId.value))
    }

    private fun findFieldOrThrow(fieldId: String): Field {
        val id = DomainId.from(fieldId)
        return fieldReadPort.findById(id)
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

    private fun parseFieldStatus(raw: String): FieldStatusCode {
        val id = parseId(raw, "status_id")
        return FieldStatusCode.fromId(id.value)
            ?: throw ApplicationNotFoundException("field status not found")
    }

    private fun parseSlot(date: String, startTime: String, endTime: String): TimeSlot {
        return try {
            TimeSlot.from(date, startTime, endTime)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException(exception.message ?: "slot is invalid")
        }
    }
}
