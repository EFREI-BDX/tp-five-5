package org.efrei.five.apimanagematch.repository;

import org.efrei.five.apimanagematch.domain.external.IMatchByTeamRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcMatchByTeamRepository implements IMatchByTeamRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMatchByTeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Id> findNotStartedMatchIdsByTeamId(Id teamId) {
        return jdbcTemplate.query(
                "SELECT * FROM fn_get_not_started_match_ids_by_team_uuid(?)",
                (rs, rowNum) -> new Id((UUID) rs.getObject("match_uuid")),
                teamId.value()
        );
    }
}
