package pl.edu.pg.eti.graphgame.users.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_account")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    private Long id;
    private String login;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_role")
    private GraphGameUserRole role;

}
