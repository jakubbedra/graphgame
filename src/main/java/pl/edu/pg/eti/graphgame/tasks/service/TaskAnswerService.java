package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.graphgame.exceptions.IncompleteTaskEntityException;
import pl.edu.pg.eti.graphgame.exceptions.UnsupportedTaskSubjectException;
import pl.edu.pg.eti.graphgame.graphs.GraphAlgorithms;
import pl.edu.pg.eti.graphgame.graphs.GraphClassChecker;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Edge;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedGraph;
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
            case MIN_VERTEX_COVER:
                return vertices.size() == GraphAlgorithms.minVertexCoverSize(graph) && GraphAlgorithms.checkVertexCover(graph, vertices);
            case EULER_CYCLE:
                return GraphAlgorithms.checkEulerCycle(graph, vertices);
            case HAMILTON_CYCLE:
                return GraphClassChecker.isHamiltonCycle(graph, vertices);
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    public boolean checkEdgeSelectionAnswer(List<Edge> edges, Task task, Graph graph) {
        switch (task.getSubject()) {
            case EULER_CYCLE:
                return checkEulerCycleEdgeSelection(edges, graph);
            case MIN_SPANNING_TREE:
                int totalWeight = edges.stream()
                        .map(Edge::getWeight)
                        .reduce(0, (subtotal, edge) -> subtotal += edge);
                return totalWeight == GraphAlgorithms.getMinSpanningTreeTotalWeight((WeightedGraph) graph) &&
                        GraphClassChecker.isConnected(getGraphFromEdges(edges, graph.getN()));
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    public boolean checkBooleanTaskAnswer(boolean answer, Task task, Graph graph) {
        switch (task.getSubject()) {
            case EULER_CYCLE:
                return answer == GraphClassChecker.isEulerian(graph);
            case TREE_GRAPHS:
                return answer == GraphClassChecker.isTree(graph);
            case BIPARTITE_GRAPHS:
                return answer == GraphClassChecker.isBipartite(graph);
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    private Graph getGraphFromEdges(List<Edge> edges, int n) {
        Graph g = new AdjacencyMatrixGraph(n);
        for (int i = 0; i < edges.size(); i++) {
            g.addEdge(edges.get(i).getV1(), edges.get(i).getV2());
        }
        return g;
    }

    private boolean checkEulerCycleEdgeSelection(List<Edge> edges, Graph graph) {
        if (edges.size() <= 1) {
            return false;
        }
        // checking if it even is a cycle
        List<Integer> vertices = new LinkedList<>();
        if (getCommonVertex(edges.get(0), edges.get(1)) == edges.get(0).getV1()) {
            vertices.add(edges.get(0).getV2());
        } else {
            vertices.add(edges.get(0).getV1());
        }
        for (int i = 0; i < edges.size() - 1; i++) {
            int commonVertex = getCommonVertex(edges.get(i), edges.get(i + 1));
            if (commonVertex == -1) {
                return false;
            }
            vertices.add(commonVertex);
        }
        int commonVertex = getCommonVertex(edges.get(0), edges.get(edges.size() - 1));
        if (commonVertex == -1) {
            return false;
        }
        vertices.add(commonVertex);
        return GraphAlgorithms.checkEulerCycle(graph, vertices);
    }

    private int getCommonVertex(Edge e1, Edge e2) {
        if (e1.getV1() == e2.getV1() || e1.getV1() == e2.getV2()) {
            return e1.getV1();
        } else if (e1.getV2() == e2.getV2() || e1.getV2() == e2.getV1()) {
            return e1.getV2();
        } else {
            return -1;
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
                List<Integer> specialValues = extractSpecialValues(task, 1);
                return GraphClassChecker.isKRegular(graph, specialValues.get(0));
            case BIPARTITE_GRAPHS:
                List<Integer> specialValuesBG = extractSpecialValues(task, 2);
                return GraphClassChecker.isConnected(graph) &&
                        GraphClassChecker.isCompleteBipartite(graph, specialValuesBG.get(0), specialValuesBG.get(1));
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    private List<Integer> extractSpecialValues(Task task, int count) {
        if (!task.getSpecialValues().isEmpty()) {
            List<Integer> specialValues = new LinkedList<>();
            String[] split = task.getSpecialValues().split(";");
            Arrays.stream(split).forEach(s -> specialValues.add(Integer.parseInt(s)));
            if (specialValues.size() != count) {
                throw new IncompleteTaskEntityException("");
            }
            return specialValues;
        } else {
            throw new IncompleteTaskEntityException("");
        }
    }

}
