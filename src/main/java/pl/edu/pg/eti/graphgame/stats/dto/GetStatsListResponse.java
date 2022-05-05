package pl.edu.pg.eti.graphgame.stats.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetStatsListResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode
    public static class StatsDto {

        private UUID uuid;

        private Long userId;
        private String taskName;

        private int correct;
        private int wrong;

        private Date date;

    }

    private List<StatsDto> statsList;

    public static Function<List<Stats>, GetStatsListResponse> entityToDtoMapper() {
        return statsList ->
                GetStatsListResponse.builder()
                        .statsList(statsList.stream()
                                .map(stats -> StatsDto.builder()
                                        .uuid(stats.getUuid())
                                        .taskName(stats.getGraphTaskSubject().name())
                                        .userId(stats.getUser().getId())
                                        .correct(stats.getCorrect())
                                        .wrong(stats.getWrong())
                                        .date(stats.getDate())
                                        .build())
                                .collect(Collectors.toList()))
                        .build();
    }

}
