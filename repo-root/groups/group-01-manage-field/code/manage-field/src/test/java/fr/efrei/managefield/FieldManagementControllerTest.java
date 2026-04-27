package fr.efrei.managefield;

import fr.efrei.managefield.config.ApiKeyInterceptor;
import fr.efrei.managefield.controller.ApiExceptionHandler;
import fr.efrei.managefield.controller.FieldController;
import fr.efrei.managefield.controller.HealthController;
import fr.efrei.managefield.controller.ReferenceDataController;
import fr.efrei.managefield.controller.ReservationController;
import fr.efrei.managefield.mapper.FieldApiMapperImpl;
import fr.efrei.managefield.mapper.ReferenceDataApiMapperImpl;
import fr.efrei.managefield.mapper.ReservationApiMapperImpl;
import fr.efrei.managefield.service.FieldService;
import fr.efrei.managefield.service.ReferenceDataService;
import fr.efrei.managefield.service.ReservationService;
import fr.efrei.managefield.service.dto.ChangeFieldStatusCommandDto;
import fr.efrei.managefield.service.dto.ChangeReservationStatusCommandDto;
import fr.efrei.managefield.service.dto.CreateReservationCommandDto;
import fr.efrei.managefield.service.dto.FieldStatusViewResultDto;
import fr.efrei.managefield.service.dto.FieldViewResultDto;
import fr.efrei.managefield.service.dto.ListAvailableFieldsCommandDto;
import fr.efrei.managefield.service.dto.ReservationStatusViewResultDto;
import fr.efrei.managefield.service.dto.ReservationViewResultDto;
import fr.efrei.managefield.service.exception.ApplicationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies HTTP mapping, API key handling, and controller-to-service commands.
 */
class FieldManagementControllerTest {
    private static final String API_KEY = "test-key";
    private static final String FIELD_ID = "22222222-2222-4222-8222-222222222222";
    private static final String FIELD_STATUS_ID = "11111111-1111-4111-8111-111111111111";
    private static final String RESERVATION_ID = "44444444-4444-4444-8444-444444444444";
    private static final String RESERVATION_STATUS_ID = "33333333-3333-4333-8333-333333333333";

    private final ReferenceDataService referenceDataService = Mockito.mock(ReferenceDataService.class);
    private final FieldService fieldService = Mockito.mock(FieldService.class);
    private final ReservationService reservationService = Mockito.mock(ReservationService.class);
    private final MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(
            new ReferenceDataController(referenceDataService, new ReferenceDataApiMapperImpl()),
            new FieldController(fieldService, new FieldApiMapperImpl()),
            new ReservationController(reservationService, new ReservationApiMapperImpl()),
            new HealthController()
        )
        .addInterceptors(new ApiKeyInterceptor(API_KEY))
        .setControllerAdvice(new ApiExceptionHandler())
        .build();

    @BeforeEach
    void resetMocks() {
        Mockito.reset(referenceDataService, fieldService, reservationService);
    }

