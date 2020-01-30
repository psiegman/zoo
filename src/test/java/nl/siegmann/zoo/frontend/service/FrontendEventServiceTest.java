package nl.siegmann.zoo.frontend.service;

import nl.siegmann.zoo.ZooApplication;
import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;
import nl.siegmann.zoo.axon.event.AnimalCreatedEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedLikesEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedStatusEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZooApplication.class)
public class FrontendEventServiceTest {

    @Autowired
    private FrontendEventService frontendEventService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    public void handleAnimalCreatedEvent() {
        // given
        String animalId = UUID.randomUUID().toString();
        String speciesId = UUID.randomUUID().toString();

        // when
        frontendEventService.handleAnimalCreatedEvent(AnimalCreatedEvent
                .builder()
                .animalId(animalId)
                .name("test-animal")
                .speciesId(speciesId)
                .nrLikes(17)
                .status(AnimalAggregate.Status.ASLEEP)
                .build());

        // then
        Mockito.verify(messagingTemplate).convertAndSend("/animals/create", FrontendEventService
                .AnimalCreatedFrontendEvent
                .builder()
                .animalId(animalId)
                .name("test-animal")
                .status(AnimalAggregate.Status.ASLEEP.toString())
                .nrLikes(17)
                .speciesId(speciesId)
                .build());
    }

    @Test
    public void handleAnimalUpdatedStatusEvent() {
        // given
        String animalId = UUID.randomUUID().toString();

        // when
        frontendEventService.handleAnimalStatusEvent(AnimalUpdatedStatusEvent.builder().animalId(animalId).status(AnimalAggregate.Status.ASLEEP).build());

        // then
        Mockito.verify(messagingTemplate).convertAndSend("/animals/status", FrontendEventService
                .AnimalUpdatedStatusFrontendEvent
                .builder()
                .animalId(animalId)
                .status(AnimalAggregate.Status.ASLEEP.toString())
                .build());
    }

    @Test
    public void handleAnimalUpdatedLikesEvent() {
        // given
        String animalId = UUID.randomUUID().toString();

        // when
        frontendEventService.handleAnimalUpdatedLikesEvent(AnimalUpdatedLikesEvent.builder().animalId(animalId).nrLikes(17).build());

        // then
        Mockito.verify(messagingTemplate).convertAndSend("/animals/likes", FrontendEventService
                .AnimalUpdatedLikesFrontendEvent
                .builder()
                .animalId(animalId)
                .nrLikes(17)
                .build());
    }

}