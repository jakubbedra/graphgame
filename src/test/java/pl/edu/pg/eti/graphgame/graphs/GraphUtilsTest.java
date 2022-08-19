package pl.edu.pg.eti.graphgame.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

public class GraphUtilsTest {

    @Test
    public void testGraphSplit() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 0, 0},
                {1, 0, 1, 0, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 1, 0}
        };
        final int TEST_N = 5;
        final int TEST_M = 4;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Pair<Graph, Graph> graphPair = GraphUtils.splitGraph(testGraph);

        Assertions.assertEquals(3, graphPair.getFirst().getN());
        Assertions.assertEquals(3, graphPair.getFirst().getM());

        Assertions.assertEquals(2, graphPair.getSecond().getN());
        Assertions.assertEquals(1, graphPair.getSecond().getM());
    }

}
