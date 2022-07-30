package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

}
