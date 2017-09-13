package org.dx4.game.persistence;

import java.util.List;

import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.FxForPlayGame;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.secure.domain.Dx4SecureUser;


public interface Dx4MetaGameDao {
	
	public void store(Dx4MetaGame game) throws PersistenceRuntimeException;
	public List<Dx4MetaGame> getMetaGames();
	public List<Dx4MetaGame> getUnplayedMetaGames();
	public Dx4MetaGame get(String name);
	public Dx4MetaGame get(long id);
	public void insertPlayGame(Dx4PlayGame playGame,Dx4MetaGame metaGame);
	public void updatePlayGame(Dx4PlayGame playGame);
	public Dx4PlayGame getPlayGameById(long playGameId);
	public void storeFxForPlayGame(final FxForPlayGame fxpg);
	public FxForPlayGame getFxForPlayGame(long playGameId,String fromCcy,String toCcy);
	
	/*
	public Dx4PlayGame getPlayGameByPlayDate(Dx4MetaGame metaGame,Date playDate);
	public Dx4Game getGameById(long gameId);
	*/
	public void storeGameGroup(Dx4SecureUser user);
	public Dx4GameGroup getGameGroup(Dx4SecureUser user);
	public void deleteGameGroup(Dx4GameGroup gameGroup);
	public void storeGameGroup(Dx4GameGroup group);
	public void updateProviderImage(String code, byte[] image);
	public void storeMetaGameImage(final long metaGameId,final String name,final byte[] image);
	
	public void updatePayOuts(Dx4Game game);
}
