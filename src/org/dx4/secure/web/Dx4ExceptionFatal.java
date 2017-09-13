package org.dx4.secure.web;

public class Dx4ExceptionFatal extends RuntimeException {
	private static final long serialVersionUID = 7531577056438877724L;
	
	public Dx4ExceptionFatal(String message)
	{
		super("Dx4 Fatal - " + message);
	}

}
