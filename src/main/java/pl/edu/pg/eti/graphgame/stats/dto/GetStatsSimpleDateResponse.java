package pl.edu.pg.eti.graphgame.stats.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;

import java.sql.Date;
import java.util.function.Function;

/**
 * Dto for stats data excluding the ids.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetStatsSimpleDateResponse {

    private int correct;
    private int wrong;

    private Date date;

    public static Function<Stats, GetStatsSimpleDateResponse> entityToDtoMapper() {
        return stats -> GetStatsSimpleDateResponse.builder()
                .correct(stats.getCorrect())
                .wrong(stats.getWrong())
                .date(stats.getDate())
                .build();
    }

}
