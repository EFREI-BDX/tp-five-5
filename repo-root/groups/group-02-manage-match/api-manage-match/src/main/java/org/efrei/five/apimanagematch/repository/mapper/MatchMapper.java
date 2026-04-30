package org.efrei.five.apimanagematch.repository.mapper;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.valueobjects.*;
import org.efrei.five.apimanagematch.repository.dto.MatchRawResult;

public final class MatchMapper {

    private MatchMapper() {
    }

    public static Match toDomain(MatchRawResult raw) {

        return new Match(
                new Id(raw.matchUuid()),

                new Team(
                        new Id(raw.teamAUuid()),
                        new Tag(raw.teamATag()),
                        new Label(raw.teamALabel())
                ),

                new Team(
                        new Id(raw.teamBUuid()),
                        new Tag(raw.teamBTag()),
                        new Label(raw.teamBLabel())
                ),

                new Field(
                        new Id(raw.fieldUuid()),
                        new Label(raw.fieldLabel())
                ),

                new Period(raw.startTime(), raw.endTime()),
                MatchState.valueOf(raw.status())
        );
    }
}
