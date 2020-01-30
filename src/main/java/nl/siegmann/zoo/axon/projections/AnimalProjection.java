package nl.siegmann.zoo.axon.projections;

import lombok.extern.slf4j.Slf4j;
import nl.siegmann.zoo.axon.event.AnimalCreatedEvent;
import nl.siegmann.zoo.axon.event.AnimalDeletedEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedLikesEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedStatusEvent;
import nl.siegmann.zoo.query.entity.Animal;
import nl.siegmann.zoo.query.entity.Species;
import nl.siegmann.zoo.query.repository.QueryAnimalRepository;
import nl.siegmann.zoo.query.repository.QuerySpeciesRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * In charge of updating the query tables in response to Animal events.
 */
@Component
@Slf4j
public class AnimalProjection {

	private final QueryAnimalRepository queryAnimalRepository;
	private final QuerySpeciesRepository querySpeciesRepositoryRepository;

	public AnimalProjection(QueryAnimalRepository queryAnimalRepository, QuerySpeciesRepository querySpeciesRepositoryRepository) {
		this.queryAnimalRepository = queryAnimalRepository;
		this.querySpeciesRepositoryRepository = querySpeciesRepositoryRepository;
	}

	@EventHandler
	public void on(AnimalCreatedEvent event) {
		Optional<Species> optionalSpecies = querySpeciesRepositoryRepository.findById(event.getSpeciesId());
		if (!optionalSpecies.isPresent()) {
			log.error("Species with id {} not found", event.getSpeciesId());
			return;
		}
		Animal animal = Animal
				.builder()
				.id(event.getAnimalId())
				.name(event.getName())
				.species(optionalSpecies.get())
				.nrLikes(event.getNrLikes())
				.status(event.getStatus() == null ? null : Animal.Status.valueOf(event.getStatus().toString()))
				.build();
		queryAnimalRepository.save(animal);
	}

	@EventHandler
	public void on(AnimalUpdatedLikesEvent animalUpdatedLikesEvent) {
		queryAnimalRepository.updateNrLikes(animalUpdatedLikesEvent.getAnimalId(), animalUpdatedLikesEvent.getNrLikes());
	}

	@EventHandler
	public void on(AnimalUpdatedStatusEvent animalUpdatedStatusEvent) {
		Animal.Status status = Animal.Status.valueOf(animalUpdatedStatusEvent.getStatus().name());
		queryAnimalRepository.updateStatus(animalUpdatedStatusEvent.getAnimalId(), status);
	}

	@EventHandler
	public void on(AnimalDeletedEvent animalDeletedEvent) {
		queryAnimalRepository.deleteById(animalDeletedEvent.getAnimalId());
	}
}
