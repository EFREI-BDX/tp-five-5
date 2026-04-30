package org.efrei.five.apimanagematch.repository;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.external.IFieldRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Label;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository


public class JdbcFieldRepository implements IFieldRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFieldRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsert(Field f) {

        jdbcTemplate.queryForObject(
                "SELECT fn_upsert_field(?, ?)",
                UUID.class,
                f.id().value(),
                f.label().value()
        );
    }

    @Override
    public Optional<Field> findById(Id id) {
        return jdbcTemplate.query(
                "SELECT * FROM fn_get_field_by_uuid(?)",
                rs -> {
                    if (!rs.next()) return Optional.empty();

                    Field field = new Field(
                            new Id((UUID) rs.getObject("uuid")),
                            new Label(rs.getString("label"))
                    );

                    return Optional.of(field);
                },
                id.value()
        );
    }
}