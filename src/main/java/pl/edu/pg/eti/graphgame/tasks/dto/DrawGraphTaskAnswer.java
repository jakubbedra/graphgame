package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pg.eti.graphgame.graphs.model.AdjacencyMatrixGraph;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrawGraphTaskAnswer {

    private int[][] matrix;
    private int n;
    private int m;

    public static Graph mapToGraph(DrawGraphTaskAnswer answer) {
        return new AdjacencyMatrixGraph(answer.matrix, answer.n, answer.m);
    }

}
