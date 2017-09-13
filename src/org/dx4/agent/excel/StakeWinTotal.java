package org.dx4.agent.excel;

import java.util.Date;

public class StakeWinTotal extends StakeWin{


	private Date playDate;
	
	public StakeWinTotal(Date playDate,double stake,double win)
	{
		setStake(stake);
		setWin(win);
	}

	public StakeWinTotal() {
	}

	public Date getPlayDate() {
		return playDate;
	}

	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}


}
