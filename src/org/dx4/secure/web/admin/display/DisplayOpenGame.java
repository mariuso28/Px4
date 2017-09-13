package org.dx4.secure.web.admin.display;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.util.DateUtil;

public class DisplayOpenGame {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(DisplayOpenGame.class);
	private Dx4MetaGame metaGame;
	private Dx4PlayGame nextPlayGame;
	private List<Date> comingDraws;
	
	public DisplayOpenGame(Dx4MetaGame metaGame)
	{
		setMetaGame(metaGame);
		comingDraws = new ArrayList<Date>();
		nextPlayGame = null;
		@SuppressWarnings("unused")
		Date now = DateUtil.getNowWithZeroedTime();
		for (Dx4PlayGame game : metaGame.getPlayGames() )
		{
			if (!game.isPlayed())
			{
			/*	if (game.getPlayDate().before(now))
				{
					log.warn("Unplayed game in system earlier than today - ignored");
					continue;
				}
				*/
				comingDraws.add(game.getPlayDate());
				if (nextPlayGame==null)
					nextPlayGame = game;
			}
		}
			
	}

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}

	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}

	public Dx4PlayGame getNextPlayGame() {
		return nextPlayGame;
	}

	public void setNextPlayGame(Dx4PlayGame nextPlayGame) {
		this.nextPlayGame = nextPlayGame;
	}

	public List<Date> getComingDraws() {
		return comingDraws;
	}

	public void setComingDraws(List<Date> comingDraws) {
		this.comingDraws = comingDraws;
	}

	
}
