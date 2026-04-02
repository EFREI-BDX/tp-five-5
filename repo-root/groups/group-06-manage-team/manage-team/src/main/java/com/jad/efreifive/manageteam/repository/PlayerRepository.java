package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.PlayerEntity;
import com.jad.efreifive.manageteam.repository.result.OperationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {
    List<PlayerEntity> findByIdTeam(String idTeam);

    @Procedure(procedureName = "fiveteam.playerCreate", outputParameterName = "errorMessage_")
    String playerCreateProc(@Param("_id") String id,
                            @Param("_displayName") String displayName);

    @Procedure(procedureName = "fiveteam.playerUpdate", outputParameterName = "errorMessage_")
    String playerUpdateProc(@Param("_id") String id,
                            @Param("_displayName") String displayName);

    @Procedure(procedureName = "fiveteam.playerDelete", outputParameterName = "errorMessage_")
    String playerDeleteProc(@Param("_id") String id);

    @Procedure(procedureName = "fiveteam.playerAssignTeam", outputParameterName = "errorMessage_")
    String playerAssignTeamProc(@Param("_id") String id,
                                @Param("_idTeam") String idTeam);

    @Procedure(procedureName = "fiveteam.playerUnassignTeam", outputParameterName = "errorMessage_")
    String playerUnassignTeamProc(@Param("_id") String id);

    default OperationResult create(String id, String displayName) {
        return OperationResult.fromMessage(this.playerCreateProc(id, displayName));
    }

    default OperationResult update(String id, String displayName) {
        return OperationResult.fromMessage(this.playerUpdateProc(id, displayName));
    }

    default OperationResult delete(String id) {
        return OperationResult.fromMessage(this.playerDeleteProc(id));
    }

    default OperationResult assignTeam(String id, String idTeam) {
        return OperationResult.fromMessage(this.playerAssignTeamProc(id, idTeam));
    }

    default OperationResult unassignTeam(String id) {
        return OperationResult.fromMessage(this.playerUnassignTeamProc(id));
    }
}
