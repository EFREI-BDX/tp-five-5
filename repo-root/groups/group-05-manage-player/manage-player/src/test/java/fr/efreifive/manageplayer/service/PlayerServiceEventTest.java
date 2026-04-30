package fr.efreifive.manageplayer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.efreifive.manageplayer.dto.CreatePlayerRequest;
import fr.efreifive.manageplayer.dto.CreatePlayerResponse;
import fr.efreifive.manageplayer.dto.DeletePlayerResponse;
import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.PlayerStatisticsDto;
import fr.efreifive.manageplayer.dto.UpdatePlayerRequest;
import fr.efreifive.manageplayer.dto.UpdatePlayerResponse;
import fr.efreifive.manageplayer.event.out.PlayerCreatedEvent;
import fr.efreifive.manageplayer.event.out.PlayerDeletedEvent;
import fr.efreifive.manageplayer.event.out.PlayerEventPublisher;
import fr.efreifive.manageplayer.event.out.PlayerNameUpdatedEvent;
import fr.efreifive.manageplayer.mapper.PlayerMapper;
import fr.efreifive.manageplayer.repository.PlayerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerServiceEventTest {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerEventPublisher playerEventPublisher;

    private final PlayerMapper playerMapper = new PlayerMapper();

    @Test
    void createPublishesPlayerCreatedEvent() {
        PlayerService service = new PlayerService(playerRepository, playerMapper, playerEventPublisher);
        CreatePlayerRequest request = new CreatePlayerRequest(
            "Ada",
            "Lovelace",
            "ada@example.com",
            "0612345678",
            "10/12/1815",
            "femme",
            1.70
        );
        when(playerRepository.create(any(), any())).thenAnswer(invocation -> {
            UUID id = invocation.getArgument(0);
            PlayerDto player = invocation.getArgument(1);
            return new PlayerDto(
                id,
                player.firstName(),
                player.lastName(),
                player.email(),
                player.phone(),
                player.birthDate(),
                player.gender(),
                player.height(),
                player.status(),
                player.statistics(),
                player.teamIds(),
                player.createdAt(),
                player.updatedAt()
            );
        });

        CreatePlayerResponse response = service.create(request);

        ArgumentCaptor<PlayerCreatedEvent> eventCaptor = ArgumentCaptor.forClass(PlayerCreatedEvent.class);
        verify(playerEventPublisher).publish(eventCaptor.capture());
        PlayerCreatedEvent event = eventCaptor.getValue();
        assertEquals(response.id(), event.playerId());
        assertEquals("Ada", event.firstName());
        assertEquals("Lovelace", event.lastName());
        assertEquals("ada@example.com", event.email());
        assertEquals("actif", event.status());
    }

    @Test
    void updatePublishesPlayerNameUpdatedEventWhenNameChanges() {
        PlayerService service = new PlayerService(playerRepository, playerMapper, playerEventPublisher);
        UUID id = UUID.randomUUID();
        PlayerDto existingPlayer = player(id, "Ada", "Lovelace");
        when(playerRepository.findById(id)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePlayerResponse response = service.update(id, new UpdatePlayerRequest("Augusta", "King", null, null, null, null, null));

        ArgumentCaptor<PlayerNameUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(PlayerNameUpdatedEvent.class);
        verify(playerEventPublisher).publish(eventCaptor.capture());
        PlayerNameUpdatedEvent event = eventCaptor.getValue();
        assertEquals(id, event.playerId());
        assertEquals("Augusta", event.firstName());
        assertEquals("King", event.lastName());
        assertEquals(id, response.id());
    }

    @Test
    void updateDoesNotPublishNameEventWhenNameDoesNotChange() {
        PlayerService service = new PlayerService(playerRepository, playerMapper, playerEventPublisher);
        UUID id = UUID.randomUUID();
        PlayerDto existingPlayer = player(id, "Ada", "Lovelace");
        when(playerRepository.findById(id)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePlayerResponse response = service.update(id, new UpdatePlayerRequest(null, null, "ada.king@example.com", null, null, null, null));

        verify(playerEventPublisher, never()).publish(any(PlayerNameUpdatedEvent.class));
        assertEquals(id, response.id());
    }

    @Test
    void deletePublishesPlayerDeletedEvent() {
        PlayerService service = new PlayerService(playerRepository, playerMapper, playerEventPublisher);
        UUID id = UUID.randomUUID();
        PlayerDto existingPlayer = player(id, "Ada", "Lovelace");
        PlayerDto deletedPlayer = new PlayerDto(
            id,
            existingPlayer.firstName(),
            existingPlayer.lastName(),
            existingPlayer.email(),
            existingPlayer.phone(),
            existingPlayer.birthDate(),
            existingPlayer.gender(),
            existingPlayer.height(),
            "supprimé",
            existingPlayer.statistics(),
            existingPlayer.teamIds(),
            existingPlayer.createdAt(),
            existingPlayer.updatedAt()
        );
        when(playerRepository.findById(id)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.delete(id)).thenReturn(deletedPlayer);

        DeletePlayerResponse response = service.delete(id);

        ArgumentCaptor<PlayerDeletedEvent> eventCaptor = ArgumentCaptor.forClass(PlayerDeletedEvent.class);
        verify(playerEventPublisher).publish(eventCaptor.capture());
        assertEquals(id, eventCaptor.getValue().playerId());
        assertEquals(id, response.id());
        assertEquals("supprimé", response.status());
    }

    private PlayerDto player(UUID id, String firstName, String lastName) {
        return new PlayerDto(
            id,
            firstName,
            lastName,
            "ada@example.com",
            "0612345678",
            "10/12/1815",
            "femme",
            1.70,
            "actif",
            new PlayerStatisticsDto(0, 0, 0, 0, 0, 0, 0),
            List.of(),
            "2026-04-30T12:00:00Z",
            "2026-04-30T12:00:00Z"
        );
    }
}
