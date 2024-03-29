package pl.edu.pg.eti.graphgame.stats.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.List;
import java.util.UUID;

public interface StatsRepository extends CrudRepository<Stats, String> {

    List<Stats> findAllByUser(User user);

    List<Stats> findAllByUserAndGraphTaskSubject(User user, GraphTaskSubject graphTaskSubject);

}
