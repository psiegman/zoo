package nl.siegmann.zoo.axon.command;

import lombok.Builder;
import lombok.Value;
import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class AnimalUpdateStatusCommand {

	@TargetAggregateIdentifier
	private String animalId;

	private AnimalAggregate.Status status;
}
