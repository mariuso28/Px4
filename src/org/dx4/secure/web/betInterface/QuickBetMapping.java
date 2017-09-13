package org.dx4.secure.web.betInterface;

public class QuickBetMapping 
{
	private double stake;
	private long gameId;
	private String use;
	private double qstake;
	
	public QuickBetMapping()
	{
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public double getStake() {
		return stake;
	}

	public void setQstake(double qstake) {
		this.qstake = qstake;
	}

	public double getQstake() {
		return qstake;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public long getGameId() {
		return gameId;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getUse() {
		return use;
	}

	@Override
	public String toString() {
		return "QuickBetMapping [stake=" + stake + ", gameId=" + gameId
				+ ", use=" + use + "]";
	}
	
}
