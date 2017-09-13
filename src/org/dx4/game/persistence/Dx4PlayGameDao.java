package org.dx4.game.persistence;

import java.util.List;

import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.FxForPlayGame;

public interface Dx4PlayGameDao 
{
	public void insert(Dx4PlayGame playGame,Dx4MetaGame metaGame);
	public void update(Dx4PlayGame playGame);
	public List<Dx4PlayGame> getPlayGames(Dx4MetaGame metaGame);
	public Dx4PlayGame getPlayGameById(long playGameId);
	public void storeFxForPlayGame(final FxForPlayGame fxpg);
	public FxForPlayGame getFxForPlayGame(long playGameId,String fromCcy,String toCcy);
	
/*	public List<Dx4PlayGame> getUnplayedPlayGames(Dx4MetaGame metaGame);
	public Dx4PlayGame getPlayGameByPlayDate(Dx4MetaGame metaGame,Date playDate);
	*/
}
