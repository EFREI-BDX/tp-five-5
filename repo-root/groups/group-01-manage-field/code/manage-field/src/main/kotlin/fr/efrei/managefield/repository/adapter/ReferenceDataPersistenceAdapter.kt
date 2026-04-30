package fr.efrei.managefield.repository.adapter

import fr.efrei.managefield.mapper.ReferenceDataServiceMapper
import fr.efrei.managefield.repository.FieldStatusRepository
import fr.efrei.managefield.repository.ReservationStatusRepository
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import fr.efrei.managefield.service.port.ReferenceDataReadPort
import org.springframework.stereotype.Component

/**
 * Persistence adapter implementing reference data application ports with JPA views.
 */
@Component
class ReferenceDataPersistenceAdapter(
    private val fieldStatusRepository: FieldStatusRepository,
    private val reservationStatusRepository: ReservationStatusRepository,
    private val referenceDataServiceMapper: ReferenceDataServiceMapper
) : ReferenceDataReadPort {
    override fun listFieldStatuses(): List<FieldStatusViewResultDto> {
        return referenceDataServiceMapper.toFieldStatusViews(fieldStatusRepository.findAllByOrderByCodeAsc())
    }

    override fun listReservationStatuses(): List<ReservationStatusViewResultDto> {
        return referenceDataServiceMapper.toReservationStatusViews(reservationStatusRepository.findAllByOrderByCodeAsc())
    }
}
