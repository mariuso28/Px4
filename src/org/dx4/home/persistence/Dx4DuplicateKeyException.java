package org.dx4.home.persistence;

public class Dx4DuplicateKeyException extends RuntimeException {
	
	private static final long serialVersionUID = -1288890051435748072L;

	public Dx4DuplicateKeyException(String message) {
		super(message);
	}
}
