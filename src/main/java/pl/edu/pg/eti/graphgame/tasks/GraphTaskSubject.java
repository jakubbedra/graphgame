package pl.edu.pg.eti.graphgame.tasks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Enum containing the subjects of tasks
 */
public enum GraphTaskSubject {

    BFS("Breadth-First Search"),
    DFS("Depth-First Search"),
    COMPLETE_GRAPHS("Complete Graphs"),
    PATH_GRAPHS("Path Graphs"),
    CYCLE_GRAPHS("Cycle Graphs"),
    STAR_GRAPHS("Star Graphs"),
    WHEEL_GRAPHS("Wheel Graphs"),
    HYPERCUBES("Hypercubes"),
    REGULAR_GRAPHS("Regular Graphs"),
    MAX_CLIQUE("Max Clique Problem"),
    MAX_INDEPENDENT_SET("Max Independent Set Problem"),
    MIN_VERTEX_COVER("Min Vertex Cover"),
    EULER_CYCLE("Eulerian Graphs"),
    MIN_SPANNING_TREE("Min Spanning Tree"),
    TREE_GRAPHS("Tree Graphs"),
    HAMILTON_CYCLE("Hamiltonian Graphs"),
    BIPARTITE_GRAPHS("Bipartite Graphs"),
    TRAVELING_SALESMAN_PROBLEM("Traveling Salesman Problem");//draw K_r,s + "Is the given graph bipartite?"
    //PLANARITY(""),
    //ISOMORPHISM(""),
    //OTHER_GRAPHS("Special Graphs"),//todo: extend the dto for that, I guess
    //EMPTY_GRAPHS("Empty Graphs"), //trivial, should have a lower probability of choosing it;
    //BIPARTITE_GRAPHS("Bipartite Graphs"),
    //TRIPARTITE_GRAPHS("")
    //VERTEX_COLORING("Vertex Coloring"),
    //EDGE_COLORING("Edge Coloring"),

    public final String label;

    private static final List<GraphTaskSubject> VALUES = Collections.unmodifiableList(
            Arrays.asList(values())
    );

    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    private GraphTaskSubject(String label) {
        this.label = label;
    }

    public static GraphTaskSubject valueOfLabel(String label) {
        for (GraphTaskSubject g : values()) {
            if (g.label.equals(label)) {
                return g;
            }
        }
        return null;
    }

    public static GraphTaskSubject randomSubject() {
        return MIN_SPANNING_TREE;
        //return VALUES.get(RANDOM.nextInt(SIZE));
    }

}

