package org.gz.web.summary;

import org.dx4.json.message.Dx4GameTypeJson;

public class GzSummaryEntry {

	private String memberId;
	private Dx4GameTypeJson gameType;
	private double flight;
	private double retain;
	
	public GzSummaryEntry()
	{
	}

	public Dx4GameTypeJson getGameType() {
		return gameType;
	}

	public void setGameType(Dx4GameTypeJson gameType) {
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

