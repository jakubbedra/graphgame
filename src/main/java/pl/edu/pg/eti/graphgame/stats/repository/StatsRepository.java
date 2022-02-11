package pl.edu.pg.eti.graphgame.stats.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;

import java.util.UUID;

public interface StatsRepository extends CrudRepository<Stats, UUID> {
}
