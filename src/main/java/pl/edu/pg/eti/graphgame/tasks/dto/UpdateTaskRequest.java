package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;

import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UpdateTaskRequest {

    private String name;

    public static BiFunction<TaskSubject, UpdateTaskRequest, TaskSubject> dtoToEntityUpdater() {
        return (task, request) -> {
            task.setName(request.getName());
            return task;
        };
    }

}
