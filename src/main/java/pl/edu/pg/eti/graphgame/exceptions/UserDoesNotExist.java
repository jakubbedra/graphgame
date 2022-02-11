package pl.edu.pg.eti.graphgame.exceptions;

public class UserDoesNotExist extends Exception {

    public UserDoesNotExist(Long id) {
        super("User with the id: " + id + " does not exist.");
    }

}
