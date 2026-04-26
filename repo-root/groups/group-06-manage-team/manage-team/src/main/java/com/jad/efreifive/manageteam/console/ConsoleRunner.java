package com.jad.efreifive.manageteam.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.service.PlayerService;
import com.jad.efreifive.manageteam.service.TeamService;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
public class ConsoleRunner {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final ObjectMapper objectMapper;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExecutorService executorService;
    private Scanner scanner;

    public ConsoleRunner(final TeamService teamService, final PlayerService playerService,
                         final ObjectMapper objectMapper) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.objectMapper = objectMapper;
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

            case "team-list" -> this.printPretty(this.teamService.findAll());
            case "team-get" -> this.teamGet(args);
            case "team-create" -> this.teamCreate(args);
            case "team-dissolve" -> this.teamDissolve(args);
            case "team-restore" -> this.teamRestore(args);
            case "team-rename" -> this.teamRename(args);
            case "team-retag" -> this.teamRetag(args);

            case "player-list" -> this.printPretty(this.playerService.findAll());
            case "player-get" -> this.playerGet(args);
            case "player-list-by-team" -> this.playerListByTeam(args);
            case "player-create" -> this.playerCreate(args);
            case "player-update" -> this.playerUpdate(args);
            case "player-delete" -> this.playerDelete(args);
            case "player-assign" -> this.playerAssign(args);
            case "player-unassign" -> this.playerUnassign(args);

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
                           
                             team-list
                             team-get <teamId>
                             team-create <id> <label> <tag> <creationDate(yyyy-MM-dd)>
                             team-dissolve <id> <dissolutionDate(yyyy-MM-dd)>
                             team-restore <id>
                             team-rename <id> <newLabel>
                             team-retag <id> <newTag>
                           
                             player-list
                             player-get <playerId>
                             player-list-by-team <teamId>
                             player-create <id> <displayName>
                             player-update <id> <displayName>
                             player-delete <id>
                             player-assign <playerId> <teamId>
                             player-unassign <playerId>
                           
                           Tip: use quotes for values with spaces.
                             team-create 11111111-2222-3333-4444-555555555555 "Equipe Test" TST 2026-04-02
                             player-create 22222222-3333-4444-5555-666666666666 "Player Test\"""");
    }

    private void printPretty(final Object value) {
        try {
            System.out.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value));
        } catch (final JsonProcessingException exception) {
            ConsoleRunner.log.warn("Pretty print failed", exception);
            System.out.println(value);
        }
    }

    private void teamGet(final List<String> args) {
        this.requireArgs(args, 2, "team-get <teamId>");
        final TeamDto dto = this.teamService.findById(UUID.fromString(args.get(1)));
        this.printPretty(dto);
    }

    private void teamCreate(final List<String> args) {
        this.requireArgs(args, 5, "team-create <id> <label> <tag> <creationDate(yyyy-MM-dd)>");
        this.teamService.create(
                args.get(2),
                args.get(3),
                LocalDate.parse(args.get(4))
                               );
        System.out.println("OK");
    }

    private void teamDissolve(final List<String> args) {
        this.requireArgs(args, 3, "team-dissolve <id> <dissolutionDate(yyyy-MM-dd)>");
        this.teamService.dissolve(
                UUID.fromString(args.get(1)),
                LocalDate.parse(args.get(2))
                                 );
        System.out.println("OK");
    }

    private void teamRestore(final List<String> args) {
        this.requireArgs(args, 2, "team-restore <id>");
        this.teamService.restore(UUID.fromString(args.get(1)));
        System.out.println("OK");
    }

    private void teamRename(final List<String> args) {
        this.requireArgs(args, 3, "team-rename <id> <newLabel>");
        this.teamService.changeName(UUID.fromString(args.get(1)), args.get(2));
        System.out.println("OK");
    }

    private void teamRetag(final List<String> args) {
        this.requireArgs(args, 3, "team-retag <id> <newTag>");
        this.teamService.changeTag(UUID.fromString(args.get(1)), args.get(2));
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
        this.requireArgs(args, 3, "player-create <id> <displayName>");
        this.playerService.create(UUID.fromString(args.get(1)), args.get(2));
        System.out.println("OK");
    }

    private void playerUpdate(final List<String> args) {
        this.requireArgs(args, 3, "player-update <id> <displayName>");
        this.playerService.update(UUID.fromString(args.get(1)), args.get(2));
        System.out.println("OK");
    }

    private void playerDelete(final List<String> args) {
        this.requireArgs(args, 2, "player-delete <id>");
        this.playerService.delete(UUID.fromString(args.get(1)));
        System.out.println("OK");
    }

    private void playerAssign(final List<String> args) {
        this.requireArgs(args, 3, "player-assign <playerId> <teamId>");
        this.playerService.assignTeam(UUID.fromString(args.get(1)), UUID.fromString(args.get(2)));
        System.out.println("OK");
    }

    private void playerUnassign(final List<String> args) {
        this.requireArgs(args, 2, "player-unassign <playerId>");
        this.playerService.unassignTeam(UUID.fromString(args.get(1)));
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

