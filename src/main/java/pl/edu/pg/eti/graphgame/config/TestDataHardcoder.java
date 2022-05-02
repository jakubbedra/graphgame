package pl.edu.pg.eti.graphgame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.UUID;

@Component
public class TestDataHardcoder {

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

    private TaskSubject taskSubject1;
    private TaskSubject taskSubject2;
    private TaskSubject taskSubject3;

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
        initUsersAndTasks();
    }

    @PostConstruct
    public void hardcodeData() throws UserAlreadyExistsException {
        hardcodeUsers();
        hardcodeTasks();
        hardcodeStats();
    }

    private void hardcodeUsers() throws UserAlreadyExistsException {
        userService.registerNewUserAccount(user1);
        userService.registerNewUserAccount(user2);
        userService.registerNewUserAccount(user3);
        userService.registerNewUserAccount(user4);
        userService.registerNewUserAccount(user5);
        userService.registerNewUserAccount(user6);
        userService.registerNewUserAccount(user7);
        userService.registerNewUserAccount(user8);
        userService.registerNewUserAccount(user9);
        userService.registerNewUserAccount(user10);
    }

    private void hardcodeTasks() {
        taskService.saveTask(taskSubject1);
        taskService.saveTask(taskSubject2);
        taskService.saveTask(taskSubject3);
    }

    private void hardcodeStats() {
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .taskSubject(taskService.findTaskByName("sample task 3").get())
                        .correct(2)
                        .wrong(14)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .taskSubject(taskService.findTaskByName("sample task 1").get())
                        .correct(4)
                        .wrong(3)
                        .date(new Date(System.currentTimeMillis() - DAY_MILLIS))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .taskSubject(taskService.findTaskByName("sample task 1").get())
                        .correct(2)
                        .wrong(1)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .taskSubject(taskService.findTaskByName("sample task 2").get())
                        .correct(0)
                        .wrong(2)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .taskSubject(taskService.findTaskByName("sample task 3").get())
                        .correct(6)
                        .wrong(8)
                        .date(new Date(System.currentTimeMillis() - DAY_MILLIS))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .taskSubject(taskService.findTaskByName("sample task 2").get())
                        .correct(2)
                        .wrong(10)
                        .date(new Date(System.currentTimeMillis()-2*DAY_MILLIS))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .taskSubject(taskService.findTaskByName("sample task 1").get())
                        .correct(6)
                        .wrong(17)
                        .date(new Date(System.currentTimeMillis()-2*DAY_MILLIS))
                        .build()
        );
    }

    private void initUsersAndTasks() {
        user1 = User.builder()
                .id(21L)
                .login("sample user 1")
                .email("user1@example.com")
                .password("xd")
                .build();
        user2 = User.builder()
                .id(37L)
                .login("sample user 2")
                .email("user2@example.com")
                .password("xd")
                .build();
        user3 = User.builder()
                .id(21L)
                .login("sample user 3")
                .email("user3@example.com")
                .password("xd")
                .build();
        user4 = User.builder()
                .id(37L)
                .login("sample user 4")
                .email("user4@example.com")
                .password("xd")
                .build();
        user5 = User.builder()
                .id(21L)
                .login("sample user 5")
                .email("user5@example.com")
                .password("xd")
                .build();
        user6 = User.builder()
                .id(37L)
                .login("sample user 6")
                .email("user6@example.com")
                .password("xd")
                .build();
        user7 = User.builder()
                .id(21L)
                .login("sample user 7")
                .email("user7@example.com")
                .password("xd")
                .build();
        user8 = User.builder()
                .id(37L)
                .login("sample user 8")
                .email("user8@example.com")
                .password("xd")
                .build();
        user9 = User.builder()
                .id(21L)
                .login("sample user 9")
                .email("user9@example.com")
                .password("xd")
                .build();
        user10 = User.builder()
                .id(37L)
                .login("sample user 10")
                .email("user10@example.com")
                .password("xd")
                .build();
        taskSubject1 = TaskSubject.builder()
                .name("sample task 1")
                .build();
        taskSubject2 = TaskSubject.builder()
                .name("sample task 2")
                .build();
        taskSubject3 = TaskSubject.builder()
                .name("sample task 3")
                .build();
    }

}
