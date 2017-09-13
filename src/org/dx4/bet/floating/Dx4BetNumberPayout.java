package org.dx4.bet.floating;

public class Dx4BetNumberPayout {

	private String number;
	private double odds;
	
	public Dx4BetNumberPayout()
	{
	}

	public Dx4BetNumberPayout(String number, double odds) {
		setNumber(number);
		setOdds(odds);
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}
	
}
