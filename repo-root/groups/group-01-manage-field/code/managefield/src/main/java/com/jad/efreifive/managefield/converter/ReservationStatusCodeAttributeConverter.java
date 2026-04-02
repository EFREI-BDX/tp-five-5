package com.jad.efreifive.managefield.converter;

import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReservationStatusCodeAttributeConverter implements AttributeConverter<ReservationStatusCode, String> {

    @Override
    public String convertToDatabaseColumn(ReservationStatusCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public ReservationStatusCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ReservationStatusCode.of(dbData);
    }
}
