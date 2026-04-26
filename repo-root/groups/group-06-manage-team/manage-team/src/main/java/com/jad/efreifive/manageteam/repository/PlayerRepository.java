package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.PlayerEntity;
import com.jad.efreifive.manageteam.repository.result.PersistenceOperationResult;
import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {
    List<PlayerEntity> findByIdTeam(String idTeam);

    default PersistenceOperationResult create(final Id id, final Name displayName) {
        return PersistenceOperationResult.fromMessage(
                this.playerCreateProc(id.value().toString(), displayName.firstName(), displayName.lastName()));
    }

    @Procedure(procedureName = "fiveteam.playerCreate", outputParameterName = "errorMessage_")
    String playerCreateProc(@Param("_id") String id,
                            @Param("_firstName") String firstName,
                            @Param("_lastName") String lastName);

    default PersistenceOperationResult update(final Id id, final Name displayName) {
        return PersistenceOperationResult.fromMessage(
                this.playerUpdateProc(id.value().toString(), displayName.firstName(), displayName.lastName()));
    }

    @Procedure(procedureName = "fiveteam.playerUpdate", outputParameterName = "errorMessage_")
    String playerUpdateProc(@Param("_id") String id,
                            @Param("_firstName") String firstName,
                            @Param("_lastName") String lastName);

    default PersistenceOperationResult delete(final Id id) {
        return PersistenceOperationResult.fromMessage(this.playerDeleteProc(id.value().toString()));
    }

    @Procedure(procedureName = "fiveteam.playerDelete", outputParameterName = "errorMessage_")
    String playerDeleteProc(@Param("_id") String id);

    default PersistenceOperationResult assignTeam(final Id id, final Id idTeam) {
        return PersistenceOperationResult.fromMessage(
                this.playerAssignTeamProc(id.value().toString(), idTeam.value().toString()));
    }

    @Procedure(procedureName = "fiveteam.playerAssignTeam", outputParameterName = "errorMessage_")
    String playerAssignTeamProc(@Param("_id") String id,
                                @Param("_idTeam") String idTeam);

    default PersistenceOperationResult unassignTeam(final Id id) {
        return PersistenceOperationResult.fromMessage(this.playerUnassignTeamProc(id.value().toString()));
    }

    @Procedure(procedureName = "fiveteam.playerUnassignTeam", outputParameterName = "errorMessage_")
    String playerUnassignTeamProc(@Param("_id") String id);
}
