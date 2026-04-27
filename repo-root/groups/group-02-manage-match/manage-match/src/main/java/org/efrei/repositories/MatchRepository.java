package org.efrei.repositories;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import org.efrei.domain.entities.Field;
import org.efrei.domain.entities.Match;
import org.efrei.domain.entities.Team;
import org.efrei.domain.valueobjects.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface MatchRepository extends GenericRepository<MatchEntity, Integer> {

    default void save(Match m) {
        callInsertMatchProc(
                m.id().value(),
                m.teamA().id().value(),
                m.teamB().id().value(),
                m.field().id().value(),
                m.period().start(),
                m.period().end(),
                m.status().name(),
                m.teamA().tag().value(),
                m.teamA().label().value(),
                m.teamB().tag().value(),
                m.teamB().label().value(),
                m.field().label().value()
        );
    }

    @Query(value = "CALL sp_insert_match(:matchUuid, :teamAUuid, :teamBUuid, :fieldUuid, :startTime, :endTime, :statusCode, :teamATag, :teamALabel, :teamBTag, :teamBLabel, :fieldLabel)")
    void callInsertMatchProc(
            UUID matchUuid,
            UUID teamAUuid,
            UUID teamBUuid,
            UUID fieldUuid,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String statusCode,
            String teamATag,
            String teamALabel,
            String teamBTag,
            String teamBLabel,
            String fieldLabel
    );

    default void updateStatus(Id id, MatchState state) {
        callUpdateMatchStatusProc(id.value(), state.name());
    }

    @Query("CALL sp_update_match_status(:matchUuid, :statusCode)")
    void callUpdateMatchStatusProc(UUID matchUuid, String statusCode);

    default Optional<Match> findById(Id id) {
        return findRawById(id.value()).map(this::mapToDomain);
    }

    @Query("SELECT * FROM fn_get_match_details(:matchUuid)")
    Optional<MatchRawResult> findRawById(UUID matchUuid);

    private Match mapToDomain(MatchRawResult raw) {
        return new Match(
                new Id(raw.matchUuid()),
                new Team(new Id(raw.teamAUuid()), new Tag(raw.teamATag()), new Label(raw.teamALabel())),
                new Team(new Id(raw.teamBUuid()), new Tag(raw.teamBTag()), new Label(raw.teamBLabel())),
                new Field(new Id(raw.fieldUuid()), new Label(raw.fieldLabel())),
                new Period(raw.startTime(), raw.endTime()),
                MatchState.valueOf(raw.status())
        );
    }
}