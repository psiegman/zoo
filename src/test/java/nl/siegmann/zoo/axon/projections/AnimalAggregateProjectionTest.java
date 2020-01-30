package nl.siegmann.zoo.axon.projections;

import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;
import nl.siegmann.zoo.axon.event.AnimalCreatedEvent;
import nl.siegmann.zoo.query.entity.Animal;
import nl.siegmann.zoo.query.entity.Species;
import nl.siegmann.zoo.query.repository.QueryAnimalRepository;
import nl.siegmann.zoo.query.repository.QuerySpeciesRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class AnimalAggregateProjectionTest {

	@Mock
	private QueryAnimalRepository queryAnimalRepository;

	@Mock
	private QuerySpeciesRepository querySpeciesRepository;

	@InjectMocks
	private AnimalProjection animalProjection;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void onAnimalCreatedEvent() {
		// given
		String animalId = UUID.randomUUID().toString();
		String speciesId = UUID.randomUUID().toString();

		AnimalCreatedEvent animalCreatedEvent = mock(AnimalCreatedEvent.class);
		when(animalCreatedEvent.getAnimalId()).thenReturn(animalId);
		when(animalCreatedEvent.getSpeciesId()).thenReturn(speciesId);
		when(animalCreatedEvent.getNrLikes()).thenReturn(23l);
		when(animalCreatedEvent.getStatus()).thenReturn(AnimalAggregate.Status.AWAKE);
		when(animalCreatedEvent.getName()).thenReturn("test name");

		Species species = mock(Species.class);
		when(querySpeciesRepository.findById(speciesId)).thenReturn(Optional.of(species));

		// when
		animalProjection.on(animalCreatedEvent);

		// then
		verify(animalCreatedEvent).getAnimalId();
		verify(queryAnimalRepository).save(Animal
				.builder()
				.id(animalId)
				.species(species)
				.status(Animal.Status.AWAKE)
				.nrLikes(23)
				.name("test name")
				.build());
	}
}