package pl.edu.pg.eti.graphgame.graphs.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "graphs")
public class GraphEntity {

    @Id
    private UUID uuid;

    private UUID task;

    // TODO: test whether varchar length limit works
    @Column(length=2047)
    private String json;

}
