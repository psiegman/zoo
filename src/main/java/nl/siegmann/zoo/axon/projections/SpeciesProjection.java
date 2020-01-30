package nl.siegmann.zoo.axon.projections;

import lombok.extern.slf4j.Slf4j;
import nl.siegmann.zoo.axon.event.SpeciesCreatedEvent;
import nl.siegmann.zoo.frontend.service.SpeciesService;
import nl.siegmann.zoo.query.entity.Species;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

/**
 * In charge of updating the query tables in response to Species events.
 */
@Component
@Slf4j
public class SpeciesProjection {

    private final SpeciesService speciesService;

    public SpeciesProjection(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @EventHandler
    public void on(SpeciesCreatedEvent event) {
        Species species = Species
                .builder()
                .id(event.getSpeciesId())
                .name(event.getName())
                .emoji(event.getEmoji())
                .build();
        speciesService.save(species);
    }
}
