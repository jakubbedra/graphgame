package pl.edu.pg.eti.graphgame.graphs.model;

import pl.edu.pg.eti.graphgame.exceptions.NegativeVertexCountException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AdjacencyMatrixGraph implements Graph {

    protected int matrix[][];

    protected int n;
    protected int m;

    public AdjacencyMatrixGraph() {
        this.matrix = new int[0][0];
        this.n = 0;
        this.m = 0;
    }

    public AdjacencyMatrixGraph(int n) {
        this.matrix = new int[n][n];
        this.n = n;
        this.m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 0; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public AdjacencyMatrixGraph(int matrix[][], int n, int m) {
        this.matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, n);
        }
        this.n = n;
        this.m = m;
    }

    public AdjacencyMatrixGraph(Graph g) {
        this.n = g.getN();
        this.m = g.getM();
        this.matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 0;
            }
        }
        for (int i = 0; i < n; i++) {
            List<Integer> neighbours = g.neighbours(i);
            for (Integer v : neighbours) {
                this.matrix[i][v] = 1;
                this.matrix[v][i] = 1;
            }
        }
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public void addVertex() {
        if (this.matrix.length < n + 1) {
            expandMatrix(n + 1);
        }
        n++;
    }

    @Override
    public void removeVertex(int v) {
        m -= degree(v);
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                matrix[i][v] = 0;
                matrix[v][i] = 0;
            }
            for (int i = v + 1; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i - 1][j] = matrix[i][j];
                    matrix[j][i - 1] = matrix[j][i];
                }
            }
            n--;
        } else {
            throw new NegativeVertexCountException("Current vertex count is 0.");
        }
    }

    @Override
    public int degree(int v) {
        return Arrays.stream(matrix[v]).reduce(0, (subtotal, edge) -> subtotal += edge > 0 ? 1 : 0);
    }

    @Override
    public void addEdge(int v1, int v2) {
        if (edgeExists(v1, v2)) {
            return;
        }
        matrix[v1][v2] = 1;
        matrix[v2][v1] = 1;
        m++;
    }

    @Override
    public void removeEdge(int v1, int v2) {
        matrix[v1][v2] = 0;
        matrix[v2][v1] = 0;
        m--;
    }

    @Override
    public boolean edgeExists(int v1, int v2) {
        return matrix[v1][v2] != 0 && v1 < n && v2 < n;
    }

    @Override
    public int delta() {
        int delta = 0;
        for (int i = 0; i < n; i++) {
            int tmp = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] > 0) {
                    tmp++;
                }
            }
            if (tmp > delta) {
                delta = tmp;
            }
        }
        return delta;
    }

    @Override
    public List<Integer> neighbours(int v) {
        List<Integer> neighbours = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (matrix[v][i] > 0) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    /**
     * Method used to expand the array and fill the newly added columns and rows with the value of 0.
     *
     * @param n2 The new size of the matrix.
     */
    private void expandMatrix(int n2) {
        int oldMatrix[][] = this.matrix;
        matrix = new int[n2][n2];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = oldMatrix[i][j];
            }
        }
        for (int i = n; i < n2; i++) {
            for (int j = n; j < n2; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    //For testing purposes
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(matrix[i][j]).append(j != n - 1 ? ", " : "\n");
            }
        }
        return sb.toString();
    }

}
