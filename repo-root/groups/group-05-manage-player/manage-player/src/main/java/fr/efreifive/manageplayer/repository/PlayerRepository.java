package fr.efreifive.manageplayer.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.PlayerStatisticsDto;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerRepository {
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {
    };

    private final JdbcTemplate jdbcTemplate;

    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PlayerDto> findAll() {
        return jdbcTemplate.query("""
            SELECT id, firstName, lastName, email, phone, birthDate, gender, height,
                   status, teamIds, createdAt, updatedAt,
                   matchesPlayed, goalsScored, assists, wins, losses, draws, mvps
            FROM fiveplayer.PlayerView
            ORDER BY lastName, firstName
            """, this::mapPlayer);
    }

    public Optional<PlayerDto> findById(UUID id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("""
                SELECT id, firstName, lastName, email, phone, birthDate, gender, height,
                       status, teamIds, createdAt, updatedAt,
                       matchesPlayed, goalsScored, assists, wins, losses, draws, mvps
                FROM fiveplayer.PlayerView
                WHERE id = ?
                """, this::mapPlayer, id.toString()));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM fiveplayer.PlayerView WHERE email = ?",
            Integer.class,
            email
        );
        return count != null && count > 0;
    }

    public void insert(PlayerDto player) {
        jdbcTemplate.update(
            "CALL fiveplayer.playerCreate(?, ?, ?, ?, ?, ?, ?, ?, @errorMessage)",
            player.id().toString(),
            player.firstName(),
            player.lastName(),
            player.email(),
            player.phone(),
            player.gender(),
            toSqlDate(player.birthDate()),
            player.height()
        );
    }

    public void update(PlayerDto player) {
        jdbcTemplate.update(
            "CALL fiveplayer.playerUpdate(?, ?, ?, ?, ?, ?, ?, ?, @errorMessage)",
            player.id().toString(),
            player.firstName(),
            player.lastName(),
            player.email(),
            player.phone(),
            player.gender(),
            toSqlDate(player.birthDate()),
            player.height()
        );
    }

    public void markDeleted(UUID id) {
        jdbcTemplate.update(
            "CALL fiveplayer.playerDelete(?, @errorMessage)",
            id.toString()
        );
    }

    public void updateStatistics(UUID id, PlayerStatisticsDto statistics) {
        jdbcTemplate.update(
            "CALL fiveplayer.playerStatisticsUpdate(?, ?, ?, ?, ?, ?, ?, ?, @errorMessage)",
            id.toString(),
            statistics.matchesPlayed(),
            statistics.goalsScored(),
            statistics.assists(),
            statistics.wins(),
            statistics.losses(),
            statistics.draws(),
            statistics.mvps()
        );
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
                resultSet.getInt("wins"),
                resultSet.getInt("losses"),
                resultSet.getInt("draws"),
                resultSet.getInt("mvps")
            ),
            parseTeamIds(resultSet.getString("teamIds")),
            resultSet.getString("createdAt"),
            resultSet.getString("updatedAt")
        );
    }

    private Date toSqlDate(String birthDate) {
        return Date.valueOf(LocalDate.parse(birthDate, BIRTH_DATE_FORMATTER));
    }

    private List<String> parseTeamIds(String teamIds) throws SQLException {
        if (teamIds == null || teamIds.isBlank()) {
            return List.of();
        }
        try {
            return OBJECT_MAPPER.readValue(teamIds, STRING_LIST_TYPE);
        } catch (IOException exception) {
            throw new SQLException("Invalid teamIds JSON from PlayerView", exception);
        }
    }
}
