package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetTaskSubjectsResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode
    public static class TaskSubjectDto {

        private String value;
        private String label;

    }

    @Singular
    private List<TaskSubjectDto> subjects;

    public static Function<Collection<GraphTaskSubject>, GetTaskSubjectsResponse> entityToDtoMapper() {
        return tasks -> {
            GetTaskSubjectsResponseBuilder response = GetTaskSubjectsResponse.builder();
            tasks.stream()
                    .map(subject -> TaskSubjectDto.builder()
                            .value(subject.name())
                            .label(subject.label)
                            .build())
                    .forEach(response::subject);
            return response.build();
        };
    }

}
