package com.jad.efreifive.manageteam.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
@Log4j2
public class TeamCommandRepositoryImpl implements TeamCommandRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(String id, String label, String tag, LocalDate creationDate) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.teamCreate")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_label", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_tag", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_creationDate", LocalDate.class,
                                                      jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
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
            TeamCommandRepositoryImpl.log.error("Error creating team", e);
            throw new RuntimeException("Error creating team", e);
        }
    }

    @Override
    @Transactional
    public void dissolve(String id, LocalDate dissolutionDate) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.teamDissolve")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_dissolutionDate", LocalDate.class,
                                                      jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
                    .setParameter("_id", id)
                    .setParameter("_dissolutionDate", dissolutionDate);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamCommandRepositoryImpl.log.error("Error dissolving team", e);
            throw new RuntimeException("Error dissolving team", e);
        }
    }

    @Override
    @Transactional
    public void restore(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.teamRestore")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
                    .setParameter("_id", id);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamCommandRepositoryImpl.log.error("Error restoring team", e);
            throw new RuntimeException("Error restoring team", e);
        }
    }

    @Override
    @Transactional
    public void changeName(String id, String newLabel) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.teamChangeName")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_newLabel", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
                    .setParameter("_id", id)
                    .setParameter("_newLabel", newLabel);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamCommandRepositoryImpl.log.error("Error changing team name", e);
            throw new RuntimeException("Error changing team name", e);
        }
    }

    @Override
    @Transactional
    public void changeTag(String id, String newTag) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.teamChangeTag")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_newTag", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
                    .setParameter("_id", id)
                    .setParameter("_newTag", newTag);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            TeamCommandRepositoryImpl.log.error("Error changing team tag", e);
            throw new RuntimeException("Error changing team tag", e);
        }
    }
}
