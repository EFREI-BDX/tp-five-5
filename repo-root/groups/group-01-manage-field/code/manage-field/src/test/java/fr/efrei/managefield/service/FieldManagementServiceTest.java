package fr.efrei.managefield.service;

import fr.efrei.managefield.entity.FieldEntity;
import fr.efrei.managefield.entity.ReservationEntity;
import fr.efrei.managefield.mapper.FieldServiceMapperImpl;
import fr.efrei.managefield.mapper.ReservationServiceMapperImpl;
import fr.efrei.managefield.repository.FieldRepository;
import fr.efrei.managefield.repository.ReservationRepository;
import fr.efrei.managefield.repository.procedural.CreateReservationProcedureResult;
import fr.efrei.managefield.service.dto.CreateReservationCommandDto;
import fr.efrei.managefield.service.dto.FieldViewResultDto;
import fr.efrei.managefield.service.dto.ListAvailableFieldsCommandDto;
import fr.efrei.managefield.service.exception.ApplicationConflictException;
import fr.efrei.managefield.service.exception.ApplicationValidationException;
import fr.efrei.managefield.service.implementation.FieldServiceImpl;
import fr.efrei.managefield.service.implementation.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Verifies service validation, availability rules, and SQL-code translation.
 */
class FieldManagementServiceTest {
    private static final String FIELD_ID = "22222222-2222-4222-8222-222222222222";
    private static final String OTHER_FIELD_ID = "22222222-2222-4222-8222-222222222223";
    private static final String FIELD_STATUS_ID = "11111111-1111-4111-8111-111111111111";
    private static final String PENDING_RESERVATION_STATUS_ID = "33333333-3333-4333-8333-333333333331";
    private static final String RESERVATION_STATUS_ID = "33333333-3333-4333-8333-333333333333";

    private final FieldRepository fieldRepository = Mockito.mock(FieldRepository.class);
    private final ReservationRepository reservationRepository = Mockito.mock(ReservationRepository.class);
    private final FieldService fieldService = new FieldServiceImpl(
        fieldRepository,
        reservationRepository,
        new FieldServiceMapperImpl()
    );
    private final ReservationService reservationService = new ReservationServiceImpl(
        reservationRepository,
        new ReservationServiceMapperImpl()
    );

    @BeforeEach
    void resetMocks() {
        Mockito.reset(fieldRepository, reservationRepository);
    }

    @Test
    void listAvailableFieldsRemovesOverlappingActiveReservations() {
        when(fieldRepository.findAllByStatusIdOrderByNameAsc(FIELD_STATUS_ID))
            .thenReturn(List.of(field(FIELD_ID, "Field A"), field(OTHER_FIELD_ID, "Field B")));
        when(reservationRepository.findAllByDateAndStatusIdIn(
            LocalDate.parse("2026-03-18"),
            List.of(PENDING_RESERVATION_STATUS_ID, RESERVATION_STATUS_ID)
        ))
            .thenReturn(List.of(reservation(FIELD_ID, "10:30", "11:30")));

        List<FieldViewResultDto> fields = fieldService.listAvailableFields(
            new ListAvailableFieldsCommandDto("2026-03-18", "10:00", "12:00")
        );

        assertThat(fields).extracting(FieldViewResultDto::getId).containsExactly(OTHER_FIELD_ID);
    }

    @Test
    void createReservationRejectsInvalidDurationBeforeCallingRepository() {
        assertThrows(ApplicationValidationException.class, () -> reservationService.create(
            new CreateReservationCommandDto(
                FIELD_ID,
                RESERVATION_STATUS_ID,
                "2026-03-18",
                "10:00",
                "10:30"
            )
        ));

        verifyNoInteractions(reservationRepository);
    }

    @Test
    void createReservationTranslatesSqlConflictCode() {
        CreateReservationProcedureResult procedureResult = Mockito.mock(CreateReservationProcedureResult.class);
        when(procedureResult.getSqlCode()).thenReturn(1003);
        when(procedureResult.getSqlMessage()).thenReturn("reservation overlaps an active reservation");
        when(reservationRepository.createReservation(
            anyString(),
            anyString(),
            anyString(),
            any(LocalDate.class),
            any(LocalTime.class),
            any(LocalTime.class)
        )).thenReturn(procedureResult);

        ApplicationConflictException exception = assertThrows(ApplicationConflictException.class, () -> reservationService.create(
            new CreateReservationCommandDto(
                FIELD_ID,
                RESERVATION_STATUS_ID,
                "2026-03-18",
                "10:00",
                "12:00"
            )
        ));

        assertThat(exception.getMessage()).isEqualTo("reservation overlaps an active reservation");
    }

    private FieldEntity field(String id, String name) {
        FieldEntity entity = new FieldEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setStatusId(FIELD_STATUS_ID);
        entity.setCreatedAt(LocalDateTime.parse("2026-03-18T10:00:00"));
        entity.setUpdatedAt(LocalDateTime.parse("2026-03-18T10:00:00"));
        return entity;
    }

    private ReservationEntity reservation(String fieldId, String startTime, String endTime) {
        ReservationEntity entity = new ReservationEntity();
        entity.setId("44444444-4444-4444-8444-444444444444");
        entity.setFieldId(fieldId);
        entity.setStatusId(RESERVATION_STATUS_ID);
        entity.setDate(LocalDate.parse("2026-03-18"));
        entity.setStartTime(LocalTime.parse(startTime));
        entity.setEndTime(LocalTime.parse(endTime));
        entity.setCreatedAt(LocalDateTime.parse("2026-03-18T10:00:00"));
        entity.setUpdatedAt(LocalDateTime.parse("2026-03-18T10:00:00"));
        return entity;
    }
}
