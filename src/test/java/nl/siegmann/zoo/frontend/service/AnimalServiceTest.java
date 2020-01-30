package nl.siegmann.zoo.frontend.service;

import nl.siegmann.zoo.axon.aggregate.AnimalAggregate;
import nl.siegmann.zoo.axon.command.AnimalCreateCommand;
import nl.siegmann.zoo.frontend.dto.AnimalDTO;
import nl.siegmann.zoo.query.repository.QueryAnimalRepository;
import nl.siegmann.zoo.query.repository.QuerySpeciesRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class AnimalServiceTest {

    @MockBean
    private QueryAnimalRepository animalRepository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private CommandGateway commandGateway;

    @MockBean
    private QuerySpeciesRepository querySpeciesRepository;

    private AnimalService animalService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.animalService = new AnimalService(modelMapper, animalRepository, commandGateway, querySpeciesRepository);
    }

    @Test
    public void createAnimals_empty() {
        // given
        List<AnimalDTO> animals = Collections.emptyList();

        // when
        int nrAnimalsCreated = animalService.createAnimals(animals);

        // then
        Assert.assertEquals(0, nrAnimalsCreated);
        Mockito.verifyNoMoreInteractions(commandGateway);
    }

    private static <T> ArgumentMatcher<T> matches(Predicate<T> predicate) {
        return argument -> predicate.test((T) argument);
    }

    @Test
    public void createAnimals_one_valid() {
        // given
        String animalId = UUID.randomUUID().toString();
        String speciesId = UUID.randomUUID().toString();
        List<AnimalDTO> animals = Collections.singletonList(AnimalDTO
                .builder()
                .id(animalId)
                .name("test-animal")
                .nrLikes(3)
                .speciesId(speciesId)
                .status(AnimalDTO.Status.ASLEEP)
                .build());

        // when
        int nrAnimalsCreated = animalService.createAnimals(animals);

        // then
        Assert.assertEquals(1, nrAnimalsCreated);
        Mockito.verify(commandGateway).send(AnimalCreateCommand.builder()
                .animalId(animalId)
                .name("test-animal")
                .nrLikes(3)
                .speciesId(speciesId)
                .status(AnimalAggregate.Status.ASLEEP)
                .build());
        Mockito.verifyNoMoreInteractions(commandGateway);
    }

    @Test
    public void testUpdateAnimalStatusByIdOrName_not_found() {

    }
}