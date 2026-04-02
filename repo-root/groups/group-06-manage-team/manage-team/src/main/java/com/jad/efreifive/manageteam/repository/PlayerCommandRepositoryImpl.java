package com.jad.efreifive.manageteam.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Log4j2
public class PlayerCommandRepositoryImpl implements PlayerCommandRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(String id, String displayName) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.playerCreate")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_displayName", String.class,
                                                      jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
                    .setParameter("_id", id)
                    .setParameter("_displayName", displayName);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerCommandRepositoryImpl.log.error("Error creating player", e);
            throw new RuntimeException("Error creating player", e);
        }
    }

    @Override
    @Transactional
    public void update(String id, String displayName) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.playerUpdate")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_displayName", String.class,
                                                      jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
                    .setParameter("_id", id)
                    .setParameter("_displayName", displayName);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerCommandRepositoryImpl.log.error("Error updating player", e);
            throw new RuntimeException("Error updating player", e);
        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.playerDelete")
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
            PlayerCommandRepositoryImpl.log.error("Error deleting player", e);
            throw new RuntimeException("Error deleting player", e);
        }
    }

    @Override
    @Transactional
    public void assignTeam(String id, String idTeam) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.playerAssignTeam")
                    .registerStoredProcedureParameter("_id", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("_idTeam", String.class, jakarta.persistence.ParameterMode.IN)
                    .registerStoredProcedureParameter("errorMessage_", String.class,
                                                      jakarta.persistence.ParameterMode.OUT)
                    .setParameter("_id", id)
                    .setParameter("_idTeam", idTeam);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerCommandRepositoryImpl.log.error("Error assigning player to team", e);
            throw new RuntimeException("Error assigning player to team", e);
        }
    }

    @Override
    @Transactional
    public void unassignTeam(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("fiveteam.playerUnassignTeam")
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
            PlayerCommandRepositoryImpl.log.error("Error unassigning player from team", e);
            throw new RuntimeException("Error unassigning player from team", e);
        }
    }
}
