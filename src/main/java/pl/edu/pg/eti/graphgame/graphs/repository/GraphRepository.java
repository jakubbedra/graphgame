package pl.edu.pg.eti.graphgame.graphs.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.graphs.entity.GraphEntity;

import java.util.Optional;
import java.util.UUID;

public interface GraphRepository extends CrudRepository<GraphEntity, UUID> {

    Optional<GraphEntity> findFirstByTask(UUID task);

}
