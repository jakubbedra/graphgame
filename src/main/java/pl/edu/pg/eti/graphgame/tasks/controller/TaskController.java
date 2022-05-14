package pl.edu.pg.eti.graphgame.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.service.GraphService;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;
import pl.edu.pg.eti.graphgame.tasks.dto.*;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.service.TaskAnswerService;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import javax.websocket.server.PathParam;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/tasks")
@RestController
public class TaskController {

    private final TaskService taskService;
    private final TaskAnswerService taskAnswerService;
    private final UserService userService;
    private final StatsService statsService;
    private final GraphService graphService;

    @Autowired
    public TaskController(
            TaskService taskService,
            TaskAnswerService taskAnswerService,
            UserService userService,
            StatsService statsService,
            GraphService graphService
    ) {
        this.taskService = taskService;
        this.taskAnswerService = taskAnswerService;
        this.userService = userService;
        this.statsService = statsService;
        this.graphService = graphService;
    }

    @GetMapping("/subjects")
    public ResponseEntity<GetTaskSubjectsResponse> getTaskSubjects() {
        return ResponseEntity.ok(
                GetTaskSubjectsResponse.entityToDtoMapper().apply(List.of(GraphTaskSubject.values()))
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TaskQuestion> getTask(
            @PathVariable("uuid") UUID uuid
    ) {
        Optional<Task> task = taskService.findTask(uuid);
        return task.map(value -> ResponseEntity.ok(
                TaskQuestion.map(value)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<TaskQuestion> getTask(
            @PathVariable("id") Long id
    ) {
        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            Optional<Task> task = taskService.findTaskOfUser(user.get());
            return task.map(value -> ResponseEntity.ok(
                    TaskQuestion.map(value)
            )).orElseGet(() -> ResponseEntity.ok(
                    TaskQuestion.builder()
                            .type(GraphTaskType.UNDEFINED)
                            .build()
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Void> createTask(
            @PathVariable("id") Long id
    ) {
        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            Task task = taskService.createAndSaveTaskForUser(user.get());
            if (taskRequiresGraph(task)) {
                graphService.createAndSaveGraphForTask(task);
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/answer/vertexSelection/{uuid}")
    public ResponseEntity<Boolean> checkTaskAnswer(
            @PathVariable("uuid") UUID uuid,
            @RequestBody VertexSelectionTaskAnswer answer
    ) {
        Optional<Task> task = taskService.findTask(uuid);
        if (task.isPresent()) {
            if (!task.get().getType().equals(GraphTaskType.VERTEX_SELECTION)) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Graph> graph = graphService.findGraphByTask(task.get());
            if (graph.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            boolean check = taskAnswerService.checkVertexSelectionAnswer(
                    answer.getSelectedVertices(), task.get(), graph.get()
            );

            updateStats(task.get(), check);
            taskService.deleteTask(task.get());

            return ResponseEntity.ok(check);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/answer/draw/{uuid}")
    public ResponseEntity<Boolean> checkTaskAnswer(
            @PathVariable("uuid") UUID uuid,
            @RequestBody DrawGraphTaskAnswer answer
    ) {
        Optional<Task> task = taskService.findTask(uuid);
        if (task.isPresent()) {
            if (!task.get().getType().equals(GraphTaskType.DRAW)) {
                return ResponseEntity.badRequest().build();
            }

            boolean check = taskAnswerService.checkDrawGraphAnswer(
                    DrawGraphTaskAnswer.mapToGraph(answer), task.get()
            );

            updateStats(task.get(), check);
            taskService.deleteTask(task.get());

            return ResponseEntity.ok(check);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean taskRequiresGraph(Task task) {
        return task.getType() != GraphTaskType.DRAW;
    }

    private void updateStats(Task task, boolean isCorrectAnswer) {
        statsService.updateCurrentStats(
                Stats.builder()
                        .uuid(UUID.randomUUID())
                        .user(task.getUser())
                        .graphTaskSubject(task.getSubject())
                        .correct(isCorrectAnswer ? 1 : 0)
                        .wrong(isCorrectAnswer ? 0 : 1)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        );
    }

}
