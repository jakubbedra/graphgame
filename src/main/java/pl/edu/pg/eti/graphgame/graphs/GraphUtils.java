package pl.edu.pg.eti.graphgame.graphs;

import org.springframework.data.util.Pair;
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
        //todo: when checking isomorphism do a vertex -> label array for the second graph
        return Pair.of(g1, g2);
    }

}
