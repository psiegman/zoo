package nl.siegmann.zoo.query.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "species")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Species {

    @Id
    @Column(length = 64)
    private String id;

    @Column(unique = true)
    private String name;

    private String emoji;
}
