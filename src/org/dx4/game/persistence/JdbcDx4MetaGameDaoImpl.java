package org.dx4.game.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.FxForPlayGame;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.secure.domain.Dx4SecureUser;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;


public class JdbcDx4MetaGameDaoImpl extends NamedParameterJdbcDaoSupport implements Dx4MetaGameDao {

	private static final Logger log = Logger.getLogger(JdbcDx4MetaGameDaoImpl.class);
	private Dx4GameDao dx4GameDao;
	private Dx4PlayGameDao dx4PlayGameDao;
	
	private final ConcurrentHashMap<String, Dx4MetaGame> metaGameByName = new ConcurrentHashMap<String, Dx4MetaGame>();
	private final ConcurrentHashMap<Long, Dx4MetaGame> metaGameById = new ConcurrentHashMap<Long, Dx4MetaGame>();
	
	
	@Override
	public synchronized void store(Dx4MetaGame metaGame) throws PersistenceRuntimeException{
		
		Dx4MetaGame exists = get(metaGame.getName());
		if (exists!=null)
		{
			throw new PersistenceRuntimeException("Game Name : " + metaGame.getName(), " already exists.");
		}
		
		GregorianCalendar gc = new GregorianCalendar();
		
		Timestamp timestamp = new Timestamp(gc.getTime().getTime());
		
		String sql = "INSERT INTO METAGAME ( NAME, DESCRIPTION, TIMESTAMP ) VALUES ( '" 
			+ metaGame.getName()+"','"
			+ metaGame.getDescription()+"','"
			+ timestamp.toString() + "')"; 
		log.trace(sql);
		getJdbcTemplate().update(sql);
		// get the game back to grab the id
		
		metaGame.setId(getLastMetaGameId());
		
		for (Dx4Game game : metaGame.getGames())
			dx4GameDao.store(metaGame,game);
		
		for (String provider : metaGame.getProviders())
			storeProviderMap(metaGame,provider);
	}
	
	private void storeProviderMap(Dx4MetaGame metaGame, String provider) {
		String sql = "INSERT INTO PROVIDERMGLINK ( METAGAMEID, PROVIDERID )" +
				" VALUES ( " + metaGame.getId() + "," + 
				"(SELECT ID FROM PROVIDER WHERE NAME = '" + provider + "'))";
		log.trace(sql);
		getJdbcTemplate().update(sql);
	}

	private long getLastMetaGameId() {
		String sql = "SELECT MAX(ID) FROM METAGAME";
		return getJdbcTemplate().queryForObject(sql,Long.class);
	}
	
