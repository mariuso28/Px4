package org.gz.json;

import java.util.Date;

import org.dx4.json.message.Dx4GameTypeJson;

public class GzBetRollup {
	private Dx4GameTypeJson gameType;
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

	public Dx4GameTypeJson getGameType() {
		return gameType;
	}

	public void setGameType(Dx4GameTypeJson gameType) {
		this.gameType = gameType;
	}
	
	
}
