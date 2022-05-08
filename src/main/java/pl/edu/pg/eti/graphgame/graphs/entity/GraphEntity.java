package pl.edu.pg.eti.graphgame.graphs.entity;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

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

    @OneToOne
    @JoinColumn(name = "task")
    private Task task;

    private String json;

}
