package pl.edu.pg.eti.graphgame.tasks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class VertexSelectionTaskAnswer {

    /**
     * The id of selected vertices in the order given by the player.
     */
    private List<Integer> selectedVertices;

}
