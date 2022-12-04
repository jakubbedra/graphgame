package pl.edu.pg.eti.graphgame.graphs;

import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class HyperCubeTester {

    private String[] grayCodes;
    private int[] vertexCodes;
    private Graph g;
    private int n;//number of vertices - also number of gray codes
    private int bitsNo;

    public HyperCubeTester(Graph g, int bitsNo) {
        this.g = g;
        this.bitsNo = bitsNo;
        n = g.getN();
        vertexCodes = new int[n];
        grayCodes = new String[n];
        for (int i = 0; i < n; i++) {
            vertexCodes[i] = -1; //
        }
    }

    private void createGrayCodes() {
        List<String> arr = new ArrayList<>(n);
        arr.add("0");
        arr.add("1");
        for (int i = 2; i < (1 << bitsNo); i = i << 1) {
            for (int j = i - 1; j >= 0; j--) {
                arr.add(arr.get(j));
            }
            for (int j = 0; j < i; j++) {
                arr.set(j, "0" + arr.get(j));
            }
            for (int j = i; j < 2 * i; j++) {
                arr.set(j, "1" + arr.get(j));
            }
        }
        for (int i = 0; i < arr.size(); i++) {
            grayCodes[i] = arr.get(i);
        }
    }

    private int countOnes(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                count++;
            }
        }
        return count;
    }

    private boolean containsOnlyOneOne(String s) {
        return countOnes(s) == 1;
    }

    private boolean differByOnlyOneOne(String s1, String s2) {
        //int onesS1 = countOnes(s1);
        //int onesS2 = countOnes(s2);
        //if (Math.abs(onesS1 - onesS2) != 1) {
        //    return false;
        //}
        // count differences
        int differences = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differences++;
            }
        }

        return differences == 1;
    }

    private List<Integer> getOnlyOnes() {
        List<Integer> onlyOnes = new LinkedList<>();
        for (int i = 0; i < grayCodes.length; i++) {
            if (containsOnlyOneOne(grayCodes[i])) {
                onlyOnes.add(i);
            }
        }
        return onlyOnes;
    }

    private List<Integer> getAllCodesDifferingByOneOne(String s) {
        List<Integer> codes = new LinkedList<>();
        for (int i = 0; i < grayCodes.length; i++) {
            if (differByOnlyOneOne(grayCodes[i], s)) {
                codes.add(i);
            }
        }
        return codes;
    }

    // assigns a proper code to a vertex
    private void assignGrayCode(int v, int ancestor) {
        List<Integer> neighbours = g.neighbours(v);
        // get all codes differing by 1
        List<Integer> codes = getAllCodesDifferingByOneOne(grayCodes[vertexCodes[ancestor]]);
        // filter those which are used
        for (int i = 0; i < vertexCodes.length; i++) {
            if (codes.contains(vertexCodes[i])) {
                codes.remove((Integer) vertexCodes[i]);
            }
        }
        // check codes of N(v)
        for (Integer code : codes) {
            boolean conflict = false;
            // check for conflict
            for (Integer neighbour : neighbours) {
                if (vertexCodes[neighbour] != -1) {
                    if (!differByOnlyOneOne(grayCodes[code], grayCodes[vertexCodes[neighbour]])) {
                        conflict = true;
                        break;
                    }
                }
            }
            if (!conflict) {
                vertexCodes[v] = code;
                return;
            }
        }
    }

    // used to traverse the cube to assign codes to all vertices (if some vertex will not have a code -> the graph is not a hypercube)
    private List<Integer> breadthFirstSearch(int v0) {
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> visitedVertices = new LinkedList<>();
        boolean[] visited = new boolean[g.getN()];
        for (int i = 0; i < g.getN(); i++) {
            visited[i] = false;
        }

        int v = v0;
        visited[v] = true;
        queue.add(v);

        while (!queue.isEmpty()) {
            v = queue.poll();
            visitedVertices.add(v);

            List<Integer> neighbours = g.neighbours(v).stream().sorted().collect(Collectors.toList());
            for (int neighbour : neighbours) {
                if (!visited[neighbour]) {
                    // if it has not a code yet, then assign it
                    if (vertexCodes[neighbour] == -1) {
                        assignGrayCode(neighbour, v);
                    }
                    visited[neighbour] = true;
                    queue.add(neighbour);
                }
            }
        }

        return visitedVertices;
    }

    public boolean isHyperCubeLargerThan2Dimensions() {
        createGrayCodes();
        for (int i = 0; i < vertexCodes.length; i++) {
            vertexCodes[i] = -1;
        }

        //assign first codes
        int v = 0;
        vertexCodes[v] = 0;
        List<Integer> neighbours = g.neighbours(v);
        List<Integer> onlyOnes = getOnlyOnes();
        for (Integer neighbour : neighbours) {
            v++;
            vertexCodes[neighbour] = onlyOnes.get(0);
            onlyOnes.remove((int) 0);
        }

        // assign the rest with the help of bfs
        breadthFirstSearch(0);

        boolean allAssigned = true;

        for (int i = 0; i < vertexCodes.length; i++) {
            if (vertexCodes[i] == -1) {
                allAssigned = false;
            }
        }

        return allAssigned;
    }

}