    @Test
    void rejectsMissingApiKey() throws Exception {
        mockMvc.perform(get("/v1/field-statuses"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Unauthorized"))
            .andExpect(jsonPath("$.message").value("invalid API key"));
    }

    @Test
    void allowsHealthWithoutApiKey() throws Exception {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void listsReferenceStatuses() throws Exception {
        when(referenceDataService.listFieldStatuses())
            .thenReturn(List.of(new FieldStatusViewResultDto(FIELD_STATUS_ID, "active", "Active")));
        when(referenceDataService.listReservationStatuses())
            .thenReturn(List.of(new ReservationStatusViewResultDto(RESERVATION_STATUS_ID, "confirmed", "Confirmed")));

        mockMvc.perform(get("/v1/field-statuses").header(ApiKeyInterceptor.API_KEY_HEADER, API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(FIELD_STATUS_ID))
            .andExpect(jsonPath("$[0].code").value("active"))
            .andExpect(jsonPath("$[0].label").value("Active"));

        mockMvc.perform(get("/v1/reservation-statuses").header(ApiKeyInterceptor.API_KEY_HEADER, API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(RESERVATION_STATUS_ID))
            .andExpect(jsonPath("$[0].code").value("confirmed"))
            .andExpect(jsonPath("$[0].label").value("Confirmed"));
    }

    @Test
    void listsAvailableFieldsThroughAServiceCommand() throws Exception {
        when(fieldService.listAvailableFields(new ListAvailableFieldsCommandDto("2026-03-18", "10:00", "12:00")))
            .thenReturn(List.of(new FieldViewResultDto(FIELD_ID, "Field A", FIELD_STATUS_ID)));

        mockMvc.perform(
                get("/v1/fields/available")
                    .header(ApiKeyInterceptor.API_KEY_HEADER, API_KEY)
                    .param("date", "2026-03-18")
                    .param("start_time", "10:00")
                    .param("end_time", "12:00")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(FIELD_ID))
            .andExpect(jsonPath("$[0].name").value("Field A"))
            .andExpect(jsonPath("$[0].status_id").value(FIELD_STATUS_ID));
    }

    @Test
    void changesFieldStatusThroughAServiceCommand() throws Exception {
        when(fieldService.changeStatus(new ChangeFieldStatusCommandDto(FIELD_ID, FIELD_STATUS_ID)))
            .thenReturn(new FieldViewResultDto(FIELD_ID, "Field A", FIELD_STATUS_ID));

        mockMvc.perform(
                patch("/v1/fields/{field_id}/status", FIELD_ID)
                    .header(ApiKeyInterceptor.API_KEY_HEADER, API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"status_id\":\"" + FIELD_STATUS_ID + "\"}")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(FIELD_ID))
            .andExpect(jsonPath("$.status_id").value(FIELD_STATUS_ID));

        verify(fieldService).changeStatus(new ChangeFieldStatusCommandDto(FIELD_ID, FIELD_STATUS_ID));
    }

    @Test
    void createsReservationAndReturnsLocation() throws Exception {
        var command = new CreateReservationCommandDto(
            FIELD_ID,
            RESERVATION_STATUS_ID,
            "2026-03-18",
            "10:00",
            "12:00"
        );
        when(reservationService.create(command))
            .thenReturn(new ReservationViewResultDto(
                RESERVATION_ID,
                FIELD_ID,
                RESERVATION_STATUS_ID,
                "2026-03-18",
                "10:00",
                "12:00"
            ));

        mockMvc.perform(
                post("/v1/fields/{field_id}/reservations", FIELD_ID)
                    .header(ApiKeyInterceptor.API_KEY_HEADER, API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "status_id": "%s",
                          "date": "2026-03-18",
                          "start_time": "10:00",
                          "end_time": "12:00"
                        }
                        """.formatted(RESERVATION_STATUS_ID))
            )
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/v1/fields/" + FIELD_ID + "/reservations/" + RESERVATION_ID))
            .andExpect(jsonPath("$.id").value(RESERVATION_ID))
            .andExpect(jsonPath("$.field_id").value(FIELD_ID))
            .andExpect(jsonPath("$.status_id").value(RESERVATION_STATUS_ID));

        verify(reservationService).create(command);
    }

    @Test
    void changesReservationStatusThroughAServiceCommand() throws Exception {
        var command = new ChangeReservationStatusCommandDto(FIELD_ID, RESERVATION_ID, RESERVATION_STATUS_ID);
        when(reservationService.changeStatus(command))
            .thenReturn(new ReservationViewResultDto(
                RESERVATION_ID,
                FIELD_ID,
                RESERVATION_STATUS_ID,
                "2026-03-18",
                "10:00",
                "12:00"
            ));

        mockMvc.perform(
                patch("/v1/fields/{field_id}/reservations/{reservation_id}/status", FIELD_ID, RESERVATION_ID)
                    .header(ApiKeyInterceptor.API_KEY_HEADER, API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"status_id\":\"" + RESERVATION_STATUS_ID + "\"}")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(RESERVATION_ID))
            .andExpect(jsonPath("$.field_id").value(FIELD_ID));

        verify(reservationService).changeStatus(command);
    }

    @Test
    void translatesApplicationExceptionsToHttpErrors() throws Exception {
        when(fieldService.findById(FIELD_ID)).thenThrow(new ApplicationNotFoundException("field not found"));

        mockMvc.perform(get("/v1/fields/{field_id}", FIELD_ID).header(ApiKeyInterceptor.API_KEY_HEADER, API_KEY))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("NotFound"))
            .andExpect(jsonPath("$.message").value("field not found"));

        verify(fieldService).findById(FIELD_ID);
    }
}
