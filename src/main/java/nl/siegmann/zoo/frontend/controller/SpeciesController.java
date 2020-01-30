package nl.siegmann.zoo.frontend.controller;

import nl.siegmann.zoo.frontend.dto.SpeciesDTO;
import nl.siegmann.zoo.frontend.service.SpeciesService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/species")
public class SpeciesController {

	private final SpeciesService speciesService;

	public SpeciesController(SpeciesService speciesService) {
		this.speciesService = speciesService;
	}

	@GetMapping("")
	public List<SpeciesDTO> getAllSpecies() {
		return speciesService.findAll();
	}

	@PutMapping("/create")
	public int createSpecies(@Valid @RequestBody List<SpeciesDTO> species, HttpServletResponse response) throws IOException {
		return speciesService.createSpecies(species);
	}

	@DeleteMapping("/delete_all")
	public void deleteAllSpecies() {
		speciesService.deleteAll();
	}
}
