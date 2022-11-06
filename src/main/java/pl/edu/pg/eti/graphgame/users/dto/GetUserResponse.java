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
    private String password;

    public static Function<User, GetUserResponse> entityToDtoMapper() {
        return user -> GetUserResponse.builder()
                .username(user.getUsername())
                .password(user.getPasswordEncoded())
                .build();
    }

}
