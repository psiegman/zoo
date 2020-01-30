package nl.siegmann.zoo.frontend.service;

import nl.siegmann.zoo.frontend.dto.PenDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PenService {

	private final AnimalService animalService;
	private final SpeciesService speciesService;

	public PenService(AnimalService animalService, SpeciesService speciesService) {
		this.animalService = animalService;
		this.speciesService = speciesService;
	}

	public Collection<PenDTO> getAllPens() {
		Map<String, PenDTO> pensBySpeciesId = new HashMap<>();
		speciesService.findAll().forEach(s -> pensBySpeciesId.put(s.getId(), PenDTO.builder().species(s).build()));
		animalService.findAll().forEach(animal -> pensBySpeciesId.get(animal.getSpeciesId()).getAnimals().add(animal));
		List<PenDTO> result = new ArrayList<>(pensBySpeciesId.values());
		result.sort(Comparator.comparing((p) -> p.getSpecies().getName()));
		return result;
	}
}
