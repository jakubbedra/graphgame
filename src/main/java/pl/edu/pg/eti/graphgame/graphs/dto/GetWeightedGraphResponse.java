package pl.edu.pg.eti.graphgame.graphs.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.graphs.model.WeightedGraph;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetWeightedGraphResponse {

    private int[][] matrix;
    private int n;
    private int m;

    public static GetWeightedGraphResponse map(WeightedGraph graph) {
        int[][] response = new int[graph.getN()][graph.getN()];
        for (int i = 0; i < graph.getN(); i++) {
            for (int j = 0; j < graph.getN(); j++) {
                response[i][j] = 0;
            }
        }
        for (int i = 0; i < graph.getN(); i++) {
            for (int j = 0; j < graph.getN(); j++) {
                response[i][j] = graph.getEdgeWeight(i, j);
            }
        }
        return GetWeightedGraphResponse.builder()
                .matrix(response)
                .n(graph.getN())
                .m(graph.getM())
                .build();
    }

}
