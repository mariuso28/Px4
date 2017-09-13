package org.gz.agent;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.gz.baseuser.GzBaseUser;

public class GzAgent extends GzBaseUser implements Serializable
{
	private static final long serialVersionUID = 1832416920445925711L;
	private Map<String,GzBaseUser> players = new TreeMap<String,GzBaseUser>();
	
	public GzAgent()
	{
	}

	public Map<String, GzBaseUser> getPlayers() {
		return players;
	}


	public void setPlayers(Map<String, GzBaseUser> players) {
		this.players = players;
	}

}
