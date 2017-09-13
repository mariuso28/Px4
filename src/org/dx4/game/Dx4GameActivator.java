package org.dx4.game;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class Dx4GameActivator implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1768015390173219816L;

	private static final Logger log = Logger.getLogger(Dx4GameActivator.class);
	
	private long id;
	private Dx4GameGroup gameGroup;
	private long metaGameId;
	private Dx4MetaGame metaGame;
	private Date startDate;
	private Date endDate;
	
	public Dx4GameActivator()
	{
	}
	
	public Dx4GameActivator(Dx4MetaGame metaGame,Dx4GameGroup gameGroup)
	{
		this();
		setMetaGame(metaGame);
		setGameGroup(gameGroup);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			setStartDate(df.parse("01/03/2017"));
			setEndDate(df.parse("01/01/2043"));
		}
		catch (ParseException e)
		{
			log.error("Parse Error createing Dx4GameActivator - " + e.getMessage());
		}
	}
	
	public Dx4GameActivator(Dx4GameActivator parentGameActivator) {
		this(parentGameActivator.getMetaGame(),parentGameActivator.getGameGroup());
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}
	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setMetaGameId(long metaGameId) {
		this.metaGameId = metaGameId;
	}

	public long getMetaGameId() {
		return metaGameId;
	}

	public void setGameGroup(Dx4GameGroup gameGroup) {
		this.gameGroup = gameGroup;
	}

	public Dx4GameGroup getGameGroup() {
		return gameGroup;
	}

	
}
