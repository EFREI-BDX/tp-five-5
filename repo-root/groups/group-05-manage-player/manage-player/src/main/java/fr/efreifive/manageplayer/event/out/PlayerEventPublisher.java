package fr.efreifive.manageplayer.event.out;

public interface PlayerEventPublisher {
    void publish(PlayerCreatedEvent event);

    void publish(PlayerNameUpdatedEvent event);

    void publish(PlayerDeletedEvent event);
}
