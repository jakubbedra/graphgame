package pl.edu.pg.eti.graphgame.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.tasks.dto.CreateTaskRequest;
import pl.edu.pg.eti.graphgame.tasks.dto.GetTaskResponse;
import pl.edu.pg.eti.graphgame.tasks.dto.GetTasksResponse;
import pl.edu.pg.eti.graphgame.tasks.dto.UpdateTaskRequest;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;

import java.util.Optional;

@RequestMapping("/api/tasks")
@RestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(
            TaskService taskService
    ) {
        this.taskService = taskService;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<GetTaskResponse> getTask(@PathVariable("id") Long id) {
        return taskService.findTaskById(id)
                .map(value -> ResponseEntity.ok(GetTaskResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable("id") Long id, @RequestBody UpdateTaskRequest request) {
        Optional<Task> task = taskService.findTaskById(id);
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
        Optional<Task> task = taskService.findTaskById(id);
        if (task.isPresent()) {
            taskService.deleteTask(task.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
