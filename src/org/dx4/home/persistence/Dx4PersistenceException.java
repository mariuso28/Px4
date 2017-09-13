package org.dx4.home.persistence;

public class Dx4PersistenceException extends Exception {

	private static final long serialVersionUID = 1L;

	public Dx4PersistenceException(String message, String problem ) {
		super(message + " - " + problem);
	}
	
	public Dx4PersistenceException(String message) {
		super(message);
	}

}
