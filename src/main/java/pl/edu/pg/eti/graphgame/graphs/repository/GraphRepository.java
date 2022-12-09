package pl.edu.pg.eti.graphgame.graphs.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.graphs.entity.GraphEntity;

import java.util.Optional;
import java.util.UUID;

public interface GraphRepository extends CrudRepository<GraphEntity, String> {

    Optional<GraphEntity> findFirstByTask(String task);

    @Transactional
    @Modifying
    @Query("DELETE FROM GraphEntity WHERE task = ?1")
    void deleteAllByTaskId(String taskUuid);

    @Transactional
    @Modifying
    @Query("DELETE FROM GraphEntity WHERE task in (SELECT uuid FROM Task AS t WHERE t.uuid = ?1)")
    void deleteAllByUserId(Long userId);
}
