package pl.edu.pg.eti.graphgame.users.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
