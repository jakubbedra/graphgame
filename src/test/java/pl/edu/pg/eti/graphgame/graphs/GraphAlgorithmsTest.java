package pl.edu.pg.eti.graphgame.graphs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.List;

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
//todo: check if its ok xdddd
        //breadthFirstSearch.forEach(System.out::println);
        Assertions.assertThat(expected).isEqualTo(breadthFirstSearch);
    }

}
