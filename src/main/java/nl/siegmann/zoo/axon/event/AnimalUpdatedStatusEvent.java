package nl.siegmann.zoo.axon.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;

@Getter
@Builder
@AllArgsConstructor
public class AnimalUpdatedStatusEvent {

	private final String animalId;
	private final AnimalAggregate.Status status;
}
