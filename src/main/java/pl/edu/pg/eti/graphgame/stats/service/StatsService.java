package pl.edu.pg.eti.graphgame.stats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.stats.StatsUtils;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.repository.StatsRepository;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.sql.Date;
import java.util.Comparator;
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
                !s.getDate().after(endDate) && !s.getDate().before(startDate)
        ).collect(Collectors.toList());
    }

    //todo
    public List<Stats> findAllStatsByUserAndTask(User user, Task task) {
        return statsRepository.findAllByUserAndTask(user, task);
    }

    public List<Stats> findAllStatsByUserAndTaskInTimePeriod(User user, Task task, Date startDate, Date endDate) {
        return findAllStatsByUserAndTask(user, task).stream().filter(s ->
                !s.getDate().after(endDate) && !s.getDate().before(startDate)
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
     * The
     */
    @Transactional
    public void updateCurrentStats(Stats stats) {
        Optional<Stats> mostCurrent = statsRepository.findAllByUserAndTask(stats.getUser(), stats.getTask()).stream()
                .max(Comparator.comparing(Stats::getDate));
        if (mostCurrent.isPresent() && StatsUtils.equalDates(mostCurrent.get().getDate(), stats.getDate())) {
            mostCurrent.get().setCorrect(mostCurrent.get().getCorrect() + stats.getCorrect());
            mostCurrent.get().setWrong(mostCurrent.get().getWrong() + stats.getWrong());
            statsRepository.save(mostCurrent.get());
        } else {
            statsRepository.save(stats);
        }
    }

    @Transactional
    public void deleteStats(Stats stats) {
        statsRepository.delete(stats);
    }

}
