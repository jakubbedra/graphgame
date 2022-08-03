package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.graphgame.exceptions.IncompleteTaskEntityException;
import pl.edu.pg.eti.graphgame.exceptions.UnsupportedTaskSubjectException;
import pl.edu.pg.eti.graphgame.graphs.GraphAlgorithms;
import pl.edu.pg.eti.graphgame.graphs.GraphClassChecker;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class TaskAnswerService {

    @Autowired
    public TaskAnswerService() {
    }

    public boolean checkVertexSelectionAnswer(List<Integer> vertices, Task task, Graph graph) {
        switch (task.getSubject()) {
            case BFS:
                return vertices.equals(GraphAlgorithms.breadthFirstSearch(graph));
            case DFS:
                return vertices.equals(GraphAlgorithms.depthFirstSearch(graph));
            case MAX_CLIQUE:
                return vertices.size() == GraphAlgorithms.maxCliqueSize(graph) && GraphAlgorithms.checkClique(graph, vertices);
            case MAX_INDEPENDENT_SET:
                return vertices.size() == GraphAlgorithms.maxIndependentSetSize(graph) && GraphAlgorithms.checkIndependentSet(graph, vertices);
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    public boolean checkDrawGraphAnswer(Graph graph, Task task) {
        if (graph.getN() != task.getGraphVertices()) {
            return false;
        }
        switch (task.getSubject()) {
            case COMPLETE_GRAPHS:
                return GraphClassChecker.isComplete(graph);
            case PATH_GRAPHS:
                return GraphClassChecker.isPath(graph);
            case CYCLE_GRAPHS:
                return GraphClassChecker.isCycle(graph);
            case STAR_GRAPHS:
                return GraphClassChecker.isStar(graph);
            case WHEEL_GRAPHS:
                return GraphClassChecker.isWheel(graph);
            case HYPERCUBES:
                return GraphClassChecker.isHypercube(graph);
            case REGULAR_GRAPHS:
                List<Integer> specialValues = new LinkedList<>();
                if (!task.getSpecialValues().isEmpty()) {
                    String[] split = task.getSpecialValues().split(";");
                    Arrays.stream(split).forEach(s -> specialValues.add(Integer.parseInt(s)));
                    return GraphClassChecker.isKRegular(graph, specialValues.get(0));
                } else {
                 throw new IncompleteTaskEntityException("");
                }
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

}
