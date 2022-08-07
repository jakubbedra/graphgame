package pl.edu.pg.eti.graphgame.graphs.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeightedAdjacencyMatrixGraph extends AdjacencyMatrixGraph implements WeightedGraph {

    public WeightedAdjacencyMatrixGraph() {
        super();
    }

    public WeightedAdjacencyMatrixGraph(int n) {
        super(n);
    }

    public WeightedAdjacencyMatrixGraph(Graph g) {
        super(g);
    }

    public WeightedAdjacencyMatrixGraph(int matrix[][], int n, int m) {
        super(matrix, n, m);
    }

    @Override
    public void addEdge(int v1, int v2, int weight) {
        if (edgeExists(v1, v2)) {
            return;
        }
        matrix[v1][v2] = weight;
        matrix[v2][v1] = weight;
    }

    @Override
    public int getEdgeWeight(int v1, int v2) {
        return matrix[v1][v2];
    }

    @Override
    public void setEdgeWeight(int v1, int v2, int weight) {
        if (edgeExists(v1, v2)) {
            this.matrix[v1][v2] = weight;
            this.matrix[v2][v1] = weight;
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

}
