package org.dx4.player;

import java.io.Serializable;
import java.util.List;

import org.dx4.bet.Dx4MetaBet;
import org.dx4.secure.domain.Dx4SecureUser;

public class Dx4Player extends Dx4SecureUser implements Serializable
{
	private static final long serialVersionUID = -4040430351651236900L;
	private List<Dx4MetaBet> currentMetaBets;
	
	public Dx4Player()
	{
		super();
	}

	public Dx4Player(String username)
	{
		 super(username);
	}
	
	public List<Dx4MetaBet> getCurrentMetaBets() {
		return currentMetaBets;
	}

	public void setCurrentMetaBets(List<Dx4MetaBet> currentMetaBets) {
		this.currentMetaBets = currentMetaBets;
	}
}
