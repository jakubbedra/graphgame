package pl.edu.pg.eti.graphgame.graphs.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.config.Constants;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.NeighbourListsGraph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedAdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedGraph;

import java.util.*;
import java.util.ArrayList;

@Component
public class GraphFactory {

    private final Random RANDOM;

    @Autowired
    public GraphFactory() {
        this.RANDOM = new Random();
    }

    public WeightedGraph createRandomCompleteWeightedGraph(int n) {
        Graph completeGraph = createRandomCompleteGraph(n, false);
        return convertToRandomWeightedGraph(completeGraph);
    }

    public Graph createRandomMaybeCompleteGraph(int n) {
        return createRandomCompleteGraph(n, RANDOM.nextDouble()
            <Constants.PROBABILITY_GRAPH_MIGHT_NOT_BE_COMPLETE);
    }

    private Graph createRandomCompleteGraph(int n, boolean mightNotBeComplete) {
        Graph completeGraph = new NeighbourListsGraph(n);
        for(int i = 0; i<n; i++) {
            for(int j = i+1; j<n; j++) {
                completeGraph.addEdge(i, j);
            }
        }
        //delete random edges
        if(mightNotBeComplete) {
            int maxEdgesToDelete = RANDOM.nextInt(n-1)+1;
            for(int i = 0; i<n; i++) {
                for(int j = i+1; j<n; j++) {
                    if(RANDOM.nextDouble()<Constants.PROBABILITY_EDGE_REMOVAL
                        &&maxEdgesToDelete>0) {
                        completeGraph.removeEdge(i, j);
                        maxEdgesToDelete--;
                    }
                }
            }
        }
        return completeGraph;
    }

    public Graph createRandomMaybeEulerianGraph() {
        boolean mightNotBeEulerian = RANDOM.nextDouble()
            <Constants.PROBABILITY_GRAPH_MIGHT_NOT_BE_EULERIAN;
        return createRandomEulerianGraph(!mightNotBeEulerian);
    }

    public Graph createRandomEulerianGraph() {
        return createRandomEulerianGraph(true);
    }

    public Graph createRandomEulerianGraph(boolean shouldBeEulerian) {
        int n = RANDOM.nextInt(
            Constants.MAX_EULERIAN_VERTICES-Constants.MIN_EULERIAN_VERTICES
        )+Constants.MIN_EULERIAN_VERTICES;
        int m = RANDOM.nextInt(
            (n*n-n)/2-(n-1)
        )+(n-1);
        Graph graph = createRandomConnectedGraph(n, m);

        int[] unevenDegVertices = new int[n];
        int unevenDegVerticesCount = 0;
        for(int i = 0; i<n; i++) {
            if(graph.degree(i)%2!=0) {
                unevenDegVertices[unevenDegVerticesCount] = i;
                unevenDegVerticesCount++;
            }
        }

        for(int i = 1; i<unevenDegVerticesCount; i += 2) {
            if(shouldBeEulerian||RANDOM.nextBoolean()) {
                if(!graph.edgeExists(unevenDegVertices[i],
                    unevenDegVertices[i-1])) {
                    graph.addEdge(unevenDegVertices[i], unevenDegVertices[i-1]);
                } else {
                    graph.addVertex();
                    graph.addEdge(unevenDegVertices[i], graph.getN()-1);
                    graph.addEdge(unevenDegVertices[i-1], graph.getN()-1);
                }
            }
        }

        return graph;
    }

    public Graph createRandomHamiltonianGraph(int n, int m) {
        List<List<Integer>> cycle = createCycle(n);
        cycle = randomizeVertices(cycle, n);
        Graph graph = new NeighbourListsGraph(cycle, n, n);
        Edge[] possibleEdges = createRandomPossibleEdges(graph, n);

        for(int i = 0; i<m-n; i++) {
            graph.addEdge(possibleEdges[i].getV1(), possibleEdges[i].getV2());
        }

        return graph;
    }

    private List<List<Integer>> createCycle(int n) {
        List<List<Integer>> cycle = new ArrayList<>(n);
        for(int i = 0; i<n; i++) {
            cycle.add(new LinkedList<>());
        }
        for(int i = 0; i<n-1; i++) {
            cycle.get(i).add(i+1);
            cycle.get(i+1).add(i);
        }
        cycle.get(0).add(n-1);
        cycle.get(n-1).add(0);
        return cycle;
    }

    private List<List<Integer>> createPath(int n) {
        List<List<Integer>> path = new ArrayList<>(n);
        for(int i = 0; i<n; i++) {
            path.add(new LinkedList<>());
        }
        for(int i = 0; i<n-1; i++) {
            path.get(i).add(i+1);
            path.get(i+1).add(i);
        }
        return path;
    }

