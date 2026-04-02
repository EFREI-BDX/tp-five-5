package com.jad.efreifive.managefield.entity;

import com.jad.efreifive.managefield.vo.FieldName;
import com.jad.efreifive.managefield.vo.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "field",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_field_name", columnNames = "name")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldEntity {

    @jakarta.persistence.Id
    @Convert(converter = com.jad.efreifive.managefield.converter.IdAttributeConverter.class)
    @Column(name = "id", nullable = false, length = 36)
    private Id id;

    @Convert(converter = com.jad.efreifive.managefield.converter.FieldNameAttributeConverter.class)
    @Column(name = "name", nullable = false, length = 255)
    private FieldName name;

    @Convert(converter = com.jad.efreifive.managefield.converter.IdAttributeConverter.class)
    @Column(name = "status_id", nullable = false, length = 36)
    private Id statusId;
}
