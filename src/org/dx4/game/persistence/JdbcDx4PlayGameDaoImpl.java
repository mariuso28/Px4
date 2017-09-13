package org.dx4.game.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.FxForPlayGame;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.services.Dx4Config;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class JdbcDx4PlayGameDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4PlayGameDao {

	private static final Logger log = Logger.getLogger(JdbcDx4PlayGameDaoImpl.class);
	
	@Override
	public void insert(Dx4PlayGame playGame,Dx4MetaGame metaGame) {
		
		Dx4PlayGame exists = getPlayGameByPlayDate( metaGame, playGame.getPlayDate());
		if (exists!=null)
		{
			String msg = "Attempted insert for play game with existing playdate:" + playGame.getPlayDate() +
			" for Game : " + metaGame.getName();
			throw new PersistenceRuntimeException("",msg); 
		}
		
		Timestamp playDate = new Timestamp(playGame.getPlayDate().getTime());
		String sql = "INSERT INTO PLAYGAME ( METAGAMEID, PLAYDATE, PLAYED, PROVIDERCODES ) VALUES ( '" 
			+ metaGame.getId()+"','"
			+ playDate.toString()+ "', 0, '"
			+ playGame.getProviderCodes() + "')"; 
		log.info(sql);
		getJdbcTemplate().update(sql);
		playGame.setId(getLastPlayGameId());
		setPlayGameTime(playGame);
	}

	private long getLastPlayGameId() {
		String sql = "SELECT MAX(ID) FROM PLAYGAME";
		return getJdbcTemplate().queryForObject(sql,Long.class);
	}
	
	@Override
	public void update(Dx4PlayGame playGame) {
		
		log.info("JdbcDx4PlayGameDaoImpl:update : " + playGame);
		String sql;
		if (playGame.isPlayed())
		{
			Timestamp playedAt = new Timestamp(playGame.getPlayedAt().getTime());
			sql = "UPDATE PLAYGAME SET PLAYED = 1, PLAYEDAT = '" + playedAt.toString()
				+ "' WHERE ID="+ playGame.getId();
		}
		else
			sql = "UPDATE PLAYGAME SET PLAYED = 0, PLAYEDAT = NULL " 
						+ " WHERE ID="+ playGame.getId();
		
		log.info(sql);
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public List<Dx4PlayGame> getPlayGames(Dx4MetaGame metaGame) throws DataAccessException
	{
		GregorianCalendar gc = new GregorianCalendar();
		/*
		if (gc.get(Calendar.MONTH)==0)								// default to previous month's games ??
			gc.set(gc.get(Calendar.YEAR)-1,11,gc.get(Calendar.DAY_OF_MONTH));
		else
		gc.roll(Calendar.MONTH, false );
		*/
		
		gc.set(2013, 0, 1);
		Timestamp d1 = new Timestamp(gc.getTime().getTime());
		String sql = "SELECT * FROM PLAYGAME WHERE METAGAMEID="+ metaGame.getId() + 
		" AND PLAYDATE>='" + d1.toString() + "' ORDER BY PLAYDATE";
		List<Dx4PlayGame> playGames = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4PlayGame.class));
		
		for (Dx4PlayGame pg : playGames)
			setPlayGameTime(pg);
		
		metaGame.setPlayGames(playGames);
		return playGames;
	}
	
	private void setPlayGameTime(Dx4PlayGame pg) {
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(pg.getPlayDate());
		Date drawTime = Dx4Config.getDrawTime();
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.setTime(drawTime);
		
		gc.set(Calendar.HOUR_OF_DAY,gc1.get(Calendar.HOUR_OF_DAY));
		gc.set(Calendar.MINUTE,gc1.get(Calendar.MINUTE));
		gc.set(Calendar.SECOND,gc1.get(Calendar.SECOND));
		pg.setPlayDate(gc.getTime());
	}

	@Override
	public Dx4PlayGame getPlayGameById(long playGameId) {
		String sql = "SELECT * FROM PLAYGAME WHERE ID = " + playGameId;
	//	log.info(sql);
		try
		{
			Dx4PlayGame playGame = (Dx4PlayGame) getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Dx4PlayGame.class));
			setPlayGameTime(playGame);
			return playGame;
		}
		catch (DataAccessException e)
		{
			return null;
		}
	}
/*
	@Override
	public List<Dx4PlayGame> getUnplayedPlayGames(Dx4MetaGame metaGame) {
		String sql = "SELECT * FROM PLAYGAME WHERE METAGAMEID="+ metaGame.getId() +
		" AND PLAYED=0 ORDER BY PLAYDATE";
		List<Dx4PlayGame> playGames = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4PlayGame.class));
		metaGame.setPlayGames(playGames);
		return playGames;
	}
*/

	private Dx4PlayGame getPlayGameByPlayDate(Dx4MetaGame metaGame,Date playDate) {
		Timestamp pDate = new Timestamp(playDate.getTime());
		String sql = "SELECT * FROM PLAYGAME WHERE PLAYDATE='" + pDate.toString() + "' AND METAGAMEID=" 
			+ metaGame.getId();
		log.info("getPlayGameByPlayDate: " + sql);
		try
		{
			Dx4PlayGame playGame = (Dx4PlayGame) getJdbcTemplate().queryForObject(sql,BeanPropertyRowMapper.newInstance(Dx4PlayGame.class));
			setPlayGameTime(playGame);
			return playGame;
		}
		catch (DataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void storeFxForPlayGame(final FxForPlayGame fxpg) 
	{
		try
		{
			getJdbcTemplate().update("DELETE FROM fxforplaygame WHERE playgameid = " + fxpg.getPlayGameId());
			
			final Timestamp playDate = new Timestamp(fxpg.getPlayGameDate().getTime());
			
			getJdbcTemplate().update("INSERT INTO fxforplaygame (playgameid,playgamedate,fromccy,toccy,rate)"
					+ " VALUES (?,?,?,?,?)",new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,fxpg.getPlayGameId());
					preparedStatement.setTimestamp(2,playDate);
					preparedStatement.setString(3, fxpg.getFromCcy());
					preparedStatement.setString(4, fxpg.getToCcy());
					preparedStatement.setDouble(5, fxpg.getRate());
					}
				});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public FxForPlayGame getFxForPlayGame(long playGameId,String fromCcy,String toCcy) {
		String sql = "SELECT * FROM fxforplaygame WHERE playgameid = ? AND fromccy = ? AND toccy = ?";
		try
		{
			FxForPlayGame fxpg = (FxForPlayGame) getJdbcTemplate().queryForObject(sql,new Object[] { playGameId, fromCcy, toCcy }, 
											BeanPropertyRowMapper.newInstance(FxForPlayGame.class));
			return fxpg;
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return  null;
		}
		catch (DataAccessException e)
		{
			return null;
		}
	}
}
