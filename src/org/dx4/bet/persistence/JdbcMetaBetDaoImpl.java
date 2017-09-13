package org.dx4.bet.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.account.Dx4Transaction;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.excel.StakeWin;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4BetRollup;
import org.dx4.bet.Dx4DateWin;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4MetaBetExpo;
import org.dx4.bet.Dx4MetaBetExpoOrder;
import org.dx4.bet.Dx4NumberWin;
import org.dx4.bet.Dx4Win;
import org.dx4.bet.Dx4WinNumberSummary;
import org.dx4.bet.floating.Dx4BetNumberPayout;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.persistence.ProviderRowMapper;
import org.dx4.home.persistence.Dx4DuplicateKeyException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.services.Dx4ServicesRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class JdbcMetaBetDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4MetaBetDao {

	private static final Logger log = Logger.getLogger(JdbcMetaBetDaoImpl.class);
	private Dx4BetDao dx4BetDao;
	private final ConcurrentHashMap<String, Dx4ProviderJson> providerByName = new ConcurrentHashMap<String, Dx4ProviderJson>();
	private final ConcurrentHashMap<Character, Dx4ProviderJson> providerByCode = new ConcurrentHashMap<Character, Dx4ProviderJson>();
	
	
	public JdbcMetaBetDaoImpl()
	{
	}
	
	private void createProviders() {
		String sql = "SELECT * FROM PROVIDER";
		List<Dx4ProviderJson> pList = getJdbcTemplate().query(sql,new ProviderRowMapper());
		for (Dx4ProviderJson provider : pList)
		{
			providerByCode.put(provider.getCode(), provider );
			providerByName.put(provider.getName(), provider );
			log.trace("got provider : " + provider);
		}
	}

	@Override
	public Dx4ProviderJson getProviderByCode(Character code)
	{
		if (providerByCode.isEmpty())
			createProviders();
		return providerByCode.get(code);
	}
	
	@Override
	public List<Dx4ProviderJson> getProviders()
	{
		if (providerByCode.isEmpty())
			createProviders();
		return new ArrayList<Dx4ProviderJson>(providerByCode.values());
	}
	
	@Override
	public Dx4ProviderJson getProviderByName(String name)
	{
		if (name == null)
			return null;
		
		if (providerByName.isEmpty())
			createProviders();
		return providerByName.get(name);
	}
	
	@Override
	public void insert(Dx4MetaBet metaBet) {
		
		
		if (metaBet.getCp() == null)
			throw new Dx4ExceptionFatal(this.getClass().getName()+":insert CP not set");
		
		deleteMetaBet(metaBet);
		
		String sql = "INSERT INTO METABET ( PLAYERID, CPID, TYPE, METAGAMEID, OUTSTANDING, PLAYGAMEID, " +
				" TOTALSTAKE, TOTALWIN, FLOATINGMODEL  ) VALUES ( " 
					+ metaBet.getPlayer().getSeqId()+","
					+ metaBet.getCp().getSeqId()+",'"
					+ metaBet.getType().getCode()+"',"
					+ metaBet.getMetaGame().getId()+ ","
					+ 1 + "," 
					+ metaBet.getPlayGameId() + ","
					+ metaBet.getTotalStake() + "," 
					+ metaBet.getTotalWin() + ","
					+ metaBet.isFloatingModel() +
					")";
		
			log.trace(sql);
			getJdbcTemplate().update(sql);
			// get the bet back to grab the id
			metaBet.setId(getLastMetaBetId());
			dx4BetDao.insertBets(metaBet);
			updateBetRollups(metaBet);
	}
	
	public void setPlaced(Dx4MetaBet metaBet)
	{
		GregorianCalendar gc = new GregorianCalendar();
		Timestamp t1 = new Timestamp(gc.getTimeInMillis());
		String sql = "UPDATE METABET SET PLACED='" + t1 + "' WHERE ID = " +
				+ metaBet.getId();
		getJdbcTemplate().update(sql);
	}
	
	private void deleteMetaBet(Dx4MetaBet metaBet)
	{
		if (metaBet.getId()<0)
			return;
		String sql = "DELETE FROM METABET WHERE ID = " + metaBet.getId();
		getJdbcTemplate().update(sql);
		reduceBetRollups(metaBet);
	}
	
	private long getLastMetaBetId() {
		String sql = "SELECT MAX(ID) FROM METABET";
		return getJdbcTemplate().queryForObject(sql,Long.class);
	}

	@Override
	public void setPlayed(Dx4PlayGame playGame)
	{
		Timestamp t1 = new Timestamp(playGame.getPlayedAt().getTime());
		String sql = "UPDATE METABET SET OUTSTANDING=0,PLAYED='" + t1.toString() + "' WHERE PLAYGAMEID = " 
			+ playGame.getId();
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public void update(Dx4PlayGame playGame,Dx4MetaBet metaBet) {
		Timestamp t1 = new Timestamp(playGame.getPlayedAt().getTime());
		String sql = "UPDATE METABET SET OUTSTANDING=0,TOTALWIN =" + metaBet.getTotalWin() + ",PLAYED='" + t1.toString() + "'" +
				" WHERE ID=" + metaBet.getId();
		log.trace("updating : " + metaBet);
		log.trace(sql);
		getJdbcTemplate().update(sql);
		dx4BetDao.updateBets(metaBet);
	}
	
	@Override
	public Dx4MetaBet getMetaBetById(Long id) {
		
		String sql = "SELECT M.*,PG.PLAYDATE FROM METABET AS M JOIN PLAYGAME AS PG ON M.PLAYGAMEID=PG.ID WHERE M.ID = " + id;
		
		log.trace(sql);
		List<Dx4MetaBet> metaBets = getJdbcTemplate().query(sql,new Dx4MetaBetRowMapper());
		if (metaBets.isEmpty())
			return null;
		
		Dx4MetaBet metaBet = metaBets.get(0);
		populateMetaBet(metaBet);
			
		return metaBet;
	}
	
	public void populateMetaBet(Dx4MetaBet metaBet)
	{
		metaBet.setBets(dx4BetDao.getBetsForMetaBet(metaBet));
		metaBet.setWins(dx4BetDao.getWinsForMetaBet(metaBet));
	}
	
	@Override
	public List<Dx4MetaBet> getMetaBetsForPlayer(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame) {
		
		try
		{
			return getMetaBetsForPlayer1(player,flag,metaGame);
		}
		catch (DataAccessException e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
			throw new PersistenceRuntimeException(e.getMessage());
		}
	}
	
	private List<Dx4MetaBet> getMetaBetsForPlayer1(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame) throws DataAccessException{
		
		List<Dx4MetaBet> metaBets = null;
		if (flag.equals(Dx4BetRetrieverFlag.ALL))
		{
			if (metaGame!=null)
				metaBets = getAllMetaBetsForPlayer(player,metaGame);
		   else
			   metaBets = getAllMetaBetsForPlayer(player);
		}
		else
		{
			int outstanding = 0;						// values for BetRetrieverFlag.HISTORICWINONLY
			double totalWin= 0.0;
			if (flag.equals(Dx4BetRetrieverFlag.CURRENT))
			{
				outstanding = 1;
				totalWin = -1.0;
			}
			else
			if (flag.equals(Dx4BetRetrieverFlag.HISTORIC))
			{
				outstanding = 0;
				totalWin = -1.0;
			}
							
			if (metaGame!=null)
				metaBets = getMetaBetsForPlayer(player,metaGame,outstanding,totalWin);
			else
				metaBets = getMetaBetsForPlayer(player,outstanding,totalWin);
		}
		
		for (Dx4MetaBet metaBet : metaBets)
		{
			populateMetaBet(metaBet);
		}
		return metaBets;
	}
	
	private List<Dx4MetaBet> getMetaBetsForPlayer(final Dx4Player player,final Dx4MetaGame metaGame,
			final int outstanding,final double totalWin) throws DataAccessException
	{
		String sql = "SELECT M.*,PG.PLAYDATE FROM METABET AS M JOIN PLAYGAME AS PG ON M.PLAYGAMEID=PG.ID  "
				+ "WHERE M.PLAYERID=? AND M.METAGAMEID=? AND M.OUTSTANDING=? AND M.TOTALWIN>? ORDER BY M.ID DESC";
		List<Dx4MetaBet> allMetaBets = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,player.getSeqId());
				preparedStatement.setLong(2,metaGame.getId());
				preparedStatement.setInt(3,outstanding);
				preparedStatement.setDouble(3,totalWin);
			}
		}, new Dx4MetaBetRowMapper());

		return allMetaBets;
	}
	
	private List<Dx4MetaBet> getMetaBetsForPlayer(final Dx4Player player,final int outstanding,final double totalWin) throws DataAccessException
	{
		String sql = "SELECT M.*,PG.PLAYDATE FROM METABET AS M JOIN PLAYGAME AS PG ON M.PLAYGAMEID=PG.ID WHERE M.PLAYERID=? AND M.OUTSTANDING=? AND M.TOTALWIN>? ORDER BY M.ID DESC";
		List<Dx4MetaBet> allMetaBets = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,player.getSeqId());
				preparedStatement.setInt(2,outstanding);
				preparedStatement.setDouble(3,totalWin);
			}
		}, new Dx4MetaBetRowMapper());

		return allMetaBets;
	}
	
	private List<Dx4MetaBet> getAllMetaBetsForPlayer(final Dx4Player player,final Dx4MetaGame metaGame) throws DataAccessException
	{
		String sql = "select m.*,pg.playdate as playdate from metabet as m " +
				"join playgame as pg on m.playgameid = pg.id where m.playerid = ? and m.metagameid= ? order by m.id desc";
		List<Dx4MetaBet> allMetaBets = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,player.getSeqId());
				preparedStatement.setLong(2,metaGame.getId());
			}
		}, new Dx4MetaBetRowMapper());

		return allMetaBets;
	}
	
	private List<Dx4MetaBet> getAllMetaBetsForPlayer(final Dx4Player player) throws DataAccessException
	{
		String sql = "select m.*,pg.playdate as playdate from metabet as m " +
					"join playgame as pg on m.playgameid = pg.id where m.playerid = ? order by m.id desc";
		
		List<Dx4MetaBet> allMetaBets = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,player.getSeqId());
			}
		}, new Dx4MetaBetRowMapper());

		return allMetaBets;
	}
	
	
	@Override
	public Integer getMetaBetsForPlayerCount(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame) {
		
		String sql = getMetaBetsForPlayerString( player, flag, metaGame,true);
		
		log.trace(sql);
		return getJdbcTemplate().queryForObject(sql,Integer.class);
	}

	public static String getMetaBetsForPlayerString(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame,boolean count) {
		
		String sql = "SELECT COUNT(*)"; 
		if (count==false)
			sql = "SELECT M.*,PG.PLAYDATE"; 
		sql += " FROM METABET AS M JOIN PLAYGAME AS PG ON M.PLAYGAMEID=PG.ID"
				+ " WHERE M.PLAYERID = " + player.getSeqId() + " AND M.PLACED IS NOT NULL";
		if (metaGame != null)
		{
			sql += " AND M.METAGAMEID = " + metaGame.getId();  
		}
		if (flag.equals(Dx4BetRetrieverFlag.CURRENT)) 
				sql += " AND M.OUTSTANDING>0";
		if (flag.equals(Dx4BetRetrieverFlag.HISTORIC)) 
				sql += " AND M.OUTSTANDING=0";
		if (flag.equals(Dx4BetRetrieverFlag.HISTORICWINONLY)) 
			sql += " AND M.OUTSTANDING=0 AND M.TOTALWIN > 0";
		if (count==false)
			sql += " ORDER BY M.ID DESC";
		return sql;
	}
	
	public void setDx4BetDao(Dx4BetDao dx4BetDao) {
		this.dx4BetDao = dx4BetDao;
	}

	public Dx4BetDao getDx4BetDao() {
		return dx4BetDao;
	}

	@Override
	public List<Dx4MetaBet> getAllMetaBetsForMetaGame(final Dx4MetaGame metaGame, final Dx4PlayGame playGame) {
		
		String sql = "select m.*,pg.playdate as playdate from metabet as m " +
				"join playgame as pg on m.playgameid = pg.id where m.metagameid=? AND m.playgameid = ? order by m.id desc";
		
		try
		{
			List<Dx4MetaBet> allMetaBets = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setLong(1,metaGame.getId());
					preparedStatement.setLong(2,playGame.getId());
				}
			}, new Dx4MetaBetRowMapper());
			
			for (Dx4MetaBet metaBet : allMetaBets)
			{
				metaBet.setMetaGame(metaGame);
				metaBet.setPlayGame(playGame);
				populateMetaBet(metaBet);
			}
			return allMetaBets;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public Dx4MetaBet getMetaBetForTransaction(Dx4Transaction transaction) {
		String sql = "select m.*,pg.playdate as playdate from metabet as m "
					  + " join playgame as pg on m.playgameid = pg.id where m.id = " + transaction.getRefId();
		Dx4MetaBet metaBet = getJdbcTemplate().queryForObject(sql,new Dx4MetaBetRowMapper());
		populateMetaBet(metaBet);
		return metaBet;
	}

	@Override
	public NumberExpo getSingleNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure,String number)
	{
		Timestamp playDate = new Timestamp(playGame.getPlayDate().getTime());
		List<NumberExpo> expos = getNumberExpos(parentCode,playDate.toString(),role,maxExpo,exposure,number);
		if (expos.isEmpty())
			return new NumberExpo(number,0,0);
		return expos.get(0);
	}
	
	@Override
	public List<NumberExpo> getNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure)
	{
		Timestamp playDate = new Timestamp(playGame.getPlayDate().getTime());
		return getNumberExpos(parentCode,playDate.toString(),role,maxExpo,exposure,null);
	}
	
	private List<NumberExpo> getNumberExpos(String parentCode,String playDateStr,Dx4Role role,double maxExpo,boolean exposure,String number)
	{
		HashMap<String,NumberExpo> expos = new HashMap<String,NumberExpo>();
		if (role.getRank()>Dx4Role.ROLE_ZMA.getRank())
			getNumberExpo(parentCode,playDateStr,4,maxExpo,expos,exposure,number);
		if (role.getRank()>Dx4Role.ROLE_SMA.getRank())
			getNumberExpo(parentCode,playDateStr,3,maxExpo,expos,exposure,number);
		if (role.getRank()>Dx4Role.ROLE_MA.getRank())
			getNumberExpo(parentCode,playDateStr,2,maxExpo,expos,exposure,number);
		if (role.getRank()>Dx4Role.ROLE_AGENT.getRank())
			getNumberExpo(parentCode,playDateStr,1,maxExpo,expos,exposure,number);
		if (role.getRank()>Dx4Role.ROLE_PLAY.getRank())
			getNumberExpo(parentCode,playDateStr,0,maxExpo,expos,exposure,number);
		
		List<NumberExpo> values = getMergedExpos(expos);
		if (exposure)
			Collections.sort(values,new NumberExpoWinComparator() );
		else
			Collections.sort(values,new NumberExpoBetComparator() );
		return values;
	}
	
	private List<NumberExpo> getMergedExpos(HashMap<String,NumberExpo> expos)
	{
		List<NumberExpo> values = new ArrayList<NumberExpo>(expos.size());
		values.addAll(expos.values());
		return values;
	}
	
	private List<NumberExpo> getNumberExpo(String parentCode,String playDateStr,int tier,double minExpo,HashMap<String,NumberExpo> expos,Boolean exposure,String number)
	{
		String sql = createNumberExpoSql(tier,parentCode,playDateStr,minExpo,exposure,number);
		log.trace(sql);
		return getNumberExpoResults( sql, expos);
	}
	
	static final String baseSql = "select expo,tbet,number from("
			+ "select sum(winexpo) as expo,sum(betexpo) as tbet,choice as number from choice as c " +
			"join metabet as m on c.metabetid = m.id " +
			"join playgame as p on m.playgameid = p.id " +
			"join baseuser as u1 on m.playerid = u1.seqid ";
	
	static final String[] sqlTiers = { 
			"",																									// AGENT
					"join baseuser as top on u1.parentcode = top.code ",										// MA
							"join baseuser as u2 on u1.parentcode = u2.code " +									// SMA
							"join baseuser as top on u2.parentcode = top.code ",
									"join baseuser as u2 on u1.parentcode = u2.code " +							// ZMA
									"join baseuser as u3 on u2.parentcode = u3.code " +
									"join baseuser as top on u3.parentcode = top.code ",
											"join baseuser as u2 on u1.parentcode = u2.code " +					// COMP
											"join baseuser as u3 on u2.parentcode = u3.code " +
											"join baseuser as u4 on u3.parentcode = u4.code " +
											"join baseuser as top on u4.parentcode = top.code "
		};
	
	
	
	private String createNumberExpoSql(int tier,String parentCode,String playDateStr,double minExpo,Boolean exposure,String number)
	{
		// AGENT=1,MA=2,SMA=3,ZMA=4
		String sql = baseSql;
		
		if (tier == 0)
			sql += "where p.playdate='"+playDateStr + "' and u1.parentcode = '" + parentCode + "' ";
		else
			sql+= sqlTiers[tier] +
				    "where p.playdate='"+playDateStr + "' and top.parentcode = '" + parentCode + "' ";
		
		if (number != null)
			sql += "and choice='"+number+"' ";
		
		if (exposure==null)
			sql+=" group by choice) as xxx";
		else
		if (exposure==true)			// do by max win
			sql+=" group by choice) as xxx where expo>"+ minExpo + " order by expo desc";
		else					// do by max stake
			sql+=" group by choice) as xxx where tbet>"+ minExpo + " order by tbet desc";
		
		if (number==null)
			sql += " fetch first 250 rows only";
		
		return sql;
	}
	
	/*
	private String createNumberExpoSql(int tier,String parentCode,String playDateStr,double minExpo,Boolean exposure,String number)
	{
		// AGENT=1,MA=2,SMA=3,ZMA=4
		String nstr = "";
		if (number != null)
			nstr = " choice='"+number+"' and";
		
		String sql = "select expo,tbet,number from " +
		"(select sum(winexpo) as expo,sum(betexpo) as tbet,choice as number from choice where "+
		nstr + " metabetid in " + 
		"(select id from metabet where playgameid in " +
		"(select id from playgame where playdate='" + playDateStr + "') and playerid in " +
		"(select id from user where role='ROLE_PLAY' and parentCode in ";
		for (int t=0; t<tier-1; t++)
			sql += "(select code from user where parentCode in ";
		if (tier>0)
			sql += "(select code from user where parentcode = '" + parentCode + "')";
		else
			sql += "'" + parentCode + "'";
		for (int t=0; t<tier-1; t++)
			sql+=")";
		if (exposure==null)
			sql+=")) group by choice)";
		else
		if (exposure==true)			// do by max win
			sql+=")) group by choice) where expo>"+ minExpo + " order by expo desc";
		else					// do by max stake
			sql+=")) group by choice) where tbet>"+ minExpo + " order by tbet desc";
		
		if (number==null)
			sql += " fetch first 250 rows only";
		
		return sql;
	}
	*/
	
	@SuppressWarnings("unused")
	private String createNumberExpoExceededSql(int tier,String parentCode,String playDate,boolean n1Switch)
	{
		// AGENT=1,MA=2,SMA=3,ZMA=4
		String numberAs = "";
		if (n1Switch)
			numberAs = " as n1";
		
		String sql = "select expo,tbet,number " + numberAs +" from " +
		"(select sum(winexpo) as expo,sum(betexpo) as tbet,choice as number from choice where "+
		" metabetid in " + 
		"(select id from metabet where playgameid in " +
				"(select id from playgame where playdate='" + playDate + "') " +
		"and playerid in " +
		"(select id from user where role='ROLE_PLAY' and parentCode in ";
		for (int t=0; t<tier-1; t++)
			sql += "(select code from user where parentCode in ";
		if (tier>0)
			sql += "(select code from user where parentcode = '" + parentCode + "')";
		else
			sql += "'" + parentCode + "'";
		for (int t=0; t<tier-1; t++)
			sql+=")";
		sql+=")) group by choice)";
		
		return sql;
	}
	
	private List<NumberExpo> getNumberExpoResults(String sql,HashMap<String,NumberExpo> expos)
	{
		List<NumberExpo> results = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(NumberExpo.class));
		for (NumberExpo result : results)
		{
			NumberExpo exist = expos.get(result.getNumber());
			if (exist != null)
			{
				result.setExpo(result.getExpo()+exist.getExpo());
				result.setTbet(result.getTbet()+exist.getTbet());
			}
			expos.put(result.getNumber(),result);
		}
		return results;
	}
	
	@Override
	public List<String> getBlockedNumbers(Dx4MetaBet metaBet)
	{
		/*
		select * from numberexpo where number in ('8035','8020') and blocked<>0 && userid in (
				select id from user where code in ('c0z0s0m0a0','c0z0s0m0','c0z0s','c0z0','c0'))
		*/
		
		String choiceSql = "'";
		for (Dx4Bet bet : metaBet.getBets())
			choiceSql += bet.getChoice() + "','";
		String codeSql = createCodeSql(metaBet.getPlayer().getParentCode());
		String sql = "select DISTINCT(number) from numberexpo where blocked<>0 and number in ("
			+ choiceSql.substring(0,choiceSql.length()-2) + ") and userid in " +
			"(select seqid from baseuser where code in ( " + codeSql + "))";
		
		log.trace(sql);
		List<String> blocked = getJdbcTemplate().queryForList(sql,String.class);
		
		return blocked;
	}
	
	private static String createCodeSql(String parentCode) {
		String codeSql = "'" + parentCode + "','";
		String pLastCode = parentCode;
		while (true)
		{
			String pCode = getNextParentCode(pLastCode);
			if (pCode==null)
				break;
			codeSql += pCode + "','";
			pLastCode = pCode;
		}
		return codeSql.substring(0, codeSql.length()-2);
	}

	private static String getNextParentCode(String parentCode) {
		
		// 'c0z0s0m0a0','c0z0s0m0','c0z0s','c0z0','c0'
		int c=parentCode.length()-1;
		for (; c>=0; c--)
		{
			if (!Character.isDigit(parentCode.charAt(c)))
				break;
		}
		for (;  c>=0; c--)
		{
			if (Character.isDigit(parentCode.charAt(c)))
				break;
		}
		if (c<0)
			return null;
		return parentCode.substring(0,c+1);
	}

	@Override
	public void updateNumberExpoBlocked(Dx4NumberExpo numberExpo)
	{
		String sql = "UPDATE NUMBEREXPO SET BLOCKED = " + numberExpo.getBlocked() +
				" WHERE NUMBER='" + numberExpo.getNumber() + "' AND USERID="+ numberExpo.getUserId();
		log.trace("updateNumberExpoBlocked : " + sql);
		if (getJdbcTemplate().update(sql)!=1)
		{
			log.error("Attempt to updateNumberExpoBlocked failed sql error : " + sql);
		}
	}
	
	@Override
	public void resetNumberExposBlocked()
	{
		String sql = "UPDATE NUMBEREXPO SET BLOCKED = 0";
		getJdbcTemplate().update(sql);
	}
	
	class NumberExpoWinComparator implements Comparator<NumberExpo> {
	    @Override
	    public int compare(NumberExpo a, NumberExpo b) {
	    	if (a.getExpo()<b.getExpo())
	    		return 1;
	    	if (a.getExpo()>b.getExpo())
	    		return -1;
	        return 0;
	    }
	}
	
	class NumberExpoBetComparator implements Comparator<NumberExpo> {
	    @Override
	    public int compare(NumberExpo a, NumberExpo b) {
	    	if (a.getTbet()<b.getTbet())
	    		return 1;
	    	if (a.getTbet()>b.getTbet())
	    		return -1;
	        return 0;
	    }
	}
	
	@Override
	public List<Dx4NumberWin> getWinsForDate(Dx4SecureUser user,Date drawDate)
	{
		return dx4BetDao.getWinsForDate(user,drawDate);
	}
	
	@Override
	public List<Dx4DateWin> getWinDates(Dx4SecureUser user)
	{
		return dx4BetDao.getWinDates(user);
	}
	
	@Override
	public List<Dx4Win> getAgentWinsForDate(Dx4SecureUser user,Date drawDate)
	{
		List<Dx4Win> wins = dx4BetDao.getAgentWinsForDate(user,drawDate);
		
		return wins;
	}
	
	@Override
	public Double getTotalWinsForDate(Dx4SecureUser user,Date drawDate)
	{
		return dx4BetDao.getTotalWinsForDate(user,drawDate);
	}
	
	@Override
	public List<Dx4WinNumberSummary> getWinNumberSummary(String number,Character providerCode,Date drawDate)
	{
		return dx4BetDao.getWinNumberSummary(number,providerCode,drawDate);
	}
	
	@Override
	public List<Dx4MetaBetExpo> getDx4MetaBetExpos(Dx4Agent agent,Dx4PlayGame playGame,List<Dx4MetaBetExpoOrder> ordering)
	{
		Timestamp playDate = new Timestamp(playGame.getPlayDate().getTime());
		String sql = Dx4MetaBetExposureSupport.getSqlString(playDate.toString(),agent.getRole(),agent.getCode(),ordering);
		List<Dx4MetaBetExpo> mList = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4MetaBetExpo.class));
		return mList;
	}
	
	@Override
	public Integer getDx4MetaBetExposCount(Dx4Agent agent,Dx4PlayGame playGame,List<Dx4MetaBetExpoOrder> ordering)
	{
		Timestamp playDate = new Timestamp(playGame.getPlayDate().getTime());
		String sql = Dx4MetaBetExposureSupport.getSqlCountString(playDate.toString(),agent.getRole(),agent.getCode(),ordering);
		
		log.trace(sql);
		Integer cnt = 0;
		try
		{
			cnt = getJdbcTemplate().queryForObject(sql,Integer.class);
		}
		catch (EmptyResultDataAccessException e)
		{
			return 0;
		}
		return cnt;
	}

	private synchronized void reduceBetRollups(Dx4MetaBet metaBet)
	{
		for (Dx4Bet bet : metaBet.getBets())
		{
			String provs = bet.getProviderCodes();
			for (int i =0; i<provs.length(); i++)
				reduceBetRollup(bet.getChoice(),bet.getTotalStake(),bet.calcHighWin(),metaBet.getPlayGameId(),bet.getGame().getId(),provs.charAt(i));
		}
	}
	
	private void reduceBetRollup(final String number,final double stake,final double win,final long playgameId,final long gameId,final char providerCode) throws DataAccessException
	{
		String sql = "UPDATE BETROLLUP SET STAKE=STAKE-?,WIN=WIN-? WHERE NUMBER=? AND PLAYGAMEID=? AND GAMEID=? AND PROVIDERCODE=?";
	//	log.info(sql);
		getJdbcTemplate().update(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setDouble(1,stake);
				preparedStatement.setDouble(2,win);
				preparedStatement.setString(3,number);
				preparedStatement.setLong(4,playgameId);
				preparedStatement.setLong(5,gameId);
				preparedStatement.setString(6,Character.toString(providerCode));
			}
		});
	}
	
	private synchronized void updateBetRollups(Dx4MetaBet metaBet)
	{
		for (Dx4Bet bet : metaBet.getBets())
		{
			bet.calcTotalStake();						// make sure set
			String provs = bet.getProviderCodes();
			for (int i =0; i<provs.length(); i++)
			{	
				updateBetRollup(bet.getChoice(),bet.getTotalStake(),bet.calcHighWin(),metaBet.getPlayGameId(),bet.getGame().getId(),provs.charAt(i));
			}
		}
	}
	
	private void updateBetRollup(final String number,final double stake,final double win,final long playgameId,final long gameId,final char providerCode) throws DataAccessException
	{
		String sql = "SELECT * FROM BETROLLUP WHERE NUMBER=? AND PLAYGAMEID=? AND GAMEID=? AND PROVIDERCODE=?";
		List<Dx4BetRollup> rollups = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1,number);
				preparedStatement.setLong(2,playgameId);
				preparedStatement.setLong(3,gameId);
				preparedStatement.setString(4,Character.toString(providerCode));
			}
		}, BeanPropertyRowMapper.newInstance(Dx4BetRollup.class));

		if (rollups.isEmpty())
		{
			sql = "INSERT INTO BETROLLUP (NUMBER,PLAYGAMEID,GAMEID,PROVIDERCODE,STAKE,WIN) VALUES (?,?,?,?,?,?)";
		//	log.info(sql);
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1,number);
					preparedStatement.setLong(2,playgameId);
					preparedStatement.setLong(3,gameId);
					preparedStatement.setString(4,Character.toString(providerCode));
					preparedStatement.setDouble(5,stake);
					preparedStatement.setDouble(6,win);
				}
			});
		}
		else
		{
			final Dx4BetRollup br = rollups.get(0);
			sql = "UPDATE BETROLLUP SET STAKE=?,WIN=? WHERE ID =?";
//			log.info(sql);
			getJdbcTemplate().update(sql,new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setDouble(1,br.getStake() + stake);
					preparedStatement.setDouble(2,br.getWin() + win);
					preparedStatement.setLong(3,br.getId());
				}
			});
		}
	}
	
	@Override
	public List<Dx4BetRollup> getTotalBetRollups(final long playgameId,final int digits) throws DataAccessException
	{
		String sql = "select number,gameid,sum(stake) as stake,sum(win) as win from betrollup where playgameid=? and length(number) = ?" +
							" group by (number,gameid) order by number";
		List<Dx4BetRollup> rollups = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,playgameId);
				preparedStatement.setInt(2,digits);
			}
		}, BeanPropertyRowMapper.newInstance(Dx4BetRollup.class));
		return rollups;
	}
	
	@Override
	public List<Dx4BetRollup> getTotalBetRollups(final long playgameId,final int digits,final char providerCode) throws DataAccessException
	{
		String sql = "select number,gameid,sum(stake) as stake,sum(win) as win,providercode from betrollup where playgameid=? and length(number) = ? and providercode = ?" +
							" group by (number,gameid,providercode) order by number";
		List<Dx4BetRollup> rollups = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1,playgameId);
				preparedStatement.setInt(2,digits);
				preparedStatement.setString(3,String.valueOf(providerCode));
			}
		}, BeanPropertyRowMapper.newInstance(Dx4BetRollup.class));
		return rollups;
	}
	
	/* clean up routines for testing */
	
	
	public void cleanUpBetsForPlayDate(Date drawDate)
	{// used in test using previous date make sure no dangling bets and associated stuff around for the date 
		Timestamp playDate = new Timestamp(drawDate.getTime());

		// invoices and payments
		String sql = "delete from xtransaction where type='P' OR type='I' and playdate = '"+ playDate.toString() +"'";

		getJdbcTemplate().update(sql);
		
		// choice
		sql = "delete from choice where metabetid in " +
		"(select id from metabet where playgameid in " +
		"(select id from playgame where playdate = '"+ playDate.toString() +"'))";

		getJdbcTemplate().update(sql);

		// bets
		sql = "delete from bet where metabetid in " +
		"(select id from metabet where playgameid in " +
		"(select id from playgame where playdate = '"+ playDate.toString() +"'))";

		getJdbcTemplate().update(sql);

		// wins
		sql = "delete from win where metabetid in " +
		"(select id from metabet where playgameid in " +
		"(select id from playgame where playdate = '"+ playDate.toString() +"'))";

		getJdbcTemplate().update(sql);

		// metabets
		sql = "delete from metabet where playgameid in " +
		"(select id from playgame where playdate = '"+ playDate.toString() +"')";

		getJdbcTemplate().update(sql);
	}
	
	public static void main(String[] args)
	{
		String pstr = createCodeSql("c0z0s0m0a0");
		log.trace(pstr);
	}

	@Override
	public Dx4Bet getBetById(long betId) {
		
		return dx4BetDao.getBetById(betId);
	}
	
	@Override
	public List<WinNumber> getWinNumber(Dx4ProviderJson provider, long playGameId)
	{
		return dx4BetDao.getWinNumber(provider, playGameId);
	}

	@Override
	public void storeMetabetId(final UUID id) {
		try
		{
			getJdbcTemplate().update("INSERT INTO metabetunique (ID) VALUES (?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
						ps.setObject(1,id);
			      }
			    });
		}
		catch (DuplicateKeyException e)
		{
			throw new Dx4DuplicateKeyException("Duplicate metabetunique key on : " + id);
		}	
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new Dx4ServicesRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public Map<Date,StakeWin>  getTotalWinStakes()
	{
		Map<Date,StakeWin> tm = new TreeMap<Date,StakeWin>();
		
		String sql = "select pg.id,pg.playdate as date,sum(br.stake) as sum from betrollup as br " +
					"join playgame as pg on pg.id = br.playgameid " +
					"where pg.played=1 and pg.playdate <> '2017-07-04 00:00:00' group by pg.id order by pg.id";
		
		List<SumDate> stakes = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(SumDate.class));
		
		sql = "select pg.id,pg.playdate as date,sum(w.win) as sum from win as w " +
				"join metabet as mb on w.metabetid = mb.id " +
				"join playgame as pg on pg.id = mb.playgameid " +
				"where pg.played=1 and pg.playdate <> '2017-07-04 00:00:00' group by pg.id order by pg.id";
		
		List<SumDate> wins = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(SumDate.class));
		
		for (SumDate stake : stakes)
		{
			tm.put(stake.getDate(), new StakeWin(stake.getSum(),0));
		}
		
		for (SumDate win : wins)
		{
			StakeWin sw = tm.get(win.getDate());
			sw.setWin(win.getSum());
		}
		
		return tm;
	}
	
	@Override
	public List<Dx4BetNumberPayout> getBetNumberPayouts(Dx4Player player) {
		return dx4BetDao.getBetNumberPayouts(player);
	}

	@Override
	public void deleteBetNumberPayouts(Dx4Player player) {
		dx4BetDao.deleteBetNumberPayouts(player);
	}

	@Override
	public void storeBetNumberPayout(Dx4Player player, Dx4BetNumberPayout bnp) {
		dx4BetDao.storeBetNumberPayout(player, bnp);
	}

	@Override
	public void calcRetroTotalStakes(Dx4MetaGame mg) throws DataAccessException
	{
		String sql = "UPDATE bet SET totalstake = stake";
		getJdbcTemplate().update(sql);
		sql = "select * from bet as b " +
			"join game as g on b.gameid = g.id " +
			"where gtype='D4BoxSmall' or gtype='D4BoxBig'";
		List<Dx4Bet> bets = getJdbcTemplate().query(sql, new Dx4BetRowMapper());
		
		sql = "update bet set totalstake=? where id=?";
		String sql2 = "select playgameid from metabet as mb " +
				"join bet as b on b.metabetid = mb.id " +
				"where b.id = ";
		
		int cnt = 0;
		int num = bets.size();
		for (Dx4Bet bet : bets)
		{
			++cnt;
			bet.setGame(mg.getGameById(bet.getGameId()));
			double totalStake = bet.calcTotalStake();
			log.info("Updating bet : " + cnt + " of: " + num + " " + bet.getChoice() + " with total stake: " + totalStake);			
			getJdbcTemplate().update(sql, new Object[] { totalStake, bet.getId() });
			
			String provs = bet.getProviderCodes();
			
			long playGameId = getJdbcTemplate().queryForObject(sql2 + bet.getId(), Long.class);
			for (int i =0; i<provs.length(); i++)
			{
				reduceBetRollup(bet.getChoice(),bet.getStake(),bet.calcHighWin(),playGameId,bet.getGame().getId(),provs.charAt(i));
				updateBetRollup(bet.getChoice(),totalStake,bet.calcHighWin(),playGameId,bet.getGame().getId(),provs.charAt(i));
			}	
		}
	}
	
	@Override
	public void fixBetsAndRollups(Dx4MetaGame mg) throws DataAccessException
	{
		String sql = "delete from betrollup where playgameid=12 or playgameid=13";
		getJdbcTemplate().update(sql);
		
		sql = "select * from bet as b " +
					"join metabet as mb on mb.id = b.metabetid " +
					"where mb.playgameid=13 or mb.playgameid=12";
		List<Dx4Bet> bets = getJdbcTemplate().query(sql, new Dx4BetRowMapper());
		sql = "update bet set totalstake=? where id=?";
		int cnt=0;
		int num = bets.size();
		
		String sql2 = "select playgameid from metabet as mb " +
				"join bet as b on b.metabetid = mb.id " +
				"where b.id = ";
		for (Dx4Bet bet : bets)
		{
			++cnt;
			bet.setGame(mg.getGameById(bet.getGameId()));
			double totalStake = bet.calcTotalStake();
			log.info("Updating bet : " + cnt + " of: " + num + " " + bet.getChoice() + " with total stake: " + totalStake);			
			getJdbcTemplate().update(sql, new Object[] { totalStake, bet.getId() });
			
			String provs = bet.getProviderCodes();
			
			long playGameId = getJdbcTemplate().queryForObject(sql2 + bet.getId(), Long.class);
			for (int i =0; i<provs.length(); i++)
			{
				updateBetRollup(bet.getChoice(),totalStake,bet.calcHighWin(),playGameId,bet.getGame().getId(),provs.charAt(i));
			}	
		}
	}
	

}
