package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VertexSelectionTaskAnswer {

    /**
     * The id of selected vertices in the order given by the player.
     */
    private List<Integer> selectedVertices;

}
