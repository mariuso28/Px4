package org.gz.json;

import java.util.Date;

public class GzBetRollup {
	private GzGameType gameType;
	private double stake;
	private String memberId;
	private Date date;
	
	public GzBetRollup()
	{
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public GzGameType getGameType() {
		return gameType;
	}

	public void setGameType(GzGameType gameType) {
		this.gameType = gameType;
	}
	
	
}
