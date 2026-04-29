package com.group3.efreifive.recordmatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("DefaultAnnotationParam")
@Entity
@Table(name = "matchEvent", schema = "fiverecordmatch")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventEntity {

    @Id
    @Column(name = "matchEventId", columnDefinition = "BINARY(16)")
    private UUID matchEventId;

    @Column(name = "matchId", nullable = false, columnDefinition = "BINARY(16)")
    private UUID matchId;

    @Column(name = "eventId", nullable = false, columnDefinition = "BINARY(16)")
    private UUID eventId;

    @Column(name = "player1Id", columnDefinition = "BINARY(16)")
    private UUID player1Id;

    @Column(name = "player2Id", columnDefinition = "BINARY(16)")
    private UUID player2Id;

    @Column(name = "occuredAt", nullable = false)
    private LocalDateTime occuredAt;

    @Column(name = "succeeded", nullable = false)
    private Boolean succeeded;

    @Column(name = "teamId", columnDefinition = "BINARY(16)")
    private UUID teamId;

    @Column(name = "referenceEventId", columnDefinition = "BINARY(16)")
    private UUID referenceEventId;

}
