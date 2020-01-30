package nl.siegmann.zoo.axon.command;

import lombok.Builder;
import lombok.Value;
import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class AnimalCreateCommand {

	@TargetAggregateIdentifier
	private String animalId;

	private String name;
	private String speciesId;
	private long nrLikes;
	private AnimalAggregate.Status status;
}
