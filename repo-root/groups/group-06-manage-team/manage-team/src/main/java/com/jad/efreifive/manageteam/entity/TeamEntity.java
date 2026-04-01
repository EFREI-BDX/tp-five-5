package com.jad.efreifive.manageteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "team", schema = "fiveteam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "TeamEntity.teamGetAll",
                procedureName = "fiveteam.teamGetAll",
                resultSetMapping = "TeamResultSet"
        ),
        @NamedStoredProcedureQuery(
                name = "TeamEntity.teamGetById",
                procedureName = "fiveteam.teamGetById",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class)
                },
                resultSetMapping = "TeamResultSet"
        ),
        @NamedStoredProcedureQuery(
                name = "TeamEntity.teamCreate",
                procedureName = "fiveteam.teamCreate",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_label", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_tag", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_creationDate", type = LocalDate.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "TeamEntity.teamDissolve",
                procedureName = "fiveteam.teamDissolve",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_dissolutionDate", type = LocalDate.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "TeamEntity.teamRestore",
                procedureName = "fiveteam.teamRestore",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "TeamEntity.teamChangeName",
                procedureName = "fiveteam.teamChangeName",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_newLabel", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "TeamEntity.teamChangeTag",
                procedureName = "fiveteam.teamChangeTag",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_newTag", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        )
})
@SqlResultSetMapping(
        name = "TeamResultSet",
        classes = @ConstructorResult(
                targetClass = TeamEntity.class,
                columns = {
                        @ColumnResult(name = "id", type = String.class),
                        @ColumnResult(name = "label", type = String.class),
                        @ColumnResult(name = "tag", type = String.class),
                        @ColumnResult(name = "creationDate", type = LocalDate.class),
                        @ColumnResult(name = "dissolutionDate", type = LocalDate.class),
                        @ColumnResult(name = "idTeamLeader", type = String.class),
                        @ColumnResult(name = "state", type = String.class)
                }
        )
)
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

