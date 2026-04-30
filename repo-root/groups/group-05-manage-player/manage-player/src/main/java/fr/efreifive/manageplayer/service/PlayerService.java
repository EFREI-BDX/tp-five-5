package fr.efreifive.manageplayer.service;

import fr.efreifive.manageplayer.dto.CreatePlayerRequest;
import fr.efreifive.manageplayer.dto.CreatePlayerResponse;
import fr.efreifive.manageplayer.dto.DeletePlayerResponse;
import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.PlayerStatisticsDto;
import fr.efreifive.manageplayer.dto.UpdatePlayerRequest;
import fr.efreifive.manageplayer.dto.UpdatePlayerResponse;
import fr.efreifive.manageplayer.dto.UpdatePlayerStatisticsRequest;
import fr.efreifive.manageplayer.dto.UpdatePlayerStatisticsResponse;
import fr.efreifive.manageplayer.mapper.PlayerMapper;
import fr.efreifive.manageplayer.repository.PlayerRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlayerService implements IPlayerAdminService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s\\-().]{7,20}$");
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String ACTIVE_STATUS = "actif";
    private static final String DELETED_STATUS = "supprimé";

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public List<PlayerDto> findAll() {
        return playerRepository.findAll();
    }

    public PlayerDto findById(UUID id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + id + " not found"));
    }

    public CreatePlayerResponse create(CreatePlayerRequest request) {
        UUID id = UUID.randomUUID();
        String now = now();
        PlayerDto player = playerMapper.fromCreateRequest(
            new CreatePlayerRequest(
                validateName(request.firstName(), "First name"),
                validateName(request.lastName(), "Last name"),
                validateEmail(request.email()),
                validatePhone(request.phone()),
                validateBirthDate(request.birthDate()),
                validateGender(request.gender()),
                validateHeight(request.height())
            ),
            zeroStatistics(),
            ACTIVE_STATUS,
            now
        );
        PlayerDto createdPlayer = playerRepository.create(id, player);
        return new CreatePlayerResponse(createdPlayer.id(), createdPlayer.status(), createdPlayer.createdAt());
    }

    public UpdatePlayerResponse update(UUID id, UpdatePlayerRequest request) {
        PlayerDto existingPlayer = requireActivePlayer(id);
        String updatedAt = now();

        PlayerDto player = playerMapper.merge(
            existingPlayer,
            new UpdatePlayerRequest(
                request.firstName() != null ? validateName(request.firstName(), "First name") : null,
                request.lastName() != null ? validateName(request.lastName(), "Last name") : null,
                request.email() != null ? validateEmail(request.email()) : null,
                request.phone() != null ? validatePhone(request.phone()) : null,
                request.birthDate() != null ? validateBirthDate(request.birthDate()) : null,
                request.gender() != null ? validateGender(request.gender()) : null,
                request.height() != null ? validateHeight(request.height()) : null
            ),
            updatedAt
        );
        PlayerDto updatedPlayer = playerRepository.update(player);
        return new UpdatePlayerResponse(updatedPlayer.id(), updatedPlayer.updatedAt());
    }

    public DeletePlayerResponse delete(UUID id) {
        PlayerDto existingPlayer = findById(id);
        if (DELETED_STATUS.equals(existingPlayer.status())) {
            return new DeletePlayerResponse(existingPlayer.id(), existingPlayer.status(), existingPlayer.updatedAt());
        }

        PlayerDto deletedPlayer = playerRepository.delete(id);
        return new DeletePlayerResponse(deletedPlayer.id(), deletedPlayer.status(), deletedPlayer.updatedAt());
    }

    public UpdatePlayerStatisticsResponse updateStatistics(UUID id, UpdatePlayerStatisticsRequest request) {
        PlayerDto existingPlayer = requireActivePlayer(id);
        PlayerStatisticsDto statistics = validateStatistics(
            request.matchesPlayed(),
            request.goalsScored(),
            request.assists(),
            request.wins()
        );

        PlayerDto updatedPlayer = playerRepository.updateStatistics(existingPlayer.id(), statistics);
        return new UpdatePlayerStatisticsResponse(updatedPlayer.id(), updatedPlayer.statistics(), updatedPlayer.updatedAt());
    }

    @Override
    public long count() {
        return playerRepository.count();
    }

    @Override
    public void reset() {
        playerRepository.deleteAll();
    }

    private PlayerDto requireActivePlayer(UUID id) {
        PlayerDto player = findById(id);
        if (DELETED_STATUS.equals(player.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deleted player cannot be modified");
        }
        return player;
    }

    private String validateName(String value, String fieldLabel) {
        if (value == null || value.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldLabel + " must not be empty");
        }
        String normalizedValue = value.trim();
        if (normalizedValue.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldLabel + " must not exceed 100 characters");
        }
        return normalizedValue;
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must not be empty");
        }
        String normalizedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email format is invalid");
        }
        return normalizedEmail;
    }

    private String validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone must not be empty");
        }
        String normalizedPhone = phone.trim();
        if (!PHONE_PATTERN.matcher(normalizedPhone).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone format is invalid");
        }
        return normalizedPhone;
    }

    private String validateGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gender must not be empty");
        }
        String normalizedGender = gender.trim();
        if (!List.of("homme", "femme", "non binaire", "non spécifié").contains(normalizedGender)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gender is invalid");
        }
        return normalizedGender;
    }

    private String validateBirthDate(String birthDate) {
        if (birthDate == null || birthDate.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birth date must not be empty");
        }
        String normalizedBirthDate = birthDate.trim();
        try {
            LocalDate parsedBirthDate = LocalDate.parse(normalizedBirthDate, BIRTH_DATE_FORMATTER);
            if (parsedBirthDate.isAfter(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birth date must not be in the future");
            }
            return normalizedBirthDate;
        } catch (DateTimeParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birth date must match dd/MM/yyyy");
        }
    }

    private double validateHeight(Double height) {
        if (height == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Height must not be empty");
        }
        if (height <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Height must be greater than 0");
        }
        return height;
    }

    private PlayerStatisticsDto validateStatistics(Integer matchesPlayed, Integer goalsScored, Integer assists, Integer wins) {
        if (matchesPlayed == null || goalsScored == null || assists == null || wins == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Statistics fields must not be null");
        }
        if (matchesPlayed < 0 || goalsScored < 0 || assists < 0 || wins < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Statistics fields must be greater than or equal to 0");
        }
        if (wins > matchesPlayed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wins cannot be greater than matches played");
        }
        return new PlayerStatisticsDto(matchesPlayed, goalsScored, assists, wins);
    }

    private PlayerStatisticsDto zeroStatistics() {
        return new PlayerStatisticsDto(0, 0, 0, 0);
    }

    private String now() {
        return Instant.now().toString();
    }
}
