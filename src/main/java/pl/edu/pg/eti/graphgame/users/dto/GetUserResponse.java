package pl.edu.pg.eti.graphgame.users.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetUserResponse {

    private String username;
    private String email;
    private String password;

    public static Function<User, GetUserResponse> entityToDtoMapper() {
        return user -> GetUserResponse.builder()
                .username(user.getLogin())
                .email(user.getEmail())
                .password(user.getPasswordEncoded())
                .build();
    }

}
