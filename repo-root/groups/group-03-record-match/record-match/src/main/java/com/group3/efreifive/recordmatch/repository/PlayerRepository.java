package com.group3.efreifive.recordmatch.repository;

import com.group3.efreifive.recordmatch.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {

    List<PlayerEntity> findByTeamId(UUID teamId);
}