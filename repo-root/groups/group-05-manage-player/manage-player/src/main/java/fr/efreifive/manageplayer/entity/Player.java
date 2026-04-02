package fr.player.valueobject;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import fr.player.valueobject.statistics.PlayerStatistics;

/**
 * Agrégat principal - Représente un joueur dans le système ERP du centre de foot à 5.
 */
public class Player {
    private final Id id;
    private final FirstName firstName;
    private final LastName lastName;
    private final Email email;
    private final Phone phone;
    private final Gender gender;
    private final BirthDate birthDate;
    private final Height height;
    private final Set<TeamId> teamIds;
    private final PlayerStatistics statistics;
    private final Status status;
    private final DateTime createdAt;
    private final DateTime updatedAt;

    public Player(Id id, FirstName firstName, LastName lastName, Email email, Phone phone,
                 Gender gender, BirthDate birthDate, Height height, Set<TeamId> teamIds,
                 PlayerStatistics statistics, Status status, DateTime createdAt, DateTime updatedAt) {
        
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (firstName == null) {
            throw new IllegalArgumentException("FirstName must not be null");
        }
        if (lastName == null) {
            throw new IllegalArgumentException("LastName must not be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email must not be null");
        }
        if (phone == null) {
            throw new IllegalArgumentException("Phone must not be null");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender must not be null");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("BirthDate must not be null");
        }
        if (height == null) {
            throw new IllegalArgumentException("Height must not be null");
        }
        if (statistics == null) {
            throw new IllegalArgumentException("PlayerStatistics must not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt must not be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("UpdatedAt must not be null");
        }
        
        // Validate team IDs don't have duplicates
        if (teamIds != null && !teamIds.isEmpty()) {
            if (teamIds.size() != new HashSet<>(teamIds).size()) {
                throw new IllegalArgumentException("TeamIds must not contain duplicates");
            }
        }
        
        // Validate updatedAt >= createdAt
        if (updatedAt.getAsInstant().isBefore(createdAt.getAsInstant())) {
            throw new IllegalArgumentException("UpdatedAt must be greater than or equal to CreatedAt");
        }
        
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.birthDate = birthDate;
        this.height = height;
        this.teamIds = teamIds != null ? Collections.unmodifiableSet(new HashSet<>(teamIds)) : Collections.emptySet();
        this.statistics = statistics;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Id getId() {
        return id;
    }

    public FirstName getFirstName() {
        return firstName;
    }

    public LastName getLastName() {
        return lastName;
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public Height getHeight() {
        return height;
    }

    public Set<TeamId> getTeamIds() {
        return teamIds;
    }

    public PlayerStatistics getStatistics() {
        return statistics;
    }

    public Status getStatus() {
        return status;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }
}
