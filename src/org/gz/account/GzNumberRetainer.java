package org.gz.account;

import org.dx4.json.message.Dx4GameTypeJson;

public class GzNumberRetainer {

	private long id;
	private Dx4GameTypeJson gameType;
	private boolean defaultNumber;
	private String number;
	private String memberId;
	private double retain;
	
	public GzNumberRetainer()
	{
		
	}

	public GzNumberRetainer( String memberId,Dx4GameTypeJson gameType,String number,boolean defaultNumber,double retain)
	{
		setMemberId(memberId);
		setGameType(gameType);
		setNumber(number);
		setDefaultNumber(defaultNumber);
		setRetain(retain);
	}
	
	public Dx4GameTypeJson getGameType() {
		return gameType;
	}

	public void setGameType(Dx4GameTypeJson gameType) {
		this.gameType = gameType;
	}

	public boolean isDefaultNumber() {
		return defaultNumber;
	}

	public void setDefaultNumber(boolean defaultNumber) {
		this.defaultNumber = defaultNumber;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public double getRetain() {
		return retain;
	}

	public void setRetain(double retain) {
		this.retain = retain;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "GzNumberRetainer [id=" + id + ", gameType=" + gameType + ", defaultNumber=" + defaultNumber
				+ ", number=" + number + ", memberId=" + memberId + ", retain=" + retain + "]";
	}
	
	
}
