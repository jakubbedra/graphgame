package pl.edu.pg.eti.graphgame.graphs.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphFactoryTest {

    private final GraphFactory graphFactory;

    public GraphFactoryTest() {
        this.graphFactory = new GraphFactory();
    }

    @Test
    public void createRandomConnectedGraphTest() {
        int testGraphSize = 6;
        Graph graph = graphFactory.createRandomConnectedGraph(testGraphSize);

        Assertions.assertTrue(isConnected(graph));
    }

    //todo: maybe move it to a static class or sth like that dunno
    private boolean isConnected(Graph g) {
        List<Integer> visited = new LinkedList<>();
        List<Integer> expected = new ArrayList<>(g.getN());
        for (int i = 0; i < g.getN(); i++) {
            expected.add(i);
        }
        depthFirstSearch(g, 0, visited);

        return visited.size() == expected.size() &&
                visited.containsAll(expected) &&
                expected.containsAll(visited);
    }

    private void depthFirstSearch(Graph g, int v, List<Integer> visited) {
        if (visited.contains(v)) {
            return;
        }
        visited.add(v);
        for (Integer neighbour : g.neighbours(v)) {
            if (!visited.contains(neighbour)) {
                depthFirstSearch(g, neighbour, visited);
            }
        }
    }

}
