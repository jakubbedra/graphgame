package pl.edu.pg.eti.graphgame.stats.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;

import java.util.function.BiFunction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UpdateStatsRequest {

    private int correct;
    private int wrong;

    public static BiFunction<Stats, UpdateStatsRequest, Stats> dtoToEntityUpdater() {
        return (stats, request) -> {
          stats.setWrong(request.wrong);
          stats.setCorrect(request.correct);
          return stats;
        };
    }

}
