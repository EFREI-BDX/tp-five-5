package com.jad.efreifive.manageteam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "player", schema = "fiveteam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerGetAll",
                procedureName = "fiveteam.playerGetAll"
        ),
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerGetById",
                procedureName = "fiveteam.playerGetById",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerGetByTeamId",
                procedureName = "fiveteam.playerGetByTeamId",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_idTeam", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerCreate",
                procedureName = "fiveteam.playerCreate",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_displayName", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerUpdate",
                procedureName = "fiveteam.playerUpdate",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_displayName", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerDelete",
                procedureName = "fiveteam.playerDelete",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerAssignTeam",
                procedureName = "fiveteam.playerAssignTeam",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_idTeam", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "PlayerEntity.playerUnassignTeam",
                procedureName = "fiveteam.playerUnassignTeam",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "_id", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "errorMessage_", type = String.class)
                }
        )
})
public class PlayerEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "displayName", nullable = false, length = 255)
    private String displayName;

    @Column(name = "idTeam", length = 36)
    private String idTeam;

}
