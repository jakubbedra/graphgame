package pl.edu.pg.eti.graphgame.graphs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Edge {

    private int v1;
    private int v2;
    private int weight = 1;

    public Edge(int v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = 1;
    }

}
