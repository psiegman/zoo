package nl.siegmann.zoo.frontend.service;

import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;
import nl.siegmann.zoo.axon.command.AnimalCreateCommand;
import nl.siegmann.zoo.axon.command.AnimalDeleteCommand;
import nl.siegmann.zoo.axon.command.AnimalLikeCommand;
import nl.siegmann.zoo.axon.command.AnimalUpdateStatusCommand;
import nl.siegmann.zoo.frontend.dto.AnimalDTO;
import nl.siegmann.zoo.query.entity.Animal;
import nl.siegmann.zoo.query.entity.Species;
import nl.siegmann.zoo.query.repository.QueryAnimalRepository;
import nl.siegmann.zoo.query.repository.QuerySpeciesRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class AnimalService {

    private final ModelMapper modelMapper;
    private final QueryAnimalRepository queryAnimalRepository;
    private final CommandGateway commandGateway;
    private final QuerySpeciesRepository querySpeciesRepository;

    public AnimalService(ModelMapper modelMapper, QueryAnimalRepository queryAnimalRepository, CommandGateway commandGateway, QuerySpeciesRepository querySpeciesRepository) {
        this.modelMapper = modelMapper;
        this.queryAnimalRepository = queryAnimalRepository;
        this.commandGateway = commandGateway;
        this.querySpeciesRepository = querySpeciesRepository;
    }

    /**
     * Get the animal with the given name.
     *
     * @param animalName
     * @return The animal with the given name or null if not found.
     */
    public Optional<AnimalDTO> getAnimalByName(String animalName) {
        return queryAnimalRepository.findByName(animalName).map(this::map);
    }

    /**
     * Map an Animal entity to the AnimalDTO.
     *
     * @param animal
     * @return
     */
    private AnimalDTO map(Animal animal) {
        return modelMapper.map(animal, AnimalDTO.class);
    }

    /**
     * Finds all Animals.
     *
     * @return
     */
    public List<AnimalDTO> findAll() {
        return queryAnimalRepository
                .findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /**
     * Finds the animal to like by Id or Name and sends a AnimalLikeCommand for it.
     *
     * @param animalName
     * @return Whether the animal was found and a like command was sent successfully.
     */
    public boolean likeAnimalByIdOrName(String animalName) {
        Animal animal = queryAnimalRepository.findByIdOrName(animalName).orElse(null);
        if (animal == null) {
            return false;
        }
        commandGateway.send(AnimalLikeCommand
                .builder()
                .animalId(animal.getId())
                .build());
        return true;
    }

    /**
     * Finds the Animal by name or id, and sets the status to the given status if found.
     *
     * @param animalName
     * @param statusName
     * @return Whether the animal was found and an update command was sent.
     */
    public boolean updateAnimalStatusByIdOrName(String animalName, String statusName) {
        Animal animal = queryAnimalRepository.findByIdOrName(animalName).orElse(null);
        if (animal == null) {
            return false;
        }
        commandGateway.send(AnimalUpdateStatusCommand
                .builder()
                .animalId(animal.getId())
                .status(AnimalAggregate.Status.valueOf(statusName.toUpperCase()))
                .build());
        return true;
    }

    /**
     * Deletes all Animals.
     */
    public void deleteAll() {
        queryAnimalRepository.findAll().forEach(animal -> commandGateway.send(AnimalDeleteCommand.builder().animalId(animal.getId()).build()));
    }

    /**
     * Creates the given animals by creating and sending CreateAnimalCommands for them.
     *
     * @param animals
     * @return The number of Animals created.
     */
    public int createAnimals(List<AnimalDTO> animals) {
        AtomicInteger atomicInteger = new AtomicInteger();
        animals.forEach(animalDTO -> {
            commandGateway.send(AnimalCreateCommand
                    .builder()
                    .animalId(animalDTO.getId() == null ? UUID.randomUUID().toString() : animalDTO.getId())
                    .name(animalDTO.getName())
                    .speciesId(animalDTO.getSpeciesId())
                    .nrLikes(animalDTO.getNrLikes())
                    .status(animalDTO.getStatus() == null ? null : AnimalAggregate.Status.valueOf(animalDTO.getStatus().name()))
                    .build());
            atomicInteger.incrementAndGet();
        });
        return atomicInteger.get();
    }

    /**
     * Finds all the Animals for the given species.
     *
     * @param speciesName
     * @return All the Animals of the given Species.
     * @throws RuntimeException if no species with this name are found
     */
    public List<AnimalDTO> findAllAnimalsBySpeciesName(String speciesName) {
        Species species = querySpeciesRepository.findByName(speciesName).orElseThrow(() -> new RuntimeException("Species with name '" + speciesName + "' not found"));
        return queryAnimalRepository
                .findBySpeciesId(species.getId())
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
