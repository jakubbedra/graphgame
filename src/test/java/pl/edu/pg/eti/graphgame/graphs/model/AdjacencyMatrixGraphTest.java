package pl.edu.pg.eti.graphgame.graphs.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.exceptions.NegativeVertexCountException;

import java.util.List;
import java.util.Optional;

public class AdjacencyMatrixGraphTest {

    private static final int TEST_MATRIX[][] = {
            {0, 1, 0, 0, 0},
            {1, 0, 1, 1, 0},
            {0, 1, 0, 1, 1},
            {0, 1, 1, 0, 1},
            {0, 0, 1, 1, 0}
    };

    private static final int TEST_GRAPH_N = 5;
    private static final int TEST_GRAPH_M = 6;

    @Test
    public void testAddVertexMatrixExpansion() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);

        g.addVertex();

        Assertions.assertThat(g.getN()).isEqualTo(TEST_GRAPH_N + 1);
    }

    @Test
    public void testAddVertexNoMatrixExpansion() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);
        g.addVertex();

        g.removeVertex(4);
        g.addVertex();

        Assertions.assertThat(g.getN()).isEqualTo(TEST_GRAPH_N + 1);
        Assertions.assertThat(g.edgeExists(2, 4)).isNotEqualTo(true);
    }

    @Test
    public void testRemoveVertex() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);

        g.removeVertex(4);

        Assertions.assertThat(g.getN()).isEqualTo(TEST_GRAPH_N - 1);
    }

    @Test
    public void testRemoveVertexNothingToRemove() {
        Graph g = new AdjacencyMatrixGraph(0);

        Optional<NegativeVertexCountException> ex = Optional.empty();
        try {
            g.removeVertex(0);
        } catch (NegativeVertexCountException e) {
            ex = Optional.of(e);
        }

        Assertions.assertThat(ex).isNotEmpty();
    }

    @Test
    public void testAddEdge() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);

        g.addEdge(0, 2);

        Assertions.assertThat(g.getM()).isEqualTo(TEST_GRAPH_M + 1);
        Assertions.assertThat(g.edgeExists(0, 2)).isEqualTo(true);
    }

    @Test
    public void testRemoveEdge() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);

        g.removeEdge(0, 1);

        Assertions.assertThat(g.getM()).isEqualTo(TEST_GRAPH_M - 1);
        Assertions.assertThat(g.edgeExists(0, 1)).isEqualTo(false);
    }

    @Test
    public void testDelta() {
        Graph gg = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);

        int delta = gg.delta();

        Assertions.assertThat(delta).isEqualTo(3);
    }

    @Test
    public void testDegree() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);

        int degree = g.degree(4);

        Assertions.assertThat(degree).isEqualTo(2);
    }

    @Test
    public void testDegreeNoNeighbours() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);
        g.addVertex();

        int degree = g.degree(5);

        Assertions.assertThat(degree).isEqualTo(0);
    }

    @Test
    public void testNeighbours() {
        Graph g = new AdjacencyMatrixGraph(TEST_MATRIX, TEST_GRAPH_N, TEST_GRAPH_M);
        List<Integer> expectedNeighbours = List.of(2, 3);

        List<Integer> neighbours = g.neighbours(4);

        Assertions.assertThat(neighbours.containsAll(expectedNeighbours)).isEqualTo(true);
        Assertions.assertThat(expectedNeighbours.containsAll(neighbours)).isEqualTo(true);
    }

}
