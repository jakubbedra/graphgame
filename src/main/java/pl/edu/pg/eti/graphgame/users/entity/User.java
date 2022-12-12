package pl.edu.pg.eti.graphgame.users.entity;

import lombok.*;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import javax.persistence.*;
import java.util.List;

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
    private String username;
    private String passwordEncoded;

    @OneToMany(mappedBy="user")
    private List<Task> tasks;

}
