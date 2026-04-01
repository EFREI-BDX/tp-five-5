package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.PlayerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Log4j2
public class PlayerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PlayerEntity> findAll() {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery(
                    "PlayerEntity.playerGetAll");
            return query.getResultList();
        } catch (Exception e) {
            PlayerRepository.log.error("Error retrieving all players", e);
            throw new RuntimeException("Error retrieving all players", e);
        }
    }

    public Optional<PlayerEntity> findById(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery(
                            "PlayerEntity.playerGetById")
                    .setParameter("_id", id);
            List<PlayerEntity> result = query.getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
        } catch (Exception e) {
            PlayerRepository.log.error("Error retrieving player by id: {}", id, e);
            throw new RuntimeException("Error retrieving player by id: " + id, e);
        }
    }

    public List<PlayerEntity> findByTeamId(String idTeam) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery(
                            "PlayerEntity.playerGetByTeamId")
                    .setParameter("_idTeam", idTeam);
            return query.getResultList();
        } catch (Exception e) {
            PlayerRepository.log.error("Error retrieving players by team id: {}", idTeam, e);
            throw new RuntimeException("Error retrieving players by team id: " + idTeam, e);
        }
    }

    @Transactional
    public void create(String id, String displayName) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("PlayerEntity.playerCreate")
                    .setParameter("_id", id)
                    .setParameter("_displayName", displayName);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerRepository.log.error("Error creating player", e);
            throw new RuntimeException("Error creating player", e);
        }
    }

    @Transactional
    public void update(String id, String displayName) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("PlayerEntity.playerUpdate")
                    .setParameter("_id", id)
                    .setParameter("_displayName", displayName);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerRepository.log.error("Error updating player", e);
            throw new RuntimeException("Error updating player", e);
        }
    }

    @Transactional
    public void delete(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery("PlayerEntity.playerDelete")
                    .setParameter("_id", id);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerRepository.log.error("Error deleting player", e);
            throw new RuntimeException("Error deleting player", e);
        }
    }

    @Transactional
    public void assignTeam(String id, String idTeam) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery(
                            "PlayerEntity.playerAssignTeam")
                    .setParameter("_id", id)
                    .setParameter("_idTeam", idTeam);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerRepository.log.error("Error assigning player to team", e);
            throw new RuntimeException("Error assigning player to team", e);
        }
    }

    @Transactional
    public void unassignTeam(String id) {
        try {
            StoredProcedureQuery query = this.entityManager.createNamedStoredProcedureQuery(
                            "PlayerEntity.playerUnassignTeam")
                    .setParameter("_id", id);
            query.execute();
            String errorMessage = (String) query.getOutputParameterValue("errorMessage_");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            PlayerRepository.log.error("Error unassigning player from team", e);
            throw new RuntimeException("Error unassigning player from team", e);
        }
    }
}

