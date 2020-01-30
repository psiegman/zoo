package nl.siegmann.zoo.query.repository;

import nl.siegmann.zoo.query.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QueryAnimalRepository extends JpaRepository<Animal, String> {
	Optional<Animal> findByName(String animalName);

	List<Animal> findBySpeciesId(String id);

	boolean existsAnimalByName(String animalName);

	@Query("SELECT a FROM Animal a WHERE a.id = :animalIdOrName OR a.name = :animalIdOrName")
	Optional<Animal> findByIdOrName(String animalIdOrName);

	@Modifying
	@Query("UPDATE Animal a SET nrLikes = :nrLikes WHERE a.id = :animalId")
	void updateNrLikes(String animalId, long nrLikes);

	@Modifying
	@Query("UPDATE Animal a SET status = :status WHERE a.id = :animalId")
	void updateStatus(String animalId, Animal.Status status);
}
