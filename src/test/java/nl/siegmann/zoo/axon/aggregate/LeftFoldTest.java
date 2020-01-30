package nl.siegmann.zoo.axon.aggregate;

import lombok.Value;
import org.junit.Test;

import java.util.stream.Stream;

public class LeftFoldTest {

	@Test
	public void example() {
		Stream<Object> events = Stream.of(new NameChanged("Pipo"), new AddressChanged("CircusTent"), new NameChanged("Mamaloe"));
		PersonAggregate aggregate = events.reduce(
				new PersonAggregate(null, null),
				(person, event) -> {
					if(event instanceof NameChanged) {
						return new PersonAggregate(((NameChanged) event).getName(), person.getAddress());
					} else if(event instanceof AddressChanged) {
						return new PersonAggregate(person.getName(), ((AddressChanged) event).getAddress());
					}
					return person;
				},
				(previousAggregate, newAggregate) -> newAggregate
		);

		System.out.println(aggregate);
	}

	@Value
	static class NameChanged {
		final String name;
	}

	@Value
	static class AddressChanged {
		final String address;
	}

	@Value
	static class PersonAggregate {
		final String name;
		final String address;
	}
}
