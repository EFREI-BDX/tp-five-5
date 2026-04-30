package fr.efrei.managefield.service.implementation

import fr.efrei.managefield.service.ReferenceDataService
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import fr.efrei.managefield.service.port.ReferenceDataReadPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Default [ReferenceDataService] implementation.
 */
@Service
class ReferenceDataServiceImpl(
    private val referenceDataReadPort: ReferenceDataReadPort
) : ReferenceDataService {
    @Transactional(readOnly = true)
    override fun listFieldStatuses(): List<FieldStatusViewResultDto> {
        return referenceDataReadPort.listFieldStatuses()
    }

    @Transactional(readOnly = true)
    override fun listReservationStatuses(): List<ReservationStatusViewResultDto> {
        return referenceDataReadPort.listReservationStatuses()
    }
}
