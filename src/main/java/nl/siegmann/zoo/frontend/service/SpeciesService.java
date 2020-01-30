package nl.siegmann.zoo.frontend.service;

import nl.siegmann.zoo.axon.command.SpeciesCreateCommand;
import nl.siegmann.zoo.frontend.dto.SpeciesDTO;
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
public class SpeciesService {

    private final CommandGateway commandGateway;
    private final QuerySpeciesRepository querySpeciesRepository;
    private final ModelMapper modelMapper;

    public SpeciesService(CommandGateway commandGateway, QuerySpeciesRepository querySpeciesRepository, ModelMapper modelMapper) {
        this.commandGateway = commandGateway;
        this.querySpeciesRepository = querySpeciesRepository;
        this.modelMapper = modelMapper;
    }

    public List<SpeciesDTO> findAll() {
        return querySpeciesRepository
                .findAll()
                .stream()
                .map(s -> modelMapper.map(s, SpeciesDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        querySpeciesRepository.deleteAll();
    }

    public void save(nl.siegmann.zoo.query.entity.Species species) {
        querySpeciesRepository.save(species);
    }

    public int createSpecies(List<SpeciesDTO> speciesDTOS) {
        AtomicInteger atomicInteger = new AtomicInteger();
        speciesDTOS.forEach(aSpeciesDTO -> {
            commandGateway.send(SpeciesCreateCommand
                    .builder()
                    .speciesId(UUID.randomUUID().toString())
                    .name(aSpeciesDTO.getName())
                    .emoji(aSpeciesDTO.getEmoji())
                    .build());
            atomicInteger.incrementAndGet();
        });
        return atomicInteger.get();

    }

    public Optional<SpeciesDTO> findById(String speciesId) {
        return querySpeciesRepository
                .findById(speciesId)
                .map(s -> modelMapper.map(s, SpeciesDTO.class));
    }
}
