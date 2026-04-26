package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.TeamEntity;
import com.jad.efreifive.manageteam.repository.result.PersistenceOperationResult;
import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Label;
import com.jad.efreifive.manageteam.valueobject.Period;
import com.jad.efreifive.manageteam.valueobject.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TeamRepository extends JpaRepository<TeamEntity, String> {

    default PersistenceOperationResult create(Id id, Label label, Tag tag, Period period) {
        final String idString = id.value().toString();
        final String labelValue = label.value();
        final String tagValue = tag.value();
        final LocalDate creationDate = period.creationDate();
        return PersistenceOperationResult.fromMessage(
                this.teamCreateProc(idString, labelValue, tagValue, creationDate));
    }

    @Procedure(procedureName = "fiveteam.teamCreate", outputParameterName = "errorMessage_")
    String teamCreateProc(@Param("_id") String id,
                          @Param("_label") String label,
                          @Param("_tag") String tag,
                          @Param("_creationDate") LocalDate creationDate);

    default PersistenceOperationResult dissolve(final Id id, final Period period) {
        return PersistenceOperationResult.fromMessage(
                this.teamDissolveProc(id.value().toString(), period.dissolutionDate()));
    }

    @Procedure(procedureName = "fiveteam.teamDissolve", outputParameterName = "errorMessage_")
    String teamDissolveProc(@Param("_id") String id,
                            @Param("_dissolutionDate") LocalDate dissolutionDate);

    default PersistenceOperationResult restore(final Id id) {
        return PersistenceOperationResult.fromMessage(this.teamRestoreProc(id.value().toString()));
    }

    @Procedure(procedureName = "fiveteam.teamRestore", outputParameterName = "errorMessage_")
    String teamRestoreProc(@Param("_id") String id);

    default PersistenceOperationResult changeName(final Id id, final Label newLabel) {
        return PersistenceOperationResult.fromMessage(this.teamChangeNameProc(id.value().toString(), newLabel.value()));
    }

    @Procedure(procedureName = "fiveteam.teamChangeName", outputParameterName = "errorMessage_")
    String teamChangeNameProc(@Param("_id") String id,
                              @Param("_newLabel") String newLabel);

    default PersistenceOperationResult changeTag(final Id id, final Tag newTag) {
        return PersistenceOperationResult.fromMessage(this.teamChangeTagProc(id.value().toString(), newTag.value()));
    }

    @Procedure(procedureName = "fiveteam.teamChangeTag", outputParameterName = "errorMessage_")
    String teamChangeTagProc(@Param("_id") String id,
                             @Param("_newTag") String newTag);

    default PersistenceOperationResult changeTeamLeader(final Id id, Id newTeamLeaderId) {
        return PersistenceOperationResult.fromMessage(this.changeTeamLeaderProc(id.value().toString(),
                                                                                newTeamLeaderId.value().toString()));
    }

    @Procedure(procedureName = "fiveteam.teamChangeLeader", outputParameterName = "errorMessage_")
    String changeTeamLeaderProc(@Param("_id") String id,
                                @Param("_idLeader") String newTeamLeaderId);
}
