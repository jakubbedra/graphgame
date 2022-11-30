package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class GraphAlgorithms {
	
    public static boolean breadthFirstSearch(Graph graph, List<Integer> answer) {
		if(answer.size() == graph.getN()) {
            if(answer.stream().allMatch(n -> n >= 0 && n < graph.getN()) == false) {
                return false;
            }

			if(answer.stream().distinct().count() != answer.size()) {
				return false;
			}
		} else {
			return false;
		}
        boolean[] visited = new boolean[graph.getN()];
        for(int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
		
		int current = 0;
		visited[answer.get(0)] = true;
		int added = 1;
		
		while(current < answer.size()) {
			List<Integer> neigh = graph.neighbours(answer.get(current)).stream().filter(n->visited[n]==false).collect(Collectors.toList());
			List<Integer> nexts = answer.stream().skip(added).limit(neigh.size()).collect(Collectors.toList());
			
			if(nexts.containsAll(neigh) && neigh.containsAll(nexts)) {
				neigh.forEach(n -> visited[n] = true);
				added += neigh.size();
				current++;
			} else {
				return false;
			}
		}
		
        return current == graph.getN() && added == graph.getN();
    }
	
    public static boolean depthFirstSearch(Graph graph, List<Integer> answer) {
		if(answer.size() == graph.getN()) {
			if(answer.stream().distinct().count() != answer.size()) {
				return false;
			}
		} else {
			return false;
		}
        boolean[] visited = new boolean[graph.getN()];
        for(int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
		
		Integer step[] = new Integer[]{0};
		boolean ret =  dfs_step_any_order(answer.get(0), graph, answer, visited, step);
		return ret;
    }
	
	public static boolean dfs_step_any_order(int from, Graph g, List<Integer> answer, boolean[] visited, Integer step[]) {
		step[0]++;
		visited[from] = true;
		
		
		while(true) {
			if(step[0]+1 >= answer.size()) {
				return true;
			}
			
			Set<Integer> neighbours = g.neighbours(from).stream().filter(n->visited[n]==false).collect(Collectors.toSet());
			
			int next = answer.get(step[0]);
			
			if(neighbours.isEmpty()) {
				return true;
			}
			if(neighbours.contains(next)) {
				if(dfs_step_any_order(next, g, answer, visited, step) == false) {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	

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

    private static int minCycleTSP(WeightedGraph g, List<Integer> vertices, int depth) {
        int minCycleValue = Integer.MAX_VALUE;
        if (depth != g.getN()) {
            for (int i = 0; i < g.getN(); i++) {
                if (!vertices.contains(i)) {
                    vertices.add(i);
                    minCycleValue = Math.min(minCycleValue, minCycleTSP(g, vertices, depth + 1));
                    vertices.remove((Integer) i);
                }
            }
        } else {
            minCycleValue = 0;
            for (int i = 0; i < vertices.size() - 1; i++) {
                minCycleValue += g.getEdgeWeight(vertices.get(i), vertices.get(i + 1));
            }
            minCycleValue += g.getEdgeWeight(vertices.get(0), vertices.get(vertices.size() - 1));
            return minCycleValue;
        }
        return minCycleValue;
    }

    public static int solveTSP(WeightedGraph g) {
        return minCycleTSP(g, new ArrayList<>(g.getN()), 0);
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
                    if (neighbours1.containsAll(neighbours3) && neighbours3.containsAll(neighbours1)) {
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

    public static boolean areGraphsHomeomorphic(Graph g1, Graph g2) {
        return areGraphsIsomorphic(retractEdges(g1), retractEdges(g2));
    }

    private static Graph retractEdges(Graph graph) {
        int removableVertices = 0;
        for (int i = 0; i < graph.getN(); i++) {
            if (graph.degree(i) == 2) {
                List<Integer> neighbours = graph.neighbours(i);
                if (!graph.edgeExists(neighbours.get(0), neighbours.get(1))) {
                    Graph g2 = new AdjacencyMatrixGraph(graph);
                    g2.addEdge(neighbours.get(0), neighbours.get(1));
                    g2.removeVertex(i);
                    return retractEdges(g2);
                }
            }
        }
        return graph;
    }

    private static int[] distances(Graph graph, int s) {
        int[] d = new int[graph.getN()];
        int[] predecessor = new int[graph.getN()];
        List<Integer> vertices = new LinkedList<>();
        for (int i = 0; i < graph.getN(); i++) {
            d[i] = Integer.MAX_VALUE;
            predecessor[i] = -1;
            vertices.add(i);
        }
        d[s] = 0;
        PriorityQueue<Integer> q = new PriorityQueue<>(Comparator.comparingInt(v -> d[v]));
        q.addAll(vertices);
        while (!q.isEmpty()) {
            int u = q.poll();
            q.add(u);
            u = q.poll();
            for (int v : graph.neighbours(u)) {
                int weight = graph instanceof WeightedGraph ? ((WeightedGraph) graph).getEdgeWeight(u, v) : 1;
                if (d[v] > d[u] + weight && d[u] != Integer.MAX_VALUE) {
                    d[v] = d[u] + weight;
                    predecessor[v] = u;
                }
            }
        }
        return d;
    }

    public static int distance(Graph graph, int s, int t) {
        return distances(graph, s)[t];
    }

    public static int eccentricity(Graph graph, int v) {
        return Arrays.stream(distances(graph, v)).max().getAsInt();
    }

    public static int radius(Graph graph) {
        int[] e = new int[graph.getN()];
        for (int i = 0; i < graph.getN(); i++) {
            e[i] = eccentricity(graph, i);
        }
        return Arrays.stream(e).min().getAsInt();
    }

    public static int diameter(Graph graph) {
        int[] d = new int[graph.getN()];
        for (int i = 0; i < graph.getN(); i++) {
            d[i] = eccentricity(graph, i);
        }
        return Arrays.stream(d).max().getAsInt();
    }

    public static int solveCPP(WeightedGraph graph) {
        int totalDistance = 0;
        for (int i = 0; i < graph.getN(); i++) {
            for (int j = i + 1; j < graph.getN(); j++) {
                totalDistance += graph.getEdgeWeight(i, j);
            }
        }
        if (GraphClassChecker.isEulerian(graph)) {
            return totalDistance;
        }
        // finding the odd vertices
        List<Integer> oddVertices = new LinkedList<>();
        for (int i = 0; i < graph.getN(); i++) {
            if (graph.degree(i) % 2 != 0) {
                oddVertices.add(i);
            }
        }
        return totalDistance + addShortest(graph, oddVertices);
    }

    private static int addShortest(WeightedGraph graph, List<Integer> oddVertices) {
        int additionalDistance = 0;
        while (!oddVertices.isEmpty()) {
            int shortestDistance = Integer.MAX_VALUE;
            int v1 = -1;
            int v2 = -1;
            for (int i = 0; i < oddVertices.size(); i++) {
                for (int j = i + 1; j < oddVertices.size(); j++) {
                    int d = distance(graph, oddVertices.get(i), oddVertices.get(j));
                    if (d < shortestDistance) {
                        shortestDistance = d;
                        v1 = oddVertices.get(i);
                        v2 = oddVertices.get(j);
                    }
                }
            }
            oddVertices.remove((Integer) v1);
            oddVertices.remove((Integer) v2);
            // add the path (?)
            additionalDistance += shortestDistance;
        }
        return additionalDistance;
    }

    private static int[] colorVerticesInOrder(Graph g, List<Integer> vertices) {
        int[] colors = new int[g.getN()];
        for (int i = 0; i < g.getN(); i++) {
            colors[i] = -1;
        }
        for (int v : vertices) {
            List<Integer> neighbours = g.neighbours(v);
            int max = Integer.MIN_VALUE;
            for (int u : neighbours) {
                if (colors[u] > max) {
                    max = colors[u];
                }
            }
            for (int i = 0; i < max; i++) {
                // check if a neighbour has this color
                boolean neighbourHasThisColor = false;
                for (int u : neighbours) {
                    if (colors[u] == i) {
                        neighbourHasThisColor = true;
                    }
                }
                if (!neighbourHasThisColor) {
                    colors[v] = i;
                    break;
                }
            }
            // must use a new color
            if (colors[v] == -1) {
                colors[v] = max + 1;
            }
        }
        return colors;
    }

    private static int getMinMaxColorVertex(Graph g, List<Integer> vertices) {
        if (vertices.size() == g.getN()) {
            return Arrays.stream(colorVerticesInOrder(g, vertices)).max().getAsInt();
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < g.getN(); i++) {
            if (!vertices.contains((Integer) i)) {
                vertices.add((Integer) i);
                min = Math.min(min, getMinMaxColorVertex(g, vertices));
                vertices.remove((Integer) i);
            }
        }
        return min;
    }

    public static boolean isVertexColoringValid(Graph g, int[] colors) {
        for (int v = 0; v < g.getN(); v++) {
            List<Integer> neighbours = g.neighbours(v);
            for (int u : neighbours) {
                if (colors[v] == colors[u]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int calculateChromaticNumber(Graph g) {
        return getMinMaxColorVertex(g, new LinkedList<>()) + 1;
    }

    private static int colorEdgesInOrder(Graph g, List<Edge> edges) {
        int[][] colors = new int[g.getN()][g.getN()];
        for (int i = 0; i < g.getN(); i++) {
            for (int j = 0; j < g.getN(); j++) {
                colors[i][j] = -1;
            }
        }
        int colorsUsed = -1;
        for (Edge e : edges) {
            // check all the edges of v1
            List<Integer> foundColors = new LinkedList<>();
            int v1 = e.getV1();
            for (int i = 0; i < g.getN(); i++) {
                if (v1 != i && colors[v1][i] != -1 && !foundColors.contains((Integer) colors[v1][i])) {
                    foundColors.add(colors[v1][i]);
                }
            }
            // check all the edges of v2
            int v2 = e.getV2();
            for (int i = 0; i < g.getN(); i++) {
                if (v2 != i && colors[v2][i] != -1 && !foundColors.contains((Integer) colors[v2][i])) {
                    foundColors.add(colors[v2][i]);
                }
            }
            // find the minimum available color
            int maxColor = -1;
            foundColors.sort(Comparator.comparingInt(Integer::intValue));
            for (int c : foundColors) {
                if (c > maxColor + 1) {
                    break;
                }
                maxColor = c;
            }
            colors[v1][v2] = maxColor + 1;
            colors[v2][v1] = maxColor + 1;
            colorsUsed = Math.max(colorsUsed, maxColor+1);
        }
        return colorsUsed + 1;
    }

    private static int getMinMaxColorEdges(Graph g, List<Edge> edges) {
        if (edges.size() == g.getM()) {
            return colorEdgesInOrder(g, edges);
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < g.getN(); i++) {
            for (int j = 0; j < g.getN(); j++) {
                if (g.edgeExists(i, j)) {
                    Edge e = new Edge(i, j);
                    if (!containsEdge(edges, e)) {
                        edges.add(e);
                        min = Math.min(min, getMinMaxColorEdges(g, edges));
                        edges.remove(e);
                    }
                }
            }
        }
        return min;
    }

    private static boolean containsEdge(List<Edge> edges, Edge e) {
        for (Edge e1 : edges) {
            if (e1.getV2() == e.getV2() && e1.getV1() == e.getV1() ||
                    e1.getV2() == e.getV1() && e1.getV1() == e.getV2()
            ) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEdgeColoringValid(Graph g, int[][] colors) {
        for (int i = 0; i < g.getN(); i++) {
            List<Integer> colorsInRow = new LinkedList<>();
            for (int j = 0; j < g.getN(); j++) {
                if (colors[i][j] != -1 && colorsInRow.contains(colors[i][j])) {
                    return false;
                }
                colorsInRow.add(colors[i][j]);
            }
        }
        return true;
    }

    public static int calculateChromaticIndex(Graph g) {
        return getMinMaxColorEdges(g, new LinkedList<>());
    }

}
