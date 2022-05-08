package pl.edu.pg.eti.graphgame.graphs.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.graphs.entity.GraphEntity;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import java.util.Optional;
import java.util.UUID;

public interface GraphRepository extends CrudRepository<GraphEntity, UUID> {

    Optional<GraphEntity> findFirstByTask(Task task);

}
