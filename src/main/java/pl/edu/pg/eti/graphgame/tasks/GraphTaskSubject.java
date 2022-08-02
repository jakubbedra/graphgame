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
    MAX_CLIQUE("Max Clique Problem");
    //SPECIAL_GRAPHS("Special Graphs"),//todo: extend the dto for that, I guess
    //EMPTY_GRAPHS("Empty Graphs"), //trivial, should have a lower probability of choosing it;
    //BIPARTITE_GRAPHS("Bipartite Graphs"),
    //HYPERCUBES("Hypercubes"),
    //EULERIAN_GRAPHS("Eulerian Graphs"),//also do some cycle finding algorithms
    //HAMILTONIAN_GRAPHS("Hamiltonian Graphs"),//for this one aswell
    //VERTEX_COLORING("Vertex Coloring"),
    //EDGE_COLORING("Edge Coloring"),
    //VERTEX_COVER("Vertex Cover"),

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
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
