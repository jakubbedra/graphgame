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
    COMPLETE_GRAPHS("Complete Graphs");

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
