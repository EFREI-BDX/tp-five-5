package fr.efreifive.manageplayer.event.out;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringPlayerEventPublisher implements PlayerEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringPlayerEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(PlayerCreatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(PlayerNameUpdatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(PlayerDeletedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
