package com.group3.efreifive.recordmatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@SuppressWarnings("DefaultAnnotationParam")
@Entity
@Table(name = "event", schema = "fiverecordmatch")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class EventEntity {

    @Id
    @Column(name = "eventId", columnDefinition = "BINARY(16)")
    private UUID eventId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "nbPlayers", nullable = false)
    private Integer nbPlayers;

}
