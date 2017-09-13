package org.dx4.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.secure.domain.Dx4SecureUser;

public class Dx4GameGroup implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7454596213077860821L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameGroup.class);
	
	private long id;
	private long userId;
	private Dx4SecureUser user;
	private List<Dx4GameActivator> gameActivators;
	
	public Dx4GameGroup()
	{		
		gameActivators = new ArrayList<Dx4GameActivator>();
		setUserId(-1L);
	}
	
	public Dx4GameGroup(Dx4SecureUser user)
	{
		this();
		setUser(user);
		setUserId(user.getSeqId());
	}

	
	public Dx4GameActivator getGameActivator(Dx4MetaGame metaGame)
	{
		for (Dx4GameActivator gameActivator : gameActivators)
		{
			if (gameActivator.getMetaGame().getId()==metaGame.getId())
			{
				return gameActivator;
			}
		}
		return null;
	}
	
	public boolean hasDigits(int digits)
	{
		for (Dx4GameActivator gameActivator : gameActivators)
		{
			if (gameActivator.getMetaGame().hasDigits(digits))
				return true;
		}
		return false;
	}
	
	public Dx4GameActivator getGameActivator(String metaGameName)
	{
		for (Dx4GameActivator gameActivator : gameActivators)
		{
			if (gameActivator.getMetaGame().getName().equals(metaGameName))
			{
				return gameActivator;
			}
		}
		return null;
	}
	
	public boolean containsMetaGame(Dx4MetaGame metaGame)
	{
		return getGameActivator(metaGame) != null;
	}
	
	public void setGameActivators(List<Dx4GameActivator> gameActivators) {
		this.gameActivators = gameActivators;
	}

	public List<Dx4GameActivator> getGameActivators() {
		return gameActivators;
	}
	
	public Dx4SecureUser getUser() {
		return user;
	}

	public void setUser(Dx4SecureUser user) {
		this.user = user;
	}

	public List<Dx4MetaGame> getDigitsMetaGames(int length) {
		List<Dx4MetaGame> metaGames = new ArrayList<Dx4MetaGame>();
		for (Dx4GameActivator ga : gameActivators)
		{
			if (ga.getMetaGame().hasDigits(length))
				metaGames.add(ga.getMetaGame());
		}
		return metaGames;
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
}
