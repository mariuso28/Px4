package org.dx4.bet.persistence;

public class WinNumber 
{
	private String number;
	private double win;
	private long gameId;
	
	public WinNumber()
	{
	}

	public static String createKey(String number,long gameId)
	{
		return number + ":" + Long.toString(gameId);
	}
	
	public String createKey()
	{
		return createKey(number,gameId);
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getWin() {
		return win;
	}

	public void setWin(double win) {
		this.win = win;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

}

