package pl.edu.pg.eti.graphgame.tasks.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, UUID> {

    List<Task> getAllByUser(User user);

    Optional<Task> findFirstByUser(User user);

}
