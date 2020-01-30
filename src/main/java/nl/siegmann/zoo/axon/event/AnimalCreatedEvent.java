package nl.siegmann.zoo.axon.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;

@Getter
@Builder
@AllArgsConstructor
public class AnimalCreatedEvent {

	private String animalId;
	private String name;
	private String speciesId;
	private long nrLikes;
	private AnimalAggregate.Status status;
}
