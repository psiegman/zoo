package nl.siegmann.zoo.axon.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SpeciesCreatedEvent {

	private String speciesId;
	private String name;
	private String emoji;
}
