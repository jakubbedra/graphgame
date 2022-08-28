package pl.edu.pg.eti.graphgame.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.stats.StatsUtils;
import pl.edu.pg.eti.graphgame.stats.dto.GetSimplifiedStatsListResponse;
import pl.edu.pg.eti.graphgame.stats.dto.GetStatsListResponse;
import pl.edu.pg.eti.graphgame.stats.dto.GetSummedStatsResponse;
import pl.edu.pg.eti.graphgame.stats.dto.UpdateStatsRequest;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;
import pl.edu.pg.eti.graphgame.users.service.UserSessionService;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users/{userId}/stats")
public class UserStatsController {

    private final StatsService statsService;
    private final UserService userService;
    private final TaskService taskService;
    private final UserSessionService userSessionService;

    @Autowired
    public UserStatsController(
            StatsService statsService,
            UserService userService,
            TaskService taskService,
            UserSessionService userSessionService
    ) {
        this.statsService = statsService;
        this.userService = userService;
        this.taskService = taskService;
        this.userSessionService = userSessionService;
    }

    /**
     * Returns summed stats of the user for all kinds of tasks.
     * When not all the dates are passed through request params,
     * this method will return all the stats of the user that
     * will be found.
     */
    @GetMapping
    public ResponseEntity<GetSummedStatsResponse> getUserStatsOverall(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "startDate", required = false) Optional<Date> startDate,
            @RequestParam(name = "endDate", required = false) Optional<Date> endDate,
            @RequestParam("token") String token
    ) {

        if(userSessionService.hasAccess(token, userId)) {
            return userSessionService.getResponseTokenAccessUser(token,
                userId);
        }

        Optional<User> user = userService.findUser(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Stats> statsList = findUserStats(user.get(), startDate, endDate, Optional.empty());

        if (statsList.isEmpty()) {
            return ResponseEntity.ok(
                    GetSummedStatsResponse.builder()
                            .correct(0)
                            .wrong(0)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    GetSummedStatsResponse.entityToDtoMapper().apply(statsList)
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<GetSimplifiedStatsListResponse> getUserStatsListOverall(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "startDate", required = false) Optional<Date> startDate,
            @RequestParam(name = "endDate", required = false) Optional<Date> endDate,
            @RequestParam("token") String token
    ) {
        Optional<User> user = userService.findUser(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if(!userSessionService.hasAccess(token, userId)) {
            return userSessionService.getResponseTokenAccessUser(token,
                userId);
        }

        List<Stats> statsList = findUserStats(
                user.get(), startDate, endDate, Optional.empty()
        );
        return ResponseEntity.ok(
                GetSimplifiedStatsListResponse.entityToDtoMapper().apply(sumStatsByDates(statsList))
        );
    }

    @GetMapping("/{taskName}")
    public ResponseEntity<GetSummedStatsResponse> getUserStatsTask(
            @PathVariable("userId") Long userId,
            @PathVariable("taskName") String taskName,
//            @PathVariable("taskId") Long taskId,
            @RequestParam(name = "startDate", required = false) Optional<Date> startDate,
            @RequestParam(name = "endDate", required = false) Optional<Date> endDate,
            @RequestParam("token") String token
    ) {
        Optional<User> user = userService.findUser(userId);

        if(!userSessionService.hasAccess(token, userId)) {
            return userSessionService.getResponseTokenAccessUser(token,
                userId);
        }

        //        Optional<TaskSubject> task = taskService.findTaskById(taskId);
        GraphTaskSubject taskSubject = GraphTaskSubject.valueOf(taskName);
        if (user.isEmpty() || taskSubject == null) {
            return ResponseEntity.notFound().build();
        }

        List<Stats> statsList = findUserStats(user.get(), startDate, endDate, Optional.of(taskSubject));

        if (statsList.isEmpty()) {
            return ResponseEntity.ok(
                    GetSummedStatsResponse.builder()
                            .correct(0)
                            .wrong(0)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    GetSummedStatsResponse.entityToDtoMapper().apply(statsList)
            );
        }
    }

    @GetMapping("/{taskName}/list")
    public ResponseEntity<GetStatsListResponse> getUserStatsListTask(
            @PathVariable("userId") Long userId,
            @PathVariable("taskName") String taskName,
            @RequestParam(name = "startDate", required = false) Optional<Date> startDate,
            @RequestParam(name = "endDate", required = false) Optional<Date> endDate,
            @RequestParam("token") String token
    ) {
        Optional<User> user = userService.findUser(userId);

        if(!userSessionService.hasAccess(token, userId)) {
            return userSessionService.getResponseTokenAccessUser(token,
                userId);
        }

        GraphTaskSubject taskSubject = GraphTaskSubject.valueOf(taskName);
        if (user.isEmpty() || taskSubject == null) {
            return ResponseEntity.notFound().build();
        }
        List<Stats> statsList = findUserStats(
                user.get(), startDate, endDate, Optional.of(taskSubject)
        );
        return ResponseEntity.ok(
                GetStatsListResponse.entityToDtoMapper().apply(statsList)
        );
    }

    /**
     * Method for updating user's stats for a specific task during a day.
     * If no stats for a given task on a day are present, new stats will be created.
     * The stats are updated by <strong>adding</s> the stats given in the DTO to those
     * stored in the data layer.
     */
    @PutMapping("/{taskName}")
    public ResponseEntity<Void> updateDailyUserStats(
            @PathVariable("userId") Long userId,
            @PathVariable("taskName") String taskName,
            @RequestBody UpdateStatsRequest request,
            @RequestParam("token") String token
    ) {
        Optional<User> user = userService.findUser(userId);
        if(!userSessionService.hasAccess(token, userId)) {
            return userSessionService.getResponseTokenAccessUser(token,
                userId);
        }
        GraphTaskSubject taskSubject = GraphTaskSubject.valueOf(taskName);
        if (user.isEmpty() || taskSubject == null) {
            return ResponseEntity.notFound().build();
        }
        statsService.updateCurrentStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(user.get())
                        .graphTaskSubject(taskSubject)
                        .date(new Date(System.currentTimeMillis()))
                        .correct(request.getCorrect())
                        .wrong(request.getWrong())
                        .build()
        );
        return ResponseEntity.accepted().build();
    }

    private List<Stats> findUserStats(
            User user, Optional<Date> startDate, Optional<Date> endDate, Optional<GraphTaskSubject> task
    ) {
        if (task.isPresent()) {
            if (startDate.isPresent() && endDate.isPresent()) {
                return statsService.findAllStatsByUserAndTaskInTimePeriod(
                        user, task.get(), startDate.get(), endDate.get()
                );
            } else {
                return statsService.findAllStatsByUserAndTask(user, task.get());
            }
        } else {
            if (startDate.isPresent() && endDate.isPresent()) {
                return statsService.findAllStatsByUserInTimePeriod(
                        user, startDate.get(), endDate.get()
                );
            } else {
                return statsService.findAllStatsByUser(user);
            }
        }
    }

    private List<Stats> sumStatsByDates(List<Stats> statsList) {
        statsList = statsList.stream().sorted(Comparator.comparing(Stats::getDate)).collect(Collectors.toList());
        List<Stats> results = new LinkedList<>();
        if (!statsList.isEmpty()) {
            Date lastDate = statsList.get(0).getDate();
            int lastCorrect = 0;
            int lastWrong = 0;
            for (Stats s : statsList) {
                if (StatsUtils.equalDates(s.getDate(), lastDate)) {
                    lastCorrect += s.getCorrect();
                    lastWrong += s.getWrong();
                } else {
                    results.add(Stats.builder()
                            .correct(lastCorrect)
                            .wrong(lastWrong)
                            .date(lastDate)
                            .build());
                    lastCorrect = s.getCorrect();
                    lastWrong = s.getWrong();
                    lastDate = s.getDate();
                }
            }
            results.add(Stats.builder()
                    .correct(lastCorrect)
                    .wrong(lastWrong)
                    .date(lastDate)
                    .build());
        }
        return results;
    }

}
