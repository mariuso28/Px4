package org.dx4.agent.excel;

import java.util.Map;
import java.util.TreeMap;

public class TotalMap {

	private String gameName;
	private Map<String,StakeWin> totals;
	
	public TotalMap(String gameName)
	{
		setGameName(gameName);
		totals = new TreeMap<String,StakeWin>();
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Map<String, StakeWin> getTotals() {
		return totals;
	}

	public void setTotals(Map<String, StakeWin> totals) {
		this.totals = totals;
	}
	
	
}
