package fr.efreifive.manageplayer.service;

import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.PlayerRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlayerService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+33|0)[1-9]([0-9]{8}|[0-9\\s.\\-]{8,})$");
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ConcurrentHashMap<UUID, PlayerDto> players = new ConcurrentHashMap<>();
    private final TeamService teamService;

    public PlayerService(TeamService teamService) {
        this.teamService = teamService;
    }

    public List<PlayerDto> findAll() {
        return players.values().stream()
            .sorted(Comparator.comparing(PlayerDto::lastName)
                .thenComparing(PlayerDto::firstName))
            .toList();
    }

    public PlayerDto findById(UUID id) {
        PlayerDto player = players.get(id);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + id + " not found");
        }
        return player;
    }

    public PlayerDto create(PlayerRequest request) {
        PlayerDto player = new PlayerDto(
            UUID.randomUUID(),
            validateName(request.firstName(), "First name"),
            validateName(request.lastName(), "Last name"),
            validateEmail(request.email()),
            validatePhone(request.phone()),
            validateGender(request.gender()),
            validateBirthDate(request.birthDate()),
            validateHeight(request.height()),
            validateTeamIds(request.teamIds())
        );
        players.put(player.id(), player);
        return player;
    }

    public PlayerDto update(UUID id, PlayerRequest request) {
        findById(id);
        PlayerDto player = new PlayerDto(
            id,
            validateName(request.firstName(), "First name"),
            validateName(request.lastName(), "Last name"),
            validateEmail(request.email()),
            validatePhone(request.phone()),
            validateGender(request.gender()),
            validateBirthDate(request.birthDate()),
            validateHeight(request.height()),
            validateTeamIds(request.teamIds())
        );
        players.put(id, player);
        return player;
    }

    public void delete(UUID id) {
        findById(id);
        players.remove(id);
    }

    public boolean exists(UUID id) {
        return players.containsKey(id);
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

    private List<UUID> validateTeamIds(List<UUID> teamIds) {
        if (teamIds == null) {
            return List.of();
        }
        if (teamIds.size() != new HashSet<>(teamIds).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team ids must be unique");
        }
        for (UUID teamId : teamIds) {
            if (!teamService.exists(teamId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team with id " + teamId + " not found");
            }
        }
        return List.copyOf(new ArrayList<>(teamIds));
    }
}
