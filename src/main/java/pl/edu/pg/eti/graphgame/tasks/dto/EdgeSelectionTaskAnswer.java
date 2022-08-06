package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;
import pl.edu.pg.eti.graphgame.graphs.model.Edge;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdgeSelectionTaskAnswer {

    /**
     * The id of selected vertices in the order given by the player.
     */
    private List<Edge> selectedEdges;

}