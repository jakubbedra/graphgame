package pl.edu.pg.eti.graphgame.graphs;

import org.springframework.data.util.Pair;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@Deprecated // LR-testing. does not work, but maybe someday I will get back here, so i leave it as legacy code...
public class GraphPlanarityChecker {

    private Graph graph;

    boolean contradiction;

    private int[] height; // height of vertex

    private DirectedEdge[] parentEdge;
    private List<DirectedEdge>[] adjacencyLists;
    private Stack<Pair<Interval, Interval>> s;

    public GraphPlanarityChecker(Graph graph) {
        contradiction = false;
        this.graph = graph;
        height = new int[graph.getN()];
        adjacencyLists = new List[graph.getN()];
        parentEdge = new DirectedEdge[graph.getN()];
        s = new Stack<>();
        for (int i = 0; i < graph.getN(); i++) {
            height[i] = Integer.MAX_VALUE;
            adjacencyLists[i] = new LinkedList<>();
            parentEdge[i] = null;
        }
        convertToAdjLists();
    }

    private void convertToAdjLists() {
        for (int i = 0; i < graph.getN(); i++) {
            List<Integer> neighbours = graph.neighbours(i);
            for (int v : neighbours) {
                DirectedEdge edge = retrieveEdge(i, v);
                if (edge == null) {
                    edge = new DirectedEdge(i, v);
                }
                adjacencyLists[i].add(edge);
            }
        }
    }

    public boolean isPlanar() {
        //if (graph.getM() > 3 * graph.getN() - 6) {
        //    return false;
        //}

        orient();
        test();

        return !contradiction;
    }

    private void orient() {
        height[0] = 0;
        dfs1(0);
    }

    private void dfs1(int v) {
        DirectedEdge e = parentEdge[v];
        for (DirectedEdge vw : adjacencyLists[v]) {
            if (!vw.isOriented()) {
                vw.setSource(v);
                vw.lwpt = height[v];
                vw.lwpt2 = height[v];
                int w = vw.getOtherVertex(v);
                if (height[w] == Integer.MAX_VALUE) {
                    parentEdge[w] = vw;
                    height[w] = height[v] + 1;
                    dfs1(w);
                } else {
                    vw.lwpt = height[w];
                }
                // determine nesting depth
                vw.nestingDepth = 2 * vw.lwpt;
                if (vw.lwpt2 < height[v]) {
                    vw.nestingDepth++;
                }
                // update lowpoints of parent edge e
                if (e != null) {
                    if (vw.lwpt < e.lwpt) {
                        e.lwpt2 = Math.min(e.lwpt, vw.lwpt2);
                        e.lwpt = vw.lwpt;
                    } else if (vw.lwpt > e.lwpt) {
                        e.lwpt2 = Math.min(e.lwpt2, vw.lwpt);
                    } else {
                        e.lwpt2 = Math.min(e.lwpt2, vw.lwpt2);
                    }
                }
            }
        }
    }

    private void test() {
        // sort lists
        for (int i = 0; i < graph.getN(); i++) {
            adjacencyLists[i].sort(Comparator.comparingInt(u -> u.nestingDepth));
        }
        dfs2(0);
    }

    private void dfs2(int v) {
        DirectedEdge e = parentEdge[v];
        DirectedEdge e1 = null;
        for (DirectedEdge ei : adjacencyLists[v]) {
            if (ei.getSource() == v) {
                if (e1 == null) {
                    e1 = ei;
                }
                ei.stackBottom = s.isEmpty() ? null : s.peek();
                if (ei.getTarget() != 0 && ei.equals(parentEdge[ei.getTarget()])) {
                    dfs2(ei.getTarget());
                } else {
                    ei.lowptEdge = ei;
                    s.push(Pair.of(new Interval(), new Interval(ei, ei)));
                }
                if (ei.lwpt < height[v]) {
                    if (ei.equals(e1)) {
                        e.lowptEdge = e1.lowptEdge;
                    } else {
                        addConstraints(e, ei);
                    }
                }
            }
        }
        if (e != null) {
            int u = e.getSource();
            trimBackEdges(u);
            if (e.lwpt < height[u]) {
                DirectedEdge hL = s.isEmpty() ? null : s.peek().getFirst().high;
                DirectedEdge hR = s.isEmpty() ? null : s.peek().getSecond().high;
                if (hL != null && (hR == null || hL.lwpt > hR.lwpt)) {
                    e.ref = hL;
                } else {
                    e.ref = hR;
                }
            }
        }
    }