    public Graph createRandomMaybeBipartiteGraph(int n) {
        return createRandomBipartiteGraph(
            n, RANDOM.nextDouble()
                <Constants.PROBABILITY_GRAPH_MIGHT_NOT_BE_BIPARTITE
        );
    }

    public Graph createRandomBipartiteGraph(int n,
        boolean mightNotBeBipartite) {
        int r = RANDOM.nextInt(n-1)+1;
        int s = n-r;
        int m = (r==1||s==1) ? m = n-1 : RANDOM.nextInt(r*s-(n-1))+(n-1);

        List<List<Integer>> path = createPath(n);
        if(mightNotBeBipartite) {
            path = randomizeVertices(path, n);
        }

        Graph graph = new NeighbourListsGraph(path, n, n-1);
        Edge[] possibleEdges;

        if(mightNotBeBipartite) {
            possibleEdges = createRandomPossibleEdges(graph, n);
        } else {
            possibleEdges = createRandomPossibleEdgesForBipartiteGraph(graph,
                n);
        }

        for(int i = 0; i<m-graph.getM(); i++) {
            graph.addEdge(possibleEdges[i].getV1(), possibleEdges[i].getV2());
        }

        return graph;
    }

    public Graph createRandomGraphWith2ConnectedComponentsHomeomorphism(int n,
        int m) {
        boolean shouldBeHomeomorphic =
            RANDOM.nextDouble()<Constants.PROBABILITY_SHOULD_BE_HOMEOMORPHIC;
        return createRandomGraphWith2ConnectedHomeomorphicComponents(n, m, n, m,
            shouldBeHomeomorphic);
    }

    public Graph createRandomGraphWith2ConnectedHomeomorphicComponents(int n1,
        int m1, int n2, int m2, boolean shouldBeHomeomorphic) {
        List<List<Integer>> randomSpanningTree = createRandomSpanningTree(n1);
        randomSpanningTree = randomizeVertices(randomSpanningTree, n1);
        Graph graph = new NeighbourListsGraph(randomSpanningTree, n1, n1-1);
        Edge[] possibleEdges = createRandomPossibleEdges(graph, n1);
        for(int i = 0; i<m1-(n1-1); i++) {
            graph.addEdge(possibleEdges[i].getV1(), possibleEdges[i].getV2());
        }

        // creating a graph isomorphic to the first graph
        Graph graph2 = null;

        if(!shouldBeHomeomorphic) {
            graph2 = createRandomConnectedGraph(n2, m2);
        } else {
            List<List<Integer>> neighbourLists = randomizeVertices(
                ((NeighbourListsGraph)graph).getNeighbourLists(), n1);
            graph2 = new NeighbourListsGraph(neighbourLists, n1, m1);
        }

        graph = extendRandomEdges(graph);
        // extend random edges
        graph2 = extendRandomEdges(graph2);

        graph.merge(graph2);
        return graph;
    }

    private Graph extendRandomEdges(Graph graph) {
        int toExtend = RANDOM.nextInt(
            Constants.MAX_EXTENDED_EDGES_HOMEOMORPHISM);
        Edge[] randomEdgesToExtend = getRandomEdgesToExtend(graph,
            graph.getN());
        for(int i = 0; i<toExtend&&i<graph.getN(); i++) {
            Edge e = randomEdgesToExtend[i];
            if(e!=null) {
                graph.addVertex();
                graph.addEdge(e.getV1(), graph.getN()-1);
                graph.addEdge(e.getV2(), graph.getN()-1);
                graph.removeEdge(e.getV1(), e.getV2());
            }
        }
        return graph;
    }

    private Edge[] getRandomEdgesToExtend(Graph graph, int n) {
        Edge[] possibleEdges = new Edge[n*n-n];
        int possibleEdgesCount = 0;
        for(int i = 0; i<n; i++) {
            for(int j = i; j<n; j++) {
                if(i!=j&&graph.edgeExists(i, j)) {
                    possibleEdges[possibleEdgesCount] = new Edge(i, j);
                    possibleEdgesCount++;
                }
            }
        }

        // randomizing the edges
        for(int i = 0; i<possibleEdgesCount; i++) {
            Edge tmp = possibleEdges[i];
            int r = RANDOM.nextInt(possibleEdgesCount-i)+i;
            possibleEdges[i] = possibleEdges[r];
            possibleEdges[r] = tmp;
        }

        return possibleEdges;
    }

