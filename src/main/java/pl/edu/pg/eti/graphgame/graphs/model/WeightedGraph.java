package pl.edu.pg.eti.graphgame.graphs.model;

public interface WeightedGraph extends Graph {

    /**
     * Returns the weight of the edge {v1; v2}.
     */
    float getEdgeWeight(int v1, int v2);

    /**
     * Sets the weight of the edge {v1; v2} to a given value.
     */
    void setEdgeWeight(int v1, int v2, float weight);

}
