package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.ArrayList;
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
        if(hasK5OrK3_3(g)){
            return false;
        }

        return true;
    }

    private boolean hasK5OrK3_3(RetractableAdjacencyMatrix g) {
        if (g.getN() == 5) {
            if (GraphClassChecker.isComplete(g)) {
                return true;
            }
        } else if (g.getN() == 6) {
            if (isSubgraphK3_3(g, List.of(0, 1, 2, 3, 4, 5))) {
                return true;
            }
        }
        for (int i = 0; i < g.getN(); i++) {
            RetractableAdjacencyMatrix g2 = new RetractableAdjacencyMatrix(g);
            // merge vertices
            if (i != g.getN() - 1)
                g2.mergeVertices(i, i + 1);
            else
                g2.mergeVertices(i, 0);
            if (hasK5OrK3_3(g2)) {
                return true;
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