    public Graph createRandomGraphWith2ConnectedComponentsIsomorphism(int n,
        int m) {
        boolean shouldBeIsomorphic = RANDOM.nextBoolean();
        if(!shouldBeIsomorphic) {
            int n2 = RANDOM.nextInt(4)+n;
            int m2 = Math.min(RANDOM.nextInt(6)+m, (n*n-n)/2);
            return createRandomGraphWith2ConnectedComponents(n, m, n2, m2,
                false);
        }
        return createRandomGraphWith2ConnectedComponents(n, m, n, m, true);
    }

    public Graph createRandomGraphWith2ConnectedComponents(int n1, int m1,
        int n2, int m2, boolean shouldBeIsomorphic) {
        List<List<Integer>> randomSpanningTree = createRandomSpanningTree(n1);
        randomSpanningTree = randomizeVertices(randomSpanningTree, n1);
        Graph graph = new NeighbourListsGraph(randomSpanningTree, n1, n1-1);
        Edge[] possibleEdges = createRandomPossibleEdges(graph, n1);
        for(int i = 0; i<m1-(n1-1); i++) {
            graph.addEdge(possibleEdges[i].getV1(), possibleEdges[i].getV2());
        }

        // creating a graph isomorphic to the first graph
        Graph graph2 = null;

        if(!shouldBeIsomorphic) {
            graph2 = createRandomConnectedGraph(n2, m2);
        } else {
            List<List<Integer>> neighbourLists = ((NeighbourListsGraph)graph).getNeighbourLists();
            neighbourLists = randomizeVertices(neighbourLists, n1);
            graph2 = new NeighbourListsGraph(neighbourLists, n1, m1);
        }

        graph.merge(graph2);
        return graph;
    }

    public Graph createRandomConnectedGraph(int n, int m) {
        return createRandomConnectedGraph(n, m, false);
    }

    public Graph createRandomConnectedGraph(int n, int m,
        boolean randomWeights) {
        // create a spanning tree with n vertices O(n)
        List<List<Integer>> randomSpanningTree = createRandomSpanningTree(n);
        // remove bias by randomizing the vertex indices O(n)
        randomSpanningTree = randomizeVertices(randomSpanningTree, n);
        // generate all the possible remaining edges and choose m - (n-1) random ones to add
        Graph graph = new NeighbourListsGraph(randomSpanningTree, n, n-1);
        // O(n^2)
        // calculating possible edges
        Edge[] possibleEdges = createRandomPossibleEdges(graph, n);
        // adding the edges
        for(int i = 0; i<m-(n-1); i++) {
            graph.addEdge(possibleEdges[i].getV1(), possibleEdges[i].getV2());
        }
        if(randomWeights) {
            return convertToRandomWeightedGraph(graph);
        }
        return graph;
    }

    private WeightedGraph convertToRandomWeightedGraph(Graph graph) {
        WeightedGraph graph2 = new WeightedAdjacencyMatrixGraph(graph);
        for(int i = 0; i<graph.getN(); i++) {
            for(int j = i+1; j<graph.getN(); j++) {
                graph2.setEdgeWeight(i, j,
                    RANDOM.nextInt(Constants.MAX_EDGE_WEIGHT-1)+1);
            }
        }
        return graph2;
    }

    private Edge[] createRandomPossibleEdgesForBipartiteGraph(Graph graph,
        int n) {
        // calculating possible edges
        Edge[] possibleEdges = new Edge[n*n-n];
        int possibleEdgesCount = 0;
        for(int i = 0; i<n; i++) {
            for(int j = i; j<n; j++) {
                if(i!=j&&!graph.edgeExists(i, j)&&i%2==0&&j%2==1) {
                    possibleEdges[possibleEdgesCount] = new Edge(i, j);
                    possibleEdgesCount++;
                }
            }
        }

        // randomizing the edges
        for(int i = 0; i<possibleEdgesCount; i++) {
            Edge tmp = possibleEdges[i];
            int r = RANDOM.nextInt(possibleEdgesCount-i)+i;
            possibleEdges[i] = possibleEdges[r];
            possibleEdges[r] = tmp;
        }

        return possibleEdges;
    }

    private Edge[] createRandomPossibleEdges(Graph graph, int n) {
        // calculating possible edges
        Edge[] possibleEdges = new Edge[n*n-n];
        int possibleEdgesCount = 0;
        for(int i = 0; i<n; i++) {
            for(int j = i; j<n; j++) {
                if(i!=j&&!graph.edgeExists(i, j)) {
                    possibleEdges[possibleEdgesCount] = new Edge(i, j);
                    possibleEdgesCount++;
                }
            }
        }

        // randomizing the edges
        for(int i = 0; i<possibleEdgesCount; i++) {
            Edge tmp = possibleEdges[i];
            int r = RANDOM.nextInt(possibleEdgesCount-i)+i;
            possibleEdges[i] = possibleEdges[r];
            possibleEdges[r] = tmp;
        }

        return possibleEdges;
    }

