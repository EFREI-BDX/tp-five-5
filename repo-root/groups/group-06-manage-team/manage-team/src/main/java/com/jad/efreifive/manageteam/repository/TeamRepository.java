package com.jad.efreifive.manageteam.repository;

import com.jad.efreifive.manageteam.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, String> {
}
