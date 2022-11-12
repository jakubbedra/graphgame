package pl.edu.pg.eti.graphgame.graphs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.graphs.dto.GetGraphResponse;
import pl.edu.pg.eti.graphgame.graphs.dto.GetWeightedGraphResponse;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedGraph;
import pl.edu.pg.eti.graphgame.graphs.service.GraphService;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.service.UserSessionService;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/graphs")
@RestController
public class GraphController {

    private final GraphService graphService;
    private final TaskService taskService;
    private final UserSessionService userSessionService;

    @Autowired
    public GraphController(
            GraphService graphService,
            TaskService taskService,
            UserSessionService userSessionService
    ) {
        this.graphService = graphService;
        this.taskService = taskService;
        this.userSessionService = userSessionService;
    }

    @GetMapping("/task/{uuid}")
    public ResponseEntity<GetGraphResponse> getGraph(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("token") String token
    ) {
        Optional<Task> task = taskService.findTask(uuid);
		
        if(task.isEmpty()) {
			System.out.println("   ...   There is no task with UUID: " + uuid.toString());
            return ResponseEntity.notFound().build();
        } else {
            if(!userSessionService.hasTaskAccess(token, uuid)) {
				System.out.println("   ...   There is no task "+uuid+" in the session: " + token);
                return userSessionService.getResponseTokenAccessTask(token, uuid);
            }
            Optional<Graph> graph = graphService.findGraphByTask(task.get());
            if(graph.isEmpty()) {
				System.out.println("   ...   Graph is empty");
                return ResponseEntity.notFound().build();
            }
			System.out.println("   ...   Everything is ok");
            return ResponseEntity.ok(
                    GetGraphResponse.map(graph.get())
            );
        }
    }

    @GetMapping("/weighted/task/{uuid}")
    public ResponseEntity<GetWeightedGraphResponse> getWeightedGraph(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("token") String token
    ) {
        Optional<Task> task = taskService.findTask(uuid);
        if (task.isEmpty()) {
			System.out.println("   ...   There is no task with UUID: " + uuid.toString());
            return ResponseEntity.notFound().build();
        } else {
            if (!userSessionService.hasTaskAccess(token, uuid)) {
				System.out.println("   ...   There is no task "+uuid+" in the session: " + token);
                return userSessionService.getResponseTokenAccessTask(token, uuid);
            }
            Optional<Graph> graph = graphService.findGraphByTask(task.get());
            if (graph.isEmpty()) {
				System.out.println("   ...   Graph is empty");
                return ResponseEntity.notFound().build();
            }
			System.out.println("   ...   Everything is ok");
            return ResponseEntity.ok(
                    GetWeightedGraphResponse.map((WeightedGraph) graph.get())
            );
        }
    }

}