	@Override
	public synchronized Dx4MetaGame get(String name) {
		
		Dx4MetaGame metaGame = metaGameByName.get(name);
		if (metaGame!=null)
			return metaGame;
		
		String sql = "SELECT * FROM METAGAME WHERE name='"+ name + "'";
		log.trace("sql = "  + sql );
		
		try
		{
			metaGame = (Dx4MetaGame) getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Dx4MetaGame.class) );
			populateMetaGame(metaGame);
		}catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		return metaGame;
	}

	private void getProviders(Dx4MetaGame metaGame) {
		String sql = "SELECT NAME FROM PROVIDER WHERE ID " +
				" IN (SELECT PROVIDERID FROM PROVIDERMGLINK WHERE METAGAMEID=" + metaGame.getId() + ")";
		List<String> providers = (List<String>) getJdbcTemplate().queryForList(sql,String.class);
		metaGame.setProviders(providers);
	}

	@Override
	public void updateProviderImage(final String code,final byte[] image)
	{
		try
		{
			getJdbcTemplate().update("UPDATE PROVIDER SET image = ? WHERE code=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setBytes(1, image);
							ps.setString(2,code);
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
	public synchronized List<Dx4MetaGame> getMetaGames() {
		
		String sql = "SELECT * FROM METAGAME";
		log.trace("sql = "  + sql );
		
		List<Dx4MetaGame> metaGames = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4MetaGame.class));
		for (Dx4MetaGame metaGame : metaGames)
		{
			populateMetaGame(metaGame);
		}
	
		return metaGames;
	}

	@Override
	public synchronized Dx4MetaGame get(long id) {
//		Dx4MetaGame metaGame = metaGameById.get(id);
//		if (metaGame!=null)
//			return metaGame;
		
		return getDB(id);
	}

	private synchronized Dx4MetaGame getDB(long id) {
		
		String sql = "SELECT * FROM METAGAME WHERE ID = " + id;
		log.trace(sql);
		Dx4MetaGame metaGame;
		try
		{
			metaGame = getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Dx4MetaGame.class));
			populateMetaGame(metaGame);
		}
		catch (DataAccessException e)
		{
			return null;
		}
		return metaGame;
	}
	
	public void setDx4GameDao(Dx4GameDao dx4GameDao) {
		this.dx4GameDao = dx4GameDao;
	}

	public Dx4GameDao getDx4GameDao() {
		return dx4GameDao;
	}

	public void setDx4PlayGameDao(Dx4PlayGameDao dx4PlayGameDao) {
		this.dx4PlayGameDao = dx4PlayGameDao;
	}

	public Dx4PlayGameDao getDx4PlayGameDao() {
		return dx4PlayGameDao;
	}

	@Override
	public void insertPlayGame(Dx4PlayGame playGame,Dx4MetaGame metaGame) {
		dx4PlayGameDao.insert(playGame,metaGame);
		getDB(metaGame.getId());						// refesh metaGame
	}

	@Override
	public void updatePlayGame(Dx4PlayGame playGame) {
		log.trace("JdbcDx4MetaGameDaoImpl:updatePlayGame : " + playGame);
		dx4PlayGameDao.update(playGame);
	}

	@Override
	public synchronized List<Dx4MetaGame> getUnplayedMetaGames() {
		
		//String sql = "SELECT * FROM METAGAME WHERE ID IN (SELECT METAGAMEID FROM PLAYGAME WHERE PLAYED = 0)";
		//log.trace("sql = "  + sql );
		/*
		List<Dx4MetaGame> metaGames = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(Dx4MetaGame.class));
		for (Dx4MetaGame metaGame : metaGames)
		{
			populateMetaGame(metaGame);
		}
		*/
		
		if (metaGameByName.isEmpty())
			getMetaGames();
		
		List<Dx4MetaGame> metaGames = new ArrayList<Dx4MetaGame>();
		for (Dx4MetaGame metaGame : metaGameByName.values())
		{
			if (metaGame.isUnplayed())
				metaGames.add(metaGame);
		}
	
		return metaGames;
	}

	private void populateMetaGame(Dx4MetaGame metaGame) throws DataAccessException
	{
		dx4GameDao.getGames(metaGame);
		dx4PlayGameDao.getPlayGames(metaGame);
		getProviders(metaGame);
		metaGameById.put(metaGame.getId(), metaGame);
		metaGameByName.put(metaGame.getName(), metaGame);
		getMetaGameImages(metaGame);
	}

	@Override
	public Dx4PlayGame getPlayGameById(long playGameId) {
		return dx4PlayGameDao.getPlayGameById(playGameId);
	}
