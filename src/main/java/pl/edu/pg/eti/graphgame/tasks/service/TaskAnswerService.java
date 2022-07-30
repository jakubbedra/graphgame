package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.graphgame.exceptions.UnsupportedTaskSubjectException;
import pl.edu.pg.eti.graphgame.graphs.GraphAlgorithms;
import pl.edu.pg.eti.graphgame.graphs.GraphClassChecker;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

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
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    public boolean checkDrawGraphAnswer(Graph graph, Task task) {
        switch (task.getSubject()) {
            case COMPLETE_GRAPHS:
                return GraphClassChecker.isComplete(graph) && graph.getN() == task.getGraphVertices();
            case PATH_GRAPHS:
                return GraphClassChecker.isPath(graph) && graph.getN() == task.getGraphVertices();
            case CYCLE_GRAPHS:
                return GraphClassChecker.isCycle(graph) && graph.getN() == task.getGraphVertices();
            case STAR_GRAPHS:
                return GraphClassChecker.isStar(graph) && graph.getN() == task.getGraphVertices();
            case WHEEL_GRAPHS:
                return GraphClassChecker.isWheel(graph) && graph.getN() == task.getGraphVertices();
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

}
