package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CreateTaskRequest {

    private String name;

    public static Function<CreateTaskRequest, Task> dtoToEntityMapper() {
        return request -> Task.builder()
                .name(request.getName())
                .build();
    }

}
