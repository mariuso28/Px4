package org.dx4.json.message;

public class NumberPrizeJson {

	private String number;
	private double prize;
	private boolean up;
	
	public NumberPrizeJson()
	{
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getPrize() {
		return prize;
	}

	public void setPrize(double prize) {
		this.prize = prize;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}
	
}
