package pl.edu.pg.eti.graphgame.graphs.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NeighbourListsGraph implements Graph {

    /**
     * Array list of linked lists, representing vertices and their neighbours
     */
    private final List<List<Integer>> neighbourLists;

    private int m;

    public NeighbourListsGraph() {
        neighbourLists = new ArrayList<>();
        m = 0;
    }

    public NeighbourListsGraph(int n) {
        neighbourLists = new ArrayList<>(n);
        m = 0;
    }

    public NeighbourListsGraph(Graph g) {
        this.m = g.getM();
        neighbourLists = new ArrayList<>(g.getN());
        for (int i = 0; i < g.getN(); i++) {
            neighbourLists.add(g.neighbours(i));
        }
    }

    @Override
    public int getN() {
        return neighbourLists.size();
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public void addVertex() {
        neighbourLists.add(new LinkedList<>());
    }

    @Override
    public void removeVertex(int v) {
        List<Integer> neighbours = neighbourLists.get(v);
        for (Integer neighbour : neighbours) {
            neighbourLists.get(neighbour).remove((Integer) v);
        }
        neighbourLists.remove(v);
        for (List<Integer> n : neighbourLists) {
            for (int i = 0; i < n.size(); i++) {
                int tmp = n.get(i);
                if (tmp > v) {
                    tmp--;
                    n.set(i, tmp);
                }
            }
        }
        m -= neighbours.size();
    }

    @Override
    public int degree(int v) {
        return neighbourLists.get(v).size();
    }

    @Override
    public void addEdge(int v1, int v2) {
        neighbourLists.get(v1).add(v2);
        neighbourLists.get(v2).add(v1);
        m++;
    }

    @Override
    public void removeEdge(int v1, int v2) {
        neighbourLists.get(v1).remove((Integer) v2);
        neighbourLists.get(v2).remove((Integer) v1);
        m--;
    }

    @Override
    public boolean edgeExists(int v1, int v2) {
        for (int i : neighbourLists.get(v1)) {
            if (i == v2) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int delta() {
        int delta = 0;
        for (List<Integer> neighbours : neighbourLists) {
            if (neighbours.size() > delta) {
                delta = neighbours.size();
            }
        }
        return delta;
    }

    @Override
    public List<Integer> neighbours(int v) {
        return neighbourLists.get(v);
    }

    //For testing purposes
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (List<Integer> neighbours : neighbourLists) {
            sb.append("[").append(i).append("] ");
            for (int v : neighbours) {
                sb.append(v).append(", ");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

}
