package org.dx4.bet.persistence;


public class NumberExpo
{
	private String number;
	private double expo;
	private double tbet;
	
	public NumberExpo()
	{
	}
	
	public NumberExpo(String number,double expo,double tbet)
	{
		setNumber(number);
		setExpo(expo);
		setTbet(tbet);
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public double getExpo() {
		return expo;
	}
	public void setExpo(double expo) {
		this.expo = expo;
	}

	public void setTbet(double tbet) {
		this.tbet = tbet;
	}

	public double getTbet() {
		return tbet;
	}

	@Override
	public String toString() {
		return "NumberExpo [number=" + number + ", expo=" + expo + ", tbet="
				+ tbet + "]";
	}
	
}
