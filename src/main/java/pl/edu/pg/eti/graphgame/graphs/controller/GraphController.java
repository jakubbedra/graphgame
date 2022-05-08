package pl.edu.pg.eti.graphgame.graphs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.eti.graphgame.graphs.dto.GetGraphResponse;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.service.GraphService;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/graphs")
@RestController
public class GraphController {

    private final GraphService graphService;
    private final TaskService taskService;

    @Autowired
    public GraphController(
            GraphService graphService,
            TaskService taskService
    ) {
        this.graphService = graphService;
        this.taskService = taskService;
    }

    @GetMapping("/task/{uuid}")
    public ResponseEntity<GetGraphResponse> getGraph(
            @RequestParam("uuid") UUID uuid
    ) {
        Optional<Task> task = taskService.findTask(uuid);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Optional<Graph> graph = graphService.findGraphByTask(task.get());
            if (graph.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(
                    GetGraphResponse.map(graph.get())
            );
        }
    }

}
