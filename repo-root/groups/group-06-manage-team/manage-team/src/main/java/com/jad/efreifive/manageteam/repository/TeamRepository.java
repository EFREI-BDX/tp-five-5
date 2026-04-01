package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.TeamEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Log4j2
public class TeamRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<TeamEntity> findAll() {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("TeamEntity.teamGetAll");
            return query.getResultList();
        } catch (Exception e) {
            TeamRepository.log.error("Error retrieving all teams", e);
            throw new RuntimeException("Error retrieving all teams", e);
        }
    }

    public Optional<TeamEntity> findById(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("TeamEntity.teamGetById")
                    .setParameter("_id", id);
            List<TeamEntity> result = query.getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
        } catch (Exception e) {
            TeamRepository.log.error("Error retrieving team by id: {}", id, e);
            throw new RuntimeException("Error retrieving team by id: " + id, e);
        }
    }

    @Transactional
    public void create(String id, String label, String tag, LocalDate creationDate) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("TeamEntity.teamCreate")
                    .setParameter("_id", id)
                    .setParameter("_label", label)
                    .setParameter("_tag", tag)
                    .setParameter("_creationDate", creationDate);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamRepository.log.error("Error creating team", e);
            throw new RuntimeException("Error creating team", e);
        }
    }

    @Transactional
    public void dissolve(String id, LocalDate dissolutionDate) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("TeamEntity.teamDissolve")
                    .setParameter("_id", id)
                    .setParameter("_dissolutionDate", dissolutionDate);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamRepository.log.error("Error dissolving team", e);
            throw new RuntimeException("Error dissolving team", e);
        }
    }

    @Transactional
    public void restore(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("TeamEntity.teamRestore")
                    .setParameter("_id", id);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamRepository.log.error("Error restoring team", e);
            throw new RuntimeException("Error restoring team", e);
        }
    }

    @Transactional
    public void changeName(String id, String newLabel) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("TeamEntity.teamChangeName")
                    .setParameter("_id", id)
                    .setParameter("_newLabel", newLabel);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamRepository.log.error("Error changing team name", e);
            throw new RuntimeException("Error changing team name", e);
        }
    }

    @Transactional
    public void changeTag(String id, String newTag) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("TeamEntity.teamChangeTag")
                    .setParameter("_id", id)
                    .setParameter("_newTag", newTag);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamRepository.log.error("Error changing team tag", e);
            throw new RuntimeException("Error changing team tag", e);
        }
    }
}

