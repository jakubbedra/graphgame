package pl.edu.pg.eti.graphgame.stats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.stats.StatsUtils;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.repository.StatsRepository;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
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
    public List<Stats> findAllStatsByUserAndTask(User user, GraphTaskSubject taskSubject) {
        return statsRepository.findAllByUserAndGraphTaskSubject(user, taskSubject);
        //return statsRepository.findAllByUserAndTaskSubject(user, taskSubject);
    }

    public List<Stats> findAllStatsByUserAndTaskInTimePeriod(User user, GraphTaskSubject taskSubject, Date startDate, Date endDate) {
        return findAllStatsByUserAndTask(user, taskSubject).stream().filter(s ->
                !s.getDate().after(endDate) && !s.getDate().before(startDate)
        ).collect(Collectors.toList());
    }

    public List<Stats> findAllStatsByUserAndDate(User user, Date date) {
        return statsRepository.findAllByUser(user).stream()
                .filter(s -> StatsUtils.equalDates(s.getDate(), date))
                .collect(Collectors.toList());
    }

    public List<Stats> findAllStatsByUserAndTaskAndDate(User user, GraphTaskSubject taskSubject, Date date) {
        return statsRepository.findAllByUserAndGraphTaskSubject(user, taskSubject).stream()
                .filter(s -> StatsUtils.equalDates(s.getDate(), date))
                .collect(Collectors.toList());
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
     *
     * The stats param already needs to have today's date.
     */
    @Transactional
    public void updateCurrentStats(Stats stats) {
        Optional<Stats> mostCurrent = statsRepository.findAllByUserAndGraphTaskSubject(stats.getUser(), stats.getGraphTaskSubject()).stream()
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
