package nl.siegmann.zoo.frontend.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.zoo.axon.event.AnimalCreatedEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedLikesEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedStatusEvent;
import org.axonframework.eventhandling.EventHandler;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Listens to Axon Events and sends events to the web clients.
 */
@Component
@Slf4j
public class FrontendEventService {

    /**
     * Event sent to the frontend.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnimalCreatedFrontendEvent {

        private String animalId;
        private String name;
        private String speciesId;
        private long nrLikes;
        private String status;
    }

    /**
     * Event sent to the frontend.
     */
    @Data
    @Builder
    public static class AnimalUpdatedLikesFrontendEvent {
        private String animalId;
        private long nrLikes;
    }

    /**
     * Event sent to the frontend.
     */
    @Data
    @Builder
    public static class AnimalUpdatedStatusFrontendEvent {
        private String animalId;
        private String status;
    }

    private final SimpMessagingTemplate messageTemplate;
    private final ModelMapper modelMapper;

    public FrontendEventService(SimpMessagingTemplate messageTemplate, ModelMapper modelMapper) {
        this.messageTemplate = messageTemplate;
        this.modelMapper = modelMapper;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void handleAnimalCreatedEvent(AnimalCreatedEvent animalCreatedAxonEvent) {
        AnimalCreatedFrontendEvent animalCreatedFrontendEvent = modelMapper.map(animalCreatedAxonEvent, AnimalCreatedFrontendEvent.class);
        this.messageTemplate.convertAndSend("/animals/create", animalCreatedFrontendEvent);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void handleAnimalUpdatedLikesEvent(AnimalUpdatedLikesEvent animalLikedEvent) {
        AnimalUpdatedLikesFrontendEvent animalLikesUpdatedEvent = AnimalUpdatedLikesFrontendEvent
                .builder()
                .animalId(animalLikedEvent.getAnimalId())
                .nrLikes(animalLikedEvent.getNrLikes())
                .build();
        this.messageTemplate.convertAndSend("/animals/likes", animalLikesUpdatedEvent);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void handleAnimalStatusEvent(AnimalUpdatedStatusEvent event) {
        AnimalUpdatedStatusFrontendEvent frontendEvent = AnimalUpdatedStatusFrontendEvent
                .builder()
                .animalId(event.getAnimalId())
                .status(event.getStatus().toString())
                .build();
        this.messageTemplate.convertAndSend("/animals/status", frontendEvent);
    }


}
