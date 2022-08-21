package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

public class NamedGraphsChecker {

    public static boolean isTriangleGraph(Graph graph) {
        return GraphClassChecker.isCycle(graph) && graph.getN() == 3;
    }

    public static boolean isDiamondGraph(Graph graph) {
        return graph.getN() == 4 && graph.getM() == 5 && GraphClassChecker.isConnected(graph);
    }

    public static boolean isPetersenGraph(Graph graph) {
        Graph petersenGraph = new AdjacencyMatrixGraph(PETERSEN_GRAPH, 10, 15);
        return GraphAlgorithms.areGraphsIsomorphic(graph, petersenGraph);
    }

    public static boolean isButterflyGraph(Graph graph) {
        Graph butterfly = new AdjacencyMatrixGraph(BUTTERFLY_GRAPH, 5, 6);
        return GraphAlgorithms.areGraphsIsomorphic(graph, butterfly);
    }

    private static final int[][] PETERSEN_GRAPH = {
            {0, 1, 0, 0, 1, 1, 0, 0, 0, 0},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 1, 1},
            {0, 0, 1, 0, 0, 1, 0, 0, 0, 1},
            {0, 0, 0, 1, 0, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 1, 1, 0, 0}
    };

    private static final int[][] BUTTERFLY_GRAPH = {
            {0, 1, 1, 0, 0},
            {1, 0, 1, 0, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 1, 0, 1},
            {0, 0, 1, 1, 0}
    };

}
