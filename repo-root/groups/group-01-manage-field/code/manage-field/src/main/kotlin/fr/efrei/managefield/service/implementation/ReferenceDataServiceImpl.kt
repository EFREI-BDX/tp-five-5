package fr.efrei.managefield.service.implementation

import fr.efrei.managefield.mapper.ReferenceDataServiceMapper
import fr.efrei.managefield.repository.FieldStatusRepository
import fr.efrei.managefield.repository.ReservationStatusRepository
import fr.efrei.managefield.service.ReferenceDataService
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Default [ReferenceDataService] implementation.
 */
@Service
class ReferenceDataServiceImpl(
    private val fieldStatusRepository: FieldStatusRepository,
    private val reservationStatusRepository: ReservationStatusRepository,
    private val referenceDataServiceMapper: ReferenceDataServiceMapper
) : ReferenceDataService {
    @Transactional(readOnly = true)
    override fun listFieldStatuses(): List<FieldStatusViewResultDto> {
        return referenceDataServiceMapper.toFieldStatusViews(fieldStatusRepository.findAllByOrderByCodeAsc())
    }

    @Transactional(readOnly = true)
    override fun listReservationStatuses(): List<ReservationStatusViewResultDto> {
        return referenceDataServiceMapper.toReservationStatusViews(
            reservationStatusRepository.findAllByOrderByCodeAsc()
        )
    }
}
