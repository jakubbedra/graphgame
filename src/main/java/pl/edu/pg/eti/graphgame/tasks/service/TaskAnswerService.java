package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.graphgame.config.Constants;
import pl.edu.pg.eti.graphgame.exceptions.IncompleteTaskEntityException;
import pl.edu.pg.eti.graphgame.exceptions.InvalidGraphSizeInColoringAnswerException;
import pl.edu.pg.eti.graphgame.exceptions.UnsupportedTaskSubjectException;
import pl.edu.pg.eti.graphgame.graphs.GraphAlgorithms;
import pl.edu.pg.eti.graphgame.graphs.GraphClassChecker;
import pl.edu.pg.eti.graphgame.graphs.GraphUtils;
import pl.edu.pg.eti.graphgame.graphs.NamedGraphsChecker;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Edge;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedGraph;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class TaskAnswerService {

    @Autowired
    public TaskAnswerService() {
    }

    public boolean checkVertexSelectionAnswer(List<Integer> vertices, Task task,
                                              Graph graph) {
        switch (task.getSubject()) {
            case BFS:
                return GraphAlgorithms.breadthFirstSearch(graph, vertices);
            case DFS:
                return GraphAlgorithms.depthFirstSearch(graph, vertices);
            case MAX_CLIQUE:
                return vertices.size() == GraphAlgorithms.maxCliqueSize(graph)
                        && GraphAlgorithms.checkClique(graph, vertices);
            case MAX_INDEPENDENT_SET:
                return vertices.size() == GraphAlgorithms.maxIndependentSetSize(graph)
                        && GraphAlgorithms.checkIndependentSet(graph, vertices);
            case MIN_VERTEX_COVER:
                return vertices.size() == GraphAlgorithms.minVertexCoverSize(graph)
                        && GraphAlgorithms.checkVertexCover(graph, vertices);
            case EULER_CYCLE:
                return GraphAlgorithms.checkEulerCycle(graph, vertices);
            case HAMILTON_CYCLE:
                return GraphClassChecker.isHamiltonCycle(graph, vertices);
            case TRIVIAL_QUESTIONS:
                return checkTrivialQuestionVertexSelection(graph, vertices,
                        task.getDescriptionDetails());
            case DISTANCES:
                return checkDistancesVertexSelection(graph, vertices, task);
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    private boolean checkDistancesVertexSelection(Graph graph,
                                                  List<Integer> vertices, Task task) {
        switch (task.getDescriptionDetails()) {
            case Constants.ECCENTRICITY:
                List<Integer> specialValues = extractSpecialValues(task, 1);
                return vertices.size() - 1 == GraphAlgorithms.eccentricity(graph,
                        specialValues.get(0))
                        && verticesFormAPath(vertices);
            case Constants.RADIUS:
                return vertices.size() - 1 == GraphAlgorithms.radius(graph)
                        && verticesFormAPath(vertices);
            case Constants.DIAMETER:
                return vertices.size() - 1 == GraphAlgorithms.diameter(graph)
                        && verticesFormAPath(vertices);
        }
        return false;
    }

    private boolean verticesFormAPath(List<Integer> vertices) {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (vertices.get(i) == vertices.get(j) && i != j) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkTrivialQuestionVertexSelection(Graph graph,
                                                        List<Integer> vertices, String descriptionDetails) {
        if (descriptionDetails.equals(Constants.MIN_CLIQUE)) {
            return vertices.size() == 1;
        } else if (descriptionDetails.equals(Constants.MAX_VERTEX_COVER)) {
            for (int i = 0; i < graph.getN(); i++) {
                if (!vertices.contains((Integer) i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkEdgeSelectionAnswer(List<Edge> edges, Task task,
                                            Graph graph) {
        List<Integer> vertices = null;
        switch (task.getSubject()) {
            case EULER_CYCLE:
                return checkEulerCycleEdgeSelection(edges, graph);
            case TRAVELING_SALESMAN_PROBLEM:
                vertices = edgesFormACycleVertices(edges);
                return graph.getN() + 1 == vertices.size() && allVerticesIncluded(graph,
                        vertices) &&
                        sumTotalWeight(edges) == GraphAlgorithms.solveTSP(
                                (WeightedGraph) graph);
            case CHINESE_POSTMAN_PROBLEM:
                vertices = edgesFormACycleVertices(edges);
                return allVerticesIncluded(graph, vertices) &&
                        sumTotalWeight(edges) == GraphAlgorithms.solveCPP(
                                (WeightedGraph) graph) && allEdgesIncluded(graph, vertices);
            case MIN_SPANNING_TREE:
                return sumTotalWeight(edges)
                        == GraphAlgorithms.getMinSpanningTreeTotalWeight(
                        (WeightedGraph) graph) &&
                        GraphClassChecker.isConnected(
                                getGraphFromEdges(edges, graph.getN()));
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    public boolean checkVertexColoringTaskAnswer(int[] colors, Task task,
                                                 Graph graph) {
        if (colors.length != graph.getN()) {
            throw new InvalidGraphSizeInColoringAnswerException("Vertex coloring invalid amount of colors in answer.");
        }
        switch (task.getSubject()) {
            case CYCLE_GRAPHS:
                return GraphAlgorithms.isVertexColoringValid(graph, colors) &&
                        getNumberOfColorsUsed(colors, graph.getN()) == 2 + (graph.getN() % 2);
            case PATH_GRAPHS:
                return GraphAlgorithms.isVertexColoringValid(graph, colors) &&
                        getNumberOfColorsUsed(colors, graph.getN()) <= 2;
            case STAR_GRAPHS:
            case BIPARTITE_GRAPHS:
            case TREE_GRAPHS:
                return GraphAlgorithms.isVertexColoringValid(graph, colors) &&
                        getNumberOfColorsUsed(colors, graph.getN()) == 2;
            case COMPLETE_GRAPHS:
                return GraphAlgorithms.isVertexColoringValid(graph, colors) &&
                        getNumberOfColorsUsed(colors, graph.getN()) == graph.getN();
            case VERTEX_COLORING:
                return GraphAlgorithms.isVertexColoringValid(graph, colors) &&
                        getNumberOfColorsUsed(colors, graph.getN())
                                == GraphAlgorithms.calculateChromaticNumber(graph);
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    public boolean checkEdgeColoringTaskAnswer(int[][] colors, Task task,
                                               Graph graph) {
        if (colors.length != graph.getN()) {
            throw new InvalidGraphSizeInColoringAnswerException(
                    "Edge coloring invalid amount of colors in answer.");
        } else {
            for (int i = 0; i < colors.length; ++i) {
                if (colors[i].length != graph.getN()) {
                    throw new InvalidGraphSizeInColoringAnswerException(
                            "Edge coloring invalid amount of colors in answer.");
                }
            }
        }
        int delta, colorsCount;
        switch (task.getSubject()) {
            case CYCLE_GRAPHS:
                return countEdgeColors(colors, graph.getN()) == 2 + (graph.getN() % 2) &&
                        GraphAlgorithms.isEdgeColoringValid(graph, colors);
            case PATH_GRAPHS:
                return countEdgeColors(colors, graph.getN()) <= 2 &&
                        GraphAlgorithms.isEdgeColoringValid(graph, colors);
            case STAR_GRAPHS:
                return countEdgeColors(colors, graph.getN()) == graph.getM() &&
                        GraphAlgorithms.isEdgeColoringValid(graph, colors);
            case BIPARTITE_GRAPHS:
            case TREE_GRAPHS:
                delta = graph.delta();
                colorsCount = countEdgeColors(colors, graph.getN());
                return colorsCount == delta &&
                        GraphAlgorithms.isEdgeColoringValid(graph, colors);
            case COMPLETE_GRAPHS:
                delta = graph.delta();
                colorsCount = countEdgeColors(colors, graph.getN());
                if (graph.getN() % 2 == 0) {
                    return (colorsCount == delta) &&
                            GraphAlgorithms.isEdgeColoringValid(graph, colors);
                } else {
                    return (colorsCount == delta + 1) &&
                            GraphAlgorithms.isEdgeColoringValid(graph, colors);
                }
            case EDGE_COLORING:
                delta = graph.delta();
                colorsCount = countEdgeColors(colors, graph.getN());
                return (colorsCount == delta || colorsCount == delta + 1) &&
                        GraphAlgorithms.isEdgeColoringValid(graph, colors);
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    private int countEdgeColors(int[][] colors, int n) {
        List<Integer> foundColors = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (colors[i][j] != -1 && !foundColors.contains(
                        (Integer) colors[i][j])) {
                    foundColors.add(colors[i][j]);
                }
            }
        }
        return foundColors.size();
    }

    private int getNumberOfColorsUsed(int[] colors, int n) {
        List<Integer> checkedColors = new LinkedList<>();
        int numberOfColors = 0;
        for (int i = 0; i < n; i++) {
            if (!checkedColors.contains((Integer) colors[i])) {
                numberOfColors++;
                checkedColors.add(colors[i]);
            }
        }
        return numberOfColors;
    }

    private boolean allVerticesIncluded(Graph graph, List<Integer> vertices) {
        for (int i = 0; i < graph.getN(); i++) {
            if (!vertices.contains(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean allEdgesIncluded(Graph graph, List<Integer> vertices) {
        Graph copy = new AdjacencyMatrixGraph(graph);
        for (int i = 0; i < graph.getN() - 1; i++) {
            if (copy.edgeExists(vertices.get(i), vertices.get(i + 1))) {
                copy.removeEdge(vertices.get(i), vertices.get(i + 1));
            }
        }
        return copy.getM() == 0;
    }

    private int sumTotalWeight(List<Edge> edges) {
        return edges.stream()
                .map(Edge::getWeight)
                .reduce(0, (subtotal, edge) -> subtotal += edge);
    }

    public boolean checkBooleanTaskAnswer(boolean answer, Task task,
                                          Graph graph) {
        Pair<Graph, Graph> graphPair = null;
        switch (task.getSubject()) {
            case EULER_CYCLE:
                return answer == GraphClassChecker.isEulerian(graph);
            case HAMILTON_CYCLE:
                return answer == GraphClassChecker.isHamiltonian(graph);
            case TREE_GRAPHS:
                return answer == GraphClassChecker.isTree(graph);
            case BIPARTITE_GRAPHS:
                return answer == GraphClassChecker.isBipartite(graph);
            case COMPLETE_GRAPHS:
                return answer == GraphClassChecker.isComplete(graph);
            case PLANAR_GRAPHS:
                return answer == GraphClassChecker.isPlanar(graph);
            case ISOMORPHISM:
                graphPair = GraphUtils.splitGraph(graph);
                return answer == GraphAlgorithms.areGraphsIsomorphic(
                        graphPair.getFirst(), graphPair.getSecond());
            case HOMEOMORPHISM:
                graphPair = GraphUtils.splitGraph(graph);
                return answer == GraphAlgorithms.areGraphsHomeomorphic(
                        graphPair.getFirst(), graphPair.getSecond());
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

    private boolean checkEulerCycleEdgeSelection(List<Edge> edges,
                                                 Graph graph) {
        if (edges.size() <= 1) {
            return false;
        }
        List<Integer> vertices = edgesFormACycleVertices(edges);
        return GraphAlgorithms.checkEulerCycle(graph, vertices);
    }

    private List<Integer> edgesFormACycleVertices(List<Edge> edges) {
        List<Integer> vertices = new LinkedList<>();
        if (edges.size() < 2) {
            return vertices;
        }
        if (getCommonVertex(edges.get(0), edges.get(1)) == edges.get(0).getV1()) {
            vertices.add(edges.get(0).getV2());
        } else {
            vertices.add(edges.get(0).getV1());
        }
        for (int i = 0; i < edges.size() - 1; i++) {
            int commonVertex = getCommonVertex(edges.get(i), edges.get(i + 1));
            if (commonVertex == -1) {
                return vertices;
            }
            vertices.add(commonVertex);
        }
        int commonVertex = getCommonVertex(edges.get(0),
                edges.get(edges.size() - 1));
        if (commonVertex != -1) {
            vertices.add(commonVertex);
        }
        return vertices;
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
        if (graph.getN() != task.getGraphVertices()
                && task.getSubject() != GraphTaskSubject.NAMED_GRAPHS) {
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
                        GraphClassChecker.isCompleteBipartite(graph,
                                specialValuesBG.get(0), specialValuesBG.get(1));
            case NAMED_GRAPHS:
                return checkNamedGraphsTaskAnswer(graph,
                        task.getDescriptionDetails());
            case TRIVIAL_QUESTIONS:
                if (task.getDescriptionDetails().equals(Constants.EMPTY_GRAPH)) {
                    return GraphClassChecker.isEmpty(graph);
                }
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    private boolean checkNamedGraphsTaskAnswer(Graph graph,
                                               String descriptionDetails) {
        switch (descriptionDetails) {
            case "Petersen Graph":
                return NamedGraphsChecker.isPetersenGraph(graph);
            case "Butterfly":
                return NamedGraphsChecker.isButterflyGraph(graph);
            case "Diamond":
                return NamedGraphsChecker.isDiamondGraph(graph);
            case "Triangle":
                return NamedGraphsChecker.isTriangleGraph(graph);
        }
        return false;
    }

    private List<Integer> extractSpecialValues(Task task, int count) {
        if (!task.getSpecialValues().isEmpty()) {
            List<Integer> specialValues = new LinkedList<>();
            String[] split = task.getSpecialValues().split(";");
            Arrays.stream(split)
                    .forEach(s -> specialValues.add(Integer.parseInt(s)));
            if (specialValues.size() != count) {
                throw new IncompleteTaskEntityException("");
            }
            return specialValues;
        } else {
            throw new IncompleteTaskEntityException("");
        }
    }

}
