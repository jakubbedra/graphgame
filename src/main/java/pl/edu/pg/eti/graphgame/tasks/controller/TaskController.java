package pl.edu.pg.eti.graphgame.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.dto.*;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
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
    private final UserService userService;

    @Autowired
    public TaskController(
            TaskService taskService,
            UserService userService
    ) {
        this.taskService = taskService;
        this.userService = userService;
    }

    /*
        @PostMapping
        public ResponseEntity<Void> createTask(@RequestBody CreateTaskRequest request) {
            try {
                taskService.saveTask(
                        CreateTaskRequest.dtoToEntityMapper().apply(request)
                );
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
        }

        @GetMapping
        public ResponseEntity<GetTasksResponse> getTasks() {
            return ResponseEntity.ok(
                    GetTasksResponse.entityToDtoMapper().apply(taskService.findAllTasks())
            );
        }
    */

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
    public ResponseEntity<Void> createTask(@PathVariable("id") Long id) {
        //todo: checking if player already has a task

        //TODO: graphService.createGraphForTask(task)

        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            taskService.createAndSaveTaskForUser(user.get());
            //graphService.createAndSaveGraphForTask(task);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
    @GetMapping("/{id}")
    public ResponseEntity<GetTaskResponse> getTask(@PathVariable("id") Long id) {
        return taskService.findTaskById(id)
                .map(value -> ResponseEntity.ok(GetTaskResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable("id") Long id, @RequestBody UpdateTaskRequest request) {
        Optional<TaskSubject> task = taskService.findTaskById(id);
        if (task.isPresent()) {
            UpdateTaskRequest.dtoToEntityUpdater().apply(task.get(), request);
            taskService.updateTask(task.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        Optional<TaskSubject> task = taskService.findTaskById(id);
        if (task.isPresent()) {
            taskService.deleteTask(task.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
*/
}
