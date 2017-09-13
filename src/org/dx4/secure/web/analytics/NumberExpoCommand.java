package org.dx4.secure.web.analytics;

import java.io.Serializable;

public class NumberExpoCommand implements Serializable
{
	private static final long serialVersionUID = -2418063883059426684L;
	private String number;
	private String betNumber;
	private String winNumber;
	private double winExpo;
	private double betExpo;
	private String useNumber;
	
	public NumberExpoCommand()
	{
	}

	public double getWinExpo() {
		return winExpo;
	}

	public void setWinExpo(double winExpo) {
		this.winExpo = winExpo;
	}

	public double getBetExpo() {
		return betExpo;
	}

	public void setBetExpo(double betExpo) {
		this.betExpo = betExpo;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public String getBetNumber() {
		return betNumber;
	}

	public void setBetNumber(String betNumber) {
		this.betNumber = betNumber;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getUseNumber() {
		return useNumber;
	}

	public void setUseNumber(String useNumber) {
		this.useNumber = useNumber;
	}

	@Override
	public String toString() {
		return "NumberExpoCommand [number=" + number + ", winExpo=" + winExpo
				+ ", betExpo=" + betExpo + "]";
	}
	
	
}
