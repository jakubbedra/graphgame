package pl.edu.pg.eti.graphgame.users.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginUserResponse {

    private String username;
    private String _token;
    private String _tokenExpirationTime;
    private Long user_id;

}
