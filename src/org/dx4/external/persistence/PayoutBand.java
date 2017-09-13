package org.dx4.external.persistence;

public class PayoutBand 
{
	private int upper;
	private int lower;
	private double odds;
	
	public PayoutBand()
	{
	}

	public int getUpper() {
		return upper;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}

	@Override
	public String toString() {
		return "PayoutBand [upper=" + upper + ", lower=" + lower + ", odds=" + odds + "]";
	}

}
