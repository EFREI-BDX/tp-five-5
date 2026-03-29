package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Value object representing a team lifecycle period.
 * <ul>
 *   <li>{@code creationDate}: required, between 2020-01-01 and today.</li>
 *   <li>{@code dissolutionDate}: optional; when present, it must be >= creationDate,
 *       >= 2020-01-01, and <= today.</li>
 * </ul>
 */
public record Period(
        @NotNull LocalDate creationDate,
        LocalDate dissolutionDate
) {

    /** Minimum allowed date for period bounds. */
    private static final LocalDate MIN_DATE = LocalDate.of(2020, 1, 1);

    /**
     * Compact constructor validating all business invariants.
     */
    public Period {
        final LocalDate today = LocalDate.now();

        if (creationDate == null) {
            throw new IllegalArgumentException("Period.creationDate must not be null");
        }
        if (creationDate.isBefore(Period.MIN_DATE)) {
            throw new IllegalArgumentException("Period.creationDate must be >= 2020-01-01");
        }
        if (creationDate.isAfter(today)) {
            throw new IllegalArgumentException("Period.creationDate must be <= today");
        }

        if (dissolutionDate != null) {
            if (dissolutionDate.isBefore(Period.MIN_DATE)) {
                throw new IllegalArgumentException("Period.dissolutionDate must be >= 2020-01-01");
            }
            if (dissolutionDate.isAfter(today)) {
                throw new IllegalArgumentException("Period.dissolutionDate must be <= today");
            }
            if (dissolutionDate.isBefore(creationDate)) {
                throw new IllegalArgumentException("Period.dissolutionDate must be >= creationDate");
            }
        }
    }

    /**
     * Creates an open period with only a creation date.
     */
    public static Period of(final LocalDate creationDate) {
        return new Period(creationDate, null);
    }

    /**
     * Creates a closed period with creation and dissolution dates.
     */
    public static Period of(final LocalDate creationDate, final LocalDate dissolutionDate) {
        return new Period(creationDate, dissolutionDate);
    }

    /**
     * Returns whether the team is currently active.
     */
    public boolean isActive() {
        return this.dissolutionDate == null || this.dissolutionDate.isAfter(LocalDate.now());
    }

}
