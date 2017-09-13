package org.gz.web.summary;

import org.gz.json.GzGameType;

public class GzSummaryEntry {

	private String memberId;
	private GzGameType gameType;
	private double flight;
	private double retain;
	
	public GzSummaryEntry()
	{
	}

	public GzGameType getGameType() {
		return gameType;
	}

	public void setGameType(GzGameType gameType) {
		this.gameType = gameType;
	}

	public double getFlight() {
		return flight;
	}

	public void setFlight(double flight) {
		this.flight = flight;
	}

	public double getRetain() {
		return retain;
	}

	public void setRetain(double retain) {
		this.retain = retain;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}

