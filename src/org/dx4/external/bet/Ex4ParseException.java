package org.dx4.external.bet;

public class Ex4ParseException extends Ex4Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2793419973756229852L;

	public Ex4ParseException(String text,String field,String reason)
	{
		super("Error parsing : " + text + " for : " + field + " reason: " + reason);
	}
	
	public Ex4ParseException(String message) {
		super(message);
	}

}
