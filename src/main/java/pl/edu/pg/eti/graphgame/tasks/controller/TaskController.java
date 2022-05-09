package pl.edu.pg.eti.graphgame.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.service.GraphService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;
import pl.edu.pg.eti.graphgame.tasks.dto.*;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.service.TaskAnswerService;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/tasks")
@RestController
public class TaskController {

    private final TaskService taskService;
    private final TaskAnswerService taskAnswerService;
    private final UserService userService;
    private final GraphService graphService;

    @Autowired
    public TaskController(
            TaskService taskService,
            TaskAnswerService taskAnswerService,
            UserService userService,
            GraphService graphService
    ) {
        this.taskService = taskService;
        this.taskAnswerService = taskAnswerService;
        this.userService = userService;
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
            @PathParam("uuid") UUID uuid
    ) {
        Optional<Task> task = taskService.findTask(uuid);
        return task.map(value -> ResponseEntity.ok(
                TaskQuestion.map(value)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<TaskQuestion> getTask(
            @PathParam("id") Long id
    ) {
        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            Optional<Task> task = taskService.findTaskOfUser(user.get());
            return task.map(value -> ResponseEntity.ok(
                    TaskQuestion.map(value)
            )).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Void> createTask(@PathParam("id") Long id) {
        //todo: checking if player already has a task

        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            taskService.deleteAllUserTasks(user.get());
            Task task = taskService.createAndSaveTaskForUser(user.get());
            if (taskRequiresGraph(task)) {
                graphService.createAndSaveGraphForTask(task);
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Method for checking the answers to the task
     *
     * @param uuid UUID of the Task
     * @return true if the given answer was correct, false otherwise
     */
    @PostMapping("/vertexSelection/{uuid}/answer")
    public ResponseEntity<Boolean> checkTaskAnswer(
            @PathParam("uuid") UUID uuid,
            @RequestBody VertexSelectionTaskAnswer answer
    ) {
        Optional<Task> task = taskService.findTask(uuid);
        if (task.isPresent()) {
            Optional<Graph> graph = graphService.findGraphByTask(task.get());
            if (graph.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            boolean check = taskAnswerService.checkVertexSelectionAnswer(
                    answer.getSelectedVertices(), task.get(), graph.get()
            );

            //todo: REMOVE THE TASK FROM DB AFTER CHECKING ANSWER!!!!

            return ResponseEntity.ok(check);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean taskRequiresGraph(Task task) {
        return task.getType() != GraphTaskType.DRAW;
    }

}
