package fr.efreifive.manageplayer.event.out;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

class SpringPlayerEventPublisherTest {
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final SpringPlayerEventPublisher publisher = new SpringPlayerEventPublisher(applicationEventPublisher);

    @Test
    void publishPlayerCreatedEventDelegatesToSpringPublisher() {
        PlayerCreatedEvent event = new PlayerCreatedEvent(UUID.randomUUID(), "Ada", "Lovelace", "ada@example.com", "actif");

        publisher.publish(event);

        verify(applicationEventPublisher).publishEvent(event);
    }

    @Test
    void publishPlayerNameUpdatedEventDelegatesToSpringPublisher() {
        PlayerNameUpdatedEvent event = new PlayerNameUpdatedEvent(UUID.randomUUID(), "Ada", "Byron");

        publisher.publish(event);

        verify(applicationEventPublisher).publishEvent(event);
    }

    @Test
    void publishPlayerDeletedEventDelegatesToSpringPublisher() {
        PlayerDeletedEvent event = new PlayerDeletedEvent(UUID.randomUUID());

        publisher.publish(event);

        verify(applicationEventPublisher).publishEvent(event);
    }
}
