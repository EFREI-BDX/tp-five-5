package org.efrei.five.apimanagematch.repository;

import org.efrei.five.apimanagematch.domain.external.IMatchByTeamRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcMatchByTeamRepository implements IMatchByTeamRepository {

    private static final String FIND_NOT_STARTED_BY_TEAM = """
            SELECT m.uuid
            FROM matches m
            JOIN teams t ON (m.team_a_id = t.id OR m.team_b_id = t.id)
            JOIN ref_status rs ON m.current_status = rs.id
            WHERE t.uuid = ?
              AND rs.code = 'NOT_STARTED'
            """;

    private final JdbcTemplate jdbcTemplate;

    public JdbcMatchByTeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Id> findNotStartedMatchIdsByTeamId(Id teamId) {
        return jdbcTemplate.queryForList(FIND_NOT_STARTED_BY_TEAM, UUID.class, teamId.value())
                .stream()
                .map(Id::new)
                .toList();
    }
}