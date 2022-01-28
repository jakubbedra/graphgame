package pl.edu.pg.eti.graphgame.users.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.users.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
