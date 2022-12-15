package pl.edu.pg.eti.graphgame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.Random;
import java.util.UUID;

@Component
public class TestDataHardcoder {

    private static boolean enabled = false;

    public static void enable() {
        enabled = true;
    }

    private final StatsService statsService;
    private final TaskService taskService;
    private final UserService userService;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;
    private User user7;
    private User user8;
    private User user9;
    private User user10;

    private final int DAY_MILLIS = 24 * 60 * 60 * 1000;

    @Autowired
    public TestDataHardcoder(
            StatsService statsService,
            TaskService taskService,
            UserService userService
    ) {
        this.statsService = statsService;
        this.userService = userService;
        this.taskService = taskService;
        if (enabled) {
            initUsersAndTasks();
        }
    }

    @PostConstruct
    public void hardcodeData() throws UserAlreadyExistsException {
        if (!enabled) {
            return;
        }
        var user = userService.findUserByUsername("user1");
        if (user.isEmpty()) {
            hardcodeUsers();
            hardcodeStats();
        }
    }

    private void hardcodeUsers() throws UserAlreadyExistsException {
        userService.registerNewUserAccountWithPassword(user1, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user2, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user3, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user4, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user5, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user6, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user7, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user8, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user9, "qwerty1234");
        userService.registerNewUserAccountWithPassword(user10, "qwerty1234");
    }

    private final static Random RANDOM = new Random();

    private void randomStatsForUser(String username, Date date) {
        GraphTaskSubject[] values = GraphTaskSubject.values();
        for (GraphTaskSubject subject : values) {
            statsService.saveStats(
                    Stats.builder()
                            .uuid(UUID.randomUUID().toString())
                            .user(userService.findUserByUsername(username).get())
                            .graphTaskSubject(subject)
                            .correct(Math.abs((int) RANDOM.nextGaussian() * RANDOM.nextInt(20)))
                            .wrong(Math.abs((int) RANDOM.nextGaussian() * RANDOM.nextInt(20)))
                            .date(date)
                            .build()
            );
        }
    }

    private void hardcodeStats() {
        int limit = 5;
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user1.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user2.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user3.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user4.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user5.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user6.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user7.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
        for (int i = 0; i < limit; i++) {
            randomStatsForUser(user8.getUsername(), new Date(System.currentTimeMillis() - (long) i * DAY_MILLIS));
        }
    }

    private void initUsersAndTasks() {
        user1 = User.builder()
                .id(21L)
                .username("sample_user1")
                .build();
        user2 = User.builder()
                .id(37L)
                .username("sample_user2")
                .build();
        user3 = User.builder()
                .id(21L)
                .username("sample_user3")
                .build();
        user4 = User.builder()
                .id(37L)
                .username("sample_user4")
                .build();
        user5 = User.builder()
                .id(21L)
                .username("sample_user5")
                .build();
        user6 = User.builder()
                .id(37L)
                .username("sample_user6")
                .build();
        user7 = User.builder()
                .id(21L)
                .username("sample_user7")
                .build();
        user8 = User.builder()
                .id(37L)
                .username("sample_user8")
                .build();
        user9 = User.builder()
                .id(21L)
                .username("sample_user9")
                .build();
        user10 = User.builder()
                .id(37L)
                .username("sample_user10")
                .build();
    }

}
