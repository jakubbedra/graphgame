package pl.edu.pg.eti.graphgame.config;

/*
todo: create a config file instead...
 */
public class Constants {

    public static final int MIN_GRAPH_VERTICES = 3;
    public static final int MAX_GRAPH_VERTICES = 14;

    public static final int MIN_COMPLETE_GRAPH_VERTICES = 2;
    public static final int MAX_COMPLETE_GRAPH_VERTICES = 7;

    public static final int MIN_MAX_CLIQUE_VERTICES = 5;
    public static final int MAX_MAX_CLIQUE_VERTICES = 10;

    //currently, no higher than Q_4 is allowed
    public static final int[] HYPERCUBE_VERTICES = {2, 4, 8, 16};

    public static final int MIN_REGULAR_GRAPH_K = 2;
    public static final int MAX_REGULAR_GRAPH_K = 5;

    public static final int MIN_EULERIAN_VERTICES = 4;
    public static final int MAX_EULERIAN_VERTICES = 10;

    public static final int MAX_TSP_VERTICES = 6;
    public static final int MIN_TSP_VERTICES = 3;

    public static final int MAX_EDGE_WEIGHT = 20;

    public static final double PROBABILITY_GRAPH_MIGHT_NOT_BE_EULERIAN = 0.7;
    public static final double PROBABILITY_GRAPH_MIGHT_BE_TREE = 0.5;
    public static final double PROBABILITY_GRAPH_MIGHT_NOT_BE_BIPARTITE = 0.9;

}
