package pl.edu.pg.eti.graphgame.tasks.entity;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;
import pl.edu.pg.eti.graphgame.users.entity.User;

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
@Table(name = "tasks")
public class Task {

    @Id
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    private GraphTaskSubject subject;

    private GraphTaskType type;

    private int graphVertices;

    private int graphEdges;

    private boolean graphWeighted = false;

    private String specialValues;

    private String descriptionDetails = "";

}
