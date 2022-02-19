package pl.edu.pg.eti.graphgame.stats.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;

import java.util.List;
import java.util.function.Function;

/**
 * Response containing only the 2 most basic fields of the stats class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetSummedStatsResponse {

    private int correct;
    private int wrong;

    public static Function<List<Stats>, GetSummedStatsResponse> entityToDtoMapper() {
        return stats -> GetSummedStatsResponse.builder()
                .correct(stats.stream().map(Stats::getCorrect).reduce(0, Integer::sum))
                .wrong(stats.stream().map(Stats::getWrong).reduce(0, Integer::sum))
                .build();
    }

}
