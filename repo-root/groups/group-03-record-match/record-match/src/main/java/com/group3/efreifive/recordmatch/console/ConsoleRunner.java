package com.group3.efreifive.recordmatch.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.efreifive.recordmatch.dto.EventDto;
import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.dto.PlayerDto;
import com.group3.efreifive.recordmatch.service.IEventService;
import com.group3.efreifive.recordmatch.service.IMatchEventService;
import com.group3.efreifive.recordmatch.service.IMatchService;
import com.group3.efreifive.recordmatch.service.IPlayerService;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@ConditionalOnProperty(name = "app.console.enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class ConsoleRunner implements CommandLineRunner {

    private final IMatchService matchService;
    private final IEventService eventService;
    private final IPlayerService playerService;
    private final IMatchEventService matchEventService;
    private final ObjectMapper objectMapper;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExecutorService executorService;
    private Scanner scanner;

    public ConsoleRunner(final IMatchService matchService,
                         final IEventService eventService,
                         final IPlayerService playerService,
                         final IMatchEventService matchEventService,
                         final ObjectMapper objectMapper) {
        this.matchService = matchService;
        this.eventService = eventService;
        this.playerService = playerService;
        this.matchEventService = matchEventService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        startConsole();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startConsole() {
        if (!this.running.compareAndSet(false, true)) {
            return;
        }
        this.scanner = new Scanner(System.in);
        this.executorService = Executors.newSingleThreadExecutor(runnable -> {
            final Thread thread = new Thread(runnable, "console-thread");
            thread.setDaemon(true);
            return thread;
        });
        this.executorService.submit(this::runLoop);
        ConsoleRunner.log.info("Console thread started");
        System.out.println("Console ready. Type 'help' for commands.");
    }

    private void runLoop() {
        while (this.running.get()) {
            try {
                if (!this.scanner.hasNextLine()) {
                    break;
                }

                final String input = this.scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }

                this.handleCommand(input);
            } catch (final IllegalStateException exception) {
                ConsoleRunner.log.info("Console scanner closed, stopping loop");
                break;
            } catch (final RuntimeException exception) {
                ConsoleRunner.log.error("Unexpected error in console thread", exception);
                System.out.println("error: " + exception.getMessage());
            }
        }

        this.running.set(false);
        ConsoleRunner.log.info("Console thread stopped");
    }

    private void handleCommand(final String input) {
        final List<String> args = this.tokenize(input);
        if (args.isEmpty()) {
            return;
        }

        final String command = args.getFirst().toLowerCase();

        switch (command) {
            case "help" -> this.printHelp();
            case "exit", "quit" -> this.running.set(false);

            case "match-list" -> this.printPretty(this.matchService.findAll());
            case "match-get" -> this.matchGet(args);
            case "match-create" -> this.matchCreate(args);

            case "event-list" -> this.printPretty(this.eventService.findAll());
            case "event-get" -> this.eventGet(args);
            case "event-create" -> this.eventCreate(args);

            case "player-get" -> this.playerGet(args);
            case "player-list-by-team" -> this.playerListByTeam(args);
            case "player-create" -> this.playerCreate(args);

            case "matchevent-get" -> this.matchEventGet(args);
            case "matchevent-list-by-match" -> this.matchEventListByMatch(args);
            case "matchevent-record" -> this.matchEventRecord(args);

            default -> System.out.println("Unknown command. Type 'help'.");
        }
    }

    private List<String> tokenize(final String input) {
        final List<String> tokens = new ArrayList<>();
        final StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (Character.isWhitespace(c) && !inQuotes) {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }
            current.append(c);
        }

        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }

        return tokens;
    }

    private void printHelp() {
        System.out.println("""
                           Available commands:
                             help
                             quit | exit
                           
                             match-list
                             match-get <matchId>
                             match-create <team1Id> <team2Id> <startedAt(yyyy-MM-dd'T'HH:mm:ss)> <durationMinutes>
                           
                             event-list
                             event-get <eventId>
                             event-create <name> <nbPlayers>
                           
                             player-get <playerId>
                             player-list-by-team <teamId>
                             player-create <playerId> <teamId>
                           
                             matchevent-get <matchEventId>
                             matchevent-list-by-match <matchId>
                             matchevent-record <matchId> <eventId> <player1Id> [player2Id]
                           
                           Tip: use quotes for values with spaces.
                           """);
    }

    private void printPretty(final Object value) {
        try {
            System.out.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value));
        } catch (final JsonProcessingException exception) {
            ConsoleRunner.log.warn("Pretty print failed", exception);
            System.out.println(value);
        } catch (final Exception exception) {
            ConsoleRunner.log.warn("Print fallback triggered: " + exception.getMessage());
            System.out.println(value);
        }
    }

    private void matchGet(final List<String> args) {
        this.requireArgs(args, 2, "match-get <matchId>");
        final MatchDto dto = this.matchService.findById(UUID.fromString(args.get(1)));
        this.printPretty(dto);
    }

    private void matchCreate(final List<String> args) {
        this.requireArgs(args, 5, "match-create <team1Id> <team2Id> <startedAt(yyyy-MM-dd'T'HH:mm:ss)> <durationMinutes>");
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final MatchDto dto = new MatchDto(
                UUID.randomUUID(),
                UUID.fromString(args.get(1)),
                UUID.fromString(args.get(2)),
                LocalDateTime.parse(args.get(3), formatter),
                Integer.parseInt(args.get(4))
        );
        this.matchService.create(dto);
        System.out.println("OK");
    }

    private void eventGet(final List<String> args) {
        this.requireArgs(args, 2, "event-get <eventId>");
        final EventDto dto = this.eventService.findById(UUID.fromString(args.get(1)));
        this.printPretty(dto);
    }

    private void eventCreate(final List<String> args) {
        this.requireArgs(args, 3, "event-create <name> <nbPlayers>");
        final EventDto dto = new EventDto(
                UUID.randomUUID(),
                args.get(1),
                Integer.parseInt(args.get(2))
        );
        this.eventService.create(dto);
        System.out.println("OK");
    }

    private void playerGet(final List<String> args) {
        this.requireArgs(args, 2, "player-get <playerId>");
        final PlayerDto dto = this.playerService.findById(UUID.fromString(args.get(1)));
        this.printPretty(dto);
    }

    private void playerListByTeam(final List<String> args) {
        this.requireArgs(args, 2, "player-list-by-team <teamId>");
        this.printPretty(this.playerService.findByTeamId(UUID.fromString(args.get(1))));
    }

    private void playerCreate(final List<String> args) {
        this.requireArgs(args, 3, "player-create <playerId> <teamId>");
        final PlayerDto dto = new PlayerDto(
                UUID.fromString(args.get(1)),
                UUID.fromString(args.get(2))
        );
        this.playerService.create(dto);
        System.out.println("OK");
    }

    private void matchEventGet(final List<String> args) {
        this.requireArgs(args, 2, "matchevent-get <matchEventId>");
        final MatchEventDto dto = this.matchEventService.findById(UUID.fromString(args.get(1)));
        this.printPretty(dto);
    }

    private void matchEventListByMatch(final List<String> args) {
        this.requireArgs(args, 2, "matchevent-list-by-match <matchId>");
        this.printPretty(this.matchEventService.findByMatchId(UUID.fromString(args.get(1))));
    }

    private void matchEventRecord(final List<String> args) {
        this.requireArgs(args, 4, "matchevent-record <matchId> <eventId> <player1Id> [player2Id]");
        final UUID player2Id = args.size() > 4 ? UUID.fromString(args.get(4)) : null;
        final MatchEventDto dto = new MatchEventDto(
                UUID.randomUUID(),
                UUID.fromString(args.get(1)),
                UUID.fromString(args.get(2)),
                UUID.fromString(args.get(3)),
                player2Id,
                LocalDateTime.now(),
                false,
                null,
                null
        );
        this.matchEventService.recordEvent(dto);
        System.out.println("OK");
    }

    private void requireArgs(final List<String> args, final int expected, final String usage) {
        if (args.size() < expected) {
            throw new IllegalArgumentException("Usage: " + usage);
        }
    }

    @PreDestroy
    public void stopConsole() {
        this.running.set(false);
        if (this.scanner != null) {
            this.scanner.close();
        }
        if (this.executorService != null) {
            this.executorService.shutdownNow();
            try {
                if (!this.executorService.awaitTermination(2L, TimeUnit.SECONDS)) {
                    ConsoleRunner.log.warn("Console thread did not terminate within timeout");
                }
            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                ConsoleRunner.log.warn("Interrupted while waiting for console thread shutdown");
            }
        }
        this.scanner = null;
        this.executorService = null;
    }
}
