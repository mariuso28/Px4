package org.gz.home.persistence;

public class GzPersistenceRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 3996983417038185066L;

	public GzPersistenceRuntimeException(String message, String problem ) {
		super(message + " - " + problem);
	}

	public GzPersistenceRuntimeException(String message) {
		super(message);
	}
}
