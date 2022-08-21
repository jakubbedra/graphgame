package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

    /**
     * Number of edges of the graph related to the task.
     */
    private int graphEdges;

    /**
     * True if the graph has weighted edges.
     */
    private boolean graphWeighted;

    /**
     * Integer value(s) that can be specific to a given task subject. Might be empty.
     */
    private List<Integer> specialValues;

    /**
     * Used for non-trivial description details, such as a special name for the graphs;
     */
    private String descriptionDetails;

    public static TaskQuestion map(Task taskEntity) {
        List<Integer> specialValues = new LinkedList<>();
        if (taskEntity.getSpecialValues() != null && !taskEntity.getSpecialValues().isEmpty()) {
            String[] split = taskEntity.getSpecialValues().split(";");
            Arrays.stream(split).forEach(s -> specialValues.add(Integer.parseInt(s)));
        }
        return TaskQuestion.builder()
                .taskUuid(taskEntity.getUuid())
                .userId(taskEntity.getUser().getId())
                .subject(taskEntity.getSubject())
                .type(taskEntity.getType())
                .graphVertices(taskEntity.getGraphVertices())
                .graphEdges(taskEntity.getGraphEdges())
                .graphWeighted(taskEntity.isGraphWeighted())
                .specialValues(specialValues)
                .descriptionDetails(taskEntity.getDescriptionDetails())
                .build();
    }

}

