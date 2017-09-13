package org.dx4.agent.excel;

public class StakeWin {

	private double stake;
	private double win;
	
	public StakeWin(double stake,double win)
	{
		setStake(stake);
		setWin(win);
	}

	public StakeWin() {
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public double getWin() {
		return win;
	}

	public void setWin(double win) {
		this.win = win;
	}
	
	
}
