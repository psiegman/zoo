package nl.siegmann.zoo.axon.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class AnimalDeleteCommand {

    @TargetAggregateIdentifier
    private String animalId;
}
