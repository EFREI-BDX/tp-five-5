package com.jad.efreifive.manageteam.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Value object (enum) representing a team state.
 * <ul>
 *   <li>{@link #ACTIVE} - "Active"</li>
 *   <li>{@link #INCOMPLETE} - "Incomplète"</li>
 *   <li>{@link #DISSOLVED} - "Dissoute"</li>
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
     * Returns the business value used for JSON output and display.
     */
    @JsonValue
    public String getValue() {
        return this.value;
    }

    /**
     * Resolves a {@link TeamState} from its business value (case-insensitive).
     *
     * @param value business value ("Active", "Incomplète", or "Dissoute")
     * @return the matching {@link TeamState}
     * @throws IllegalArgumentException when no matching state is found
     */
    @JsonCreator
    public static TeamState fromValue(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TeamState value must not be blank");
        }

        return Arrays.stream(TeamState.values())
                .filter(state -> state.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown TeamState value: \"" + value + "\". Allowed values: "
                                + Arrays.stream(TeamState.values()).map(TeamState::getValue).toList()
                ));
    }

    @Override
    public String toString() {
        return this.value;
    }
}
