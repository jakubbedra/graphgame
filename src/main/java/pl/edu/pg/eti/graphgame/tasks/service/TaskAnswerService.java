package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.graphgame.exceptions.UnsupportedTaskSubjectException;
import pl.edu.pg.eti.graphgame.graphs.GraphAlgorithms;
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
                return GraphAlgorithms.isComplete(graph) && graph.getN() == task.getGraphVertices();
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

}
