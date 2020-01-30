package nl.siegmann.zoo.dataloader;

import nl.siegmann.zoo.axon.command.AnimalCreateCommand;
import nl.siegmann.zoo.axon.command.SpeciesCreateCommand;
import nl.siegmann.zoo.frontend.dto.AnimalDTO;
import nl.siegmann.zoo.frontend.dto.SpeciesDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import java.io.Reader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads initial zoo data.
 * <p>
 * Data example:
 * <pre>
 * !!Zoo
 * menagerie:
 *   species:
 *     ant:
 *       emoji: 🐜
 *       animals:
 *         annie:
 *           nrLikes: 3
 *         albert:
 * </pre>
 */
@Component
public class ZooDataLoader {

	private final CommandGateway commandGateway;

	public ZooDataLoader(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	public void loadTestData(Reader reader) {
		Yaml yaml = new Yaml();
		Zoo zooData = yaml.load(reader);

		List<SpeciesCreateCommand> speciesCreateCommands = createSpecies(zooData);
		createAnimals(zooData, speciesCreateCommands);
	}

	private List<SpeciesCreateCommand> createSpecies(Zoo zoo) {
		List<SpeciesCreateCommand> speciesCreateCommands = new ArrayList();
		zoo
				.getMenagerie()
				.getSpecies()
				.forEach((name, speciesData) -> {
					SpeciesCreateCommand speciesCreateCommand = SpeciesCreateCommand
							.builder()
							.speciesId(UUID.randomUUID().toString())
							.name(name)
							.emoji(speciesData == null ? "" : speciesData.getEmoji())
							.build();
					speciesCreateCommands.add(speciesCreateCommand);
					commandGateway.send(speciesCreateCommand);
				});
		return speciesCreateCommands;
	}

	private void createAnimals(Zoo zoo, Collection<SpeciesCreateCommand> speciesCreateCommands) {
		// make species-by-name lookup map
		Map<String, SpeciesCreateCommand> speciesCreateCommandByName = speciesCreateCommands.stream().collect(Collectors.toMap(SpeciesCreateCommand::getName, Function.identity()));

		// create and send AnimalCreateCommands
		zoo.getMenagerie().getSpecies().forEach((speciesName, species) -> {
			species.getAnimals().forEach((animalName, animalData) -> {
				AnimalCreateCommand animalCreateCommand = AnimalCreateCommand
					.builder()
					.animalId(UUID.randomUUID().toString())
					.name(animalName)
					.speciesId(speciesCreateCommandByName.get(speciesName).getSpeciesId())
					.nrLikes(animalData == null || animalData.getNrLikes() == null ? 0 : animalData.getNrLikes())
					.build();
				commandGateway.send(animalCreateCommand);
			});
		});
	}
}
