package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import java.util.UUID;

@Getter
@Setter
@Builder
public class TaskQuestion {

    /**
     * The UUID of the task.
     */
    private UUID taskUuid;

    /**
     * The uuid of the user who has this task assigned.
     */
    private Long userId;

    /**
     * The subject of the task.
     */
    private GraphTaskSubject subject;

    /**
     * The game mode of the task.
     */
    private GraphTaskType type;

    /**
     * Number of vertices of the graph related to the task.
     */
    private int graphVertices;

    public static TaskQuestion map(Task taskEntity) {
        return TaskQuestion.builder()
                .taskUuid(taskEntity.getUuid())
                .userId(taskEntity.getUser().getId())
                .subject(taskEntity.getSubject())
                .type(taskEntity.getType())
                .graphVertices(taskEntity.getGraphVertices())
                .build();
    }

}
