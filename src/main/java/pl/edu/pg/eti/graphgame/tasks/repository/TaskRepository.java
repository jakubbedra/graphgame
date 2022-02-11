package pl.edu.pg.eti.graphgame.tasks.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
