package org.dx4.bet;

import java.io.Serializable;

public class Dx4NumberWin implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3808766590837481251L;
	private String number;
	private Double win;
	private Character providerCode;
	private String place;
	
	public Dx4NumberWin()
	{
	}
	
	public Dx4NumberWin(String number, Double win) {
		super();
		this.number = number;
		this.win = win;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
	}

	public Character getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(Character providerCode) {
		this.providerCode = providerCode;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "Dx4NumberWin [number=" + number + ", win=" + win
				+ ", providerCode=" + providerCode + ", place=" + place + "]";
	}
	
	
	
}
