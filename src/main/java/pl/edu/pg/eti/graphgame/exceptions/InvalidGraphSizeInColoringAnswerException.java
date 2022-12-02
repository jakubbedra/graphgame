package pl.edu.pg.eti.graphgame.exceptions;

public class InvalidGraphSizeInColoringAnswerException extends RuntimeException{

    public InvalidGraphSizeInColoringAnswerException(String errorMessage) {
        super(errorMessage);
    }

}
