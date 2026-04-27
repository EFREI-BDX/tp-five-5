package org.efrei.services;

import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Singleton;
import org.efrei.domain.entities.Field;
import org.efrei.domain.entities.Match;
import org.efrei.domain.entities.Team;
import org.efrei.domain.valueobjects.*;
import org.efrei.externalapi.FieldManagementClient;
import org.efrei.repositories.MatchRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class MatchService {

    private final MatchRepository repository;
    private final TaskScheduler taskScheduler;
    private final FieldManagementClient fieldClient;

    public MatchService(MatchRepository repository,
                        TaskScheduler taskScheduler,
                        FieldManagementClient fieldClient) {
        this.repository = repository;
        this.taskScheduler = taskScheduler;
        this.fieldClient = fieldClient;
    }

    public Optional<Match> getMatch(Id id) {
        return repository.findById(id);
    }

    public void createMatch(Match match) {
        try {

            fieldClient.createReservation(
                    match.field().id().value(),
                    new FieldManagementClient.ReservationRequest(
                            match.period().start().toString(),
                            match.period().end().toString()
                    )
            );

            repository.save(match);

            scheduleStatusChanges(match);

        } catch (HttpClientResponseException e) {
            throw new RuntimeException("Échec de la réservation du terrain : " + e.getMessage());
        }
    }

    private void scheduleStatusChanges(Match match) {
        LocalDateTime now = LocalDateTime.now();
        Duration delayStart = Duration.between(now, match.period().start());
        taskScheduler.schedule(delayStart, () -> {
            repository.updateStatus(match.id(), MatchState.IN_PROGRESS);
            LocalDateTime startActual = LocalDateTime.now();
            Duration delayUntilEnd = Duration.between(startActual, match.period().end());
            taskScheduler.schedule(delayUntilEnd, () -> {
                repository.updateStatus(match.id(), MatchState.FINISHED);
            });
        });
    }

    public Match createMatch(Id teamAId, Id teamBId, Id fieldId, Period period) {

        String teamATag = "TBD";
        String teamALabel = "Team A";
        String teamBTag = "TBD";
        String teamBLabel = "Team B";
        String fieldLabel = "Terrain Principal";

        Match match = new Match(
                new Id(UUID.randomUUID()),
                new Team(teamAId, new Tag(teamATag), new Label(teamALabel)),
                new Team(teamBId, new Tag(teamBTag), new Label(teamBLabel)),
                new Field(fieldId, new Label(fieldLabel)),
                period,
                MatchState.NOT_STARTED
        );

        try {
            fieldClient.createReservation(
                    match.field().id().value(),
                    new FieldManagementClient.ReservationRequest(
                            match.period().start().toString(),
                            match.period().end().toString()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Impossible de réserver le terrain : " + e.getMessage());
        }

        repository.save(match);

        scheduleStatusChanges(match);

        return match;
    }

    public void cancelMatch(Id id) {
        Match match = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match non trouvé"));

        if (match.status() == MatchState.FINISHED) {
            throw new IllegalStateException("Impossible d'annuler un match terminé");
        }

        try {
            fieldClient.cancelReservation(match.field().id().value());
        } catch (Exception e) {
            throw new IllegalStateException("Note: Impossible de notifier le service de terrain: " + e.getMessage());
        }

        repository.updateStatus(id, MatchState.CANCELLED);
    }
}