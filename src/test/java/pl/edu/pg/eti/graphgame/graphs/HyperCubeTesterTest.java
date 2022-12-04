package pl.edu.pg.eti.graphgame.graphs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

public class HyperCubeTesterTest {

    @Test
    public void isHyperCubeTestHypercubeGraph() {
        int[][] cube = {
                {0, 1, 0, 1, 1, 0, 0, 0},
                {1, 0, 1, 0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 1, 0},
                {1, 0, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {0, 1, 0, 0, 1, 0, 1, 0},
                {0, 0, 1, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 1, 0, 1, 0}
        };
        Graph g = new AdjacencyMatrixGraph(cube, 8, 12);

        HyperCubeTester tester = new HyperCubeTester(g, 3);
        boolean passed = tester.isHyperCubeLargerThan2Dimensions();

        Assertions.assertThat(passed).isTrue();
    }

    @Test
    public void isHyperCubeTestNotHypercubeGraph() {
        int[][] cube = {
                {0, 1, 0, 1, 0, 1, 0, 0},
                {1, 0, 1, 0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 1, 0},
                {1, 0, 1, 0, 0, 0, 0, 1},
                {0, 1, 0, 0, 0, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 1, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 1, 0, 1, 0}
        };
        Graph g = new AdjacencyMatrixGraph(cube, 8, 12);

        HyperCubeTester tester = new HyperCubeTester(g, 3);
        boolean passed = tester.isHyperCubeLargerThan2Dimensions();

        Assertions.assertThat(passed).isFalse();
    }

    @Test
    public void isHyperCubeTestTesseract() {
        int[][] cube = {
                {0, 1, 0, 1, 1, 0, 0, 0,    1, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 1, 0, 0,    0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 1, 0,    0, 0, 1, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0, 0, 1,    0, 0, 0, 1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 1,    0, 0, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 1, 0, 1, 0,    0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 1, 0, 1,    0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 1, 1, 0, 1, 0,    0, 0, 0, 0, 0, 0, 0, 1},

                {1, 0, 0, 0, 0, 0, 0, 0,    0, 1, 0, 1, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0,    1, 0, 1, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0,    0, 1, 0, 1, 0, 0, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 0,    1, 0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 0, 0,    1, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 0,    0, 1, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0,    0, 0, 1, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1,    0, 0, 0, 1, 1, 0, 1, 0}
        };
        Graph g = new AdjacencyMatrixGraph(cube, 16, 32);

        HyperCubeTester tester = new HyperCubeTester(g, 4);
        boolean passed = tester.isHyperCubeLargerThan2Dimensions();

        Assertions.assertThat(passed).isTrue();
    }
    @Test
    public void isHyperCubeTestInvalidTesseract() {
        int[][] cube = {
                {0, 1, 0, 1, 1, 0, 0, 0,    1, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 1, 0, 0,    0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 1, 0,    0, 0, 1, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0, 0, 1,    0, 0, 0, 1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 1,    0, 0, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 1, 0, 1, 0,    0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 1, 0, 1,    0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 1, 0,    0, 0, 0, 0, 0, 0, 1, 0},

                {1, 0, 0, 0, 0, 0, 0, 0,    0, 1, 0, 1, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0,    1, 0, 1, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0,    0, 1, 0, 1, 0, 0, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 0,    1, 0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 0, 0, 0,    1, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 0,    0, 1, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1,    0, 0, 1, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 1, 0,    0, 0, 0, 1, 1, 0, 1, 0}
        };
        Graph g = new AdjacencyMatrixGraph(cube, 16, 32);

        HyperCubeTester tester = new HyperCubeTester(g, 4);
        boolean passed = tester.isHyperCubeLargerThan2Dimensions();

        Assertions.assertThat(passed).isFalse();
    }

}
