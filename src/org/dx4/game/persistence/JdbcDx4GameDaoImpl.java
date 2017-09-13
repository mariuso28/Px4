package org.dx4.game.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.payout.Dx4PayOut;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class JdbcDx4GameDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4GameDao {

	private static final Logger log = Logger.getLogger(JdbcDx4GameDaoImpl.class);
	
	@Override
	public void store(Dx4MetaGame metaGame,Dx4Game game) {
		
		String sql = "INSERT INTO GAME ( METAGAMEID, GTYPE, STAKE, MINBET, MAXBET ) VALUES ( " 
			+ metaGame.getId() + ",'" 
			+ game.getGtype().name() + "',"
			+ game.getStake() + "," 
			+ game.getMinBet() + "," 
			+ game.getMaxBet() + " )";
		log.trace(sql);
		getJdbcTemplate().update(sql);
		game.setId(getLastGameId());
		storePayOuts(game);
	}
	
	private long getLastGameId() {
		String sql = "SELECT MAX(ID) FROM GAME";
		return getJdbcTemplate().queryForObject(sql,Long.class);
	}
	
	private void storePayOuts(Dx4Game game)
	{
		String sql = "SELECT MAX(ID) FROM GAME";
		log.trace("sql = "  + sql );
		long gameId = getJdbcTemplate().queryForObject(sql,Long.class);
		for (Dx4PayOut payOut : game.getPayOuts())
		{
			if (payOut.getPayOut()<=0.0)
				continue;
			sql = "INSERT INTO PAYOUT (GAMEID,TYPE,PAYOUT) VALUES (" +  gameId + ",'" + payOut.getType() +
					"'," + payOut.getPayOut() +")";
			log.trace(sql);
			getJdbcTemplate().update(sql);
		}
	}
	
	@Override
	public void updatePayOuts(Dx4Game game)
	{
		String sql = "DELETE FROM PAYOUT WHERE GAMEID = " + game.getId();
		log.trace("sql = "  + sql );
		getJdbcTemplate().update(sql);
		for (Dx4PayOut payOut : game.getPayOuts())
		{
			sql = "INSERT INTO PAYOUT (GAMEID,TYPE,PAYOUT) VALUES (" +  game.getId() + ",'" + payOut.getType() +
					"'," + payOut.getPayOut() +")";
			log.trace(sql);
			getJdbcTemplate().update(sql);
		}
	}
	
	@Override
	public List<Dx4Game> getGames(Dx4MetaGame metaGame) throws DataAccessException{
		 
		String sql = "SELECT * FROM GAME WHERE METAGAMEID = " + metaGame.getId() 
		+ " ORDER BY ID";
		log.trace("sql = "  + sql );
		
		List<Dx4Game> games = getJdbcTemplate().query(sql,new Dx4GameRowMapper());
		for (Dx4Game game : games)
			game.setPayOuts(getPayOuts(game.getId()));
		metaGame.setGames(games);
		return games;
	}
	
	private List<Dx4PayOut> getPayOuts(long gameId) throws DataAccessException
	{
		String sql = "SELECT * FROM PAYOUT WHERE GAMEID = " + gameId + " ORDER BY PAYOUT DESC";
		log.trace("sql = "  + sql );
		
		return (List<Dx4PayOut>) getJdbcTemplate().query(sql,new Dx4PayOutRowMapper());
	}
	/*
	@Override
	public Dx4Game getGameById(long gameId) {
		
		String sql = "SELECT * FROM GAME WHERE ID = " + gameId;
		log.trace("sql = "  + sql );
		
		Dx4Game game = (Dx4Game) getJdbcTemplate().queryForObject(sql,new Dx4GameRowMapper());
		game.setPayOuts(getPayOuts(game.getId()));
		return game;
	}
	*/
}
