package pl.edu.pg.eti.graphgame.exceptions;

public class UsernameAlreadyInUseException extends Exception {
	public UsernameAlreadyInUseException(String errorMessage) {
		super(errorMessage);
	}
}
