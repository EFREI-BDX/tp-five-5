package com.group3.efreifive.recordmatch.repository;

import com.group3.efreifive.recordmatch.entity.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchEventRepository extends JpaRepository<MatchEventEntity, UUID> {

    List<MatchEventEntity> findByMatchId(UUID matchId);

    List<MatchEventEntity> findByTeamId(UUID teamId);
}