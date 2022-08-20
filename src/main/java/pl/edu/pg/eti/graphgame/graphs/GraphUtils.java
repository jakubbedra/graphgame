package pl.edu.pg.eti.graphgame.graphs;

import org.springframework.data.util.Pair;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.NeighbourListsGraph;

import java.util.LinkedList;
import java.util.List;

public class GraphUtils {

    /**
     * Splits a graph that has 2 connected components into 2 graphs
     * NOTE: make sure that graph 1 has the vertices labeled as <0, x)
     * and graph 2 as <x, n)
     */
    public static Pair<Graph, Graph> splitGraph(Graph g) {
        //do bfs on the graph (while bfs-ing create the first graph)
        List<Integer> vertices1 = GraphAlgorithms.breadthFirstSearch(g);
        Graph g1 = new NeighbourListsGraph(vertices1.size());
        for (int v : vertices1) {
            List<Integer> neighbours = g.neighbours(v);
            for (int w : neighbours) {
                if (v != w) {
                    g1.addEdge(v, w);
                }
            }
        }
        List<Integer> vertices2 = new LinkedList<>();
        for (int i = 0; i < g.getN(); i++) {
            if (!vertices1.contains(i)) {
                vertices2.add(i);
            }
        }

        Graph g2 = new NeighbourListsGraph(vertices2.size());
        for (int v : vertices2) {
            List<Integer> neighbours = g.neighbours(v);
            for (int w : neighbours) {
                if (v - vertices1.size() != w - vertices1.size()) {
                    g2.addEdge(v - vertices1.size(), w - vertices1.size());
                }
            }
        }
        return Pair.of(g1, g2);
    }

    public static Graph copyWithRetractedEdges(Graph graph) {
        Graph graph2 = new AdjacencyMatrixGraph(graph);
        boolean hasDeg2Vertex = false;
        do {
            hasDeg2Vertex = false;
            int deg2V = -1;
            for (int i = 0; i < graph2.getN(); i++) {
                if (graph2.degree(i) == 2) {
                    hasDeg2Vertex = true;
                    deg2V = i;
                    break;
                }
            }
            if (hasDeg2Vertex) {
                List<Integer> neighbours = graph2.neighbours(deg2V);
                try {
                    graph2.addEdge(neighbours.get(0), neighbours.get(1));
                } catch (IndexOutOfBoundsException exc) {
                    exc.printStackTrace();
                }
                graph2.removeVertex(deg2V);
            }
        } while (hasDeg2Vertex);
        return graph2;
    }

}
