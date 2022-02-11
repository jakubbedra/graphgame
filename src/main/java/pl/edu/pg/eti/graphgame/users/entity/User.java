package pl.edu.pg.eti.graphgame.users.entity;

import lombok.*;
import pl.edu.pg.eti.graphgame.users.UserRole;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "user_accounts")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String login;
    private String email;
    private String password;
    private UserRole role;

}
