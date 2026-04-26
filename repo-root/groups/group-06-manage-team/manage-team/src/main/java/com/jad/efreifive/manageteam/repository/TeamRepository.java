package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.TeamEntity;
import com.jad.efreifive.manageteam.repository.result.PersistenceOperationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TeamRepository extends JpaRepository<TeamEntity, String> {

    default PersistenceOperationResult create(String id, String label, String tag, LocalDate creationDate) {
        return PersistenceOperationResult.fromMessage(this.teamCreateProc(id, label, tag, creationDate));
    }

    @Procedure(procedureName = "fiveteam.teamCreate", outputParameterName = "errorMessage_")
    String teamCreateProc(@Param("_id") String id,
                          @Param("_label") String label,
                          @Param("_tag") String tag,
                          @Param("_creationDate") LocalDate creationDate);

    default PersistenceOperationResult dissolve(String id, LocalDate dissolutionDate) {
        return PersistenceOperationResult.fromMessage(this.teamDissolveProc(id, dissolutionDate));
    }

    @Procedure(procedureName = "fiveteam.teamDissolve", outputParameterName = "errorMessage_")
    String teamDissolveProc(@Param("_id") String id,
                            @Param("_dissolutionDate") LocalDate dissolutionDate);

    default PersistenceOperationResult restore(String id) {
        return PersistenceOperationResult.fromMessage(this.teamRestoreProc(id));
    }

    @Procedure(procedureName = "fiveteam.teamRestore", outputParameterName = "errorMessage_")
    String teamRestoreProc(@Param("_id") String id);

    default PersistenceOperationResult changeName(String id, String newLabel) {
        return PersistenceOperationResult.fromMessage(this.teamChangeNameProc(id, newLabel));
    }

    @Procedure(procedureName = "fiveteam.teamChangeName", outputParameterName = "errorMessage_")
    String teamChangeNameProc(@Param("_id") String id,
                              @Param("_newLabel") String newLabel);

    default PersistenceOperationResult changeTag(String id, String newTag) {
        return PersistenceOperationResult.fromMessage(this.teamChangeTagProc(id, newTag));
    }

    @Procedure(procedureName = "fiveteam.teamChangeTag", outputParameterName = "errorMessage_")
    String teamChangeTagProc(@Param("_id") String id,
                             @Param("_newTag") String newTag);
}
