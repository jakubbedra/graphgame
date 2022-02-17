package pl.edu.pg.eti.graphgame.exceptions;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException(Long id) {
        super("User with the id: " + id + " does not exist.");
    }

}
