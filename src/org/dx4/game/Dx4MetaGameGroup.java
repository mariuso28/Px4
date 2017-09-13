package org.dx4.game;

import java.util.ArrayList;
import java.util.List;

public class Dx4MetaGameGroup 
{
	private List<Dx4MetaGame> metaGames;

	public Dx4MetaGameGroup()
	{
		metaGames = new ArrayList<Dx4MetaGame>();
	}

	public void setMetaGames(List<Dx4MetaGame> metaGames) {
		this.metaGames = metaGames;
	}

	public List<Dx4MetaGame> getMetaGames() {
		return metaGames;
	}
	
	
}
