package org.gz.agent;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.gz.baseuser.GzBaseUser;
import org.gz.game.GzPackage;

public class GzAgent extends GzBaseUser implements Serializable
{
	private static final long serialVersionUID = 1832416920445925711L;
	private Map<String,GzBaseUser> players = new TreeMap<String,GzBaseUser>();
	private GzPackage gzPackage;
	
	public GzAgent()
	{
	}

	public void usePackage(GzPackage gzPackage)
	{
		setGzPackage(gzPackage);
		gzPackage.setMember(this);
		gzPackage.setMemberId(this.getMemberId());
	}
	
	public Map<String, GzBaseUser> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, GzBaseUser> players) {
		this.players = players;
	}

	public GzPackage getGzPackage() {
		return gzPackage;
	}

	public void setGzPackage(GzPackage gzPackage) {
		this.gzPackage = gzPackage;
	}

}
