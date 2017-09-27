package org.gz.web.gzpackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gz.game.GzGameTypePayoutsEntry;

public class GzPackageCommand implements Serializable{

	private static final long serialVersionUID = 7279602431428718044L;
	private String gname;
	private String pname;
	private List<GzGameTypePayoutsEntry> gameTypePayoutsEntry = new ArrayList<GzGameTypePayoutsEntry>();
	private String newPackageName;
	private String newGroupName;
	
	public GzPackageCommand()
	{
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public List<GzGameTypePayoutsEntry> getGameTypePayoutsEntry() {
		return gameTypePayoutsEntry;
	}

	public void setGameTypePayoutsEntry(List<GzGameTypePayoutsEntry> gameTypePayoutsEntry) {
		this.gameTypePayoutsEntry = gameTypePayoutsEntry;
	}

	public String getNewPackageName() {
		return newPackageName;
	}

	public void setNewPackageName(String newPackageName) {
		this.newPackageName = newPackageName;
	}

	public String getNewGroupName() {
		return newGroupName;
	}

	public void setNewGroupName(String newGroupName) {
		this.newGroupName = newGroupName;
	}
	

}
