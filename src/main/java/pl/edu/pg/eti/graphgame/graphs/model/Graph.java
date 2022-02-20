package pl.edu.pg.eti.graphgame.graphs.model;

import java.util.List;

public interface Graph {

    int getN();
    int getM();

    void addVertex();
    void deleteVertex(int v);
    int degree(int v);

    void addEdge(int v1, int v2);
    void deleteEdge(int v1, int v2);
    boolean edgeExists(int v1, int v2);

    //todo: methods below can be implemented very easily using methods above,
    //so maybe jus remove them?
    int delta();
    List<Integer> neighbours(int v);
    List<Integer> commonNeighbours(int v1, int v2);

}
