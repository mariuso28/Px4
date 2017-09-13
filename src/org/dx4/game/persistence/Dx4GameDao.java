package org.dx4.game.persistence;

import java.util.List;

import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;

public interface Dx4GameDao {
	public void store(Dx4MetaGame metaGame, Dx4Game game);
	public List<Dx4Game> getGames(Dx4MetaGame metaGame);
	public void updatePayOuts(Dx4Game game);
	
	// public Dx4Game getGameById(long gameId);
}
