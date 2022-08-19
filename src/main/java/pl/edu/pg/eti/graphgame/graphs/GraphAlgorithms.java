package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class GraphAlgorithms {

    public static List<Integer> breadthFirstSearch(Graph graph) {
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> visitedVertices = new LinkedList<>();
        boolean[] visited = new boolean[graph.getN()];
        for (int i = 0; i < graph.getN(); i++) {
            visited[i] = false;
        }

        int v = 0;
        visited[v] = true;
        queue.add(v);

        while (!queue.isEmpty()) {
            v = queue.poll();
            visitedVertices.add(v);

            List<Integer> neighbours = graph.neighbours(v).stream().sorted().collect(Collectors.toList());
            for (int neighbour : neighbours) {
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.add(neighbour);
                }
            }
        }

        return visitedVertices;
    }

    public static List<Integer> depthFirstSearch(Graph graph) {
        List<Integer> visitOrder = new LinkedList<>();
        boolean[] visited = new boolean[graph.getN()];

        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        dfsVisit(graph, 0, visited, visitOrder);

        return visitOrder;
    }

    private static void dfsVisit(Graph g, int v, boolean[] visited, List<Integer> visitOrder) {
        visited[v] = true;
        visitOrder.add(v);
        List<Integer> neighbours = g.neighbours(v).stream().sorted().collect(Collectors.toList());
        for (int i : neighbours) {
            if (!visited[i]) {
                dfsVisit(g, i, visited, visitOrder);
            }
        }
    }

    public static boolean checkClique(Graph g, List<Integer> vertices) {
        int[] array = new int[vertices.size()];
        int i = 0;
        for (Integer vertex : vertices) {
            array[i] = vertex;
            i++;
        }
        return isClique(g, array, vertices.size());
    }

    private static boolean isClique(Graph g, int[] selectedVertices, int b) {
        for (int i = 0; i < b; i++) {
            for (int j = i + 1; j < b; j++) {
                if (!g.edgeExists(selectedVertices[i], selectedVertices[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int maxClique(Graph g, int[] selectedVertices, int i, int l) {
        int max = 0;

        for (int j = i; j < g.getN(); j++) {
            selectedVertices[l] = j;

            //for (int chuj = 0; chuj < l+1; chuj++) {
            //    System.out.print(selectedVertices[chuj] + ", ");
            //}
            //System.out.println();

            if (isClique(g, selectedVertices, l + 1)) {
                max = Math.max(max, l + 1);
                max = Math.max(max, maxClique(g, selectedVertices, j + 1, l + 1));
            }
        }

        return max;
    }

    public static int maxCliqueSize(Graph g) {
        int[] selectedVertices = new int[g.getN()];
        return maxClique(g, selectedVertices, 0, 0);
    }

    public static boolean checkIndependentSet(Graph g, List<Integer> vertices) {
        int[] array = new int[vertices.size()];
        int i = 0;
        for (Integer vertex : vertices) {
            array[i] = vertex;
            i++;
        }
        return isIndependentSet(g, array, vertices.size());
    }

    private static boolean isIndependentSet(Graph g, int[] selectedVertices, int b) {
        for (int i = 0; i < b; i++) {
            for (int j = i + 1; j < b; j++) {
                if (g.edgeExists(selectedVertices[i], selectedVertices[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int maxIndependentSetSize(Graph g, int[] selectedVertices, int i, int l) {
        int max = 0;
        for (int j = i; j < g.getN(); j++) {
            selectedVertices[l] = j;
            if (isIndependentSet(g, selectedVertices, l + 1)) {
                max = Math.max(max, l + 1);
                max = Math.max(max, maxIndependentSetSize(g, selectedVertices, j + 1, l + 1));
            }
        }
        return max;
    }

    public static int maxIndependentSetSize(Graph g) {
        int[] selectedVertices = new int[g.getN()];
        return maxIndependentSetSize(g, selectedVertices, 0, 0);
    }

    public static boolean checkVertexCover(Graph g, List<Integer> vertices) {
        int[] array = new int[vertices.size()];
        int i = 0;
        for (Integer vertex : vertices) {
            array[i] = vertex;
            i++;
        }
        return isVertexCover(g, array, vertices.size());
    }

    private static boolean isVertexCover(Graph g, int[] selectedVertices, int b) {
        Graph g2 = new AdjacencyMatrixGraph(g);
        for (int i = 0; i < b; i++) {
            List<Integer> neighbours = g2.neighbours(selectedVertices[i]);
            for (Integer v : neighbours) {
                g2.removeEdge(v, selectedVertices[i]);
            }
        }
        // if no edge is left, then the given set is vertex cover
        return g2.getM() == 0;
    }

    private static int minVertexCover(Graph g, int[] selectedVertices, int i, int l) {
        int min = Integer.MAX_VALUE;
        for (int j = i; j < g.getN(); j++) {
            selectedVertices[l] = j;

            if (isVertexCover(g, selectedVertices, l + 1)) {
                min = Math.min(min, l + 1);
            } else {
                min = Math.min(min, minVertexCover(g, selectedVertices, j + 1, l + 1));
            }
        }
        return min;
    }

    public static int minVertexCoverSize(Graph g) {
        int[] selectedVertices = new int[g.getN()];
        return minVertexCover(g, selectedVertices, 0, 0);
    }

    public static boolean checkEulerCycle(Graph g, List<Integer> vertices) {
        Graph g2 = new AdjacencyMatrixGraph(g);
        for (int i = 0; i < vertices.size() - 1; i++) {
            if (!g2.edgeExists(vertices.get(i), vertices.get(i + 1))) {
                return false;
            }
            g2.removeEdge(vertices.get(i), vertices.get(i + 1));
        }
        return g2.getM() == 0;
    }

    /**
     * Uses Kruskal's algorithm.
     */
    private static List<Edge> getMinSpanningTreeEdges(WeightedGraph g) {
        List<Edge> edges = new LinkedList<>();
        for (int i = 0; i < g.getN(); i++) {
            for (int j = 1; j < g.getN(); j++) {
                if (g.edgeExists(i, j)) {
                    edges.add(new Edge(i, j, g.getEdgeWeight(i, j)));
                }
            }
        }
        edges.sort(Comparator.comparingInt(Edge::getWeight));
        List<Edge> spanningTreeEdges = new LinkedList<>();
        List<List<Integer>> vertexSets = new ArrayList<>(g.getN());
        for (int i = 0; i < g.getN(); i++) {
            vertexSets.add(new LinkedList<>());
            vertexSets.get(i).add(i);
        }

        for (Edge e : edges) {
            if (!inSameSet(e.getV1(), e.getV2(), vertexSets)) {
                spanningTreeEdges.add(e);
                vertexSets = mergeSets(e.getV1(), e.getV2(), vertexSets);
            }
            if (vertexSets.size() == 1) {
                break;
            }
        }

        return spanningTreeEdges;
    }

    private static boolean inSameSet(int v1, int v2, List<List<Integer>> vertexSets) {
        for (int i = 0; i < vertexSets.size(); i++) {
            boolean v1Found = false;
            boolean v2Found = false;
            for (int j : vertexSets.get(i)) {
                if (j == v1) {
                    v1Found = true;
                } else if (j == v2) {
                    v2Found = true;
                }
            }
            if (v1Found && v2Found) {
                return true;
            }
        }
        return false;
    }

    private static List<List<Integer>> mergeSets(int v1, int v2, List<List<Integer>> vertexSets) {
        List<List<Integer>> ret = new ArrayList<>(vertexSets.size() - 1);
        List<Integer> tmp = new LinkedList<>();
        int v1Ind = -1;
        int v2Ind = -1;
        for (int i = 0; i < vertexSets.size(); i++) {
            if (v1Ind == -1 && vertexSets.get(i).contains(v1)) {
                tmp.addAll(vertexSets.get(i));
                v1Ind = i;
            } else if (v2Ind == -1 && vertexSets.get(i).contains(v2)) {
                tmp.addAll(vertexSets.get(i));
                v2Ind = i;
            }
        }
        ret.add(tmp);
        for (int i = 0; i < vertexSets.size(); i++) {
            if (i != v1Ind && i != v2Ind) {
                ret.add(vertexSets.get(i));
            }
        }
        return ret;
    }

    public static int getMinSpanningTreeTotalWeight(WeightedGraph g) {
        return getMinSpanningTreeEdges(g).stream()
                .map(Edge::getWeight)
                .reduce(0, (subtotal, edge) -> subtotal += edge);
    }

    private static int minPathTSP(WeightedGraph g, List<Integer> vertices, int depth) {
        int minPathValue = Integer.MAX_VALUE;
        if (depth != g.getN()) {
            for (int i = 0; i < g.getN(); i++) {
                if (!vertices.contains(i)) {
                    vertices.add(i);
                    minPathValue = Math.min(minPathValue, minPathTSP(g, vertices, depth + 1));
                    vertices.remove((Integer) i);
                }
            }
        } else {
            minPathValue = 0;
            for (int i = 0; i < vertices.size() - 1; i++) {
                minPathValue += g.getEdgeWeight(vertices.get(i), vertices.get(i + 1));
            }
            minPathValue += g.getEdgeWeight(vertices.get(0), vertices.get(vertices.size() - 1));
            return minPathValue;
        }
        return minPathValue;
    }

    public static int solveTSP(WeightedGraph g) {
        return minPathTSP(g, new ArrayList<>(g.getN()), 0);
    }


    private static boolean checkIsomorphism(Graph g1, Graph g2, List<Integer> vertices) {
        if (vertices.size() == g1.getN()) {
            int sameNeighbours = 0;
            for (int i = 0; i < g1.getN(); i++) {
                List<Integer> neighbours2 = g2.neighbours(i);
                List<Integer> neighbours3 = new LinkedList<>();
                for (int j = 0; j < neighbours2.size(); j++) {
                    neighbours3.add(vertices.indexOf(neighbours2.get(j)));
                }
                for (int j = 0; j < g1.getN(); j++) {
                    List<Integer> neighbours1 = g1.neighbours(j);
                    if(neighbours1.containsAll(neighbours3) && neighbours3.containsAll(neighbours1)) {
                        sameNeighbours++;
                        break;
                    }
                }
            }
            if (sameNeighbours == g1.getN()) {
                return true;
            }
        }
        // do deeper
        for (int i = 0; i < g1.getN(); i++) {
            if (!vertices.contains((Integer) i)) {
                vertices.add(i);
                if (checkIsomorphism(g1, g2, vertices)) {
                    return true;
                }
                vertices.remove((Integer) i);
            }
        }
        return false;
    }

    public static boolean areGraphsIsomorphic(Graph g1, Graph g2) {
        if (g1.getN() != g2.getN() || g1.getM() != g2.getM()) {
            return false;
        }

        return checkIsomorphism(g1, g2, new ArrayList<>(g1.getN()));
    }

}
