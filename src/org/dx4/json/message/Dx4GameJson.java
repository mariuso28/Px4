package org.dx4.json.message;

import java.util.List;

public class Dx4GameJson {
	
	private long id;
	private Dx4GameTypeJson type;
	private double stake;
	private double minBet;
	private double maxBet;
	private List<Dx4PayOutJson> payOuts;
	
	public Dx4GameJson()
	{
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public double getMinBet() {
		return minBet;
	}

	public void setMinBet(double minBet) {
		this.minBet = minBet;
	}

	public double getMaxBet() {
		return maxBet;
	}

	public void setMaxBet(double maxBet) {
		this.maxBet = maxBet;
	}

	public List<Dx4PayOutJson> getPayOuts() {
		return payOuts;
	}

	public void setPayOuts(List<Dx4PayOutJson> payOuts) {
		this.payOuts = payOuts;
	}

	public Dx4GameTypeJson getType() {
		return type;
	}

	public void setType(Dx4GameTypeJson type) {
		this.type = type;
	}

	
}
