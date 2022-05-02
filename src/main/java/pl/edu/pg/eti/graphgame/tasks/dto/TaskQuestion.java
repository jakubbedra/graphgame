package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;

import java.util.UUID;

@Getter
@Setter
@Builder
public class TaskQuestion {

    /**
     * The UUID of the task
     */
    private UUID taskUuid;

    /**
     * The uuid of the user who has this task assigned
     */
    private Long userId;

    /**
     * The subject of the task
     */
    private GraphTaskSubject graphTaskSubject;

    /**
     * The game mode of the task
     */
    private GraphTaskType graphTaskType;

    /**
     * Te content of the task (only the string explaining what to do)
     */
    private String content;

    /**
     * For now placeholder, will contain the generated graph, if the task needs it
     */
    private String graph;

}
