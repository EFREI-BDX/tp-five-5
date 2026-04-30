package org.efrei.five.apimanagematch.domain.mapper;

import org.efrei.five.apimanagematch.domain.command.common.MatchResponseDto;
import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.entities.Team;

public class MatchMapper {
    public static MatchResponseDto toDto(Match match) {

        Team teamA = match.teamA();
        Team teamB = match.teamB();
        Field field = match.field();
        return new MatchResponseDto(
                Match.getIdValue(match),
                new MatchResponseDto.TeamResponse(
                        Team.getIdValue(teamA),
                        Team.getTagValue(teamA),
                        Team.getLabelValue(teamA)
                ),
                new MatchResponseDto.TeamResponse(
                        Team.getIdValue(teamB),
                        Team.getTagValue(teamB),
                        Team.getLabelValue(teamB)
                ),
                new MatchResponseDto.FieldResponse(
                        Field.getIdValue(field),
                        Field.getLabelValue(field)
                ),
                new MatchResponseDto.PeriodResponse(
                        Match.getStartValue(match),
                        Match.getEndValue(match)
                ),
                Match.getMatchState(match)
        );
    }


}
