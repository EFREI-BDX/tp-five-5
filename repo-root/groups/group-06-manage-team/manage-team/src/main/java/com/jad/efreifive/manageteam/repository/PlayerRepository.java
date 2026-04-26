package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.PlayerEntity;
import com.jad.efreifive.manageteam.repository.result.PersistenceOperationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {
    List<PlayerEntity> findByIdTeam(String idTeam);

    default PersistenceOperationResult create(String id, String displayName) {
        return PersistenceOperationResult.fromMessage(this.playerCreateProc(id, displayName));
    }

    @Procedure(procedureName = "fiveteam.playerCreate", outputParameterName = "errorMessage_")
    String playerCreateProc(@Param("_id") String id,
                            @Param("_displayName") String displayName);

    default PersistenceOperationResult update(String id, String displayName) {
        return PersistenceOperationResult.fromMessage(this.playerUpdateProc(id, displayName));
    }

    @Procedure(procedureName = "fiveteam.playerUpdate", outputParameterName = "errorMessage_")
    String playerUpdateProc(@Param("_id") String id,
                            @Param("_displayName") String displayName);

    default PersistenceOperationResult delete(String id) {
        return PersistenceOperationResult.fromMessage(this.playerDeleteProc(id));
    }

    @Procedure(procedureName = "fiveteam.playerDelete", outputParameterName = "errorMessage_")
    String playerDeleteProc(@Param("_id") String id);

    default PersistenceOperationResult assignTeam(String id, String idTeam) {
        return PersistenceOperationResult.fromMessage(this.playerAssignTeamProc(id, idTeam));
    }

    @Procedure(procedureName = "fiveteam.playerAssignTeam", outputParameterName = "errorMessage_")
    String playerAssignTeamProc(@Param("_id") String id,
                                @Param("_idTeam") String idTeam);

    default PersistenceOperationResult unassignTeam(String id) {
        return PersistenceOperationResult.fromMessage(this.playerUnassignTeamProc(id));
    }

    @Procedure(procedureName = "fiveteam.playerUnassignTeam", outputParameterName = "errorMessage_")
    String playerUnassignTeamProc(@Param("_id") String id);
}
