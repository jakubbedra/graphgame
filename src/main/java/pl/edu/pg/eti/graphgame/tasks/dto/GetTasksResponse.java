package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetTasksResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode
    public static class TaskDto {

        private Long id;
        private String name;

    }

    @Singular
    private List<TaskDto> tasks;

    public static Function<Collection<TaskSubject>, GetTasksResponse> entityToDtoMapper() {
        return tasks -> {
            GetTasksResponseBuilder response = GetTasksResponse.builder();
            tasks.stream()
                    .map(task -> TaskDto.builder()
                            .id(task.getId())
                            .name(task.getName())
                            .build())
                    .forEach(response::task);
            return response.build();
        };
    }

}
