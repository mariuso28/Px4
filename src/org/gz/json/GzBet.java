package org.gz.json;

import java.util.Date;

import org.dx4.json.message.Dx4GameTypeJson;

public class GzBet {
	private String memberId;
	private String choice;  
	private Dx4GameTypeJson gameType;  			
	private double totalStake;
	private Date playDate;
	
	public GzBet()
	{
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public Dx4GameTypeJson getGameType() {
		return gameType;
	}

	public void setGameType(Dx4GameTypeJson gameType) {
		this.gameType = gameType;
	}

	public double getTotalStake() {
		return totalStake;
	}

	public void setTotalStake(double totalStake) {
		this.totalStake = totalStake;
	}

	public Date getPlayDate() {
		return playDate;
	}

	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}

	@Override
	public String toString() {
		return "GzBet [memberId=" + memberId + ", choice=" + choice + ", gameType=" + gameType + ", totalStake="
				+ totalStake + ", playDate=" + playDate + "]";
	}
}
