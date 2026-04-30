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
@Table(name = "player", schema = "fiverecordmatch")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PlayerEntity {

    @Id
    @Column(name = "playerId", columnDefinition = "BINARY(16)")
    private UUID playerId;

    @Column(name = "teamId", nullable = false, columnDefinition = "BINARY(16)")
    private UUID teamId;

}
