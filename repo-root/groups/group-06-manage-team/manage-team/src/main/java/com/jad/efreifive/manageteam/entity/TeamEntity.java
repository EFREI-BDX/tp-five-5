package com.jad.efreifive.manageteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "TeamView", schema = "fiveteam")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "label", nullable = false, length = 255)
    private String label;

    @Column(name = "tag", nullable = false, length = 3)
    private String tag;

    @Column(name = "creationDate", nullable = false)
    private LocalDate creationDate;

    @Column(name = "dissolutionDate")
    private LocalDate dissolutionDate;

    @Column(name = "idTeamLeader", length = 36)
    private String idTeamLeader;

    @Column(name = "state", nullable = false, length = 20)
    private String state;

}
