package nl.siegmann.zoo.axon.aggregate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.zoo.axon.command.SpeciesCreateCommand;
import nl.siegmann.zoo.axon.event.SpeciesCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Data
@Slf4j
public class SpeciesAggregate {

    @AggregateIdentifier
    private String id;

    SpeciesAggregate() {
    }

    @CommandHandler
    public SpeciesAggregate(SpeciesCreateCommand command) {
        AggregateLifecycle.apply(SpeciesCreatedEvent
                .builder()
                .speciesId(command.getSpeciesId())
                .emoji(command.getEmoji())
                .name(command.getName())
                .build());
    }

    @EventSourcingHandler
    public void on(SpeciesCreatedEvent event) {
        this.id = event.getSpeciesId();
    }
}