/*
	@Override
	public Dx4Game getGameById(long gameId) {
		return dx4GameDao.getGameById(gameId);
	}
	
	@Override
	public Dx4PlayGame getPlayGameByPlayDate(Dx4MetaGame metaGame,Date playDate) 
	{
		
		return dx4PlayGameDao.getPlayGameByPlayDate(metaGame,playDate);
	}
*/
	
	@Override
	public void storeGameGroup(Dx4SecureUser user) {
		log.info("Storing game group for : " + user.getEmail());
		if (user.getGameGroup()==null)
		{
			Dx4GameGroup group = new Dx4GameGroup(user);
			user.setGameGroup(group);
		}
		user.getGameGroup().setUser(user);
		user.getGameGroup().setUserId(user.getSeqId());
		storeGameGroup(user.getGameGroup());
	}
	
	@Override
	public void storeGameGroup(Dx4GameGroup gameGroup)
	{	
		log.info("Storing game group : " + gameGroup);
		Dx4GameGroup exists = getGameGroup(gameGroup);
		if (exists!=null)
		{
			deleteGameGroup(gameGroup);
		}
		
		String sql = "INSERT INTO GAMEGROUP ( USERID ) VALUES ( " + gameGroup.getUserId() + ")";
				
		log.info(sql);
		getJdbcTemplate().update(sql);
		gameGroup.setId(getLastGameGroupId());
		
		for (Dx4GameActivator gameActivator : gameGroup.getGameActivators())
			storeGameActivator(gameGroup,gameActivator);
	}
	
	private long getLastGameGroupId() {
		String sql = "SELECT MAX(ID) FROM GAMEGROUP";
		return getJdbcTemplate().queryForObject(sql,Long.class);
	}
	
	private Dx4GameGroup getGameGroup(Dx4GameGroup gameGroup)
	{
		String sql = "SELECT * FROM GAMEGROUP WHERE ID="+ gameGroup.getId();
		try
		{
			gameGroup = (Dx4GameGroup) getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Dx4GameGroup.class) );
			getGameActivators(gameGroup);
		}catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		return gameGroup;
	}
	
	public void deleteGameGroup(Dx4GameGroup gameGroup) {
		
		String sql = "DELETE FROM GAMEACTIVATOR WHERE GROUPID = " + gameGroup.getId();
		log.trace(sql);
		getJdbcTemplate().update(sql);
		
		sql = "DELETE FROM GAMEGROUP WHERE ID = " + gameGroup.getId();
		log.trace(sql);
		getJdbcTemplate().update(sql);
	}

	private void storeGameActivator(Dx4GameGroup gameGroup,
			Dx4GameActivator gameActivator) {
		Timestamp t1 = new Timestamp(gameActivator.getStartDate().getTime());
		Timestamp t2 = new Timestamp(gameActivator.getEndDate().getTime());
		String sql = "INSERT INTO GAMEACTIVATOR ( GROUPID, METAGAMEID, STARTDATE, ENDDATE ) VALUES ( " + gameGroup.getId()  
		+ "," + gameActivator.getMetaGame().getId() + ",'"
		+ t1.toString() + "','"
		+ t2.toString() + "')"; 
		
		log.info(sql);
		getJdbcTemplate().update(sql);
		gameActivator.setId(getLastGameActivator());
	}
	
	private long getLastGameActivator() {
		String sql = "SELECT MAX(ID) FROM GAMEACTIVATOR";
		return getJdbcTemplate().queryForObject(sql,Long.class);
	}
	
	@Override
	public Dx4GameGroup getGameGroup(Dx4SecureUser user)
	{
		String sql = "SELECT * FROM GAMEGROUP WHERE USERID="+ user.getSeqId();
		Dx4GameGroup gameGroup = getGameGroup(sql);
		if (gameGroup!=null)
			gameGroup.setUser(user);
		return gameGroup;
	}
	
	private Dx4GameGroup getGameGroup(String sql)
	{
		Dx4GameGroup gameGroup = null;
		try
		{
			gameGroup = (Dx4GameGroup) getJdbcTemplate().queryForObject(sql, 
					BeanPropertyRowMapper.newInstance(Dx4GameGroup.class) );
			getGameActivators(gameGroup);
		}catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		return gameGroup;
	}

	private void getGameActivators(Dx4GameGroup gameGroup) 
	{
		String sql = "SELECT * FROM GAMEACTIVATOR WHERE GROUPID = " + gameGroup.getId();
		log.trace("sql = "  + sql );
		
		List<Dx4GameActivator> gameActivators = getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(Dx4GameActivator.class));
		for (Dx4GameActivator gameActivator : gameActivators)
		{
			gameActivator.setMetaGame(get(gameActivator.getMetaGameId()));
			gameActivator.setGameGroup(gameGroup);
		}
		
		gameGroup.setGameActivators(gameActivators);
	}
	
	private void getMetaGameImages(Dx4MetaGame mg)
	{
		String sql = "SELECT * FROM METAGAMEIMG WHERE METAGAMEID = " + mg.getId();
		log.trace("sql = "  + sql );
		List<ImageHolder> images = getJdbcTemplate().query(sql,new ImageHolderRowMapper());
		for (ImageHolder ih : images)
			mg.getImages().put(ih.getName(), ih.getImage());
	}

	@Override
	public void storeMetaGameImage(final long metaGameId,final String name,final byte[] image)
	{
		try
		{
			getJdbcTemplate().update("DELETE FROM METAGAMEIMG WHERE NAME=?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1,name);
						}
					});
			
			getJdbcTemplate().update("INSERT INTO METAGAMEIMG (METAGAMEID,NAME,IMAGE) VALUES (?,?,?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setLong(1, metaGameId);
							ps.setString(2,name);
							ps.setBytes(3, image);
							
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
	public void updatePayOuts(Dx4Game game)
	{
		dx4GameDao.updatePayOuts(game);
	}

	@Override
	public void storeFxForPlayGame(FxForPlayGame fxpg) {
		dx4PlayGameDao.storeFxForPlayGame(fxpg);
	}

	@Override
	public FxForPlayGame getFxForPlayGame(long playGameId, String fromCcy, String toCcy) {
		return dx4PlayGameDao.getFxForPlayGame(playGameId, fromCcy, toCcy);
	}
	
}
