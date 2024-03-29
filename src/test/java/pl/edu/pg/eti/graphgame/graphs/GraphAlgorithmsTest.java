package pl.edu.pg.eti.graphgame.graphs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.config.Constants;
import pl.edu.pg.eti.graphgame.graphs.factory.GraphFactory;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedAdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedGraph;

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
        final int NUMBER_OF_ITERATIONS = 1000;
        GraphFactory factory = new GraphFactory();
        int verticesAvg = 0;
        int edgesAvg = 0;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            int graphVertices = RANDOM.nextInt(
                    Constants.MAX_MAX_CLIQUE_VERTICES - Constants.MIN_MAX_CLIQUE_VERTICES
            ) + Constants.MIN_MAX_CLIQUE_VERTICES;
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

    @Test
    public void maxIndependentSetTest() {
        final int[][] TEST = {
                {0, 0, 1, 1, 0},
                {0, 0, 1, 0, 1},
                {1, 1, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
        };
        final int N = 5;
        final int M = 4;
        Graph g = new AdjacencyMatrixGraph(TEST, N, M);

        int expectedMaxIndependentSet = 3;

        Assertions.assertThat(GraphAlgorithms.maxIndependentSetSize(g)).isEqualTo(expectedMaxIndependentSet);
    }

    @Test
    public void minVertexCoverTest() {
        final int[][] TEST_MATRIX = {
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0}
        };
        final int TEST_N = 9;
        final int TEST_M = 8;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        int expectedMinVertexCoverSize = 3;

        Assertions.assertThat(GraphAlgorithms.minVertexCoverSize(testGraph)).isEqualTo(expectedMinVertexCoverSize);
    }

    @Test
    public void minVertexCoverStarTest() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 0},
                {1, 0, 1, 1},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        };
        final int TEST_N = 4;
        final int TEST_M = 3;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        int expectedMinVertexCoverSize = 1;

        Assertions.assertThat(GraphAlgorithms.minVertexCoverSize(testGraph)).isEqualTo(expectedMinVertexCoverSize);
    }

    @Test
    public void minVertexCoverCompleteGraphTest() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0}
        };
        final int TEST_N = 8;
        final int TEST_M = 28;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        int expectedMinVertexCoverSize = 7;

        Assertions.assertThat(GraphAlgorithms.minVertexCoverSize(testGraph)).isEqualTo(expectedMinVertexCoverSize);
    }

    @Test
    public void checkEulerCycleTest() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 1, 1, 0},
                {1, 0, 1, 0, 0, 0},
                {1, 1, 0, 1, 1, 0},
                {1, 0, 1, 0, 1, 1},
                {1, 0, 1, 1, 0, 1},
                {0, 0, 0, 1, 1, 0}
        };
        final int TEST_N = 6;
        final int TEST_M = 10;

        Graph graph = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        List<Integer> cycle = List.of(0, 1, 2, 0, 3, 2, 4, 5, 3, 4, 0);

        Assertions.assertThat(GraphAlgorithms.checkEulerCycle(graph, cycle)).isTrue();
    }

    @Test
    public void minSpanningTreeTest() {
        final int[][] TEST_MATRIX = {
                {0, 3, 0, 0, 1},
                {3, 0, 5, 0, 4},
                {0, 5, 0, 2, 6},
                {0, 0, 2, 0, 7},
                {1, 4, 6, 7, 0}
        };
        final int TEST_N = 5;
        final int TEST_M = 7;
        final int EXPECTED_WEIGHT = 11;

        WeightedGraph graph = new WeightedAdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.getMinSpanningTreeTotalWeight(graph)).isEqualTo(EXPECTED_WEIGHT);
    }

    @Test
    public void travelingSalesmanProblemTest() {
        final int[][] TEST_MATRIX = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        final int TEST_N = 4;
        final int TEST_M = 7;
        final int EXPECTED_WEIGHT = 80;

        WeightedGraph graph = new WeightedAdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.solveTSP(graph)).isEqualTo(EXPECTED_WEIGHT);
    }

    @Test
    public void isomorphismTestIsomorphicGraphs() {
        final int[][] TEST_MATRIX1 = {
                {0, 1, 0, 0},
                {1, 0, 1, 1},
                {0, 1, 0, 1},
                {0, 1, 1, 0}
        };
        final int TEST_N1 = 4;
        final int TEST_M1 = 4;

        final int[][] TEST_MATRIX2 = {
                {0, 1, 1, 1},
                {1, 0, 1, 0},
                {1, 1, 0, 0},
                {1, 0, 0, 0}
        };
        final int TEST_N2 = 4;
        final int TEST_M2 = 4;

        Graph graph1 = new AdjacencyMatrixGraph(TEST_MATRIX1, TEST_N1, TEST_M1);
        Graph graph2 = new AdjacencyMatrixGraph(TEST_MATRIX2, TEST_N2, TEST_M2);

        Assertions.assertThat(GraphAlgorithms.areGraphsIsomorphic(graph1, graph2)).isTrue();
    }

    @Test
    public void isomorphismTestNotIsomorphicGraphs() {
        final int[][] TEST_MATRIX1 = {
                {0, 1, 1, 1, 0, 0},
                {1, 0, 1, 1, 0, 0},
                {1, 1, 0, 1, 1, 0},
                {1, 1, 1, 0, 0, 0},
                {0, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 1, 0}
        };
        final int TEST_N1 = 6;
        final int TEST_M1 = 8;

        final int[][] TEST_MATRIX2 = {
                {0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 1},
                {0, 0, 1, 0, 1, 1},
                {1, 0, 1, 1, 0, 1},
                {0, 1, 1, 1, 1, 0}
        };
        final int TEST_N2 = 6;
        final int TEST_M2 = 8;

        Graph graph1 = new AdjacencyMatrixGraph(TEST_MATRIX1, TEST_N1, TEST_M1);
        Graph graph2 = new AdjacencyMatrixGraph(TEST_MATRIX2, TEST_N2, TEST_M2);

        Assertions.assertThat(GraphAlgorithms.areGraphsIsomorphic(graph1, graph2)).isFalse();
    }

    @Test
    public void homeomorphismTestHomeomorphicGraphs() {
        final int[][] TEST_MATRIX1 = {
                {0, 0, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0, 0, 0},
                {1, 0, 1, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1},
                {0, 1, 0, 1, 0, 0, 0},
                {1, 0, 0, 0, 1, 0, 0}
        };
        final int TEST_N1 = 7;
        final int TEST_M1 = 6;

        final int[][] TEST_MATRIX2 = {
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1},
                {1, 1, 0, 0, 1},
                {0, 0, 1, 1, 0}
        };
        final int TEST_N2 = 5;
        final int TEST_M2 = 4;

        Graph graph1 = new AdjacencyMatrixGraph(TEST_MATRIX1, TEST_N1, TEST_M1);
        Graph graph2 = new AdjacencyMatrixGraph(TEST_MATRIX2, TEST_N2, TEST_M2);

        Assertions.assertThat(GraphAlgorithms.areGraphsHomeomorphic(graph1, graph2)).isTrue();
    }

    @Test
    public void distanceTest() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 0, 1, 1, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 1, 1},
                {0, 0, 1, 0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 1, 1, 0, 0}
        };
        final int TEST_N = 10;
        final int TEST_M = 15;

        Graph graph = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.distance(graph, 0, 2)).isEqualTo(2);
    }

    @Test
    public void eccentricityTest() {
        final int[][] TEST_MATRIX = {
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {1, 1, 0, 1, 1, 1, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0},
        };
        final int TEST_N = 7;
        final int TEST_M = 6;

        Graph graph = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.eccentricity(graph, 1)).isEqualTo(3);
        Assertions.assertThat(GraphAlgorithms.eccentricity(graph, 2)).isEqualTo(2);
    }

    @Test
    public void radiusTest() {
        final int[][] TEST_MATRIX = {
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {1, 1, 0, 1, 1, 1, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0},
        };
        final int TEST_N = 7;
        final int TEST_M = 6;

        Graph graph = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.radius(graph)).isEqualTo(2);
    }

    @Test
    public void diameterTest() {
        final int[][] TEST_MATRIX = {
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {1, 1, 0, 1, 1, 1, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0},
        };
        final int TEST_N = 7;
        final int TEST_M = 6;

        Graph graph = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.diameter(graph)).isEqualTo(3);
    }

    @Test
    public void testChinesePostmanProblem() {
        final int[][] TEST_MATRIX = {
                {0, 3, 1, 0, 5, 0},
                {3, 0, 0, 1, 0, 6},
                {1, 0, 0, 0, 2, 0},
                {0, 1, 0, 0, 0, 1},
                {5, 0, 2, 0, 0, 4},
                {0, 6, 0, 1, 4, 0}
        };
        final int TEST_N = 6;
        final int TEST_M = 8;

        WeightedGraph graph = new WeightedAdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.solveCPP(graph)).isEqualTo(28);
    }

    @Test
    public void testCalculateChromaticNumberCompleteGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 0, 1},
                {1, 1, 1, 1, 1, 0}
        };
        final int TEST_N = 6;
        final int TEST_M = 15;

        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.calculateChromaticNumber(g)).isEqualTo(TEST_N);
    }

    @Test
    public void testCalculateChromaticNumberPlanarGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 1, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 0, 1, 1},
                {1, 1, 1, 0, 0},
                {1, 1, 1, 0, 0}
        };
        final int TEST_N = 5;
        final int TEST_M = 9;

        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.calculateChromaticNumber(g)).isEqualTo(4);
    }

    @Test
    public void testCalculateChromaticIndexStarGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 0, 0, 0},
                {1, 0, 1, 1, 1, 1},
                {0, 1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0}
        };
        final int TEST_N = 6;
        final int TEST_M = 5;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_N, TEST_M);

        Assertions.assertThat(GraphAlgorithms.calculateChromaticIndex(g)).isEqualTo(5);
    }

    //@Test
    public void edgeColoringSpeedTest(){
        final Random RANDOM = new Random();
        final int NUMBER_OF_ITERATIONS = 1000;
        GraphFactory factory = new GraphFactory();
        int verticesAvg = 0;
        int edgesAvg = 0;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            int graphVertices = RANDOM.nextInt(
                    Constants.MAX_VERTEX_COLORING_VERTICES - Constants.MIN_VERTEX_COLORING_VERTICES
            ) + Constants.MIN_VERTEX_COLORING_VERTICES;
            int graphEdges = RANDOM.nextInt(
                    Constants.MAX_EDGE_COLORING_EDGES - (graphVertices - 1)
            ) + (graphVertices - 1);
            Graph g = factory.createRandomConnectedGraph(
                    graphVertices, graphEdges
            );
            // speed test
            GraphAlgorithms.calculateChromaticIndex(g);

            verticesAvg += graphVertices;
            edgesAvg += graphEdges;
        }
        verticesAvg /= NUMBER_OF_ITERATIONS;
        edgesAvg /= NUMBER_OF_ITERATIONS;
        System.out.println("Average number of vertices: " + verticesAvg);
        System.out.println("Average number of edges: " + edgesAvg);
    }

}
