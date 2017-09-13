package org.dx4.game;

import java.util.Date;

public class FxForPlayGame {

	private long playGameId;
	private Date playGameDate;
	private String fromCcy;
	private String toCcy;
	private double rate;
	
	public FxForPlayGame()
	{
	}

	public FxForPlayGame(long playGameId,Date playGameDate,String fromCcy,String toCcy,double rate)
	{
		setPlayGameId(playGameId);
		setPlayGameDate(playGameDate);
		setFromCcy(fromCcy);
		setToCcy(toCcy);
		setRate(rate);
	}

	public long getPlayGameId() {
		return playGameId;
	}

	public void setPlayGameId(long playGameId) {
		this.playGameId = playGameId;
	}

	public Date getPlayGameDate() {
		return playGameDate;
	}

	public void setPlayGameDate(Date playGameDate) {
		this.playGameDate = playGameDate;
	}

	public String getFromCcy() {
		return fromCcy;
	}

	public void setFromCcy(String fromCcy) {
		this.fromCcy = fromCcy;
	}

	public String getToCcy() {
		return toCcy;
	}

	public void setToCcy(String toCcy) {
		this.toCcy = toCcy;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
}
