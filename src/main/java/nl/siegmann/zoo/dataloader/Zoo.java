package nl.siegmann.zoo.dataloader;

import lombok.Data;

import java.util.Map;


/**
 * Represents the data used in the dataloader yaml.
 */
@Data
public class Zoo {

	@Data
	public static class Menagerie {

		@Data
		public static class Species {

			@Data
			public static class Animal {
				private String id;
				private Long nrLikes;
			}

			private String id;
			private String emoji;
			private Map<String, Animal> animals;
		}

		private Map<String, Species> species;
	}

	private Menagerie menagerie;
}
