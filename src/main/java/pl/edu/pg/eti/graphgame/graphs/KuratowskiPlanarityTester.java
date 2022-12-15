package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KuratowskiPlanarityTester {

    public KuratowskiPlanarityTester() {
    }

    @Deprecated // It just does not work, was a nice try though... xd
    public boolean isPlanar2(Graph graph) {
        if (graph.getM() > 3 * graph.getN() - 6) {
            return false;
        }
        // removing all the deg 2 vertices
        Graph graph2 = GraphUtils.copyWithRetractedEdges(graph);

        // finding clique 5
        if (graph2.getN() >= 5) {
            if (containsK5(graph, new ArrayList<>(5))) {
                return false;
            }
        }

        // finding subgraph K 3,3
        // if first 3 vertices have connection to the last 3
        if (containsK3_3(graph, new ArrayList<>(6))) {
            return false;
        }

        return true;
    }

    public boolean isPlanar(Graph graph) {
        if (graph.getM() > 3 * graph.getN() - 6) {
            return false;
        }

        //choose edge
        RetractableAdjacencyMatrix g = new RetractableAdjacencyMatrix(graph);
        if (hasK5OrK3_3(g)) {
            return false;
        }

        return true;
    }

    private boolean hasK5OrK3_3(RetractableAdjacencyMatrix g) {
        if (g.getN() <= 4) {
            return false;
        }
        if (g.getN() == 5) {
            if (GraphClassChecker.isComplete(g)) {
                return true;
            }
        } else if (g.getN() == 6) {
            if (containsK3_3(g, new LinkedList<>())) {
                return true;
            }
        }
        for (int i = 0; i < g.getN(); i++) {
            int v2 = i == g.getN() - 1 ? 0 : i + 1;
            if(g.edgeExists(i, v2)){
                RetractableAdjacencyMatrix g2 = new RetractableAdjacencyMatrix(g);
                g2.mergeVertices(i, v2);
                if (hasK5OrK3_3(g2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsK5(Graph graph, List<Integer> vertices) {
        if (vertices.size() == 5) {
            return GraphAlgorithms.checkClique(graph, vertices);
        }
        for (int i = 0; i < graph.getN(); i++) {
            if (!vertices.contains(i)) {
                vertices.add((Integer) i);
                if (containsK5(graph, vertices)) {
                    return true;
                }
                vertices.remove((Integer) i);
            }
        }
        return false;
    }

    private boolean isSubgraphK3_3(Graph graph, List<Integer> vertices) {
        for (int i = 0; i < 3; i++) {
            for (int j = 3; j < 6; j++) {
                if (!graph.edgeExists(vertices.get(i), vertices.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean containsK3_3(Graph graph, List<Integer> vertices) {
        if (vertices.size() == 6) {
            return isSubgraphK3_3(graph, vertices);
        }
        for (int i = 0; i < graph.getN(); i++) {
            if (!vertices.contains(i)) {
                vertices.add((Integer) i);
                if (containsK3_3(graph, vertices)) {
                    return true;
                }
                vertices.remove((Integer) i);
            }
        }
        return false;
    }


    private static class RetractableAdjacencyMatrix extends AdjacencyMatrixGraph {

        public RetractableAdjacencyMatrix(Graph g) {
            super(g);
        }

        /**
         * Merges v into u.
         */
        public void mergeVertices(int u, int v) {
            for (int i = 0; i < n; i++) {
                if (matrix[i][v] != 0) {
                    matrix[i][u] = matrix[i][v];
                    matrix[u][i] = matrix[v][i];
                    matrix[i][v] = 0;
                    matrix[v][i] = 0;
                }
            }
            removeVertex(v);
        }

    }

}
