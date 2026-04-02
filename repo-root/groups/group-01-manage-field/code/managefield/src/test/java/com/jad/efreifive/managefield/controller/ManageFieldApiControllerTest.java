package com.jad.efreifive.managefield.controller;

import com.jad.efreifive.managefield.config.SecurityConfig;
import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.entity.ReservationEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.exception.ConflictException;
import com.jad.efreifive.managefield.exception.GlobalExceptionHandler;
import com.jad.efreifive.managefield.exception.NotFoundException;
import com.jad.efreifive.managefield.mapper.FieldMapperImpl;
import com.jad.efreifive.managefield.mapper.ReferenceDataMapperImpl;
import com.jad.efreifive.managefield.mapper.ReservationMapperImpl;
import com.jad.efreifive.managefield.mapper.ValueObjectMapperImpl;
import com.jad.efreifive.managefield.security.ApiKeyAuthenticationFilter;
import com.jad.efreifive.managefield.security.ApiKeyProperties;
import com.jad.efreifive.managefield.service.FieldService;
import com.jad.efreifive.managefield.service.ReferenceDataService;
import com.jad.efreifive.managefield.service.ReservationService;
import com.jad.efreifive.managefield.vo.FieldName;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import com.jad.efreifive.managefield.vo.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
    ReferenceDataController.class,
    FieldController.class,
    ReservationController.class,
    HealthController.class
})
@Import({
    SecurityConfig.class,
    ApiKeyAuthenticationFilter.class,
    GlobalExceptionHandler.class,
    ValueObjectMapperImpl.class,
    FieldMapperImpl.class,
    ReferenceDataMapperImpl.class,
    ReservationMapperImpl.class
})
@TestPropertySource(properties = "managefield.security.api-key=test-api-key")
class ManageFieldApiControllerTest {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String API_KEY = "test-api-key";
    private static final Id ACTIVE_FIELD_STATUS_ID = Id.of("11111111-1111-4111-8111-111111111111");
    private static final Id PENDING_RESERVATION_STATUS_ID = Id.of("33333333-3333-4333-8333-333333333333");
    private static final Id FIELD_ID = Id.of("22222222-2222-4222-8222-222222222222");
    private static final Id RESERVATION_ID = Id.of("44444444-4444-4444-8444-444444444444");
    private static final Id MAINTENANCE_FIELD_STATUS_ID = Id.of("55555555-5555-4555-8555-555555555555");
    private static final Id CONFIRMED_RESERVATION_STATUS_ID = Id.of("66666666-6666-4666-8666-666666666666");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReferenceDataService referenceDataService;

    @MockitoBean
    private FieldService fieldService;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private ApiKeyProperties apiKeyProperties;

    @BeforeEach
    void setUpSecurity() {
        when(apiKeyProperties.matches(API_KEY)).thenReturn(true);
        when(apiKeyProperties.matches(null)).thenReturn(false);
        when(apiKeyProperties.matches("invalid-api-key")).thenReturn(false);
    }

