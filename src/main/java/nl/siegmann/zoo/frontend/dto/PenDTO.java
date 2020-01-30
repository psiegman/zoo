package nl.siegmann.zoo.frontend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonTypeName("Pen")
public class PenDTO {

    private SpeciesDTO species;

	@JsonProperty("animals")
    @Builder.Default
    private final Collection<AnimalDTO> animals = new ArrayList();

    public int hashCode() {
        return species.hashCode();
    }
}
