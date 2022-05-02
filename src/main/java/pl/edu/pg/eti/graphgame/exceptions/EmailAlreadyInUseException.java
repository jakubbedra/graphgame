package pl.edu.pg.eti.graphgame.exceptions;

public class EmailAlreadyInUseException extends Exception {
	public EmailAlreadyInUseException(String errorMessage) {
		super(errorMessage);
	}
}
