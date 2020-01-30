package nl.siegmann.zoo.frontend.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonTypeName("Animal")
public class AnimalDTO {

    public enum Status {
        ASLEEP,
        AWAKE
    }

    private String id;

    private String name;

    private String speciesId;

    private long nrLikes;

    private Status status;
}
