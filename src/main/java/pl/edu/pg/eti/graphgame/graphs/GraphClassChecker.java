package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.LinkedList;
import java.util.List;

public class GraphClassChecker {

    /**
     * Checks if the given graph is an empty graph
     */
    public static boolean isEmpty(Graph graph) {
        return graph.getM() == 0;
    }

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
        if (graph.getN() < 3) {
            return false;
        }
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

        if (graph.getN() == 1 || graph.getN() == 2 && graph.getM() == 1 || isCycle(graph) && graph.getN() == 4) {
            return true;
        }

        int n = (int) Math.ceil(log2(graph.getN()));
        if (graph.getM() != ((int) Math.pow(2, n - 1) * n)) {
            return false;
        }
        // todo
        // 1. generate n-sized gray codes
        // 2. assign gray code numbers to vertices
        // if it cannot be done, return false
        HyperCubeTester tester = new HyperCubeTester(graph, n);

        return tester.isHyperCubeLargerThan2Dimensions();
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

    public static boolean isTree(Graph graph) {
        return isConnected(graph) && graph.getN() - graph.getM() == 1;
    }

    private static boolean isBipartite(Graph g, int[] colors, int parent) {
        LinkedList<Integer> q = new LinkedList<>();
        q.add(parent);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v = 0; v < g.getN(); v++) {
                if (g.edgeExists(u, v) && colors[v] == -1) {
                    if (colors[u] == 1) {
                        colors[v] = 0;
                    } else if (colors[u] == 0) {
                        colors[v] = 1;
                    }
                    q.add(v);
                } else if (g.edgeExists(u, v) && colors[u] == colors[v]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isBipartite(Graph graph) {
        if (graph.getN() <= 1) {
            return false;
        } else if (graph.getN() == 2) {
            return true;
        }
        int[] colors = new int[graph.getN()];
        for (int i = 0; i < graph.getN(); i++) {
            colors[i] = -1; // no color assigned
        }
        colors[0] = 0;

        return isBipartite(graph, colors, 0);
    }

    public static boolean isCompleteBipartite(Graph graph, int r, int s) {
        if (!isBipartite(graph)) {
            return false;
        }
        int group1 = 0;
        int group2 = 0;
        for (int i = 0; i < graph.getN(); i++) {
            int degree = graph.degree(i);
            if (degree == r) {
                group2++;
            } else if (degree == s) {
                group1++;
            }
        }
        return group1 == r && group2 == s;
    }

    public static boolean isEulerian(Graph graph) {
        for (int i = 0; i < graph.getN(); i++) {
            if (graph.degree(i) % 2 != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHamiltonCycle(Graph graph, List<Integer> vertices) {
        if (vertices.size() != graph.getN() && vertices.size() != graph.getN() + 1) {
            return false;
        }
        for (int i = 0; i < vertices.size() - 1; i++) {
            if (!graph.edgeExists(vertices.get(i), vertices.get(i + 1))) {
                return false;
            }
        }
        return graph.edgeExists(vertices.get(0), vertices.get(vertices.size() - 1))
                || vertices.get(0).intValue() == vertices.get(vertices.size() - 1).intValue();
    }

    private static boolean checkHamiltonCycle(Graph graph, List<Integer> vertices) {
        if (vertices.size() == graph.getN()) {
            return isHamiltonCycle(graph, vertices);
        } else {
            for (int i = 0; i < graph.getN(); i++) {
                if (!vertices.contains(i)) {
                    vertices.add(i);
                    boolean hamiltonCycle = checkHamiltonCycle(graph, vertices);
                    vertices.remove((Integer) i);
                    if (hamiltonCycle) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isHamiltonian(Graph graph) {
        return checkHamiltonCycle(graph, new LinkedList<>());
    }

    public static boolean isPlanar(Graph graph) {
        KuratowskiPlanarityTester planarityTester = new KuratowskiPlanarityTester();
        return planarityTester.isPlanar(graph);
    }

}
