package nl.siegmann.zoo.query.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "animal")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

	public enum Status {
		ASLEEP,
		AWAKE
	}

	@Id
	@Column(length = 64)
	private String id;

	@Column(unique = true)
	private String name;

	private long nrLikes;

	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "species_id")
	private Species species;
}
