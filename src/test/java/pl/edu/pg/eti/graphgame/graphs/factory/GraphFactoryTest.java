package pl.edu.pg.eti.graphgame.graphs.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.config.Constants;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GraphFactoryTest {

    private static final Random RANDOM = new Random();

    private final GraphFactory graphFactory;

    public GraphFactoryTest() {
        this.graphFactory = new GraphFactory();
    }

    @Test
    public void createRandomConnectedGraphV2Test() {
        final int TEST_N = 9;
        final int TEST_M = 10;

        Graph graph = graphFactory.createRandomConnectedGraph(TEST_N, TEST_M);

        Assertions.assertTrue(isConnected(graph));
        Assertions.assertEquals(graph.getN(), TEST_N);
        Assertions.assertEquals(graph.getM(), TEST_M);
    }

    @Test
    public void createRandomConnectedGraphV2TestAShitTonOfRandomGraphs() {
        for (int i = 0; i < 1000000; i++) {
            int graphVertices = RANDOM.nextInt(
                    Constants.MAX_COMPLETE_GRAPH_VERTICES - Constants.MIN_COMPLETE_GRAPH_VERTICES
            ) + Constants.MIN_GRAPH_VERTICES;
            int graphEdges = RANDOM.nextInt(
                    (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
            ) + (graphVertices - 1);//todo DIVIDE BY 2!!!!!!!!!!
            try {
                Graph graph = graphFactory.createRandomConnectedGraph(graphVertices, graphEdges);
                //if (!isConnected(graph)) System.out.println(graph);
                Assertions.assertTrue(isConnected(graph));
                Assertions.assertEquals(graph.getN(), graphVertices);
                Assertions.assertEquals(graph.getM(), graphEdges);
            } catch (Exception e) {
                System.out.println("i = " + i + ";");
                System.out.println("n = " + graphVertices + ";");
                System.out.println("m = " + graphEdges + ";");
            }
        }
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
