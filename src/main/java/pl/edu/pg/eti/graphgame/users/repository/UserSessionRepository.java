package pl.edu.pg.eti.graphgame.users.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.entity.UserSession;

import java.util.*;

public interface UserSessionRepository extends CrudRepository<UserSession, String> {

    List<User> findAllByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserSession WHERE STRCMP(expirationDatetime, ?1) < 0")
    void deleteAllExpiredBefore(String datetime);
}

