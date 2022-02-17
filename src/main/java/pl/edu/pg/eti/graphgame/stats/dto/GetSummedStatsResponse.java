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

    public GetSummedStatsResponse(List<Stats> stats) {
        this.correct = 0;
        this.wrong = 0;
        stats.forEach(s -> {
            correct += s.getCorrect();
            wrong += s.getWrong();
        });
    }

    public static Function<Stats, GetSummedStatsResponse> entityToDtoMapper() {
        return stats -> GetSummedStatsResponse.builder()
                .correct(stats.getCorrect())
                .wrong(stats.getWrong())
                .build();
    }

}
