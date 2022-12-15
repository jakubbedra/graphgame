package pl.edu.pg.eti.graphgame.graphs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

public class GraphPlanarityCheckerTest {

    public GraphPlanarityCheckerTest() {
    }

    @Test
    public void testKuratowskiPlanarityTesterK5Minor() {
        final int[][] TEST_MATRIX = {
                {0, 1, 1, 1, 1, 0},
                {1, 0, 1, 1, 1, 0},
                {1, 1, 0, 1, 1, 0},
                {1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 0, 1},
                {0, 0, 0, 1, 1, 0}
        };
        final int TEST_N = 6;
        final int TEST_M = 12;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
        KuratowskiPlanarityTester planarityChecker = new KuratowskiPlanarityTester();
        Assertions.assertThat(planarityChecker.isPlanar(testGraph)).isFalse();
    }

    @Test
    public void testKuratowskiPlanarityTesterPlanarGraph() {
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
        KuratowskiPlanarityTester planarityChecker = new KuratowskiPlanarityTester();
        Assertions.assertThat(planarityChecker.isPlanar(testGraph)).isTrue();
    }

    @Test
    public void testKuratowskiPlanarityTesterK3_3Minor(){
        final int[][] TEST_MATRIX = {
                {0, 1, 0, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 1, 1, 0},
                {0, 0, 1, 1, 0, 1, 1, 1, 0},
                {0, 1, 1, 0, 1, 0, 1, 0, 1},
                {0, 1, 1, 1, 1, 1, 0, 0, 0},
                {1, 0, 0, 1, 1, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 1, 0, 1, 0}
        };
        final int TEST_N = 9;
        final int TEST_M = 19;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
        KuratowskiPlanarityTester planarityChecker = new KuratowskiPlanarityTester();
        Assertions.assertThat(planarityChecker.isPlanar(testGraph)).isFalse();
    }

    @Test
    public void testKuratowskiPlanarityTesterAnotherK3_3Minor(){
        final int[][] TEST_MATRIX = {
                {0, 0, 1, 0, 0, 1, 1},
                {0, 0, 0, 0, 1, 1, 1},
                {1, 0, 0, 1, 1, 0, 0},
                {0, 0, 1, 0, 1, 1, 1},
                {0, 1, 1, 1, 0, 0, 0},
                {1, 1, 0, 1, 0, 0, 1},
                {1, 1, 0, 1, 0, 1, 0},
        };
        final int TEST_N = 7;
        final int TEST_M =12 ;
        Graph testGraph = new AdjacencyMatrixGraph(
                TEST_MATRIX, TEST_N, TEST_M
        );
        KuratowskiPlanarityTester planarityChecker = new KuratowskiPlanarityTester();
        Assertions.assertThat(planarityChecker.isPlanar(testGraph)).isFalse();
    }

}
