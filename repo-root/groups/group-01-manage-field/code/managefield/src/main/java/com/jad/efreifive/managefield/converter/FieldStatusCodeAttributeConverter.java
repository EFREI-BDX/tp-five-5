package com.jad.efreifive.managefield.converter;

import com.jad.efreifive.managefield.vo.FieldStatusCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FieldStatusCodeAttributeConverter implements AttributeConverter<FieldStatusCode, String> {

    @Override
    public String convertToDatabaseColumn(FieldStatusCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public FieldStatusCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : FieldStatusCode.of(dbData);
    }
}
