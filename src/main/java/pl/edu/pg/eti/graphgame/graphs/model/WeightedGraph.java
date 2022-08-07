package pl.edu.pg.eti.graphgame.graphs.model;

public interface WeightedGraph extends Graph {

    /**
     * Adds an edge {v1; v2} with the specified weight.
     */
    void addEdge(int v1, int v2, int weight);

    /**
     * Returns the weight of the edge {v1; v2}.
     */
    int getEdgeWeight(int v1, int v2);

    /**
     * Sets the weight of the edge {v1; v2} to a given value.
     */
    void setEdgeWeight(int v1, int v2, int weight);

}
