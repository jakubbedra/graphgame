package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetTaskResponse {

    private String name;

    public static Function<TaskSubject, GetTaskResponse> entityToDtoMapper() {
        return task -> GetTaskResponse.builder()
                .name(task.getName())
                .build();
    }

}
