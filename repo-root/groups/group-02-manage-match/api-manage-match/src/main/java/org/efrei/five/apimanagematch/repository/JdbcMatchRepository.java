package org.efrei.five.apimanagematch.repository;

import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.external.IMatchRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.MatchState;
import org.efrei.five.apimanagematch.repository.dto.MatchRawResult;
import org.efrei.five.apimanagematch.repository.mapper.MatchMapper;
import org.efrei.five.apimanagematch.repository.mapper.MatchRawResultRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcMatchRepository implements IMatchRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<MatchRawResult> rowMapper;

    public JdbcMatchRepository(JdbcTemplate jdbcTemplate,
                               MatchRawResultRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<Match> findById(Id id) {

        List<MatchRawResult> results = jdbcTemplate.query(
                "SELECT * FROM fn_get_match_details(?)",
                rowMapper,
                id.value()
        );

        return results.stream()
                .findFirst()
                .map(MatchMapper::toDomain);
    }

    @Override
    public void updateMatchStatus(Id id, MatchState state) {
        jdbcTemplate.queryForObject(
                "SELECT fn_update_match_status(?, ?)",
                Boolean.class,
                id.value(),
                state.name()
        );
    }

    @Override
    public void save(Match match) {
        jdbcTemplate.queryForObject(
                "SELECT fn_insert_match(?, ?, ?, ?, ?, ?, ?)",
                UUID.class,
                match.id().value(),
                match.teamA().id().value(),
                match.teamB().id().value(),
                match.field().id().value(),
                match.period().start(),
                match.period().end(),
                match.status().name()
        );
    }

}