package org.efrei.five.apimanagematch.repository.mapper;

import org.efrei.five.apimanagematch.domain.valueobjects.*;
import org.efrei.five.apimanagematch.repository.dto.MatchRawResult;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


@Component
public class MatchRawResultRowMapper implements RowMapper<MatchRawResult> {

    @Override
    public MatchRawResult mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new MatchRawResult(
                rs.getObject("match_uuid", UUID.class),

                rs.getObject("team_a_uuid", UUID.class),
                rs.getString("team_a_tag"),
                rs.getString("team_a_label"),

                rs.getObject("team_b_uuid", UUID.class),
                rs.getString("team_b_tag"),
                rs.getString("team_b_label"),

                rs.getObject("field_uuid", UUID.class),
                rs.getString("field_label"),

                rs.getTimestamp("start_time").toLocalDateTime(),
                rs.getTimestamp("end_time").toLocalDateTime(),

                rs.getString("status")
        );
    }
}
