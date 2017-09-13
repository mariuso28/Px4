package org.dx4.bet;

import java.io.Serializable;

public class Dx4Win implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3007630453095894234L;
	private long id;
	private long metaBetId;
	private Dx4MetaBet metaBet;
	private long betId;
	private Dx4Bet bet;
	private String result;
	private String place;
	private double win;
	private char providerCode;
	
	public Dx4Win()
	{
	}
	
	public Dx4Win(Dx4MetaBet metaBet, Dx4Bet bet, String result, String place,
			 double win,char providerCode) {
		super();
		this.metaBet = metaBet;
		this.bet = bet;
		
		this.win = win;
		setPlace(place);
		setResult(result);
		setProviderCode(providerCode);
	}

	public long getMetaBetId() {
		return metaBetId;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setMetaBetId(long metaBetId) {
		this.metaBetId = metaBetId;
	}
	public Dx4MetaBet getMetaBet() {
		return metaBet;
	}
	public void setMetaBet(Dx4MetaBet metaBet) {
		this.metaBet = metaBet;
	}
	public long getBetId() {
		return betId;
	}
	public void setBetId(long betId) {
		this.betId = betId;
	}
	public Dx4Bet getBet() {
		return bet;
	}
	public void setBet(Dx4Bet bet) {
		this.bet = bet;
	}
	
	public double getWin() {
		return win;
	}
	public void setWin(double win) {
		this.win = win;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public char getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(char providerCode) {
		this.providerCode = providerCode;
	}

	
	
	
}
