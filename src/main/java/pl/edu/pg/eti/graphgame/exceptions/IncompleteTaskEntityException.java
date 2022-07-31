package pl.edu.pg.eti.graphgame.exceptions;

public class IncompleteTaskEntityException extends RuntimeException{

    public IncompleteTaskEntityException(String errorMessage) {
        super(errorMessage);
    }

}
