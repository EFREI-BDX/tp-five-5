package com.jad.efreifive.manageteam.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Value-object (enum) représentant l'état d'une équipe.
 * <ul>
 *   <li>{@link #ACTIVE} → "Active"</li>
 *   <li>{@link #INCOMPLETE} → "Incomplète"</li>
 *   <li>{@link #DISSOLVED} → "Dissoute"</li>
 * </ul>
 */
public enum TeamState {

    ACTIVE("Active"),
    INCOMPLETE("Incomplète"),
    DISSOLVED("Dissoute");

    private final String value;

    TeamState(final String value) {
        this.value = value;
    }

    /**
     * Retourne la valeur métier affichable (ex. "Active").
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Recherche un {@link TeamState} à partir de sa valeur métier (insensible à la casse).
     *
     * @param value la valeur métier ("Active", "Incomplète" ou "Dissoute")
     * @return le {@link TeamState} correspondant
     * @throws IllegalArgumentException si la valeur ne correspond à aucun état connu
     */
    @JsonCreator
    public static TeamState fromValue(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TeamState value must not be blank");
        }
        return Arrays.stream(values())
                .filter(state -> state.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown TeamState value: \"" + value + "\". Allowed values: "
                                + Arrays.stream(values()).map(TeamState::getValue).toList()
                ));
    }

    @Override
    public String toString() {
        return value;
    }
}

