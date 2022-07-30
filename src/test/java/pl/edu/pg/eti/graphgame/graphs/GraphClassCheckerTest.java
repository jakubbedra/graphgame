package pl.edu.pg.eti.graphgame.graphs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

public class GraphClassCheckerTest {

    @Test
    public void isCompleteTestCompleteGraph() {
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
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
        Assertions.assertThat(GraphClassChecker.isComplete(testGraph)).isTrue();
    }

    @Test
    public void isCompleteTestNoCompleteGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1, 1},
                {0, 1, 0, 1, 1, 0},
                {1, 1, 1, 0, 1, 1},
                {0, 1, 1, 1, 0, 1},
                {1, 1, 0, 1, 1, 0}
        };
        final int TEST_N = 6;
        final int TEST_M = 15;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
        Assertions.assertThat(GraphClassChecker.isComplete(testGraph)).isFalse();
    }

    @Test
    public void isConnectedTestConnectedGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 0},
                {1, 0, 1, 1},
                {1, 1, 0, 0},
                {0, 1, 0, 0}
        };
        final int TEST_N = 4;
        final int TEST_M = 4;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
        Assertions.assertThat(GraphClassChecker.isConnected(testGraph)).isTrue();
    }

    @Test
    public void isConnectedTestDisconnectedGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 0}
        };
        final int TEST_N = 4;
        final int TEST_M = 3;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
        Assertions.assertThat(GraphClassChecker.isConnected(testGraph)).isFalse();
    }

    @Test
    public void isPathTestPathGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}
        };
        final int TEST_N = 3;
        final int TEST_M = 2;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Assertions.assertThat(GraphClassChecker.isPath(testGraph)).isTrue();
    }

    @Test
    public void isPathTestTwoVerticesPathGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1},
                {1, 0}
        };
        final int TEST_N = 2;
        final int TEST_M = 1;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Assertions.assertThat(GraphClassChecker.isPath(testGraph)).isTrue();
    }

    @Test
    public void isPathTestNoPathGraph() {
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

        Assertions.assertThat(GraphClassChecker.isPath(testGraph)).isFalse();
    }

    @Test
    public void isCycleTestCycleGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };
        final int TEST_N = 4;
        final int TEST_M = 4;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Assertions.assertThat(GraphClassChecker.isCycle(testGraph)).isTrue();
    }

    @Test
    public void isCycleTestNoCycleGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 1},
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {1, 0, 1, 0}
        };
        final int TEST_N = 4;
        final int TEST_M = 5;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Assertions.assertThat(GraphClassChecker.isCycle(testGraph)).isFalse();
    }

    @Test
    public void isStarTestStarGraph() {
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

        Assertions.assertThat(GraphClassChecker.isStar(testGraph)).isTrue();
    }

    @Test
    public void isStarTestNoStarGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };
        final int TEST_N = 4;
        final int TEST_M = 4;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Assertions.assertThat(GraphClassChecker.isStar(testGraph)).isFalse();
    }

    @Test
    public void isWheelTestWheelGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 1, 1},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 0}
        };
        final int TEST_N = 5;
        final int TEST_M = 8;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Assertions.assertThat(GraphClassChecker.isWheel(testGraph)).isTrue();
    }

    @Test
    public void isWheelTestNoWheelGraph() {
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 1, 1},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 1, 1},
                {1, 0, 1, 0, 0},
                {1, 1, 1, 0, 0}
        };
        final int TEST_N = 5;
        final int TEST_M = 7;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );

        Assertions.assertThat(GraphClassChecker.isWheel(testGraph)).isFalse();
    }

    @Test
    public void isHypercubeTestHypercubes() {
        // n = 1
        final int[][] TEST1 = {{0}};
        Graph testGraph1 = new AdjacencyMatrixGraph(
                TEST1, 1, 0
        );
        Assertions.assertThat(GraphClassChecker.isHypercube(testGraph1)).isTrue();

        // n = 2
        final int[][] TEST2 = {
                {0, 1},
                {1, 0}
        };
        Graph testGraph2 = new AdjacencyMatrixGraph(
                TEST2, 2, 1
        );
        Assertions.assertThat(GraphClassChecker.isHypercube(testGraph2)).isTrue();

        // n = 3
        final int[][] TEST3 = {
                {0, 1, 1, 0},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {0, 1, 1, 0}
        };
        Graph testGraph3 = new AdjacencyMatrixGraph(
                TEST3, 4, 4
        );
        Assertions.assertThat(GraphClassChecker.isHypercube(testGraph3)).isTrue();

        //n = 4
        final int[][] TEST4 = {
                {0, 1, 1, 0, 1, 0, 0, 0},
                {1, 0, 0, 1, 0, 1, 0, 0},
                {1, 0, 0, 1, 0, 0, 1, 0},
                {0, 1, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 1, 0},
                {0, 1, 0, 0, 1, 0, 0, 1},
                {0, 0, 1, 0, 1, 0, 0, 1},
                {0, 0, 0, 1, 0, 1, 1, 0},
        };
        Graph testGraph4 = new AdjacencyMatrixGraph(
                TEST4, 8, 12
        );
        Assertions.assertThat(GraphClassChecker.isHypercube(testGraph3)).isTrue();
    }

    @Test
    public void isHypercubeTestNoHypercube() {
        final int[][] TEST = {
                {0, 1, 1, 0},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {0, 1, 1, 0}
        };
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST, 4, 5
        );
        Assertions.assertThat(GraphClassChecker.isHypercube(testGraph)).isFalse();
    }

    @Test
    public void isKRegularTestKRegularGraph() {
        // k = 3
        final int[][] TEST = {
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
        };
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST, 4, 6
        );
        Assertions.assertThat(GraphClassChecker.isKRegular(testGraph, 3)).isTrue();

        // k = 2
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };
        Graph testGraph2 = new AdjacencyMatrixGraph(
                TEST, 4, 4
        );
        Assertions.assertThat(GraphClassChecker.isKRegular(testGraph2, 2)).isTrue();
    }

    @Test
    public void isKRegularTestNoKRegularGraph() {
        final int[][] TEST = {
                {0, 1, 0, 1, 1},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 0}
        };
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST, 5, 8
        );
        Assertions.assertThat(GraphClassChecker.isKRegular(testGraph, 4)).isFalse();
    }

    @Test
    public void isKRegularTestWrongKValue() {
        final int[][] TEST = {
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
        };
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST, 4, 6
        );
        Assertions.assertThat(GraphClassChecker.isKRegular(testGraph, 4)).isFalse();
    }

}

