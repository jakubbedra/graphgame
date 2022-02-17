package pl.edu.pg.eti.graphgame.stats.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;

import java.sql.Date;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetStatsResponse {

    private Long userId;
    private Long taskId;

    private int correct;
    private int wrong;

    private Date date;

    public static Function<Stats, GetStatsResponse> entityToDtoMapper() {
        return stats -> GetStatsResponse.builder()
                .taskId(stats.getTask().getId())
                .userId(stats.getUser().getId())
                .correct(stats.getCorrect())
                .wrong(stats.getWrong())
                .date(stats.getDate())
                .build();
    }

}
