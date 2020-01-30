package nl.siegmann.zoo.axon.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnimalUpdatedLikesEvent {

	private String animalId;
	private long nrLikes;
}
