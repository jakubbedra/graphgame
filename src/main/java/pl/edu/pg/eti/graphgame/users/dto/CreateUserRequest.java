package pl.edu.pg.eti.graphgame.users.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateUserRequest {

    private String username;
    private String password;

}
