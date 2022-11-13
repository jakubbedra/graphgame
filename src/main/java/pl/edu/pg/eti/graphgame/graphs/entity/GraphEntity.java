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
    @Column(length=36)
    private String uuid;

    @Column(length=36)
    private String task;

    // TODO: test whether varchar length limit works
    @Column(length=4095)
    private String json;

}
