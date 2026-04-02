package com.jad.efreifive.managefield.converter;

import com.jad.efreifive.managefield.vo.FieldName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FieldNameAttributeConverter implements AttributeConverter<FieldName, String> {

    @Override
    public String convertToDatabaseColumn(FieldName attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public FieldName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : FieldName.of(dbData);
    }
}