    @Test
    void shouldExposeHealthWithoutApiKey() throws Exception {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shouldRejectUnauthorizedRequestWithoutApiKey() throws Exception {
        mockMvc.perform(get("/v1/field-statuses"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Unauthorized"))
            .andExpect(jsonPath("$.message").value("Missing or invalid API key."));
    }

    @Test
    void shouldListFieldStatuses() throws Exception {
        when(referenceDataService.listFieldStatuses()).thenReturn(List.of(activeFieldStatus()));

        mockMvc.perform(get("/v1/field-statuses").header(API_KEY_HEADER, API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ACTIVE_FIELD_STATUS_ID.asString()))
            .andExpect(jsonPath("$[0].code").value("active"))
            .andExpect(jsonPath("$[0].label").value("Active"));
    }

    @Test
    void shouldListReservationStatuses() throws Exception {
        when(referenceDataService.listReservationStatuses()).thenReturn(List.of(pendingReservationStatus()));

        mockMvc.perform(get("/v1/reservation-statuses").header(API_KEY_HEADER, API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(PENDING_RESERVATION_STATUS_ID.asString()))
            .andExpect(jsonPath("$[0].code").value("pending"))
            .andExpect(jsonPath("$[0].label").value("Pending"));
    }

    @Test
    void shouldListAvailableFields() throws Exception {
        when(fieldService.listAvailableFields(any(TimeSlot.class))).thenReturn(List.of(activeField()));

        mockMvc.perform(
                get("/v1/fields/available")
                    .header(API_KEY_HEADER, API_KEY)
                    .param("date", "2026-03-18")
                    .param("start_time", "10:00")
                    .param("end_time", "11:30")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(FIELD_ID.asString()))
            .andExpect(jsonPath("$[0].name").value("Field A"))
            .andExpect(jsonPath("$[0].status_id").value(ACTIVE_FIELD_STATUS_ID.asString()));
    }

    @Test
    void shouldReturn400ForInvalidAvailabilitySlot() throws Exception {
        mockMvc.perform(
                get("/v1/fields/available")
                    .header(API_KEY_HEADER, API_KEY)
                    .param("date", "2026-03-18")
                    .param("start_time", "10:00")
                    .param("end_time", "10:30")
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void shouldGetField() throws Exception {
        when(fieldService.getField(FIELD_ID)).thenReturn(activeField());

        mockMvc.perform(get("/v1/fields/{field_id}", FIELD_ID.asString()).header(API_KEY_HEADER, API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(FIELD_ID.asString()))
            .andExpect(jsonPath("$.name").value("Field A"))
            .andExpect(jsonPath("$.status_id").value(ACTIVE_FIELD_STATUS_ID.asString()));
    }

    @Test
    void shouldReturn404WhenFieldDoesNotExist() throws Exception {
        when(fieldService.getField(FIELD_ID)).thenThrow(new NotFoundException("Field not found."));

        mockMvc.perform(get("/v1/fields/{field_id}", FIELD_ID.asString()).header(API_KEY_HEADER, API_KEY))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("NotFound"));
    }

    @Test
    void shouldUpdateFieldStatus() throws Exception {
        when(fieldService.updateFieldStatus(FIELD_ID, MAINTENANCE_FIELD_STATUS_ID)).thenReturn(maintenanceField());

        mockMvc.perform(
                patch("/v1/fields/{field_id}/status", FIELD_ID.asString())
                    .header(API_KEY_HEADER, API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "status_id": "55555555-5555-4555-8555-555555555555"
                        }
                        """)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status_id").value(MAINTENANCE_FIELD_STATUS_ID.asString()));
    }

    @Test
    void shouldCreateReservation() throws Exception {
        when(reservationService.createReservation(any(Id.class), any(Id.class), any(TimeSlot.class))).thenReturn(pendingReservation());

        mockMvc.perform(
                post("/v1/fields/{field_id}/reservations", FIELD_ID.asString())
                    .header(API_KEY_HEADER, API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "status_id": "33333333-3333-4333-8333-333333333333",
                          "date": "2026-03-18",
                          "start_time": "10:00",
                          "end_time": "11:30"
                        }
                        """)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(RESERVATION_ID.asString()))
            .andExpect(jsonPath("$.field_id").value(FIELD_ID.asString()))
            .andExpect(jsonPath("$.status_id").value(PENDING_RESERVATION_STATUS_ID.asString()))
            .andExpect(jsonPath("$.date").value("2026-03-18"))
            .andExpect(jsonPath("$.start_time").value("10:00"))
            .andExpect(jsonPath("$.end_time").value("11:30"));
    }

    @Test
    void shouldReturn409WhenReservationConflicts() throws Exception {
        when(reservationService.createReservation(any(Id.class), any(Id.class), any(TimeSlot.class)))
            .thenThrow(new ConflictException("Another active reservation already overlaps this time slot."));

        mockMvc.perform(
                post("/v1/fields/{field_id}/reservations", FIELD_ID.asString())
                    .header(API_KEY_HEADER, API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "status_id": "33333333-3333-4333-8333-333333333333",
                          "date": "2026-03-18",
                          "start_time": "10:00",
                          "end_time": "11:30"
                        }
                        """)
            )
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("ConflictError"));
    }

    @Test
    void shouldUpdateReservationStatus() throws Exception {
        when(reservationService.updateReservationStatus(FIELD_ID, RESERVATION_ID, CONFIRMED_RESERVATION_STATUS_ID))
            .thenReturn(confirmedReservation());

        mockMvc.perform(
                patch("/v1/fields/{field_id}/reservations/{reservation_id}/status", FIELD_ID.asString(), RESERVATION_ID.asString())
                    .header(API_KEY_HEADER, API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "status_id": "66666666-6666-4666-8666-666666666666"
                        }
                        """)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status_id").value(CONFIRMED_RESERVATION_STATUS_ID.asString()));
    }

    private FieldStatusEntity activeFieldStatus() {
        return FieldStatusEntity.builder()
            .id(ACTIVE_FIELD_STATUS_ID)
            .code(FieldStatusCode.of("active"))
            .label("Active")
            .build();
    }

    private ReservationStatusEntity pendingReservationStatus() {
        return ReservationStatusEntity.builder()
            .id(PENDING_RESERVATION_STATUS_ID)
            .code(ReservationStatusCode.of("pending"))
            .label("Pending")
            .build();
    }

    private FieldEntity activeField() {
        return FieldEntity.builder()
            .id(FIELD_ID)
            .name(FieldName.of("Field A"))
            .statusId(ACTIVE_FIELD_STATUS_ID)
            .build();
    }

    private FieldEntity maintenanceField() {
        return FieldEntity.builder()
            .id(FIELD_ID)
            .name(FieldName.of("Field A"))
            .statusId(MAINTENANCE_FIELD_STATUS_ID)
            .build();
    }

    private ReservationEntity pendingReservation() {
        return ReservationEntity.builder()
            .id(RESERVATION_ID)
            .fieldId(FIELD_ID)
            .statusId(PENDING_RESERVATION_STATUS_ID)
            .timeSlot(TimeSlot.of("2026-03-18", "10:00", "11:30"))
            .build();
    }

    private ReservationEntity confirmedReservation() {
        return ReservationEntity.builder()
            .id(RESERVATION_ID)
            .fieldId(FIELD_ID)
            .statusId(CONFIRMED_RESERVATION_STATUS_ID)
            .timeSlot(TimeSlot.of("2026-03-18", "10:00", "11:30"))
            .build();
    }
}
