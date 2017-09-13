package org.dx4.secure.web.agent;

import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.secure.domain.Dx4SecureUser;

public class AgentGamesForm {
	private Dx4GameGroup parentGameGroup;
	private Dx4GameGroup gameGroup;

	private AgentGamesForm(Dx4GameGroup parentGameGroup,Dx4GameGroup gameGroup) {
		setParentGameGroup(parentGameGroup);
		setGameGroup(gameGroup);
		filterParentGroup();
	}
	
	public AgentGamesForm(Dx4SecureUser parent,Dx4SecureUser user) {
		this(parent.getGameGroup(),user.getGameGroup());
	}
	
	
	private void filterParentGroup()
	{
		if (gameGroup==null)
			return;
		for (Dx4GameActivator gameActivator : gameGroup.getGameActivators())
		{
			Dx4GameActivator ga = parentGameGroup.getGameActivator(gameActivator.getMetaGame());
			if (ga==null)
				continue;
			parentGameGroup.getGameActivators().remove(ga);
		}
	}
	
	public Dx4GameGroup getParentGameGroup() {
		return parentGameGroup;
	}

	public void setParentGameGroup(Dx4GameGroup parentGameGroup) {
		this.parentGameGroup = parentGameGroup;
	}

	public Dx4GameGroup getGameGroup() {
		return gameGroup;
	}

	public void setGameGroup(Dx4GameGroup gameGroup) {
		this.gameGroup = gameGroup;
	}
}
