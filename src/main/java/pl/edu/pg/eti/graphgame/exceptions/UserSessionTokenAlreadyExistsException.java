package pl.edu.pg.eti.graphgame.exceptions;

public class UserSessionTokenAlreadyExistsException extends Exception {

    public UserSessionTokenAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

}
