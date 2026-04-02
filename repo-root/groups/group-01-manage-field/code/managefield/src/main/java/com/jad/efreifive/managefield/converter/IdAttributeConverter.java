package com.jad.efreifive.managefield.converter;

import com.jad.efreifive.managefield.vo.Id;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IdAttributeConverter implements AttributeConverter<Id, String> {

    @Override
    public String convertToDatabaseColumn(Id attribute) {
        return attribute == null ? null : attribute.asString();
    }

    @Override
    public Id convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Id.of(dbData);
    }
}
