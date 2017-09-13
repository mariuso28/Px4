package org.dx4.external.bet;

public class Ex4MetaBetCreateException extends Ex4Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8331030958990453157L;

	public Ex4MetaBetCreateException(String source,String message) {
		super("MetaBet cannot be created from : " + source + " - " + message);
	}
}
