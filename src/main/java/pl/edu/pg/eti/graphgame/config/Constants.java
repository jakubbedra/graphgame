package pl.edu.pg.eti.graphgame.config;

public class Constants {

    public static final int MIN_GRAPH_VERTICES = 3;
    public static final int MAX_GRAPH_VERTICES = 14;

    public static final int MIN_COMPLETE_GRAPH_VERTICES = 2;
    public static final int MAX_COMPLETE_GRAPH_VERTICES = 7;

    public static final int MIN_MAX_CLIQUE_VERTICES = 5;
    public static final int MAX_MAX_CLIQUE_VERTICES = 10;

    public static final int MIN_ISOMORPHISM_VERTICES = 3;
    public static final int MAX_ISOMORPHISM_VERTICES = 7;

    public static final int MIN_VERTEX_COLORING_VERTICES = 4;
    public static final int MAX_VERTEX_COLORING_VERTICES = 8;

    public static final int MAX_EDGE_COLORING_EDGES = 11;

    public static final int MAX_EXTENDED_EDGES_HOMEOMORPHISM = 4;

    //currently, no higher than Q_4 is allowed
    public static final int[] HYPERCUBE_VERTICES = {2, 4, 8, 16};

    public static final int MIN_REGULAR_GRAPH_K = 2;
    public static final int MAX_REGULAR_GRAPH_K = 5;

    public static final int MIN_EULERIAN_VERTICES = 4;
    public static final int MAX_EULERIAN_VERTICES = 10;

    public static final int MAX_TSP_VERTICES = 6;
    public static final int MIN_TSP_VERTICES = 4;

    public static final int MAX_DISTANCES_VERTICES = 6;
    public static final int MIN_DISTANCES_VERTICES = 4;

    public static final int MAX_EDGE_WEIGHT = 20;

    public static final double PROBABILITY_GRAPH_MIGHT_NOT_BE_EULERIAN = 0.7;
    public static final double PROBABILITY_GRAPH_MIGHT_BE_TREE = 0.5;
    public static final double PROBABILITY_GRAPH_MIGHT_NOT_BE_BIPARTITE = 0.9;
    public static final double PROBABILITY_GRAPH_MIGHT_NOT_BE_COMPLETE = 0.7;
    public static final double PROBABILITY_EDGE_REMOVAL = 0.1;
    public static final double PROBABILITY_SHOULD_BE_HOMEOMORPHIC = 0.2;

    public static final String[] GRAPH_NAMES = {"Petersen Graph", "Diamond", "Triangle", "Butterfly"};

    public static final String MIN_CLIQUE = "Minimum Clique";
    public static final String MAX_VERTEX_COVER = "Maximum Vertex Cover";
    public static final String EMPTY_GRAPH = "Empty Graph";
    public static final String[] TRIVIAL_QUESTIONS = {MIN_CLIQUE, MAX_VERTEX_COVER, EMPTY_GRAPH};

    public static final String ECCENTRICITY = "Eccentricity";
    public static final String RADIUS = "Radius";
    public static final String DIAMETER = "Diameter";
    public static final String[] DISTANCES = {ECCENTRICITY, RADIUS, DIAMETER};

}
