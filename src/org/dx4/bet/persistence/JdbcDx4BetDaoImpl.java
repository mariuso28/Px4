package org.dx4.bet.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4DateWin;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4NumberWin;
import org.dx4.bet.Dx4Win;
import org.dx4.bet.Dx4WinNumberSummary;
import org.dx4.bet.floating.Dx4BetNumberPayout;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.persistence.JdbcDx4SecureUserDaoImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class JdbcDx4BetDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4BetDao {

	private static final Logger log = Logger.getLogger(JdbcDx4BetDaoImpl.class);
	
	@Override
	public void insertBets(Dx4MetaBet metaBet) 
	{
		String sql = "DELETE FROM BET WHERE METABETID = " + metaBet.getId();
		getJdbcTemplate().update(sql);
		
		for (Dx4Bet bet : metaBet.getBets())
			insertBet(bet,metaBet);
	}
	
	private void insertBet(final Dx4Bet bet,final Dx4MetaBet metaBet) 
	{
		bet.calcTotalStake();						// make sure set
		try
		{
			getJdbcTemplate().update("INSERT INTO bet (metabetid,gameid,stake,totalstake,choice,providercodes,odds)"
					+ " VALUES (?,?,?,?,?,?,?)",new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,metaBet.getId());
					preparedStatement.setLong(2,bet.getGame().getId());
					preparedStatement.setDouble(3, bet.getStake());
					preparedStatement.setDouble(4, bet.getTotalStake());
					preparedStatement.setString(5, bet.getChoice());
					preparedStatement.setString(6, bet.getProviderCodes());
					preparedStatement.setDouble(7, bet.getOdds());
					}
				});
			bet.setId(getLastBetId());
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	private long getLastBetId() {
		String sql = "SELECT MAX(ID) FROM BET";
		return getJdbcTemplate().queryForObject(sql,Long.class);
	}
	
	@Override
	public void updateBets(Dx4MetaBet metaBet) 
	{
		for (Dx4Win win : metaBet.getWins())
			updateWin(win);
	}

	@Override
	public List<Dx4Win> getWinsForMetaBet(final Dx4MetaBet metaBet)
	{
		String sql = "SELECT * FROM WIN WHERE METABETID = ? ORDER BY ID DESC";
		try
		{
			List<Dx4Win> wins = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,metaBet.getId());
				}
			}, BeanPropertyRowMapper.newInstance(Dx4Win.class));
			
			for (Dx4Win win : wins)
			{
				win.setMetaBet(metaBet);
				win.setBet(metaBet.getBetById(win.getBetId()));
			}
			return wins;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	
	private void updateWin(Dx4Win win) 
	{
		String sql = "INSERT INTO WIN (METABETID, BETID, RESULT, PLACE, WIN, PROVIDERCODE) VALUES (" +
				win.getMetaBet().getId() +"," + win.getBet().getId()+",'"+ win.getResult() 
				+"','"+ win.getPlace() + "'," + win.getWin()+",'"+ win.getProviderCode()+"')";
			
		log.info(sql);
		getJdbcTemplate().update(sql);
	}

	@Override
	public List<Dx4Bet> getBetsForMetaBet(final Dx4MetaBet metaBet)
	{
		String sql = "SELECT * FROM BET WHERE METABETID = ? ORDER BY ID DESC";
		try
		{
			List<Dx4Bet> bets = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,metaBet.getId());
				}
			}, new Dx4BetRowMapper());
			
			return bets;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}	
	
	private String getPlayerIdSql(Dx4SecureUser user)
	{
		if (user.getRole().isAgentRole())	
			return "(SELECT ID FROM METABET WHERE PLAYERID IN ("
				+ JdbcDx4SecureUserDaoImpl.buildGetPlayerIdsForUserSQL(user)
				+
				"))";
		return "(SELECT ID FROM METABET WHERE PLAYERID=" + user.getId()+")";
	}
	
	@Override
	public List<Dx4NumberWin> getWinsForDate(Dx4SecureUser user,Date drawDate)
	{
		Timestamp playDate = new Timestamp(drawDate.getTime());

		String sql =
			"SELECT RESULT,PROVIDERCODE,PLACE,SUM(WIN) AS TOT FROM WIN WHERE METABETID IN " +
			"(SELECT ID FROM METABET WHERE PLAYGAMEID IN " +
			"(SELECT ID FROM PLAYGAME WHERE PLAYEDAT = '" + playDate.toString() + "'))" +
			"AND METABETID IN " + 
			getPlayerIdSql(user) + 
			" GROUP BY RESULT,PROVIDERCODE,PLACE ORDER BY TOT DESC";

		List<Dx4NumberWin> wins = getJdbcTemplate().query(sql,
				new Dx4NumberWinRowMapper());

		log.info("getWinsForDate : " + sql );
		log.info("#wins : " + wins);
		return wins;
	}
	
	@Override
	public Double getTotalWinsForDate(Dx4SecureUser user,Date drawDate)
	{
		Timestamp playDate = new Timestamp(drawDate.getTime());

		String sql =
			"SELECT SUM(WIN) AS TOT FROM WIN WHERE METABETID IN " +
			"(SELECT ID FROM METABET WHERE PLAYGAMEID IN " +
			"(SELECT ID FROM PLAYGAME WHERE PLAYEDAT = '" + playDate.toString() + "'))" +
			"AND METABETID IN " + 
			getPlayerIdSql(user);

		Double win = getJdbcTemplate().queryForObject(sql,
				Double.class);

		log.trace("getTotalWinsForDate : " + sql );
		log.trace("#wins : " + win);
		
		return win;
	}
	
	@Override
	public List<Dx4DateWin> getWinDates(Dx4SecureUser user)
	{
		String sql = "SELECT SUM(TOTALWIN) AS WIN,PLAYEDAT AS DATE FROM METABET,PLAYGAME " +
			"WHERE PLAYGAME.ID = METABET.PLAYGAMEID AND METABET.OUTSTANDING < 1 AND PLAYERID IN ( "
			+ JdbcDx4SecureUserDaoImpl.buildGetPlayerIdsForUserSQL(user)
			+
			") GROUP BY PLAYEDAT";

		log.trace("getWinDates " + sql);
		try
		{
			List<Dx4DateWin> wins = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Dx4DateWin.class));
			return wins;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new PersistenceRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public List<Dx4Win> getAgentWinsForDate(Dx4SecureUser user,Date drawDate)
	{
		Timestamp playDate = new Timestamp(drawDate.getTime());

		String sql =
			"SELECT * FROM WIN WHERE METABETID IN " +
			"(SELECT ID FROM METABET WHERE PLAYGAMEID IN " +
			"(SELECT ID FROM PLAYGAME WHERE PLAYEDAT = '" + playDate.toString() + "')) " +
			"AND METABETID IN " + 
			getPlayerIdSql(user);

		List<Dx4Win> wins = getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(Dx4Win.class));
		
		
		return wins;
	}
	
	@Override
	public List<Dx4WinNumberSummary> getWinNumberSummary(String number,Character providerCode,Date drawDate)
	{
		Timestamp playDate = new Timestamp(drawDate.getTime());
		
		String sql = "SELECT WIN,STAKE,(SELECT GTYPE FROM GAME WHERE ID = GAMEID) FROM ("+
				" SELECT WIN,STAKE,GAMEID FROM ( " +
				" SELECT SUM(WIN) AS WIN,SUM(STAKE) AS STAKE,GAMEID FROM BET,WIN WHERE BET.ID = WIN.BETID AND WIN.ID IN " +
				" (SELECT ID FROM WIN WHERE WIN.RESULT = '" + number + "' AND PROVIDERCODE ='"+providerCode+"' AND " +
				" WIN.METABETID IN ( " +
				" SELECT ID FROM METABET WHERE PLAYGAMEID IN " +
				" (SELECT ID FROM PLAYGAME WHERE PLAYEDAT = '" + playDate.toString() + "'))) " +
				" GROUP BY GAMEID ))";
		
		log.info("getWinNumberSummary " + sql);
				 
		List<Dx4WinNumberSummary> summary = getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(Dx4WinNumberSummary.class));
				 
			return summary;
	}

	@Override
	public Dx4Bet getBetById(final long betId) {
		String sql = "SELECT * FROM BET WHERE ID = ?";
		try
		{
			List<Dx4Bet> bets = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,betId);
				}
			}, new Dx4BetRowMapper());
			
			return bets.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public List<WinNumber> getWinNumber(final Dx4ProviderJson provider,final long playGameId)
	{
		if (provider == null)
			return getWinNumber(playGameId);
					
		String sql = "select sum(w.win) as win,b.choice as number,b.gameid as gameid from win as w " +
				"join bet as b on b.id = w.betid " +
				"join metabet as mb on mb.id = w.metabetid " +
				"join playgame as pg on pg.id = mb.playgameid " +
				"where w.providercode = ? and pg.id = ? group by b.choice,b.gameid";
		try
		{
			List<WinNumber> wn = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1,Character.toString(provider.getCode()));
					preparedStatement.setLong(2,playGameId);
				}
			},BeanPropertyRowMapper.newInstance(WinNumber.class));
			
			return wn;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	private List<WinNumber> getWinNumber(final long playGameId)
	{
		String sql = "select sum(w.win) as win,b.choice as number,b.gameid as gameid from win as w " +
				"join bet as b on b.id = w.betid " +
				"join metabet as mb on mb.id = w.metabetid " +
				"join playgame as pg on pg.id = mb.playgameid " +
				"where pg.id = ? group by b.choice,b.gameid";
		try
		{
			List<WinNumber> wn = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,playGameId);
				}
			},BeanPropertyRowMapper.newInstance(WinNumber.class));
			
			return wn;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public List<Dx4BetNumberPayout> getBetNumberPayouts(final Dx4Player player)
	{
		String sql = "SELECT * FROM betnumberpayout WHERE playerseqid = ?";
		try
		{
			List<Dx4BetNumberPayout> bnps = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,player.getSeqId());
				}
			},BeanPropertyRowMapper.newInstance(Dx4BetNumberPayout.class));
			
			return bnps;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public void deleteBetNumberPayouts(final Dx4Player player)
	{
		String sql = "DELETE FROM betnumberpayout WHERE playerseqid=?";
		try
		{
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,player.getSeqId());
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
	public void storeBetNumberPayout(Dx4Player player,Dx4BetNumberPayout bnp)
	{
		try
		{
			storeBetNumberPayout(player.getSeqId(),bnp);
		}
		catch (DuplicateKeyException e)
		{
			log.info("BetNumberPayout for : "+  player.getEmail() + " number : " + bnp.getNumber() + " exists updating");
			updateBetNumberPayout(player.getSeqId(),bnp);
			
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	private void updateBetNumberPayout(final long playerSeqId,final Dx4BetNumberPayout bnp) throws DataAccessException
	{
		String sql = "UPDATE betnumberpayout set odds=? where playerseqid=? and number=?";
		try
		{
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setDouble(1,bnp.getOdds());
				preparedStatement.setLong(2,playerSeqId);
				preparedStatement.setString(3,bnp.getNumber());
				}
			});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	private void storeBetNumberPayout(final long playerSeqId,final Dx4BetNumberPayout bnp) throws DuplicateKeyException,DataAccessException
	{
		String sql = "INSERT INTO betnumberpayout ( playerseqid, number, odds ) VALUES ( ?, ?, ? )";
		
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,playerSeqId);
				preparedStatement.setString(2,bnp.getNumber());
				preparedStatement.setDouble(3,bnp.getOdds());
				}
			});
	}
	
	
}
