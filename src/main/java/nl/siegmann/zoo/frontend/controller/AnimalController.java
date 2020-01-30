package nl.siegmann.zoo.frontend.controller;

import nl.siegmann.zoo.frontend.dto.AnimalDTO;
import nl.siegmann.zoo.frontend.service.AnimalService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("")
    public List<AnimalDTO> getAllAnimals() {
        return animalService.findAll();
    }

    @GetMapping(value = "", params = {"species"})
    public List<AnimalDTO> getAnimalsBySpecies(@RequestParam("species") String speciesName) {
        return animalService.findAllAnimalsBySpeciesName(speciesName);
    }

    @GetMapping("/{animalName}")
    public AnimalDTO getAnimal(@PathVariable String animalName) {
        return animalService
                .getAnimalByName(animalName)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find Animal '" + animalName + "'"));
    }

    @PostMapping("/{animalIdOrName}/like")
    public void likeAnimal(@PathVariable String animalIdOrName) {
        if (! animalService.likeAnimalByIdOrName(animalIdOrName)) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find Animal with id or name  '" + animalIdOrName + "'");
        }
    }

    @PostMapping("/{animalIdOrName}/status/{status}")
    public void updateAnimalStatus(@PathVariable String animalIdOrName, @PathVariable String status) {
        if (! animalService.updateAnimalStatusByIdOrName(animalIdOrName, status)) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find Animal with id or name  '" + animalIdOrName + "'");
        }
    }

    @PutMapping("/create")
    public int createAnimals(@Valid @RequestBody List<AnimalDTO> animals) {
        return animalService.createAnimals(animals);
    }

    @DeleteMapping("/delete_all")
    public void deleteAllAnimals() {
        animalService.deleteAll();
    }
}
