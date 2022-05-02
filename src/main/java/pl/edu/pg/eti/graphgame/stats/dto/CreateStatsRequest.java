package pl.edu.pg.eti.graphgame.stats.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.sql.Date;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CreateStatsRequest {

    private Long userId;
    private Long taskId;

    private int correct;
    private int wrong;

    public static Function<CreateStatsRequest, Stats> dtoToEntityMapper(
            Function<Long, User> userFunction,
            Function<Long, TaskSubject> taskFunction,
            Supplier<Date> dateSupplier
    ) {
        return request -> Stats.builder()
                .uuid(UUID.randomUUID())
                .user(userFunction.apply(request.getUserId()))
                .taskSubject(taskFunction.apply(request.getTaskId()))
                .date(dateSupplier.get())
                .correct(request.getCorrect())
                .wrong(request.getWrong())
                .build();
    }

}
