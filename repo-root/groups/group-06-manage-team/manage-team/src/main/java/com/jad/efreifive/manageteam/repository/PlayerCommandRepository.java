package com.jad.efreifive.manageteam.repository;

public interface PlayerCommandRepository {
    void create(String id, String displayName);
    void update(String id, String displayName);
    void delete(String id);
    void assignTeam(String id, String idTeam);
    void unassignTeam(String id);
}

