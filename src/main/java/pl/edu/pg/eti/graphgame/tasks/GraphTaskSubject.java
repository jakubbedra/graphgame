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
    MAX_INDEPENDENT_SET("Max Independent Set Problem"),////////////////
    MIN_VERTEX_COVER("Min Vertex Cover"),
    EULER_CYCLE("Eulerian Graphs"),
    MIN_SPANNING_TREE("Min Spanning Tree"),
    TREE_GRAPHS("Tree Graphs"),
    HAMILTON_CYCLE("Hamiltonian Graphs"),
    BIPARTITE_GRAPHS("Bipartite Graphs"),
    TRAVELING_SALESMAN_PROBLEM("Traveling Salesman Problem"),
    PLANAR_GRAPHS("Planar Graphs"),
    ISOMORPHISM("Isomorphism"),
    HOMEOMORPHISM("Homeomorphism"),
    NAMED_GRAPHS("Named Graphs"),
    TRIVIAL_QUESTIONS("Trivial Questions"),
    DISTANCES("Distances"),
    CHINESE_POSTMAN_PROBLEM("Chinese Postman Problem"),
    VERTEX_COLORING("Vertex Coloring"),
    EDGE_COLORING("Edge Coloring");

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
        GraphTaskSubject[] castratedSubjectList = {DFS, BFS,COMPLETE_GRAPHS, PATH_GRAPHS, STAR_GRAPHS, CYCLE_GRAPHS,
        WHEEL_GRAPHS, HYPERCUBES, REGULAR_GRAPHS, MAX_CLIQUE, MAX_INDEPENDENT_SET};
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}

