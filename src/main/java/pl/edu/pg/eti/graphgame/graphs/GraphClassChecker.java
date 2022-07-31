package pl.edu.pg.eti.graphgame.graphs;

import org.springframework.security.core.parameters.P;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.List;

public class GraphClassChecker {

    /**
     * Checks if the given graph is a complete graph
     */
    public static boolean isComplete(Graph graph) {
        for (int i = 0; i < graph.getN(); i++) {
            for (int j = 0; j < graph.getN(); j++) {
                if (i != j && !graph.edgeExists(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given graph is a path graph
     */
    public static boolean isPath(Graph graph) {
        if (!isConnected(graph)) {
            return false;
        }

        int deg1V = 0;
        int deg2V = 0;

        for (int i = 0; i < graph.getN(); i++) {
            int deg = graph.degree(i);
            if (deg == 1) {
                deg1V++;
            } else if (deg == 2) {
                deg2V++;
            } else {
                return false;
            }
        }

        return deg1V == 2 && deg2V == graph.getN() - 2;
    }

    /**
     * Checks if the given graph is connected
     */
    public static boolean isConnected(Graph graph) {
        List<Integer> visited = GraphAlgorithms.breadthFirstSearch(graph);
        for (int i = 0; i < graph.getN(); i++) {
            if (!visited.contains(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given graph is a Cycle Graph
     */
    public static boolean isCycle(Graph graph) {
        if (!isConnected(graph)) {
            return false;
        }
        for (int i = 0; i < graph.getN(); i++) {
            if (graph.degree(i) != 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given graph is a Star Graph
     */
    public static boolean isStar(Graph graph) {
        if (!isConnected(graph)) {
            return false;
        }
        int degNMinus1V = 0;
        int deg1V = 0;
        for (int i = 0; i < graph.getN(); i++) {
            int deg = graph.degree(i);
            if (deg == graph.getN() - 1) {
                degNMinus1V++;
            } else if (deg == 1) {
                deg1V++;
            } else {
                return false;
            }
        }
        return degNMinus1V == 1 && deg1V == graph.getN() - 1;
    }

    /**
     * Checks if the given graph is a Wheel Graph
     */
    public static boolean isWheel(Graph graph) {
        if (!isConnected(graph)) {
            return false;
        }
        int degNMinus1V = 0;
        int deg3V = 0;
        for (int i = 0; i < graph.getN(); i++) {
            int deg = graph.degree(i);
            if (deg == graph.getN() - 1) {
                degNMinus1V++;
            } else if (deg == 3) {
                deg3V++;
            } else {
                return false;
            }
        }
        return degNMinus1V == 1 && deg3V == graph.getN() - 1;
    }

    /**
     * Checks if graph is a Hypercube
     */
    public static boolean isHypercube(Graph graph) {
        if (!isConnected(graph) || !(graph.getN() > 0 && isKRegular(graph, graph.degree(0)))) {
            return false;
        }
        int n = (int) Math.ceil(log2(graph.getN()));
        return graph.getM() == ((int) Math.pow(2, n - 1) * n);
    }

    private static int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    /**
     * Checks if graph is a Cubic graph
     */
    public static boolean isCubic(Graph graph) {
        return isKRegular(graph, 3);
    }

    /**
     * Checks if graph is k-regular
     */
    public static boolean isKRegular(Graph graph, int k) {
        return 2 * graph.getM() == graph.getN() * k;
    }


}
