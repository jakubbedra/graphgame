package pl.edu.pg.eti.graphgame.users.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginUserRequest {

    private String email;
    private String password;

}
