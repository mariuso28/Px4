package org.dx4.bet;

public class Dx4BetRollup 
{
	private int id;
	private String number;
	private double stake;
	private double win;
	private long gameId;
	private char providerCode;
	
	public Dx4BetRollup()
	{
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public char getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(char providerCode) {
		this.providerCode = providerCode;
	}

	public double getWin() {
		return win;
	}

	public void setWin(double win) {
		this.win = win;
	}
	
	

}
