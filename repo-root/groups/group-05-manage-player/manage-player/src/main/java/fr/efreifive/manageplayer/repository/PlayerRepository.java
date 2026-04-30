package fr.efreifive.manageplayer.repository;

import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.PlayerStatisticsDto;
import fr.efreifive.manageplayer.repository.result.PersistenceOperationResult;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class PlayerRepository {
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PlayerDto> playerRowMapper = this::mapPlayer;

    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PlayerDto> findAll() {
        return jdbcTemplate.query("CALL fiveplayer.playerGetAll()", playerRowMapper);
    }

    public Optional<PlayerDto> findById(UUID id) {
        List<PlayerDto> players = jdbcTemplate.query("CALL fiveplayer.playerGetById(?)", playerRowMapper, id.toString());
        return players.stream().findFirst();
    }

    public PlayerDto create(UUID id, PlayerDto player) {
        callPlayerCreate(id, player);
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + id + " not found"));
    }

    public PlayerDto update(PlayerDto player) {
        callPlayerUpdate(player.id(), player.firstName(), player.lastName(), player.email(), player.phone(), player.birthDate(),
            player.gender(), player.height());
        return findById(player.id())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + player.id() + " not found"));
    }

    public PlayerDto delete(UUID id) {
        callPlayerDelete(id);
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + id + " not found"));
    }

    public PlayerDto updateStatistics(UUID id, PlayerStatisticsDto statistics) {
        callPlayerStatisticsUpdate(id, statistics);
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + id + " not found"));
    }

    public void addTeam(UUID playerId, UUID teamId) {
        callPlayerTeamProcedure("fiveplayer.playerJoinTeam", playerId, teamId);
    }

    public void removeTeam(UUID playerId, UUID teamId) {
        callPlayerTeamProcedure("fiveplayer.playerLeaveTeam", playerId, teamId);
    }

    public PersistenceOperationResult deleteAll() {
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement statement = connection.prepareCall("{CALL fiveplayer.playerReset(?)}")) {
                    statement.registerOutParameter(1, Types.VARCHAR);
                    statement.execute();
                }
                return null;
            });
            return PersistenceOperationResult.ok();
        } catch (DataAccessException exception) {
            return PersistenceOperationResult.failure(extractDatabaseMessage(exception));
        }
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("CALL fiveplayer.playerCount()", Long.class);
        return count != null ? count : 0;
    }

    private void callPlayerCreate(UUID id, PlayerDto player) {
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement statement = connection.prepareCall("{CALL fiveplayer.playerCreate(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                    statement.setString(1, id.toString());
                    statement.setString(2, player.firstName());
                    statement.setString(3, player.lastName());
                    statement.setString(4, player.email());
                    statement.setString(5, player.phone());
                    statement.setString(6, player.gender());
                    statement.setDate(7, toSqlDate(player.birthDate()));
                    statement.setDouble(8, player.height());
                    statement.registerOutParameter(9, Types.VARCHAR);
                    statement.execute();
                }
                return null;
            });
        } catch (DataAccessException exception) {
            throw toResponseStatusException(exception);
        }
    }

    private void callPlayerUpdate(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String birthDate,
        String gender,
        double height
    ) {
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement statement = connection.prepareCall("{CALL fiveplayer.playerUpdate(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                    statement.setString(1, id.toString());
                    statement.setString(2, firstName);
                    statement.setString(3, lastName);
                    statement.setString(4, email);
                    statement.setString(5, phone);
                    statement.setString(6, gender);
                    statement.setDate(7, toSqlDate(birthDate));
                    statement.setDouble(8, height);
                    statement.registerOutParameter(9, Types.VARCHAR);
                    statement.execute();
                }
                return null;
            });
        } catch (DataAccessException exception) {
            throw toResponseStatusException(exception);
        }
    }

    private void callPlayerDelete(UUID id) {
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement statement = connection.prepareCall("{CALL fiveplayer.playerDelete(?, ?)}")) {
                    statement.setString(1, id.toString());
                    statement.registerOutParameter(2, Types.VARCHAR);
                    statement.execute();
                }
                return null;
            });
        } catch (DataAccessException exception) {
            throw toResponseStatusException(exception);
        }
    }

    private void callPlayerStatisticsUpdate(UUID id, PlayerStatisticsDto statistics) {
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement statement = connection.prepareCall("{CALL fiveplayer.playerStatisticsUpdate(?, ?, ?, ?, ?, ?, ?, ?)}")) {
                    statement.setString(1, id.toString());
                    statement.setInt(2, statistics.matchesPlayed());
                    statement.setInt(3, statistics.goalsScored());
                    statement.setInt(4, statistics.assists());
                    statement.setInt(5, statistics.wins());
                    statement.setInt(6, 0);
                    statement.setInt(7, 0);
                    statement.registerOutParameter(8, Types.VARCHAR);
                    statement.execute();
                }
                return null;
            });
        } catch (DataAccessException exception) {
            throw toResponseStatusException(exception);
        }
    }

    private void callPlayerTeamProcedure(String procedureName, UUID playerId, UUID teamId) {
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement statement = connection.prepareCall("{CALL " + procedureName + "(?, ?, ?)}")) {
                    statement.setString(1, playerId.toString());
                    statement.setString(2, teamId.toString());
                    statement.registerOutParameter(3, Types.VARCHAR);
                    statement.execute();
                }
                return null;
            });
        } catch (DataAccessException exception) {
            throw toResponseStatusException(exception);
        }
    }

    private PlayerDto mapPlayer(ResultSet resultSet, int rowNumber) throws SQLException {
        return new PlayerDto(
            UUID.fromString(resultSet.getString("id")),
            resultSet.getString("firstName"),
            resultSet.getString("lastName"),
            resultSet.getString("email"),
            resultSet.getString("phone"),
            resultSet.getString("birthDate"),
            resultSet.getString("gender"),
            resultSet.getDouble("height"),
            resultSet.getString("status"),
            new PlayerStatisticsDto(
                resultSet.getInt("matchesPlayed"),
                resultSet.getInt("goalsScored"),
                resultSet.getInt("assists"),
                resultSet.getInt("wins")
            ),
            toTeamIds(resultSet.getString("teamIds")),
            resultSet.getString("createdAt"),
            resultSet.getString("updatedAt")
        );
    }

    private List<String> toTeamIds(String teamIds) {
        if (teamIds == null || teamIds.isBlank()) {
            return List.of();
        }
        return Arrays.stream(teamIds.split(","))
            .filter(teamId -> !teamId.isBlank())
            .toList();
    }

    private Date toSqlDate(String birthDate) {
        return Date.valueOf(LocalDate.parse(birthDate, BIRTH_DATE_FORMATTER));
    }

    private ResponseStatusException toResponseStatusException(DataAccessException exception) {
        String message = extractDatabaseMessage(exception);
        HttpStatus status = message.startsWith("No player with id") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return new ResponseStatusException(status, message, exception);
    }

    private String extractDatabaseMessage(DataAccessException exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof SQLException sqlException && sqlException.getMessage() != null) {
                return sqlException.getMessage();
            }
            current = current.getCause();
        }
        return exception.getMostSpecificCause().getMessage() != null
            ? exception.getMostSpecificCause().getMessage()
            : "Database operation failed";
    }
}
