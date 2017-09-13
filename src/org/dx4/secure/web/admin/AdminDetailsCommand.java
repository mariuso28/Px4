package org.dx4.secure.web.admin;

import java.io.Serializable;
import java.util.List;

public class AdminDetailsCommand implements Serializable
{
	private static final long serialVersionUID = -7689058406114011051L;
	private List<String> gamename;
	private List<String> drawDate;
	private List<String> cancelUpdateDate;
	private int changedIndex;
	
	public AdminDetailsCommand()
	{
	}

	public List<String> getGamename() {
		return gamename;
	}

	public void setGamename(List<String> gamename) {
		this.gamename = gamename;
	}

	public List<String> getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(List<String> drawDate) {
		this.drawDate = drawDate;
	}

	public List<String> getCancelUpdateDate() {
		return cancelUpdateDate;
	}

	public void setCancelUpdateDate(List<String> cancelUpdateDate) {
		this.cancelUpdateDate = cancelUpdateDate;
	}

	public int getChangedIndex() {
		return changedIndex;
	}

	public void setChangedIndex(int changedIndex) {
		this.changedIndex = changedIndex;
	}

}
