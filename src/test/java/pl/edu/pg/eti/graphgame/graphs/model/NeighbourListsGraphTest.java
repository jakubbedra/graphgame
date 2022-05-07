package pl.edu.pg.eti.graphgame.graphs.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NeighbourListsGraphTest {

    private Graph graph;

    @BeforeEach
    public void setup() {
        graph = new NeighbourListsGraph(6);
        graph.addVertex();
        graph.addVertex();
        graph.addVertex();
        graph.addVertex();
        graph.addVertex();
        graph.addEdge(0, 1);
        graph.addEdge(2, 1);
        graph.addEdge(0, 4);
        graph.addEdge(4, 1);
    }

    @Test
    public void addVertexTest() {
        graph.addVertex();

        Assertions.assertEquals(6, graph.getN());
    }

    @Test
    public void removeVertexTest() {
        graph.removeVertex(2);

        Assertions.assertEquals(4, graph.getN());
        Assertions.assertEquals(3, graph.getM());
        Assertions.assertTrue(graph.edgeExists(0, 3));
    }

    @Test
    public void neighboursTest() {
        List<Integer> neighbours = graph.neighbours(1);
        List<Integer> expected = List.of(0, 2, 4);

        Assertions.assertTrue(
                neighbours.size() == expected.size() &&
                        neighbours.containsAll(expected) &&
                        expected.containsAll(neighbours)
        );
    }

    @Test
    public void addEdgeTest() {
        graph.addEdge(3, 1);

        Assertions.assertTrue(graph.edgeExists(3, 1));
        Assertions.assertEquals(4, graph.degree(1));
        Assertions.assertEquals(1, graph.degree(3));
    }

    @Test
    public void removeEdgeTest() {
        graph.removeEdge(1, 4);

        Assertions.assertFalse(graph.edgeExists(1, 4));
        Assertions.assertEquals(2, graph.degree(1));
        Assertions.assertEquals(1, graph.degree(4));
    }

    @Test
    public void deltaTest() {
        int delta = graph.delta();

        Assertions.assertEquals(3, delta);
    }

    @Test
    public void degreeTest() {
        int degree = graph.degree(1);

        Assertions.assertEquals(3, degree);
    }

    @Test
    public void copyConstructorTestDifferentGraphRepresentation() {
        final int TEST_MATRIX[][] = {
                {0, 1, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 1, 0, 0, 0}
        };
        Graph matrix = new AdjacencyMatrixGraph(TEST_MATRIX, 5, 4);
        Graph list = new NeighbourListsGraph(matrix);

        Assertions.assertTrue(graph.isSame(list));
    }

}
