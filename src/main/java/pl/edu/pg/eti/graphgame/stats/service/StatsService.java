package pl.edu.pg.eti.graphgame.stats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.repository.StatsRepository;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsService(
            StatsRepository statsRepository
    ) {
        this.statsRepository = statsRepository;
    }

    public Optional<Stats> findStatsByUuid(UUID uuid) {
        return statsRepository.findById(uuid);
    }

    public List<Stats> findAllStats() {
        return (List<Stats>) statsRepository.findAll();
    }

    public List<Stats> findAllStatsByUser(User user) {
        return statsRepository.findAllByUser(user);
    }

    public List<Stats> findAllStatsByUserInTimePeriod(User user, Date startDate, Date endDate) {
        return findAllStatsByUser(user).stream().filter(s ->
                s.getDate().before(endDate) && s.getDate().after(startDate)
        ).collect(Collectors.toList());
    }

    @Transactional
    public void saveStats(Stats stats) {
        statsRepository.save(stats);
    }

    /**
     * Method used for updating a stats instance that does already exist.
     */
    @Transactional
    public void updateStats(Stats stats) {
        statsRepository.save(stats);
    }

    /**
     * Method used for updating (or creating) today's stats
     * of a given task type for a specific player.
     */
    @Transactional
    public void updateCurrentStats(Stats stats) {
        //todo
    }

    @Transactional
    public void deleteStats(Stats stats) {
        statsRepository.delete(stats);
    }

}
