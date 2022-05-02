package pl.edu.pg.eti.graphgame.tasks.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskSubject, Long> {

    List<TaskSubject> findAllByName(String name);

}
