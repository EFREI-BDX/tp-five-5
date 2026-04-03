package com.jad.efreifive.managefield.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_fields")
@Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedStoredProcedureQuery(
    name = FieldViewRecord.UPDATE_STATUS_PROCEDURE,
    procedureName = "sp_update_field_status",
    resultClasses = FieldViewRecord.class,
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_field_id", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_status_id", type = String.class)
    }
)
public class FieldViewRecord {

    public static final String UPDATE_STATUS_PROCEDURE = "FieldViewRecord.updateStatus";

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status_id", nullable = false, length = 36)
    private String statusId;
}
