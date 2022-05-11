package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

            List<Integer> neighbours = graph.neighbours(v);
            for (int neighbour : neighbours) {
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.add(neighbour);
                }
            }
        }

        return visitedVertices;
    }

    public static List<Integer> depthFirstSearch() {
        return null;
    }

    public static boolean isComplete(Graph graph) {
        for (int i = 0; i < graph.getN(); i++) {
            for (int j = 0; j < graph.getN(); j++) {
                if (i != j && !graph.edgeExists(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkConnectivity() {
        return false;
    }

}
