package nl.siegmann.zoo.frontend.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("Species")
public class SpeciesDTO {
    private String id;

    private String name;

    private String emoji;

    public int hashCode() {
        return id.hashCode();
    }

}
