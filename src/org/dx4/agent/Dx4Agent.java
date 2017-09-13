package org.dx4.agent;

import java.util.List;

import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4SecureUser;

public class Dx4Agent extends Dx4SecureUser 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1673461587686030024L;
	private List<Dx4Player> players;
	
	public Dx4Agent()
	{
	}
	
	public List<Dx4Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Dx4Player> players) {
		this.players = players;
	}
	
	
}
