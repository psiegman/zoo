package nl.siegmann.zoo.axon.aggregate;

import nl.siegmann.zoo.axon.command.AnimalCreateCommand;
import nl.siegmann.zoo.axon.command.AnimalLikeCommand;
import nl.siegmann.zoo.axon.event.AnimalCreatedEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedLikesEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedStatusEvent;
import nl.siegmann.zoo.query.repository.QueryAnimalRepository;
import nl.siegmann.zoo.query.repository.QuerySpeciesRepository;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class AnimalAggregateTest {

	@Mock
	private QueryAnimalRepository queryAnimalRepository;

	private FixtureConfiguration<AnimalAggregate> fixture;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		fixture = new AggregateTestFixture<>(AnimalAggregate.class);
		fixture.registerInjectableResource(queryAnimalRepository);
	}

	@Test
	public void testCreateAnimal() {
		String animalId = UUID.randomUUID().toString();
		String speciesId = UUID.randomUUID().toString();
		fixture.given(Collections.emptyList())
				.when(AnimalCreateCommand.builder().animalId(animalId).name("annie").speciesId(speciesId).status(AnimalAggregate.Status.ASLEEP).nrLikes(3).build())
				.expectSuccessfulHandlerExecution()
				.expectEvents(AnimalCreatedEvent.builder().animalId(animalId).name("annie").nrLikes(3).speciesId(speciesId).status(AnimalAggregate.Status.ASLEEP).build());
	}

	@Test
	public void testLikeAnimal_default() {
		String animalId = UUID.randomUUID().toString();
		fixture.given(AnimalCreatedEvent.builder().animalId(animalId).name("annie").nrLikes(17).status(AnimalAggregate.Status.AWAKE).build())
				.when(AnimalLikeCommand.builder().animalId(animalId).build())
				.expectSuccessfulHandlerExecution()
				.expectEvents(AnimalUpdatedLikesEvent.builder().animalId(animalId).nrLikes(18).build());
	}

	@Test
	public void testLikeAnimal_asleep() {
		String animalId = UUID.randomUUID().toString();
		fixture
				.given(AnimalCreatedEvent.builder().animalId(animalId).name("annie").nrLikes(17).build())
				.andGiven(AnimalUpdatedStatusEvent.builder().animalId(animalId).status(AnimalAggregate.Status.ASLEEP).build())
				.when(AnimalLikeCommand.builder().animalId(animalId).build())
				.expectSuccessfulHandlerExecution()
				.expectNoEvents();
	}
}