package nl.siegmann.zoo.query.repository;

import nl.siegmann.zoo.query.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuerySpeciesRepository extends JpaRepository<Species, String> {
	Optional<Species> findByName(String speciesName);
}
