package pl.edu.pg.eti.graphgame.stats.enitity;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.users.entity.User;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

/**
 * Class containing the user's statistics about the specific type of task during a day.
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "task_stats")
public class Stats {

    @Id
    @Column(length=36)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    private GraphTaskSubject graphTaskSubject;

    private Date date;

    /**
     * The amount of correctly solved tasks by the user on the specific day
     */
    private int correct;

    /**
     * The amount of wrongly solved tasks by the user on the specific day
     */
    private int wrong;

}

