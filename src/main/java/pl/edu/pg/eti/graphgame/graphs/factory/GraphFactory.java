package pl.edu.pg.eti.graphgame.graphs.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.config.Constants;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;
import pl.edu.pg.eti.graphgame.graphs.model.NeighbourListsGraph;

import java.util.*;
import java.util.ArrayList;

@Component
public class GraphFactory {

    private final Random RANDOM;

    @Autowired
    public GraphFactory() {
        this.RANDOM = new Random();
    }

    public Graph createRandomEulerianGraph() {
        int n = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int m = RANDOM.nextInt(
                (n * n - n) / 2 - (n - 1)
        ) + (n - 1);
        Graph graph = createRandomConnectedGraph(n, m);

        int[] unevenDegVertices = new int[n];
        int unevenDegVerticesCount = 0;
        for (int i = 0; i < n; i++) {
            if (graph.degree(i) % 2 != 0) {
                unevenDegVertices[unevenDegVerticesCount] = i;
                unevenDegVerticesCount++;
            }
        }

        for (int i = 1; i < unevenDegVerticesCount; i += 2) {
            if (!graph.edgeExists(unevenDegVertices[i], unevenDegVertices[i - 1])) {
                graph.addEdge(unevenDegVertices[i], unevenDegVertices[i - 1]);
            } else {
                graph.addVertex();
                graph.addEdge(unevenDegVertices[i], graph.getN() - 1);
                graph.addEdge(unevenDegVertices[i - 1], graph.getN() - 1);
            }
        }

        return graph;
    }

    public Graph createRandomConnectedGraph(int n, int m) {
        // create a spanning tree with n vertices O(n)
        List<List<Integer>> randomSpanningTree = createRandomSpanningTree(n);
        //System.out.println("BEFORE----------------------------------------------------------");
        //for (int i = 0; i < n; i++) {
        //    System.out.print("[" + i + "] ");
        //    randomSpanningTree.get(i).forEach(l -> System.out.print(l + ", "));
        //    System.out.println();
        //}
        //System.out.println("----------------------------------------------------------");
        // remove bias by randomizing the vertex indices O(n)
        randomSpanningTree = randomizeVertices(randomSpanningTree, n);
        //System.out.println("AFTER----------------------------------------------------------");
        //for (int i = 0; i < n; i++) {
        //    System.out.print("[" + i + "] ");
        //    randomSpanningTree.get(i).forEach(l -> System.out.print(l + ", "));
        //    System.out.println();
        //}
        //System.out.println("----------------------------------------------------------");

        // generate all the possible remaining edges and choose m - (n-1) random ones to add
        Graph graph = new NeighbourListsGraph(randomSpanningTree, n, n - 1);

        // O(n^2)
        // calculating possible edges
        Edge[] possibleEdges = new Edge[n * n - n];
        int possibleEdgesCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i != j && !graph.edgeExists(i, j)) {
                    possibleEdges[possibleEdgesCount] = new Edge(i, j);
                    possibleEdgesCount++;
                }
            }
        }

        // randomizing the edges
        for (int i = 0; i < possibleEdgesCount; i++) {
            Edge tmp = possibleEdges[i];
            int r = RANDOM.nextInt(possibleEdgesCount - i) + i;
            possibleEdges[i] = possibleEdges[r];
            possibleEdges[r] = tmp;
        }

        // adding the edges
        for (int i = 0; i < m - (n - 1); i++) {
            graph.addEdge(possibleEdges[i].getV1(), possibleEdges[i].getV2());
        }

        // todo: the generator is not yet finished, something is bugged + higher count of vertices should
        // appear much less often (use a better generator then your typical random for that one)

        // another possible method, that in certain cases should be more optimal, but is much worse overall:
        // O (n^3) n vertices, maximum of O(n^2) times
        // 1. make an array of all vertices that are not complete
        // 2. choose one at random
        // 3. make an array of all the vertices which are not his neighbours
        // 4. pick one at random
        // 5. place an edge
        // 6. repeat 1 - 5 till m count is satisfied

        return graph;
    }

    private List<List<Integer>> createRandomSpanningTree(int n) {
        List<List<Integer>> neighbourLists = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            neighbourLists.add(new LinkedList<>());
        }

        for (int i = 1; i < n; i++) {
            int v = RANDOM.nextInt(i);
            neighbourLists.get(i).add(v);
            neighbourLists.get(v).add(i);
        }

        return neighbourLists;
    }

    private List<List<Integer>> randomizeVertices(List<List<Integer>> neighbourLists, int n) {
        int[] vertices = new int[n];
        //initializing the array
        for (int i = 0; i < n; i++) {
            vertices[i] = i;
        }
        //randomizing the array
        for (int i = 0; i < n; i++) {
            int tmp = vertices[i];
            int r = RANDOM.nextInt(n - i) + i;
            vertices[i] = vertices[r];
            vertices[r] = tmp;
        }
        //changing the vertices in neighbour lists
        List<Integer>[] ret = new LinkedList[n];
        for (int i = 0; i < n; i++) {
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
        for (int i = 2; i < n; i++) {
            int count = RANDOM.nextInt(i) + 1;//random edge count (at least 1)
            int[] edges = new int[i];
            for (int j = 0; j < i; j++) {
                edges[j] = j;
            }
            List<Integer> edgesToAdd = getRandomEdges(edges, count);
            for (int edge : edgesToAdd) {
                graph.addEdge(i, edge);
            }
        }

        return graph;
    }

    private List<Integer> getRandomEdges(int[] edges, int count) {
        List<Integer> ret = new LinkedList<>();
        //randomizing
        for (int i = 0; i < count; i++) {
            int ind = RANDOM.nextInt(edges.length - i) + i;
            int tmp = edges[i];
            edges[i] = edges[ind];
            edges[ind] = tmp;
            ret.add(edges[i]);
        }
        return ret;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Edge {
        private int v1;
        private int v2;
    }

}
