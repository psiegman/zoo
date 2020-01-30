package nl.siegmann.zoo.axon.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class SpeciesCreateCommand {

	@TargetAggregateIdentifier
	private String speciesId;

	private String name;
	private String emoji;
}
