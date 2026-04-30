package com.group3.efreifive.recordmatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("DefaultAnnotationParam")
@Entity
@Table(name = "match", schema = "fiverecordmatch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {

    @Id
    @Column(name = "matchId", columnDefinition = "BINARY(16)")
    private UUID matchId;

    @Column(name = "team1Id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID team1Id;

    @Column(name = "team2Id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID team2Id;

    @Column(name = "startedAt", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "scheduledDurationMinutes", nullable = false)
    private Integer scheduledDurationMinutes;

}
