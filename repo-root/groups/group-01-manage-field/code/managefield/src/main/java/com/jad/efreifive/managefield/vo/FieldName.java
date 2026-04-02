package com.jad.efreifive.managefield.vo;

import com.jad.efreifive.managefield.exception.InvalidValueObjectException;

import java.io.Serializable;

public record FieldName(String value) implements Serializable {

    public FieldName {
        if (value == null || value.isBlank()) {
            throw new InvalidValueObjectException("field name must not be blank.");
        }
        value = value.trim();
    }

    public static FieldName of(String rawValue) {
        return new FieldName(rawValue);
    }
}