    private List<List<Integer>> createRandomSpanningTree(int n) {
        List<List<Integer>> neighbourLists = new ArrayList<>();
        for(int i = 0; i<n; i++) {
            neighbourLists.add(new LinkedList<>());
        }

        for(int i = 1; i<n; i++) {
            int v = RANDOM.nextInt(i);
            neighbourLists.get(i).add(v);
            neighbourLists.get(v).add(i);
        }

        return neighbourLists;
    }

    private List<List<Integer>> randomizeVertices(
        List<List<Integer>> neighbourLists, int n) {
        int[] vertices = new int[n];
        //initializing the array
        for(int i = 0; i<n; i++) {
            vertices[i] = i;
        }
        //randomizing the array
        for(int i = 0; i<n; i++) {
            int tmp = vertices[i];
            int r = RANDOM.nextInt(n-i)+i;
            vertices[i] = vertices[r];
            vertices[r] = tmp;
        }
        //changing the vertices in neighbour lists
        List<Integer>[] ret = new LinkedList[n];
        for(int i = 0; i<n; i++) {
            List<Integer> tmp = new LinkedList<>();
            neighbourLists.get(i).forEach(x -> tmp.add(vertices[x]));
            ret[vertices[i]] = tmp;
        }
        return Arrays.asList(ret);
    }

    /**
     * Creates a random connected Graph
     *
     * @param n number of vertices the Graph will have
     * @return generated Graph
     */
    public Graph createRandomConnectedGraph(int n) {
        Graph graph = new NeighbourListsGraph(n);

        //we start at vertex 2, because at 0 nothing really happens,
        //and at 1 we always have the edge connected to 0
        graph.addEdge(0, 1);
        for(int i = 2; i<n; i++) {
            int count = RANDOM.nextInt(i)+1;//random edge count (at least 1)
            int[] edges = new int[i];
            for(int j = 0; j<i; j++) {
                edges[j] = j;
            }
            List<Integer> edgesToAdd = getRandomEdges(edges, count);
            for(int edge : edgesToAdd) {
                graph.addEdge(i, edge);
            }
        }

        return graph;
    }

    private List<Integer> getRandomEdges(int[] edges, int count) {
        List<Integer> ret = new LinkedList<>();
        //randomizing
        for(int i = 0; i<count; i++) {
            int ind = RANDOM.nextInt(edges.length-i)+i;
            int tmp = edges[i];
            edges[i] = edges[ind];
            edges[ind] = tmp;
            ret.add(edges[i]);
        }
        return ret;
    }

    private int[] shuffleVerticesIds(int n) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i<n; ++i)
            list.add(i);
        Collections.shuffle(list);
        int[] l = new int[n];
        for(int i = 0; i<n; ++i)
            l[i] = list.get(i);
        return l;
    }

    public Graph createRandomPathGraph(int n) {
        Graph g = new NeighbourListsGraph(n);
        int[] v = shuffleVerticesIds(n);

        for(int i = 1; i<n; i++) {
            g.addEdge(v[i], v[i-1]);
        }

        return g;
    }

    public Graph createRandomCycleGraph(int n) {
        Graph g = new NeighbourListsGraph(n);
        int[] v = shuffleVerticesIds(n);

        for(int i = 1; i<n; i++) {
            g.addEdge(v[i], v[i-1]);
        }
        g.addEdge(v[0], v[n-1]);

        return g;
    }

    public Graph createCompleteGraph(int n) {
        Graph g = new NeighbourListsGraph(n);

        for(int i = 0; i<n; i++) {
            for(int j = i+1; j<n; ++j) {
                g.addEdge(i, j);
            }
        }

        return g;
    }

    public Graph createRandomTreeGraph(int n) {
        List<List<Integer>> randomSpanningTree = createRandomSpanningTree(n);
        randomSpanningTree = randomizeVertices(randomSpanningTree, n);
        Graph graph = new NeighbourListsGraph(randomSpanningTree, n, n-1);
        return graph;
    }

    public Graph createRandomStarGraph(int n) {
        Graph g = new NeighbourListsGraph(n);

        int x = RANDOM.nextInt(n);

        for(int i = 0; i<n; i++) {
            if(i != x) {
                g.addEdge(i, x);
            }
        }

        return g;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Edge {
        private int v1;
        private int v2;
    }

}
