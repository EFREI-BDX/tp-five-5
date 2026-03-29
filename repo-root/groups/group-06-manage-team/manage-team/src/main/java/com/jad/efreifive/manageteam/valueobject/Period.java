package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Value-object représentant la période de vie d'une équipe.
 * <ul>
 *   <li>{@code creationDate} : obligatoire, comprise entre le 2020-01-01 et aujourd'hui.</li>
 *   <li>{@code dissolutionDate} : optionnelle ; si renseignée, elle doit être >= creationDate,
 *       >= 2020-01-01 et <= aujourd'hui.</li>
 * </ul>
 */
public record Period(
        @NotNull LocalDate creationDate,
        LocalDate dissolutionDate
) {

    /** Date minimale autorisée pour toutes les dates de la période. */
    private static final LocalDate MIN_DATE = LocalDate.of(2020, 1, 1);

    /**
     * Constructeur compact : valide l'ensemble des invariants métier.
     */
    public Period {
        final LocalDate today = LocalDate.now();

        if (creationDate == null) {
            throw new IllegalArgumentException("Period.creationDate must not be null");
        }
        if (creationDate.isBefore(MIN_DATE)) {
            throw new IllegalArgumentException("Period.creationDate must be >= 2020-01-01");
        }
        if (creationDate.isAfter(today)) {
            throw new IllegalArgumentException("Period.creationDate must be <= today");
        }

        if (dissolutionDate != null) {
            if (dissolutionDate.isBefore(MIN_DATE)) {
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
     * Fabrique une période de création uniquement (équipe non encore dissoute).
     */
    public static Period of(final LocalDate creationDate) {
        return new Period(creationDate, null);
    }

    /**
     * Fabrique une période complète avec date de dissolution.
     */
    public static Period of(final LocalDate creationDate, final LocalDate dissolutionDate) {
        return new Period(creationDate, dissolutionDate);
    }

    /**
     * Indique si l'équipe est actuellement active (pas encore dissoute).
     */
    public boolean isActive() {
        return dissolutionDate == null || dissolutionDate.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return dissolutionDate == null
                ? "from " + creationDate
                : "from " + creationDate + " to " + dissolutionDate;
    }
}

