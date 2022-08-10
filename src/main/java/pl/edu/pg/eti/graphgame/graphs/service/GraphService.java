package pl.edu.pg.eti.graphgame.graphs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.graphgame.exceptions.UnsupportedTaskSubjectException;
import pl.edu.pg.eti.graphgame.graphs.entity.GraphEntity;
import pl.edu.pg.eti.graphgame.graphs.factory.GraphFactory;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.NeighbourListsGraph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedAdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.repository.GraphRepository;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import java.util.Optional;
import java.util.UUID;

@Service
public class GraphService {

    private final GraphFactory graphFactory;
    private final GraphRepository graphRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public GraphService(
            GraphFactory graphFactory,
            GraphRepository graphRepository
    ) {
        this.graphFactory = graphFactory;
        this.graphRepository = graphRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void createAndSaveGraphForTask(Task task) {
        Graph graph = createGraphForTask(task);
        try {
            String json = graphToJson(graph);

            graphRepository.save(GraphEntity.builder()
                    .uuid(UUID.randomUUID())
                    .task(task.getUuid())
                    .json(json)
                    .build()
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Optional<Graph> findGraphByTask(Task task) {
        GraphEntity entity = graphRepository.findFirstByTask(task.getUuid()).get();
        try {
            if (!task.isGraphWeighted()) {
                return Optional.of(graphFromJson(entity.getJson()));
            } else {
                return Optional.of(weightedGraphFromJson(entity.getJson()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private String graphToJson(Graph graph) throws JsonProcessingException {
        return objectMapper.writeValueAsString(graph);
    }

    private Graph graphFromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, NeighbourListsGraph.class);
    }

    private Graph weightedGraphFromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, WeightedAdjacencyMatrixGraph.class);
    }

    private Graph createGraphForTask(Task task) {
        GraphTaskSubject subject = task.getSubject();
        switch (subject) {
            case BFS:
            case DFS:
            case MAX_CLIQUE:
            case MAX_INDEPENDENT_SET:
            case MIN_VERTEX_COVER:
            case TREE_GRAPHS:
                return graphFactory.createRandomConnectedGraph(task.getGraphVertices(), task.getGraphEdges());
            case EULER_CYCLE:
                if (task.getType() == GraphTaskType.BOOLEAN) {
                    return graphFactory.createRandomMaybeEulerianGraph();
                } else {
                    return graphFactory.createRandomEulerianGraph();
                }
            case HAMILTON_CYCLE:
                return graphFactory.createRandomHamiltonianGraph(task.getGraphVertices(), task.getGraphEdges());
            case MIN_SPANNING_TREE:
                return graphFactory.createRandomConnectedGraph(task.getGraphVertices(), task.getGraphEdges(), true);
            case BIPARTITE_GRAPHS:
                return graphFactory.createRandomMaybeBipartiteGraph(task.getGraphVertices());
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

}
