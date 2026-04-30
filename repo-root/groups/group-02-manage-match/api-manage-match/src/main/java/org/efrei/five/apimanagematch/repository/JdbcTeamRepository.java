package org.efrei.five.apimanagematch.repository;

import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.external.ITeamRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Label;
import org.efrei.five.apimanagematch.domain.valueobjects.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository


public class JdbcTeamRepository implements ITeamRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsert(Team team) {

        jdbcTemplate.queryForObject(
                "SELECT fn_upsert_team(?, ?, ?)",
                UUID.class,
                team.id().value(),
                team.tag().value(),
                team.label().value()
        );
    }

    @Override
    public Optional<Team> findById(Id id) {
        return jdbcTemplate.query(
                "SELECT * FROM fn_get_team_by_uuid(?)",
                rs -> {
                    if (!rs.next()) return Optional.empty();

                    Team team = new Team(
                            new Id((UUID) rs.getObject("uuid")),
                            new Tag(rs.getString("tag")),
                            new Label(rs.getString("label"))
                    );

                    return Optional.of(team);
                },
                id.value()
        );
    }
}