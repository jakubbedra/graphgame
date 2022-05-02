package pl.edu.pg.eti.graphgame.tasks;

/**
 * Enum containing the subjects of tasks
 */
public enum GraphTaskSubject {

    BFS("Breadth-First Search "),
    //   EULERIAN_GRAPHS,
    //   HAMILTONIAN_GRAPHS,
    //   BIPARTITE_GRAPHS,
    COMPLETE_GRAPHS("Complete Graphs");

    public final String label;

    private GraphTaskSubject(String label) {
        this.label = label;
    }

}
