package pl.edu.pg.eti.graphgame.exceptions;

public class NegativeVertexCountException extends RuntimeException {

    public NegativeVertexCountException(String errorMessage) {
        super(errorMessage);
    }

}
