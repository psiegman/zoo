package nl.siegmann.zoo.axon.aggregate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.zoo.axon.command.AnimalCreateCommand;
import nl.siegmann.zoo.axon.command.AnimalDeleteCommand;
import nl.siegmann.zoo.axon.command.AnimalLikeCommand;
import nl.siegmann.zoo.axon.command.AnimalUpdateStatusCommand;
import nl.siegmann.zoo.axon.event.AnimalCreatedEvent;
import nl.siegmann.zoo.axon.event.AnimalDeletedEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedLikesEvent;
import nl.siegmann.zoo.axon.event.AnimalUpdatedStatusEvent;
import nl.siegmann.zoo.query.repository.QueryAnimalRepository;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.modelmapper.internal.util.Objects;

@Aggregate
@Data
@Slf4j
public class AnimalAggregate {

	public enum Status {
		ASLEEP,
		AWAKE
	}


	@AggregateIdentifier
	private String id;
	private long nrLikes;
	private Status status = Status.AWAKE;

	AnimalAggregate() {
	}

	@CommandHandler
	public AnimalAggregate(AnimalCreateCommand command, QueryAnimalRepository queryAnimalRepository) {
		if (StringUtils.isBlank(command.getName())) {
			throw new RuntimeException("Animal name must not be blank");
		}
		if (queryAnimalRepository.existsAnimalByName(command.getName())) {
			throw new RuntimeException("Animal name must be unique, '" + command.getName() + "' already exists");
		}
		AggregateLifecycle.apply(AnimalCreatedEvent
				.builder()
				.animalId(command.getAnimalId())
				.name(command.getName())
				.speciesId(command.getSpeciesId())
				.nrLikes(command.getNrLikes())
				.status(Objects.firstNonNull(command.getStatus(), Status.AWAKE))
				.build());
	}

	@CommandHandler
	public void on(AnimalLikeCommand command) {
		if (this.status != Status.AWAKE) {
			return;
		}
		AggregateLifecycle.apply(new AnimalUpdatedLikesEvent(command.getAnimalId(), nrLikes + 1));
	}

	@CommandHandler
	public void on(AnimalUpdateStatusCommand command) {
		AggregateLifecycle.apply(new AnimalUpdatedStatusEvent(command.getAnimalId(), command.getStatus()));
	}

	@CommandHandler
	public void on(AnimalDeleteCommand command) {
		AggregateLifecycle.apply(new AnimalDeletedEvent(command.getAnimalId()));
	}

	@EventSourcingHandler
	public void on(AnimalCreatedEvent event) {
		this.id = event.getAnimalId();
		this.nrLikes = event.getNrLikes();
		this.status = event.getStatus();
	}

	@EventSourcingHandler
	public void on(AnimalUpdatedLikesEvent event) {
		this.nrLikes = event.getNrLikes();
	}

	@EventSourcingHandler
	public void on(AnimalUpdatedStatusEvent event) {
		this.status = event.getStatus();
	}

	@EventSourcingHandler
	public void on(AnimalDeletedEvent event) {
		AggregateLifecycle.markDeleted();
	}
}
