package pl.edu.pg.eti.graphgame.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.dto.*;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private static final int MAX_USERS_PER_PAGE = 50;
    private static final int DEFAULT_SESSION_TOKEN_EXPIRATION_TIME_SECONDS = 3600;

    private final String DEFAULT_USER_ROLE;

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final UserService userService;
    private final StatsService statsService;
    private final TaskService taskService;

    @Autowired
    public UserController(
            JavaMailSender mailSender,
            UserService userService,
            StatsService statsService,
            TaskService taskService
    ) {
        this.DEFAULT_USER_ROLE = "ROLE_USER";
        this.mailSender = mailSender;
        this.userService = userService;
        this.statsService = statsService;
        this.taskService = taskService;
        this.passwordEncoder = new BCryptPasswordEncoder(11);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest request) {
        if (request.getEmail().equals("graphgame.service@gmail.com")) {
            return ResponseEntity.ok(
                    LoginUserResponse.builder()
                            .username("admin")
                            ._token("XD")
                            ._tokenExpirationTime(DEFAULT_SESSION_TOKEN_EXPIRATION_TIME_SECONDS + "")
                            .build()
            );
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserRequest request) {
        User user = User.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(DEFAULT_USER_ROLE)
                .build();
        try {
            userService.registerNewUserAccount(user);
            sendAccountActivationEmail(user);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("id") Long id) {
        return userService.findUser(id)
                .map(value -> ResponseEntity.ok(GetUserResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsers() {
        return ResponseEntity.ok(
                GetUsersResponse.entityToDtoMapper().apply(userService.findAllUsers())
        );
    }

    @GetMapping("/topChart/overall/{page}")
    public ResponseEntity<GetTopUsersResponse> getTopUsersAlltime(@PathVariable("page") Integer page) {
        List<Stats> selectedStats = getTopUserStatsPage(page, Optional.empty(), false);
        if (selectedStats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                GetTopUsersResponse
                        .statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
                        .apply(selectedStats)
        );
    }

    @GetMapping("/topChart/{taskId}/{page}")
    public ResponseEntity<GetTopUsersResponse> getTopUsersAlltime(
            @PathVariable("page") Integer page,
            @PathVariable("taskId") Long taskId
    ) {
        Optional<Task> task = taskService.findTaskById(taskId);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Stats> selectedStats = getTopUserStatsPage(page, task, false);
        if (selectedStats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                GetTopUsersResponse
                        .statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
                        .apply(selectedStats)
        );
    }

    @GetMapping("/topChart/overall/{page}/today")
    public ResponseEntity<GetTopUsersResponse> getTopUsersToday(
            @PathVariable("page") Integer page
    ) {
        List<Stats> selectedStats = getTopUserStatsPage(page, Optional.empty(), true);
        if (selectedStats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                GetTopUsersResponse
                        .statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
                        .apply(selectedStats)
        );
    }


    @GetMapping("/topChart/{taskId}/{page}/today")
    public ResponseEntity<GetTopUsersResponse> getTopUsersToday(
            @PathVariable("page") Integer page,
            @PathVariable("taskId") Long taskId
    ) {
        Optional<Task> task = taskService.findTaskById(taskId);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Stats> selectedStats = getTopUserStatsPage(page, task, true);
        if (selectedStats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                GetTopUsersResponse
                        .statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
                        .apply(selectedStats)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest request, @PathVariable("id") Long id) {
        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            UpdateUserRequest.dtoToEntityUpdater().apply(user.get(), request);
            userService.updateUser(user.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void sendAccountActivationEmail(User user) {

    }

    private Stats getUserScoreSummed(User user, Optional<Task> task, boolean today) {
        List<Stats> stats;
        if (!today) {
            if (task.isEmpty()) {
                stats = statsService.findAllStatsByUser(user);
            } else {
                stats = statsService.findAllStatsByUserAndTask(user, task.get());
            }
        } else {
            Date todayDate = new Date(System.currentTimeMillis());
            if (task.isEmpty()) {
                stats = statsService.findAllStatsByUserAndDate(user, todayDate);
            } else {
                stats = statsService.findAllStatsByUserAndTaskAndDate(user, task.get(), todayDate);
            }
        }
        AtomicInteger correct = new AtomicInteger();
        AtomicInteger wrong = new AtomicInteger();
        stats.forEach(s -> {
            correct.addAndGet(s.getCorrect());
            wrong.addAndGet(s.getWrong());
        });
        return Stats.builder()
                .correct(correct.get())
                .wrong(wrong.get())
                .user(user)
                .build();
    }

    private List<Stats> getTopUserStatsPage(int page, Optional<Task> task, boolean today) {
        List<User> users = new ArrayList<>(userService.findAllUsers());

        if (users.size() - (page - 1) * MAX_USERS_PER_PAGE < 0) {
            return Collections.emptyList();
        }

        List<Stats> userStatsSummed = users.stream()
                .map(u -> getUserScoreSummed(u, task, today))
                .sorted(Comparator.comparingInt(u -> u.getCorrect() - u.getWrong()))
                .collect(Collectors.toList());

        List<Stats> selectedStats = new LinkedList<>();
        for (
                int i = (page - 1) * MAX_USERS_PER_PAGE;
                i < page * MAX_USERS_PER_PAGE && i < userStatsSummed.size();
                i++
        ) {
            selectedStats.add(userStatsSummed.get(i));
        }
        return selectedStats;
    }


}
