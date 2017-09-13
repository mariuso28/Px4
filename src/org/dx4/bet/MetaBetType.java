package org.dx4.bet;

public enum MetaBetType 
{
	AGENT('A'),HEDGE('H');

	private char code;
	
	MetaBetType(char code)
	{
		setCode(code);
	}

	public void setCode(char code) {
		this.code = code;
	}

	public char getCode() {
		return code;
	}
}
