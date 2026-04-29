package com.group3.efreifive.recordmatch.repository;

import com.group3.efreifive.recordmatch.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {
}