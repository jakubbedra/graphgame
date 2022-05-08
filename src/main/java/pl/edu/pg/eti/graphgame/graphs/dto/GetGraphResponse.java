package pl.edu.pg.eti.graphgame.graphs.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.graphs.model.Graph;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class GetGraphResponse {

    private List<List<Integer>> neighbourLists;

    public static GetGraphResponse map(Graph graph) {
        List<List<Integer>> response = new ArrayList<>();
        for (int i = 0; i < graph.getN(); i++) {
            response.add(graph.neighbours(i));
        }
        return GetGraphResponse.builder()
                .neighbourLists(response)
                .build();
    }

}
