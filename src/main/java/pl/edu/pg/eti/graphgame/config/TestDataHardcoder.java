package pl.edu.pg.eti.graphgame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
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

    private Task task1;
    private Task task2;
    private Task task3;

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
    }

    private void hardcodeTasks() {
        taskService.saveTask(task1);
        taskService.saveTask(task2);
        taskService.saveTask(task3);
    }

    private void hardcodeStats() {
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .task(taskService.findTaskByName("sample task 3").get())
                        .correct(2)
                        .wrong(14)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .task(taskService.findTaskByName("sample task 1").get())
                        .correct(4)
                        .wrong(3)
                        .date(new Date(System.currentTimeMillis() - DAY_MILLIS))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .task(taskService.findTaskByName("sample task 1").get())
                        .correct(2)
                        .wrong(1)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .task(taskService.findTaskByName("sample task 2").get())
                        .correct(0)
                        .wrong(2)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .task(taskService.findTaskByName("sample task 3").get())
                        .correct(6)
                        .wrong(8)
                        .date(new Date(System.currentTimeMillis() - DAY_MILLIS))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .task(taskService.findTaskByName("sample task 2").get())
                        .correct(2)
                        .wrong(10)
                        .date(new Date(System.currentTimeMillis()-2*DAY_MILLIS))
                        .build()
        );
        statsService.saveStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(userService.findUserByName("sample user 1").get())
                        .task(taskService.findTaskByName("sample task 1").get())
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
        task1 = Task.builder()
                .id(1L)
                .name("sample task 1")
                .build();
        task2 = Task.builder()
                .id(1L)
                .name("sample task 2")
                .build();
        task3 = Task.builder()
                .id(2L)
                .name("sample task 3")
                .build();
    }

}
