package pl.edu.pg.eti.graphgame.graphs.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class NeighbourListsGraph implements Graph {

    /**
     * Array list of linked lists, representing vertices and their neighbours
     */
    protected List<List<Integer>> neighbourLists;

    protected int n;
    protected int m;

    public NeighbourListsGraph() {
        neighbourLists = new ArrayList<>();
        m = 0;
        n = 0;
    }

    public NeighbourListsGraph(int n) {
        this.n = n;
        neighbourLists = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            neighbourLists.add(new LinkedList<>());
        }
        m = 0;
    }

    public NeighbourListsGraph(Graph g) {
        this.n = g.getN();
        this.m = g.getM();
        neighbourLists = new ArrayList<>(g.getN());
        for (int i = 0; i < g.getN(); i++) {
            neighbourLists.add(new LinkedList<>());
            neighbourLists.get(i).addAll(g.neighbours(i));
        }
    }

    public NeighbourListsGraph(List<List<Integer>> neighbourLists, int n, int m) {
        this.n = n;
        this.m = m;
        this.neighbourLists = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            List<Integer> nList = new LinkedList(neighbourLists.get(i));
            this.neighbourLists.add(nList);
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
        n++;
        List<Integer> neighbours = new LinkedList<>();
        neighbourLists.add(neighbours);
    }

    @Override
    public void removeVertex(int v) {
        //if (v == 1) {
        //    System.out.println("XD");
        //}
        List<Integer> neighbours = neighbourLists.get(v);
        //try {
        for (Integer neighbour : neighbours) {
            neighbourLists.get(neighbour).remove((Integer) v);
        }
        //} catch (Exception exc) {
        //    System.out.println("xd");
        //}
        neighbourLists.remove(v);
        for (List<Integer> n : neighbourLists) {
            for (int i = 0; i < n.size(); i++) {
                int tmp = n.get(i);
                if (tmp > v) {
                    tmp--;
                    n.set(i, tmp);
                } else if (tmp == v) {
                    n.remove((Integer) tmp);
                }
            }
        }
        m -= neighbours.size();
        n--;
        //if (n == 3 && m == 1 && neighbourLists.size() == 3) {
        //    System.out.println("CHUJ");
        //}
    }

    @Override
    public int degree(int v) {
        return neighbourLists.get(v).size();
    }

    @Override
    public void addEdge(int v1, int v2) {
        if (!edgeExists(v1, v2)) {
            neighbourLists.get(v1).add(v2);
            neighbourLists.get(v2).add(v1);
            m++;
        }
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

    @Override
    public void merge(Graph other) {
        if (!(other instanceof NeighbourListsGraph)) {
            throw new UnsupportedOperationException();
        }
        // updating indices
        for (List<Integer> neighbourList : ((NeighbourListsGraph) other).neighbourLists) {
            for (int i = 0; i < neighbourList.size(); i++) {
                neighbourList.set(i, (neighbourList.get(i) + this.n));
            }
        }
        this.neighbourLists.addAll(((NeighbourListsGraph) other).neighbourLists);
        this.n += ((NeighbourListsGraph) other).n;
        this.m += ((NeighbourListsGraph) other).m;
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
