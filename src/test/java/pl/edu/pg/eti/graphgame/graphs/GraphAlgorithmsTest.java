package pl.edu.pg.eti.graphgame.graphs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.config.Constants;
import pl.edu.pg.eti.graphgame.graphs.factory.GraphFactory;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.List;
import java.util.Random;

public class GraphAlgorithmsTest {

    private final int[][] TEST_MATRIX = {
            {0, 1, 1, 0, 0, 1},
            {1, 0, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0},
            {0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0}
    };

    private final int TEST_N = 6;
    private final int TEST_M = 0;

    private Graph graph;

    @BeforeEach
    public void setup() {
        this.graph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
    }

    @Test
    public void breadthFirstSearchTest() {
        final List<Integer> expected = List.of(0, 1, 2, 5, 4, 3);
        List<Integer> breadthFirstSearch = GraphAlgorithms.breadthFirstSearch(graph);
        Assertions.assertThat(expected).isEqualTo(breadthFirstSearch);
    }

    @Test
    public void depthFirstSearchTest() {
        final List<Integer> expected = List.of(0, 1, 4, 5, 2, 3);
        List<Integer> depthFirstSearch = GraphAlgorithms.depthFirstSearch(graph);
        Assertions.assertThat(expected).isEqualTo(depthFirstSearch);
    }

    @Test
    public void maxCliqueTest() {
        final int[][] TEST = {
                {0, 1, 0, 0, 1, 0},
                {1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 0, 1, 1},
                {1, 1, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 0}
        };
        final int N = 6;
        final int M = 7;
        Graph g = new AdjacencyMatrixGraph(TEST, N, M);

        int expectedMaxClique = 3;

        Assertions.assertThat(GraphAlgorithms.maxCliqueSize(g)).isEqualTo(expectedMaxClique);
    }

    @Test
    public void maxCliqueSpeedTest() {
        final Random RANDOM = new Random();
        final int NUMBER_OF_ITERATIONS = 100;
        final int ADDITIONAL_MAX_VERTEX_COUNT = 12;
        GraphFactory factory = new GraphFactory();
        int verticesAvg = 0;
        int edgesAvg = 0;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            int graphVertices = RANDOM.nextInt(
                    Constants.MAX_GRAPH_VERTICES + ADDITIONAL_MAX_VERTEX_COUNT - Constants.MIN_GRAPH_VERTICES
            ) + Constants.MIN_GRAPH_VERTICES;
            int graphEdges = RANDOM.nextInt(
                    (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
            ) + (graphVertices - 1);
            Graph g = factory.createRandomConnectedGraph(
                    graphVertices, graphEdges
            );
            // speed test
            GraphAlgorithms.maxCliqueSize(g);

            verticesAvg += graphVertices;
            edgesAvg += graphEdges;
        }
        verticesAvg /= NUMBER_OF_ITERATIONS;
        edgesAvg /= NUMBER_OF_ITERATIONS;
        System.out.println("Average number of vertices: " + verticesAvg);
        System.out.println("Average number of edges: " + edgesAvg);
    }

}
