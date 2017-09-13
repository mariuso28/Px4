package org.dx4.json.message;

import java.util.Date;

public class Dx4BetSummaryJson {

	private long metaBetId;
	private String metaGameName;
	private Date placed;
	private Date played;
	private Date playDate;
	private double totalStake;
	private double totalWinStake;
	private double totalWin;
	
	public Dx4BetSummaryJson()
	{
	}

	public long getMetaBetId() {
		return metaBetId;
	}

	public void setMetaBetId(long metaBetId) {
		this.metaBetId = metaBetId;
	}

	public String getMetaGameName() {
		return metaGameName;
	}

	public void setMetaGameName(String metaGameName) {
		this.metaGameName = metaGameName;
	}

	public Date getPlaced() {
		return placed;
	}

	public void setPlaced(Date placed) {
		this.placed = placed;
	}

	public Date getPlayed() {
		return played;
	}

	public void setPlayed(Date played) {
		this.played = played;
	}

	public double getTotalStake() {
		return totalStake;
	}

	public void setTotalStake(double totalStake) {
		this.totalStake = totalStake;
	}

	public double getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(double totalWin) {
		this.totalWin = totalWin;
	}

	public double getTotalWinStake() {
		return totalWinStake;
	}

	public void setTotalWinStake(double totalWinStake) {
		this.totalWinStake = totalWinStake;
	}

	public Date getPlayDate() {
		return playDate;
	}

	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}

}
