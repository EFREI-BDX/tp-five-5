package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.TeamEntity;
import com.jad.efreifive.manageteam.repository.result.OperationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TeamRepository extends JpaRepository<TeamEntity, String> {

    @Procedure(procedureName = "fiveteam.teamCreate", outputParameterName = "errorMessage_")
    String teamCreateProc(@Param("_id") String id,
                          @Param("_label") String label,
                          @Param("_tag") String tag,
                          @Param("_creationDate") LocalDate creationDate);

    @Procedure(procedureName = "fiveteam.teamDissolve", outputParameterName = "errorMessage_")
    String teamDissolveProc(@Param("_id") String id,
                            @Param("_dissolutionDate") LocalDate dissolutionDate);

    @Procedure(procedureName = "fiveteam.teamRestore", outputParameterName = "errorMessage_")
    String teamRestoreProc(@Param("_id") String id);

    @Procedure(procedureName = "fiveteam.teamChangeName", outputParameterName = "errorMessage_")
    String teamChangeNameProc(@Param("_id") String id,
                              @Param("_newLabel") String newLabel);

    @Procedure(procedureName = "fiveteam.teamChangeTag", outputParameterName = "errorMessage_")
    String teamChangeTagProc(@Param("_id") String id,
                             @Param("_newTag") String newTag);

    default OperationResult create(String id, String label, String tag, LocalDate creationDate) {
        return OperationResult.fromMessage(this.teamCreateProc(id, label, tag, creationDate));
    }

    default OperationResult dissolve(String id, LocalDate dissolutionDate) {
        return OperationResult.fromMessage(this.teamDissolveProc(id, dissolutionDate));
    }

    default OperationResult restore(String id) {
        return OperationResult.fromMessage(this.teamRestoreProc(id));
    }

    default OperationResult changeName(String id, String newLabel) {
        return OperationResult.fromMessage(this.teamChangeNameProc(id, newLabel));
    }

    default OperationResult changeTag(String id, String newTag) {
        return OperationResult.fromMessage(this.teamChangeTagProc(id, newTag));
    }
}
