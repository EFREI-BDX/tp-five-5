package com.jad.efreifive.manageteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "PlayerView", schema = "fiveteam")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "displayName", nullable = false, length = 255)
    private String displayName;

    @Column(name = "idTeam", length = 36)
    private String idTeam;

}
