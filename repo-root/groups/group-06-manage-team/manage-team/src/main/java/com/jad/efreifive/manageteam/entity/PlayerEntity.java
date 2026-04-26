package com.jad.efreifive.manageteam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@SuppressWarnings("DefaultAnnotationParam")
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

    @Column(name = "firstName", nullable = false, length = 255)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 255)
    private String lastName;

    @Column(name = "idTeam", length = 36)
    private String idTeam;

}
