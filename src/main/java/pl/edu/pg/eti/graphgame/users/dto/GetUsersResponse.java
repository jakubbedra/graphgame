package pl.edu.pg.eti.graphgame.users.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetUsersResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserDto {

        private Long id;
        private String username;
        private String email;
        private String password;

    }

    @Singular
    private List<UserDto> users;

    public static Function<Collection<User>, GetUsersResponse> entityToDtoMapper() {
        return users -> {
            GetUsersResponseBuilder response = GetUsersResponse.builder();
            users.stream()
                    .map(user -> UserDto.builder()
                            .id(user.getId())
                            .username(user.getLogin())
                            .email(user.getEmail())
                            .password(user.getPasswordEncoded())
                            .build())
                    .forEach(response::user);
            return response.build();
        };
    }

}