    private void addConstraints(DirectedEdge e, DirectedEdge ei) {
        Pair<Interval, Interval> p = Pair.of(new Interval(), new Interval());
        // merge return edges of ei into P.R
        do {
            if (s.isEmpty()) {
                break;
            }
            Pair<Interval, Interval> q = s.pop();
            if (!q.getFirst().isEmpty()) {
                q = Pair.of(q.getSecond(), q.getFirst());
            }
            if (!q.getFirst().isEmpty()) {
                contradiction = true;
                return;
            } else {
                if (q.getSecond().low.lwpt > e.lwpt) {
                    if (p.getSecond().isEmpty()) {
                        p.getSecond().high = q.getSecond().high;
                    } else {
                        p.getSecond().low.ref = q.getSecond().high;
                    }
                    p.getSecond().low = q.getSecond().low;
                } else {
                    q.getSecond().low.ref = e.lowptEdge;
                }
            }
        } while (!s.isEmpty() && s.peek().equals(ei.stackBottom));

        // merge conflicting return edges of e1, ..., ei-1 into P.L
        while (true) {
            if (s.size() == 0) {
                break;
            }
            if (!(conflicting(s.peek().getFirst(), ei) || conflicting(s.peek().getSecond(), ei))) {
                break;
            }
            Pair<Interval, Interval> q = s.pop();
            if (conflicting(q.getSecond(), ei)) {
                q = Pair.of(q.getSecond(), q.getFirst());
            }
            if (conflicting(q.getSecond(), ei)) {
                contradiction = true;
                return;
            } else {
                p.getSecond().low.ref = q.getSecond().high;
                if (q.getSecond().low != null) {
                    p.getSecond().low = q.getSecond().low;
                }
            }
            if (p.getFirst().isEmpty()) {
                p.getFirst().high = q.getFirst().high;
            } else {
                p.getFirst().low.ref = q.getFirst().high;
            }
            p.getFirst().low = q.getFirst().low;
            if (s.empty()) {
                break;
            }
        }
        if (!p.getFirst().isEmpty() && !p.getSecond().isEmpty()) {
            s.push(p);
        }
    }

    private boolean conflicting(Interval i, DirectedEdge b) {
        return !i.isEmpty() && i.high.lwpt > b.lwpt;
    }

    private void trimBackEdges(int u) {
        // drop entire conflict pairs
        while (!s.isEmpty() && lowest(s.peek()) == height[u]) {
            Pair<Interval, Interval> p = s.peek();
            if (p.getFirst().low != null) {
                p.getFirst().low.side = -1;
            }
        }
        if (!s.isEmpty()) {
            Pair<Interval, Interval> p = s.peek();
            // trim left interval
            while (p.getFirst().high != null && p.getFirst().high.getTarget() == u) {
                p.getFirst().high = p.getFirst().high.ref;
            }
            if (p.getFirst().high == null && p.getFirst().low != null) {
                p.getFirst().low.ref = p.getSecond().low;
                p.getFirst().low.side = -1;
                p.getFirst().low = null;
            }
            // trim right interval
            while (p.getSecond().high != null && p.getSecond().high.getTarget() == u) {
                p.getSecond().high = p.getSecond().high.ref;
            }
            if (p.getSecond().high == null && p.getSecond().low != null) {
                p.getSecond().low.ref = p.getFirst().low;
                p.getSecond().low.side = -1;
                p.getSecond().low = null;
            }
            s.push(p);
        }
    }

    private int lowest(Pair<Interval, Interval> p) {
        if (p.getFirst().isEmpty()) return p.getSecond().low.lwpt;
        if (p.getSecond().isEmpty()) return p.getFirst().low.lwpt;
        return Math.min(p.getFirst().low.lwpt, p.getSecond().low.lwpt2);
    }

    private DirectedEdge retrieveEdge(int u, int v) {
        for (DirectedEdge edge : adjacencyLists[u]) {
            if (edge.v1 == u && edge.v2 == v || edge.v1 == v && edge.v2 == u) {
                return edge;
            }
        }
        for (DirectedEdge edge : adjacencyLists[v]) {
            if (edge.v1 == u && edge.v2 == v || edge.v1 == v && edge.v2 == u) {
                return edge;
            }
        }
        return null;
    }

    private static class Interval {
        private DirectedEdge high;
        private DirectedEdge low;

        public Interval() {
            high = null;
            low = null;
        }

        public Interval(DirectedEdge low, DirectedEdge high) {
            this.low = low;
            this.high = high;
        }

        public boolean isEmpty() {
            return this.low == null && this.high == null;
        }

    }

    private static class DirectedEdge {

        private int v1;
        private int v2;
        private int direction;

        private int side;
        private int lwpt;
        private int lwpt2;
        private int nestingDepth;
        private DirectedEdge ref;
        private DirectedEdge lowptEdge;
        private Pair<Interval, Interval> stackBottom;

        public DirectedEdge(int v1, int v2) {
            this.side = 1;
            this.v1 = v1;
            this.v2 = v2;
            this.direction = Integer.MAX_VALUE;
            this.lwpt = -1;
            this.lwpt2 = -1;
            this.nestingDepth = -1;
            this.ref = null;
            this.lowptEdge = null;
            this.stackBottom = null;
        }

        public int getOtherVertex(int v) {
            if (v == v1) return v2;
            return v1;
        }

        public boolean isOriented() {
            return direction != Integer.MAX_VALUE;
        }

        public int getSource() {
            return direction == 1 ? v1 : v2;
        }

        public int getTarget() {
            return direction == -1 ? v1 : v2;
        }

        public void setSource(int v) {
            if (v == v1) {
                this.direction = 1;
            } else if (v == v2) {
                this.direction = -1;
            }
        }

        public boolean equals(DirectedEdge edge) {
            return this.v1 == edge.v1 && this.v2 == edge.v2 ||
                    this.v1 == edge.v2 && this.v2 == edge.v1;
        }

    }

}
