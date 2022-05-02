package pl.edu.pg.eti.graphgame.users.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.entity.UserSession;

import java.util.*;

public interface UserSessionRepository extends CrudRepository<UserSession, String> {

    List<User> findAllByUser(User user);

}

