package org.dx4.secure.web.agent;


public class AgentWinCommand 
{
	private WinPlaceHolder winPlaceHolder;
	private String userPlace;
	private String subUserPlace;
	private String drawDateStr;

	public AgentWinCommand()
	{
	}
	
	public void setWinPlaceHolder(WinPlaceHolder winPlaceHolder) {
		this.winPlaceHolder = winPlaceHolder;
	}

	public WinPlaceHolder getWinPlaceHolder() {
		return winPlaceHolder;
	}

	public void setSubUserPlace(String subUserPlace) {
		this.subUserPlace = subUserPlace;
	}

	public String getSubUserPlace() {
		return subUserPlace;
	}

	public void setUserPlace(String userPlace) {
		this.userPlace = userPlace;
	}

	public String getUserPlace() {
		return userPlace;
	}

	public String getDrawDateStr() {
		return drawDateStr;
	}

	public void setDrawDateStr(String drawDateStr) {
		this.drawDateStr = drawDateStr;
	}

	
}
