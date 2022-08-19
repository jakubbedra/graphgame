package pl.edu.pg.eti.graphgame.graphs.model;

import java.util.List;

public interface Graph {

    int getN();
    int getM();

    void addVertex();
    void removeVertex(int v);
    int degree(int v);

    void addEdge(int v1, int v2);
    void removeEdge(int v1, int v2);
    boolean edgeExists(int v1, int v2);

    int delta();
    List<Integer> neighbours(int v);
    //List<Integer> commonNeighbours(int v1, int v2);

    void merge(Graph other);

    default boolean isSame(Graph g) {
        if (g.getN() != getN() || g.getM() != getM()) {
            return false;
        }

        for (int i = 0; i < g.getN(); i++) {
            for (int j = 0; j < g.getN(); j++) {
                boolean e1 = g.edgeExists(i, j);
                boolean e2 = edgeExists(i, j);
                if (e1 != e2) {
                    return false;
                }
            }
        }

        return true;
    }

}
