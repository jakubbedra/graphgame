package pl.edu.pg.eti.graphgame.users.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.function.BiFunction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UpdateUserRequest {

    private String login;
    private String password;

    public static BiFunction<User, UpdateUserRequest, User> dtoToEntityUpdater() {
        return (user, request) -> {
            user.setLogin(request.getLogin());
            user.setPassword(request.getPassword());
            return user;
        };
    }

}
