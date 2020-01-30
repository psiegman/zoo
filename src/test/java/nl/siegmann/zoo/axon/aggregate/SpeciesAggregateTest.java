package nl.siegmann.zoo.axon.aggregate;

import nl.siegmann.zoo.axon.command.SpeciesCreateCommand;
import nl.siegmann.zoo.axon.event.SpeciesCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.UUID;

public class SpeciesAggregateTest {

	private FixtureConfiguration<SpeciesAggregate> fixture;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		fixture = new AggregateTestFixture<>(SpeciesAggregate.class);
	}

	@Test
	public void testCreateSpecies() {
		String speciesId = UUID.randomUUID().toString();
		fixture.given(Collections.emptyList())
				.when(SpeciesCreateCommand.builder().speciesId(speciesId).name("ant").emoji("\uD83D\uDC1C").build())
				.expectSuccessfulHandlerExecution()
				.expectEvents(SpeciesCreatedEvent.builder().speciesId(speciesId).name("ant").emoji("\uD83D\uDC1C").build());
	}

}