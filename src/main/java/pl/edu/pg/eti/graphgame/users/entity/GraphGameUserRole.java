package pl.edu.pg.eti.graphgame.users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="user_roles")
public class GraphGameUserRole {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy="role")
    private List<User> users;

}
