package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {
    List<PlayerEntity> findByIdTeam(String idTeam);
}
