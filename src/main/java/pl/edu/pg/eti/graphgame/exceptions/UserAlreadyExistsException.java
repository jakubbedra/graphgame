package pl.edu.pg.eti.graphgame.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

}
