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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlayerService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+33|0)[1-9]([0-9 .\\-]{8,})$");
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String ACTIVE_STATUS = "actif";
    private static final String DELETED_STATUS = "supprimé";

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerDto> findAll() {
        return playerRepository.findAll();
    }

    public PlayerDto findById(UUID id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + id + " not found"));
    }

    @Transactional
    public CreatePlayerResponse create(CreatePlayerRequest request) {
        String now = now();
        String email = validateEmail(request.email());
        if (playerRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A player with email " + email + " already exists");
        }

        PlayerDto player = new PlayerDto(
            UUID.randomUUID(),
            validateName(request.firstName(), "First name"),
            validateName(request.lastName(), "Last name"),
            email,
            validatePhone(request.phone()),
            validateBirthDate(request.birthDate()),
            validateGender(request.gender()),
            validateHeight(request.height()),
            ACTIVE_STATUS,
            zeroStatistics(),
            List.of(),
            now,
            now
        );
        playerRepository.insert(player);
        return new CreatePlayerResponse(player.id(), player.status(), player.createdAt());
    }

    @Transactional
    public UpdatePlayerResponse update(UUID id, UpdatePlayerRequest request) {
        PlayerDto existingPlayer = requireActivePlayer(id);
        String updatedAt = now();
        String email = request.email() != null ? validateEmail(request.email()) : existingPlayer.email();
        if (!email.equals(existingPlayer.email()) && playerRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A player with email " + email + " already exists");
        }

        PlayerDto player = new PlayerDto(
            id,
            request.firstName() != null ? validateName(request.firstName(), "First name") : existingPlayer.firstName(),
            request.lastName() != null ? validateName(request.lastName(), "Last name") : existingPlayer.lastName(),
            email,
            request.phone() != null ? validatePhone(request.phone()) : existingPlayer.phone(),
            request.birthDate() != null ? validateBirthDate(request.birthDate()) : existingPlayer.birthDate(),
            request.gender() != null ? validateGender(request.gender()) : existingPlayer.gender(),
            request.height() != null ? validateHeight(request.height()) : existingPlayer.height(),
            existingPlayer.status(),
            existingPlayer.statistics(),
            existingPlayer.teamIds(),
            existingPlayer.createdAt(),
            updatedAt
        );
        playerRepository.update(player);
        return new UpdatePlayerResponse(player.id(), player.updatedAt());
    }

    @Transactional
    public DeletePlayerResponse delete(UUID id) {
        PlayerDto existingPlayer = findById(id);
        if (DELETED_STATUS.equals(existingPlayer.status())) {
            return new DeletePlayerResponse(existingPlayer.id(), existingPlayer.status(), existingPlayer.updatedAt());
        }

        playerRepository.markDeleted(id);
        PlayerDto deletedPlayer = findById(id);
        return new DeletePlayerResponse(deletedPlayer.id(), deletedPlayer.status(), deletedPlayer.updatedAt());
    }

    @Transactional
    public UpdatePlayerStatisticsResponse updateStatistics(UUID id, UpdatePlayerStatisticsRequest request) {
        PlayerDto existingPlayer = requireActivePlayer(id);
        PlayerStatisticsDto statistics = validateStatistics(
            request.matchesPlayed(),
            request.goalsScored(),
            request.assists(),
            request.wins(),
            request.losses(),
            request.draws(),
            request.mvps()
        );

        playerRepository.updateStatistics(id, statistics);
        PlayerDto updatedPlayer = findById(existingPlayer.id());
        return new UpdatePlayerStatisticsResponse(updatedPlayer.id(), updatedPlayer.statistics(), updatedPlayer.updatedAt());
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
        if (normalizedPhone.length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone must not exceed 20 characters");
        }
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

    private PlayerStatisticsDto validateStatistics(
        Integer matchesPlayed,
        Integer goalsScored,
        Integer assists,
        Integer wins,
        Integer losses,
        Integer draws,
        Integer mvps
    ) {
        if (matchesPlayed == null || goalsScored == null || assists == null || wins == null || losses == null || draws == null || mvps == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Statistics fields must not be null");
        }
        if (matchesPlayed < 0 || goalsScored < 0 || assists < 0 || wins < 0 || losses < 0 || draws < 0 || mvps < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Statistics fields must be greater than or equal to 0");
        }
        if (wins + losses + draws > matchesPlayed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wins, losses and draws total cannot be greater than matches played");
        }
        if (mvps > matchesPlayed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mvps cannot be greater than matches played");
        }
        return new PlayerStatisticsDto(matchesPlayed, goalsScored, assists, wins, losses, draws, mvps);
    }

    private PlayerStatisticsDto zeroStatistics() {
        return new PlayerStatisticsDto(0, 0, 0, 0, 0, 0, 0);
    }

    private String now() {
        return Instant.now().toString();
    }
}
