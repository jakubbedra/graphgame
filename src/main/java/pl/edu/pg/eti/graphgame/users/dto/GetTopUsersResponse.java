package pl.edu.pg.eti.graphgame.users.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetTopUsersResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class TopUser {

        private Long id;

        private String username;

        private int place;
        private int correctTotal;
        private int wrongTotal;

    }

    private List<TopUser> topUsers;

    private int page;

    public static Function<List<Stats>, GetTopUsersResponse> statsListToTopUsersMapper(
            int placeOfFirstUser, int page
    ) {
        return statsList -> {
            List<TopUser> topUsers = new LinkedList<>();
            for (int i = 0; i < statsList.size(); i++) {
                Stats stats = statsList.get(i);
                topUsers.add(
                        TopUser.builder()
                                .id(stats.getUser().getId())
                                .username(stats.getUser().getLogin())
                                .place(i + placeOfFirstUser)
                                .correctTotal(stats.getCorrect())
                                .wrongTotal(stats.getWrong())
                                .build()
                );
            }
            return GetTopUsersResponse.builder()
                    .topUsers(topUsers)
                    .page(page)
                    .build();
        };
    }

}
