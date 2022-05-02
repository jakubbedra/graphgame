package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;

import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CreateTaskRequest {

    private String name;

    public static Function<CreateTaskRequest, TaskSubject> dtoToEntityMapper() {
        return request -> TaskSubject.builder()
                .name(request.getName())
                .build();
    }

}
