package pl.edu.pg.eti.graphgame.graphs.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.NeighbourListsGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Component
public class GraphFactory {

    private final Random RANDOM;

    @Autowired
    public GraphFactory() {
        this.RANDOM = new Random();
    }

    /**
     * Creates a random connected Graph
     *
     * @param n number of vertices the Graph will have
     * @return generated Graph
     */
    public Graph createRandomConnectedGraph(int n) {
        Graph graph = new NeighbourListsGraph(n);

        //we start at vertex 2, because at 0 nothing really happens,
        //and at 1 we always have the edge connected to 0
        graph.addEdge(0, 1);
        for (int i = 2; i < n; i++) {
            int count = RANDOM.nextInt(i) + 1;//random edge count (at least 1)
            int[] edges = new int[i];
            for (int j = 0; j < i; j++) {
                edges[j] = j;
            }
            List<Integer> edgesToAdd = getRandomEdges(edges, count);
            for (int edge : edgesToAdd) {
                graph.addEdge(i, edge);
            }
        }

        return graph;
    }

    private List<Integer> getRandomEdges(int[] edges, int count) {
        List<Integer> ret = new LinkedList<>();
        //randomizing
        for (int i = 0; i < count; i++) {
            int ind = RANDOM.nextInt(edges.length - i) + i;
            int tmp = edges[i];
            edges[i] = edges[ind];
            edges[ind] = tmp;
            ret.add(edges[i]);
        }
        return ret;
    }

}
