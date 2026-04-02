package com.jad.efreifive.managefield.config;

import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReferenceDataCatalog {

    public static final Id FIELD_STATUS_ACTIVE_ID = Id.of("11111111-1111-4111-8111-111111111111");
    public static final Id FIELD_STATUS_MAINTENANCE_ID = Id.of("55555555-5555-4555-8555-555555555555");
    public static final Id FIELD_STATUS_INACTIVE_ID = Id.of("77777777-7777-4777-8777-777777777777");

    public static final Id RESERVATION_STATUS_PENDING_ID = Id.of("33333333-3333-4333-8333-333333333333");
    public static final Id RESERVATION_STATUS_CONFIRMED_ID = Id.of("66666666-6666-4666-8666-666666666666");
    public static final Id RESERVATION_STATUS_CANCELLED_ID = Id.of("99999999-9999-4999-8999-999999999999");

    public static List<FieldStatusEntity> fieldStatuses() {
        return List.of(
            FieldStatusEntity.builder()
                .id(FIELD_STATUS_ACTIVE_ID)
                .code(FieldStatusCode.of(FieldStatusCode.ACTIVE))
                .label("Active")
                .build(),
            FieldStatusEntity.builder()
                .id(FIELD_STATUS_INACTIVE_ID)
                .code(FieldStatusCode.of(FieldStatusCode.INACTIVE))
                .label("Inactive")
                .build(),
            FieldStatusEntity.builder()
                .id(FIELD_STATUS_MAINTENANCE_ID)
                .code(FieldStatusCode.of(FieldStatusCode.MAINTENANCE))
                .label("Maintenance")
                .build()
        );
    }

    public static List<ReservationStatusEntity> reservationStatuses() {
        return List.of(
            ReservationStatusEntity.builder()
                .id(RESERVATION_STATUS_PENDING_ID)
                .code(ReservationStatusCode.of(ReservationStatusCode.PENDING))
                .label("Pending")
                .build(),
            ReservationStatusEntity.builder()
                .id(RESERVATION_STATUS_CONFIRMED_ID)
                .code(ReservationStatusCode.of(ReservationStatusCode.CONFIRMED))
                .label("Confirmed")
                .build(),
            ReservationStatusEntity.builder()
                .id(RESERVATION_STATUS_CANCELLED_ID)
                .code(ReservationStatusCode.of(ReservationStatusCode.CANCELLED))
                .label("Cancelled")
                .build()
        );
    }
}
