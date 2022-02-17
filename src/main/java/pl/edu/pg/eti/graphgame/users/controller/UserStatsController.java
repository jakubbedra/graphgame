package pl.edu.pg.eti.graphgame.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.stats.dto.GetSummedStatsResponse;
import pl.edu.pg.eti.graphgame.stats.dto.UpdateStatsRequest;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users/{userId}/stats")
public class UserStatsController {

    private final StatsService statsService;
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public UserStatsController(
            StatsService statsService,
            UserService userService,
            TaskService taskService
    ) {
        this.statsService = statsService;
        this.userService = userService;
        this.taskService = taskService;
    }

    /**
     * Returns summed stats of the user for all kinds of tasks.
     * When not all the dates are passed through request params,
     * this method will return all the stats of the user that
     * will be found.
     */
    @GetMapping
    public ResponseEntity<GetSummedStatsResponse> getUserStatsOverallNoDate(
            @PathVariable("userId") Long userId,
            @RequestParam("startDate") Optional<Date> startDate,
            @RequestParam("endDate") Optional<Date> endDate
    ) {
        Optional<User> user = userService.findUser(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Stats> statsList;
        if (startDate.isPresent() && endDate.isPresent()) {
            statsList = statsService.findAllStatsByUserInTimePeriod(
                    user.get(), startDate.get(), endDate.get()
            );
        } else {
            statsList = statsService.findAllStatsByUser(user.get());
        }

        if (statsList.isEmpty()) {
            return ResponseEntity.ok(
                    GetSummedStatsResponse.builder()
                            .correct(0)
                            .wrong(0)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    new GetSummedStatsResponse(statsList)
            );
        }
    }

    /**
     * Method for updating user's stats for a specific task during a day.
     * If no stats for a given task on a day are present, new stats will be created.
     */
    @PutMapping("/{task}")
    public ResponseEntity<Void> updateDailyUserStats(
            @PathVariable("username") String username,
            @PathVariable("task") Long taskId,
            @RequestBody UpdateStatsRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    /**
     * Method for updating user's stats for multiple tasks during a day.
     * If no stats for a given task on a day are present, new stats will be created.
     */
    @PutMapping
    public ResponseEntity<Void> updateMultipleDailyUserStats(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok().build();
    }

}
