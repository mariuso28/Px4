package org.dx4.home.persistence;

public class PersistenceRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 3996983417038185066L;

	public PersistenceRuntimeException(String message, String problem ) {
		super(message + " - " + problem);
	}

	public PersistenceRuntimeException(String message) {
		super(message);
	}
}
