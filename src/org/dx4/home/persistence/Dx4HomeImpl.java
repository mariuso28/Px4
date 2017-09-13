package org.dx4.home.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.account.Dx4Rollup;
import org.dx4.account.Dx4Transaction;
import org.dx4.account.persistence.Dx4AccountDao;
import org.dx4.account.persistence.TransactionRowMapperPaginated;
import org.dx4.admin.Dx4Admin;
import org.dx4.admin.Dx4Version;
import org.dx4.admin.persistence.Dx4AdminDao;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.excel.StakeWin;
import org.dx4.agent.persistence.Dx4AgentDao;
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
import org.dx4.bet.persistence.Dx4MetaBetDao;
import org.dx4.bet.persistence.MetaBetExpoRowMapperPaginated;
import org.dx4.bet.persistence.MetaBetRowMapperPaginated;
import org.dx4.bet.persistence.NumberExpo;
import org.dx4.bet.persistence.WinNumber;
import org.dx4.external.persistence.DrawResultDao;
import org.dx4.external.persistence.Dx4NumberXDao;
import org.dx4.external.persistence.PayoutBand;
import org.dx4.external.support.NumberSearchEntry;
import org.dx4.external.support.NumberSearchTerm;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.FxForPlayGame;
import org.dx4.game.persistence.Dx4MetaGameDao;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4FavouriteJson;
import org.dx4.json.message.Dx4HoroscopeJson;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberRevenueJson;
import org.dx4.json.message.Dx4PlacingJson;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.json.message.Dx4ZodiacJson;
import org.dx4.player.Dx4Player;
import org.dx4.player.persistence.Dx4PlayerDao;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.persistence.Dx4SecureUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class Dx4HomeImpl implements Dx4Home {
	
		private static final Logger log = Logger.getLogger(Dx4HomeImpl.class);
		private Dx4PlayerDao dx4PlayerDao;
		private Dx4AdminDao dx4AdminDao;
		private Dx4MetaGameDao dx4MetaGameDao;
		private Dx4MetaBetDao dx4MetaBetDao;
		private DrawResultDao drawResultDao;
		private Dx4AgentDao dx4AgentDao;
		private Dx4SecureUserDao dx4SecureUserDao;
		private Dx4AccountDao dx4AccountDao;
		private Dx4NumberXDao dx4NumberXDao;
		
		@Autowired
		private JdbcTemplate jdbcTemplate;

		public void setDx4NumberXDao(Dx4NumberXDao dx4NumberXDao) {
			this.dx4NumberXDao = dx4NumberXDao;
		}

		public Dx4NumberXDao getDx4NumberXDao() {
			return dx4NumberXDao;
		}

		public Dx4SecureUserDao getDx4SecureUserDao() {
			return dx4SecureUserDao;
		}

		public void setDx4SecureUserDao(Dx4SecureUserDao dx4SecureUserDao) {
			this.dx4SecureUserDao = dx4SecureUserDao;
		}

		public Dx4MetaGameDao getDx4MetaGameDao() {
			return dx4MetaGameDao;
		}

		public void setDx4MetaGameDao(Dx4MetaGameDao dx4MetaGameDao) {
			this.dx4MetaGameDao = dx4MetaGameDao;
		}

		public Dx4MetaBetDao getDx4MetaBetDao() {
			return dx4MetaBetDao;
		}

		public void setDx4PlayerDao(Dx4PlayerDao dx4PlayerDao) {
			this.dx4PlayerDao = dx4PlayerDao;
		}

		public Dx4PlayerDao getDx4PlayerDao() {
			return dx4PlayerDao;
		}

		/**
		 * @param dx4AdminDao the dx4AdminDao to set
		 */
		public void setDx4AdminDao(Dx4AdminDao dx4AdminDao) {
			this.dx4AdminDao = dx4AdminDao;
		}

		/**
		 * @return the dx4AdminDao
		 */
		public Dx4AdminDao getDx4AdminDao() {
			return dx4AdminDao;
		}

		public Dx4AgentDao getDx4AgentDao() {
			return dx4AgentDao;
		}

		public void setDx4AgentDao(Dx4AgentDao dx4AgentDao) {
			this.dx4AgentDao = dx4AgentDao;
		}

		public void setDx4MetaBetDao(Dx4MetaBetDao dx4MetaBetDao) {
			this.dx4MetaBetDao = dx4MetaBetDao;
		}
		
		public void setDrawResultDao(DrawResultDao drawResultDao) {
			this.drawResultDao = drawResultDao;
		}

		public DrawResultDao getDrawResultDao() {
			return drawResultDao;
		}
		public void setDx4AccountDao(Dx4AccountDao dx4AccountDao) {
			this.dx4AccountDao = dx4AccountDao;
		}

		public Dx4AccountDao getDx4AccountDao() {
			return dx4AccountDao;
		}

		public Dx4HomeImpl()
		{
		}

		@Override
		public void overrideDataSourceUrl(String url) {
			BasicDataSource dataSource = (BasicDataSource) jdbcTemplate.getDataSource();
	        dataSource.setUrl(url);
		}
		
		
		@Override
		public void storeMetaGame(Dx4MetaGame game) throws PersistenceRuntimeException {
			dx4MetaGameDao.store(game);
		}

		@Override
		public List<Dx4MetaGame> getMetaGames() {
			List<Dx4MetaGame> metaGames = dx4MetaGameDao.getMetaGames();
			return metaGames;
		}

		@Override
		public Dx4MetaGame getMetaGame(String name) {
		
			return dx4MetaGameDao.get(name);	
		}

		@Override
		public void insertPlayerMetaBet(Dx4Player player,Dx4MetaBet metaBet) {
			metaBet.setPlayer(player);
			dx4MetaBetDao.insert(metaBet);
		}
		
		@Override
		public void setMetaBetPlaced(Dx4MetaBet metaBet)
		{
			dx4MetaBetDao.setPlaced(metaBet);
		}
		
		@Override
		public void updatePlayerMetaBets(Dx4Player player,Dx4PlayGame playGame) {
			for (Dx4MetaBet metaBet : player.getCurrentMetaBets())
			{
				dx4MetaBetDao.update(playGame,metaBet);
				metaBet.setProvidersFromCodes(this);
			}
		}

		@Override
		public Dx4Player getPlayerByUsername(String name) {
			
			Dx4Player player = dx4PlayerDao.getByUsername(name);
			if (player==null)
				return null;
			player.setParent(getParentForUser(player));
			List<Dx4MetaBet> currentMetaBets = getMetaBetsForPlayer(player,Dx4BetRetrieverFlag.CURRENT,null);
			player.setCurrentMetaBets(currentMetaBets);
			return player;
		}
		
		@Override
		public List<Dx4Player> getPlayersByPhone(String phone) {
			List<Dx4Player> players = dx4PlayerDao.getPlayersByPhone(phone);
			for (Dx4Player player : players)
			{
				player.setParent(getParentForUser(player));
				List<Dx4MetaBet> currentMetaBets = getMetaBetsForPlayer(player,Dx4BetRetrieverFlag.CURRENT,null);
				player.setCurrentMetaBets(currentMetaBets);
			}
			return players;
		}
			
		
		@Override
		public Dx4Player getPlayerById(long id)
		{
			Dx4Player player = dx4PlayerDao.getPlayerById(id);
			player.setParent(getParentForUser(player));
			List<Dx4MetaBet> currentMetaBets = getMetaBetsForPlayer(player,Dx4BetRetrieverFlag.CURRENT,null);
			player.setCurrentMetaBets(currentMetaBets);
			return player;
		}
		
		@Override
		public List<Dx4MetaBetExpo> getDx4MetaBetExpos(Dx4Agent agent,Dx4PlayGame playGame, List<Dx4MetaBetExpoOrder> ordering)
		{
			if (playGame==null)
				return new ArrayList<Dx4MetaBetExpo>();
			return dx4MetaBetDao.getDx4MetaBetExpos(agent,playGame,ordering);
		}
		
		@Override
		public MetaBetExpoRowMapperPaginated getMetaBetExpoRowMapperPaginated(Dx4Agent agent,Dx4PlayGame playGame, List<Dx4MetaBetExpoOrder> ordering, int pageSize)
		{
			if (playGame==null)
				return null;
			return new MetaBetExpoRowMapperPaginated( dx4MetaBetDao, pageSize, playGame,agent,ordering );
		}
		
		
		@Override
		public List<Dx4MetaBet> getMetaBetsForPlayer(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame) {
			
			List<Dx4MetaBet> dx4MetaBets = dx4MetaBetDao.getMetaBetsForPlayer(player,flag,metaGame);
			for (Dx4MetaBet metaBet : dx4MetaBets)
			{
				populateMetaBet(player,metaBet);
			}
			
			return dx4MetaBets;
		}
		
		@Override
		public void populateMetaBet(Dx4SecureUser player,Dx4MetaBet metaBet)
		{
			metaBet.setPlayer(player);
			Dx4MetaGame metaGame = getMetaGameById(metaBet.getMetaGameId());
			Dx4PlayGame playGame = metaGame.getPlayGameById(metaBet.getPlayGameId());
			metaBet.setMetaGame(metaGame);
			metaBet.setPlayGame(playGame);
			for (Dx4Bet bet : metaBet.getBets())
			{
				bet.setGame(metaGame.getGameById(bet.getGameId()));
			}
			metaBet.setProvidersFromCodes(this);
		}
		
		
		@Override
		public Dx4PlayGame getPlayGameById(long playGameId) {
			return dx4MetaGameDao.getPlayGameById(playGameId);
		}

		/*
		@Override
		public Dx4PlayGame getPlayGameByPlayDate(Dx4MetaGame metaGame,Date playDate) {
			return dx4MetaGameDao.getPlayGameByPlayDate(metaGame,playDate);
		}
		*/
		
		@Override
		public Dx4MetaGame getMetaGameById(long gameId) {

			return dx4MetaGameDao.get(gameId);
		}
		
		public void updatePayOuts(Dx4Game game)
		{
			dx4MetaGameDao.updatePayOuts(game);
		}

		@Override
		public Dx4Admin getAdminByUsername(String name) {
			Dx4Admin admin;
			try {
				admin = dx4AdminDao.getAdminByEmail(name);
			} catch (Dx4PersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new PersistenceRuntimeException(e.getMessage());
			}
			return admin;
		}

		@Override
		public void insertPlayGame(Dx4MetaGame metaGame, Dx4PlayGame playGame) {
			dx4MetaGameDao.insertPlayGame(playGame,metaGame);
		}

		@Override
		public void updatePlayGame(Dx4PlayGame playGame) 
		{
			log.info("JdbcDx4HomeImpl:updatePlayGame : " + playGame);
			dx4MetaGameDao.updatePlayGame(playGame);
		}

		@Override
		public List<Dx4MetaGame> getUnplayedMetaGames() {
			return dx4MetaGameDao.getUnplayedMetaGames();
		}
		
		@Override
		public Dx4MetaBet getMetaBetById(Dx4Player player,Long id) {
			Dx4MetaBet metaBet = dx4MetaBetDao.getMetaBetById(id);
			if (metaBet!=null)
				populateMetaBet(player,metaBet);
			return metaBet;
		}
		
		@Override
		public List<Dx4MetaBet> getAllMetaBetsForMetaGame(Dx4MetaGame metaGame, Dx4PlayGame playGame) {
			List<Dx4MetaBet> metaBetList = dx4MetaBetDao.getAllMetaBetsForMetaGame(metaGame,playGame);
			resolveGamesForBets(metaBetList,metaGame);
			return metaBetList;
		}


		
		private void resolveGamesForBets(List<Dx4MetaBet> metaBetList,Dx4MetaGame metaGame)
		{
			for (Dx4MetaBet metaBet : metaBetList)
			{
				for (Dx4Bet bet : metaBet.getBets())
					bet.setGame(metaGame.getGameById(bet.getGameId()));
			}
		}
		/*
		@Override
		public Dx4Game getGameById(long gameId) {
			return dx4MetaGameDao.getGameById(gameId);
		}
*/
		@Override
		public void setPlayed(Dx4PlayGame playGame)
		{
			dx4MetaBetDao.setPlayed(playGame);
		}
		
		@Override
		public void updateMetaBet(Dx4PlayGame playGame,Dx4MetaBet metaBet)
		{
			dx4MetaBetDao.update(playGame,metaBet);
		}

		@Override
		public void storeDrawResult(Dx4DrawResultJson result) {
			drawResultDao.store(result);
		}

		@Override
		public void removeResultsForDate(Date date) {
			drawResultDao.removeResultsForDate(date);
		}
		
		@Override
		public List<Dx4DrawResultJson> getDrawResultsForProvider(String provider) {
			return drawResultDao.getResultsForProvider(provider);
		}

		@Override
		public List<Dx4DrawResultJson> getDrawResults(Date startDate,Date endDate) {
			return drawResultDao.getResults(startDate,endDate);
		}

		@Override
		public Dx4DrawResultJson getDrawResultForId(long id) {
			return drawResultDao.getResult(id);
		}
		
		@Override
		public void populateDrawResultSpecialsAndConsolations(Dx4DrawResultJson result)
		{
			drawResultDao.populateSpecialsAndConsolations(result);
		}
		
		@Override
		public void populateDrawResults(List<Dx4DrawResultJson> results)
		{
			List<Dx4ProviderJson> providers = getProviders();
			for (Dx4DrawResultJson result : results)
			{
				populateDrawResultSpecialsAndConsolations(result);
				getDescsForDrawResult(result);
				getImagesForDrawResult(result);
				for (Dx4ProviderJson provider : providers)
				{
					if (result.getProviderId()==provider.getId())
					{
						result.setProvider(provider);
					}
				}
			}
		}
		
		@Override
		public void getImagesForDrawResult(Dx4DrawResultJson result) {
			Dx4NumberPageElementJson npe = getNumberPageElement(result.getFirstPlace(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
			result.setFirstImage(npe.getImage());
			npe = getNumberPageElement(result.getSecondPlace(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
			result.setSecondImage(npe.getImage());
			npe = getNumberPageElement(result.getThirdPlace(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
			result.setThirdImage(npe.getImage());
		}

		@Override
		public List<Date> getDrawResultsDateRange() {
			
			return drawResultDao.getResultsDateRange();
		}

		@Override
		public List<Dx4DrawResultJson> getLatestDrawResults() {
			List<Dx4DrawResultJson> drawResults = drawResultDao.getLatestDrawResults();
			populateDrawResults(drawResults);
			return drawResults;
		}
		
		@Override
		public List<Dx4DrawResultJson> getDrawResultsForNumber(String number,
				String type, Date currStartDate, Date currEndDate) {
			log.trace("Getting results for number : " + number + "gridType: " + type);
			List<Dx4DrawResultJson> drawResults;
			if (type.equals("4"))
				drawResults = drawResultDao.getResultsForNumber(number, currStartDate, currEndDate);
			else
				drawResults = drawResultDao.getResultsForNumberPart(number, currStartDate, currEndDate);
			populateDrawResults(drawResults);
			return drawResults;
		}
		
		@Override
		public Date getPrevDrawDate(Date date)
		{
			return drawResultDao.getPrevDrawDate(date);
		}
		
		@Override
		public Date  getNextDrawDate(Date date)
		{
			return drawResultDao.getNextDrawDate(date);
		}

		@Override
		public void storePlayer(Dx4Player player,Dx4SecureUser parent)
		 {
			player.setParentCode(parent.getCode());
			dx4PlayerDao.store(player);
			player.setSeqId(dx4SecureUserDao.getSeqIdForId(player.getId()));
			dx4AccountDao.store(player);	
			dx4MetaGameDao.storeGameGroup(player);
		}

		@Override
		public void storeAdmin(Dx4Admin admin){
			dx4AdminDao.store(admin);
			admin.setSeqId(dx4SecureUserDao.getSeqIdForId(admin.getId()));
			dx4AccountDao.store(admin);	
			dx4MetaGameDao.storeGameGroup(admin);
		}
		
		@Override
		public void updateVersion(Dx4Version version) {
			dx4AdminDao.updateVersion(version);
		}

		@Override
		public Dx4Version getVersion() {
				return dx4AdminDao.getVersion();
		}

		public String getVersionCode()
		{
			return dx4AdminDao.getVersionCode();
		}

		@Override
		public void getDownstreamForParent(Dx4SecureUser parent) {
			dx4SecureUserDao.getDownstreamForParent(parent);
		}

		@Override
		public void storeAgent(Dx4Agent agent,Dx4SecureUser parent)
		{
			agent.setParentCode(parent.getCode());
			dx4AgentDao.store(agent);
			agent.setSeqId(dx4SecureUserDao.getSeqIdForId(agent.getId()));
			dx4AccountDao.store(agent);	
			dx4AccountDao.storeDefaultNumberExpos(agent);
			dx4MetaGameDao.storeGameGroup(agent);
		}

		@Override
		public Dx4Agent getAgentByUsername(String name) throws Dx4PersistenceException {
		
			Dx4Agent agent = dx4AgentDao.getAgentByEmail(name);
			return agent;
		}
		
		@Override
		public void updateUserProfile(Dx4SecureUser user) throws Dx4PersistenceException {
			dx4SecureUserDao.updateBaseUserProfile(user);
		}
		
		@Override
		public void updateEnabled(Dx4SecureUser user) throws Dx4PersistenceException
		{
			dx4SecureUserDao.updateBaseUserProfile(user);
		}
		
		@Override
		public Dx4SecureUser getUserByCode(String code) throws PersistenceRuntimeException
		{
			try
			{
				return dx4SecureUserDao.getBaseUserByCode(code);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new PersistenceRuntimeException(e.getMessage());
			}
		}
		
		@Override
		public void storeAccount(Dx4SecureUser user) {
			dx4AccountDao.store(user);
		}

		@Override
		public void getAccount(Dx4SecureUser user) {
			dx4AccountDao.getForUser(user);
		}

		@Override
		public void updateAccount(Dx4SecureUser user) {
			dx4AccountDao.update(user);
		}

		@Override
		public void performWithdrawl(Dx4SecureUser user,double amount,Date date)
		{
			dx4AccountDao.performWithdrawl(user, amount, date);
		}
		
		@Override
		public void performDeposit(Dx4SecureUser user,double amount,Date date)
		{
			dx4AccountDao.performDeposit(user, amount, date);
		}
		
		@Override
		public Dx4Transaction performPayment(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId)
		{
			return dx4AccountDao.performPayment(user, cp, amount, date, refId);
		}
		
		@Override
		public Dx4Transaction performCollect(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId)
		{
			return dx4AccountDao.performCollect(user, cp, amount, date, refId);
		}
		
		@Override
		public Dx4Rollup getRollup(Dx4SecureUser user)
		{
			return dx4AccountDao.getRollup(user);
		}
		
		@Override
		public Dx4SecureUser getParentForUser(Dx4SecureUser user){
			
			if (user.getParentCode()==null || user.getParentCode().isEmpty())
				return null;
			return getUserByCode(user.getParentCode());
		}


		@Override
		public Dx4MetaBet getMetaBetForTransaction(Dx4Transaction trans) throws Dx4PersistenceException {
			Dx4MetaBet metaBet = dx4MetaBetDao.getMetaBetForTransaction(trans);

			metaBet.setPlayer(getBySeqId(metaBet.getPlayerId()));
			metaBet.setCp(getBySeqId(metaBet.getCpId()));
			Dx4MetaGame metaGame = getMetaGameById(metaBet.getMetaGameId());
			Dx4PlayGame playGame = metaGame.getPlayGameById(metaBet.getPlayGameId());
			metaBet.setMetaGame(metaGame);
			metaBet.setPlayGame(playGame);
			for (Dx4Bet bet : metaBet.getBets())
			{
				bet.setGame(metaGame.getGameById(bet.getGameId()));
			}

			return metaBet;
		}

		@Override
		public Dx4Transaction getTransactionForId(long id) {
			return dx4AccountDao.getTransactionForId(id);

		}
		
		@Override
		public Dx4SecureUser getBySeqId(long id) throws Dx4PersistenceException {
			return dx4SecureUserDao.getBaseUserBySeqId(id);
		}

		@Override
		public Dx4SecureUser getBySeqId(long id,@SuppressWarnings("rawtypes") Class clazz) throws Dx4PersistenceException {
			return dx4SecureUserDao.getBaseUserBySeqId(id,clazz);
		}
		
		@Override
		public void updateAccountBalance(Dx4Account account)
		{
			dx4AccountDao.updateBalance(account);
		}
		
		@Override
		public String getNextDrawNoForProvider(String provider)
		{
			return drawResultDao.getNextDrawNoForProvider(provider);
		}
		
		@Override
		public List<Dx4NumberRevenueJson> getNumberRevenues(Date startDate,Date endDate)
		{
			return drawResultDao.getNumberRevenues(startDate,endDate);
		}
		
		@Override
		public double getRevenueForNumber(String number,Date startDate,Date endDate)
		{
			return drawResultDao.getRevenueForNumber(number,startDate,endDate);
		}
		
		@Override
		public List<Dx4PlacingJson> getPlacingsForNumber(String number,Date startDate,Date endDate)
		{
			return drawResultDao.getPlacingsForNumber(number,startDate,endDate);
		}
		
		@Override
		public String getDescForNumber(String number)
		{
			return dx4NumberXDao.getDescForNumber(number);
		}
		
		@Override
		public Dx4NumberPageElementJson getNumberPageElement(String number, Character dictionary)
		{
			return dx4NumberXDao.getNumberPageElement(number,dictionary);
		}
		
		@Override
		public List<Date> getDrawDates()
		{
			return drawResultDao.getDrawDates();
		}
		
		@Override
		public void getDescsForDrawResult(Dx4DrawResultJson result)
		{
			dx4NumberXDao.getDescsForDrawResult(result);
		}
		
		@Override
		public NumberExpo getSingleNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure,String number)
		{
			if (playGame==null)
				return null;
			return dx4MetaBetDao.getSingleNumberExpo(parentCode,playGame,role,maxExpo,exposure,number);
		}
		
		@Override
		public List<NumberExpo> getNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure)
		{
			if (playGame==null)
				return new ArrayList<NumberExpo>();
			List<NumberExpo> results = dx4MetaBetDao.getNumberExpo(parentCode,playGame,role,maxExpo,exposure);
			return results;
		}
		
		@Override
		public Dx4GameGroup getGameGroup(Dx4SecureUser user)
		{
			return dx4MetaGameDao.getGameGroup(user);
		}
		
		@Override
		public void storeGameGroup(Dx4GameGroup group)
		{
			dx4MetaGameDao.storeGameGroup(group);
		}
		
		@Override
		public List<Dx4PlayGame> getNextPlayGamesForUser(Dx4SecureUser user)
		{
			List<Dx4PlayGame> playGames = new ArrayList<Dx4PlayGame>();
			Dx4GameGroup group = getGameGroup(user);
			for (Dx4GameActivator ga : group.getGameActivators())
			{
				Dx4PlayGame playGame = ga.getMetaGame().getNextGameAvailableForBet();
				if (playGame==null)
					continue;
				playGames.add(playGame);
			}
			return playGames;
		}
		
		@Override
		public void storeDx4NumberExpo(Dx4NumberExpo numberExpo,char digits)
		{
			dx4AccountDao.storeDx4NumberExpo(numberExpo,digits);
		}
		
		@Override
		public void deleteDx4NumberExpo(Dx4NumberExpo numberExpo,char digits)
		{
			dx4AccountDao.deleteDx4NumberExpo(numberExpo,digits);
		}
		
		@Override
		public void udateDx4NumberExpo(Dx4NumberExpo numberExpo,char digits)
		{
			dx4AccountDao.udateDx4NumberExpo(numberExpo,digits);
		}
		
		@Override
		public List<Dx4NumberExpo> getDx4NumberExposForUser(Dx4SecureUser user,char digits,boolean winOrder)
		{
			return dx4AccountDao.getDx4NumberExposForUser(user,digits,winOrder);
		}
		
		@Override
		public Dx4NumberExpo getDx4DefaultNumberExpoForUser(Dx4SecureUser user,char digits)
		{
			return dx4AccountDao.getDx4DefaultNumberExpoForUser(user,digits);
		}
		
		@Override
		public Dx4NumberExpo getDx4NumberExpoForUser(Dx4SecureUser user,String number)
		{
			return dx4AccountDao.getDx4NumberExpoForUser(user,number);
		}
		
		@Override
		public Dx4ProviderJson getProviderByCode(Character code)
		{
			return dx4MetaBetDao.getProviderByCode(code);
		}
		
		@Override
		public List<Dx4ProviderJson> getProvidersFromCodes(String codes) {
			List<Dx4ProviderJson> providers = new ArrayList<Dx4ProviderJson>();
			for (int i=0; i<codes.length(); i++)
				providers.add(getProviderByCode(codes.charAt(i)));
			return providers;
		}

		@Override
		public List<Dx4ProviderJson> getProviders()
		{
			return dx4MetaBetDao.getProviders();
		}
		
		@Override
		public Dx4ProviderJson getProviderByName(String name)
		{
			return dx4MetaBetDao.getProviderByName(name);
		}
		
		@Override
		public void deleteMember(Dx4SecureUser user) throws Dx4PersistenceException
		{
			dx4SecureUserDao.deleteMember(user);
		}

		@Override
		public String getCodeForId(long id) throws Dx4PersistenceException {
			return dx4SecureUserDao.getCodeForSeqId(id);
		}
		
		@Override
		public TransactionRowMapperPaginated getXTransactionRowMapperPaginated(long userId, int pageSize )
		{
			return new TransactionRowMapperPaginated(getDx4AccountDao(),userId,pageSize );
		}
		
		@Override
		public MetaBetRowMapperPaginated getMetaBetRowMapperPaginated(int pageSize, Dx4Player player, Dx4BetRetrieverFlag flag, Dx4MetaGame metaGame  )
		{
			return new  MetaBetRowMapperPaginated( getDx4MetaBetDao(), pageSize, player, flag, metaGame );
		}

		@Override
		public List<Dx4NumberWin> getWinsForDate(Dx4SecureUser user,Date drawDate)
		{
			return dx4MetaBetDao.getWinsForDate(user,drawDate);
		}
		
		@Override
		public List<Dx4DateWin> getWinDates(Dx4SecureUser user)
		{
			return dx4MetaBetDao.getWinDates(user);
		}
		
		public List<Dx4Win> getAgentWinsForDate(Dx4SecureUser user,Date drawDate)
		{
			List<Dx4Win> wins = dx4MetaBetDao.getAgentWinsForDate(user,drawDate);
			List<Dx4Win> retWins = new ArrayList<Dx4Win>();
			for (Dx4Win win : wins)					// long winded but make sure wins fully populated
			{
				Dx4MetaBet metaBet = getMetaBetById(win.getMetaBetId());
				win.setMetaBet(metaBet);
				win.setBet(metaBet.getBetById(win.getBetId()));
				retWins.add(metaBet.getWinById(win.getId()));
			}
			return retWins;
		}
		
		private Dx4MetaBet getMetaBetById(Long id) {
			Dx4MetaBet metaBet = dx4MetaBetDao.getMetaBetById(id);
			Dx4Player player = getPlayerById(metaBet.getPlayerId());
			populateMetaBet(player,metaBet);
			return metaBet;
		}
		
		public Double getTotalWinsForDate(Dx4SecureUser user,Date drawDate)
		{
			return dx4MetaBetDao.getTotalWinsForDate(user,drawDate);
		}
		
		public List<Dx4WinNumberSummary> getWinNumberSummary(String number,Character providerCode,Date drawDate)
		{
			return dx4MetaBetDao.getWinNumberSummary(number,providerCode,drawDate);
		}
		
		@Override
		public List<Dx4BetRollup> getTotalBetRollups(long playgameId, int digits) throws DataAccessException
		{
			return dx4MetaBetDao.getTotalBetRollups(playgameId, digits);
		}

		@Override
		public List<Dx4BetRollup> getTotalBetRollups(long playgameId, int digits, char providerCode) throws DataAccessException
		{
			return dx4MetaBetDao.getTotalBetRollups(playgameId, digits, providerCode);
		}

		@Override
		public Map<Date, StakeWin> getTotalWinStakes() {
			return dx4MetaBetDao.getTotalWinStakes();
		}
		
		@Override
		public List<Dx4NumberPageElementJson> getNumberPageElements()
		{
			return dx4NumberXDao.getNumberPageElements();
		}
		
		public List<Dx4NumberPageElementJson> getNumberPageElements(String number)
		{
			return dx4NumberXDao.getNumberPageElements(number);
		}
		
		@Override
		public void updateImage(String code, byte[] image)
		{
			dx4NumberXDao.updateImage(code, image);
		}
		
		@Override
		public void updateNumberExpoBlocked(Dx4NumberExpo numberExpo)
		{
			dx4MetaBetDao.updateNumberExpoBlocked(numberExpo);
		}
		
		@Override
		public void resetNumberExposBlocked()
		{
			dx4MetaBetDao.resetNumberExposBlocked();
		}
		
		@Override
		public List<String> getBlockedNumbers(Dx4MetaBet metaBet)
		{
			return dx4MetaBetDao.getBlockedNumbers(metaBet);
		}
		
		@Override
		public List<Dx4NumberPageElementJson> getNumberPageElementsByDesc(String searchTerm)
		{
			return dx4NumberXDao.getNumberPageElementsByDesc(searchTerm);
		}
		
		@Override
		public List<Dx4NumberPageElementJson> getNumberPageElementsRange(int num1, int num2, Character dictionary)
		{
			return dx4NumberXDao.getNumberPageElementsRange(num1,num2,dictionary);
		}
		
		@Override
		public void storeNumberSearchTerm(final NumberSearchTerm nst)
		{
			dx4NumberXDao.storeNumberSearchTerm(nst);
		}
		
		@Override
		public List<NumberSearchEntry> getNumbersFormTerm(String term)
		{
			return dx4NumberXDao.getNumbersFormTerm(term);
		}
		
		@Override
		public void cleanUpBetsForPlayDate(Date drawDate)
		{
			dx4MetaBetDao.cleanUpBetsForPlayDate(drawDate);
		}

		@Override
		public Dx4SecureUser getByUsername(String username, @SuppressWarnings("rawtypes") Class userClass) throws Dx4PersistenceException {
			return dx4SecureUserDao.getBaseUserByEmail(username,userClass);
		}

		@Override
		public String getBaseUserEmailBySeqId(final long id) throws Dx4PersistenceException
		{
			return dx4SecureUserDao.getBaseUserEmailBySeqId(id);
		}
		
		@Override
		public void setDefaultPasswordForAll(String encoded) {
			dx4SecureUserDao.setDefaultPasswordForAll(encoded);
		}

		@Override
		public void updateProviderImage(String code, byte[] image) {
			dx4MetaGameDao.updateProviderImage(code, image);
		}

		@Override
		public byte[] getRawImageForNumber(String number) {
			return dx4NumberXDao.getRawImageForNumber(number);
		}
		
		@Override
		public void storeDx4NumberPageElementJson(Dx4NumberPageElementJson npe, byte[] image)
		{
			dx4NumberXDao.storeDx4NumberPageElementJson(npe,image);
		}

		@Override
		public void removeResultsForProviderDate(Date date, char c) {
			drawResultDao.removeResultsForProviderDate(date,c);
		}

		@Override
		public void storeFavourite(Dx4FavouriteJson favourite,long betId) {
			dx4PlayerDao.storeFavourite(favourite,betId);
		}

		@Override
		public void deleteFavourite(long favouriteId) {
			dx4PlayerDao.deleteFavourite(favouriteId);
		}

		@Override
		public List<Dx4FavouriteJson> getFavourites(UUID playerId) {
			 return dx4PlayerDao.getFavourites(playerId);
		}

		@Override
		public void addNumber4D(String number) {
			dx4NumberXDao.addNumber4D(number);
		}
		
		@Override
		public String getRandom(long playgameId, int digits)
		{
			return dx4NumberXDao.getRandom(playgameId, digits);
		}

		@Override
		public void storeMetaGameImage(long metaGameId, String name, byte[] image) {
			dx4MetaGameDao.storeMetaGameImage(metaGameId, name, image);
		}

		@Override
		public Dx4Bet getBetById(long betId) {
			return dx4MetaBetDao.getBetById(betId);
		}

		@Override
		public void storeZodiacImage(String animal,int set, int year, byte[] image) {
			dx4NumberXDao.storeZodiacImage(animal, set, year, image);
		}
		
		@Override
		public List<Dx4ZodiacJson> getZodiacs(int set)
		{
			return dx4NumberXDao.getZodiacs(set);
		}

		@Override
		public void storeHoroscope(Dx4HoroscopeJson hj, byte[] image) {
			dx4NumberXDao.storeHoroscope(hj,image);
		}

		@Override
		public List<Dx4HoroscopeJson> getHoroscopes() {
			return dx4NumberXDao.getHoroscopes();
		}
		
		@Override
		public List<WinNumber> getWinNumber(Dx4ProviderJson provider, long playGameId)
		{
			return dx4MetaBetDao.getWinNumber(provider, playGameId);
		}

		@Override
		public void storeMetabetId(UUID id) {
			dx4MetaBetDao.storeMetabetId(id);
		}

		@Override
		public void initializeNumberFloatPayouts(Date lastDrawDate) {
			dx4NumberXDao.initializeNumberFloatPayouts(lastDrawDate);
		}

		@Override
		public void updateNumberFloatPayout(Dx4NumberFloatPayoutJson nfp) {
			dx4NumberXDao.updateNumberFloatPayout(nfp);
		}

		@Override
		public List<PayoutBand> createPayoutBands() {
			return dx4NumberXDao.createPayoutBands();
		}

		@Override
		public Dx4NumberFloatPayoutJson getDx4NumberFloatPayoutJson(String number) {
			return dx4NumberXDao.getDx4NumberFloatPayoutJson(number);
		}

		@Override
		public void insertNumberFloatPayout(Dx4NumberFloatPayoutJson nfp) {
			dx4NumberXDao.insertNumberFloatPayout(nfp);
		}

		@Override
		public List<Dx4BetNumberPayout> getBetNumberPayouts(Dx4Player player) {
			return dx4MetaBetDao.getBetNumberPayouts(player);
		}

		@Override
		public void deleteBetNumberPayouts(Dx4Player player) {
			dx4MetaBetDao.deleteBetNumberPayouts(player);
		}

		@Override
		public void storeBetNumberPayout(Dx4Player player, Dx4BetNumberPayout bnp) {
			dx4MetaBetDao.storeBetNumberPayout(player, bnp);
		}

		@Override
		public Dx4NumberFloatPayoutJson getWorstDx4NumberFloatPayoutForBand(int band, Dx4NumberFloatPayoutJson nfp) {
			return dx4NumberXDao.getWorstDx4NumberFloatPayoutForBand(band, nfp);
		}

		@Override
		public List<Dx4NumberFloatPayoutJson> getDx4NumberFloatPayoutTrending(int limit) {
			return dx4NumberXDao.getDx4NumberFloatPayoutTrending(limit);
		}

		@Override
		public List<PayoutBand> createPayoutBands3() {
			return dx4NumberXDao.createPayoutBands3();
		}

		@Override
		public void storeFxForPlayGame(FxForPlayGame fxpg) {
			dx4MetaGameDao.storeFxForPlayGame(fxpg);
		}

		@Override
		public FxForPlayGame getFxForPlayGame(long playGameId, String fromCcy, String toCcy) {
			return dx4MetaGameDao.getFxForPlayGame(playGameId, fromCcy, toCcy);
		}

		@Override
		public void initializeAdminProperties(Dx4Admin admin) {
			dx4AdminDao.initializeAdminProperties(admin);
		}

		
		@Override
		public void performPatches(Dx4MetaGame mg)
		{
//			removeBetsForUndrawnSingapore();					 // Should remove for next release
			
			String sql = "drop table metabetunique";
			String sql2 = "CREATE TABLE public.metabetunique " +				
							  "( id uuid NOT NULL, " +
							  "CONSTRAINT pk_metabetunique PRIMARY KEY (id)) WITH (OIDS=FALSE)";
			try
			{
				jdbcTemplate.update(sql);
			}
			catch (Exception e)									// fail if doens't exist
			{
				log.warn("Error on executing sql " + e.getMessage() + " -  ok.");
			}
			try
			{
				jdbcTemplate.update(sql2);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				log.fatal("Error on executing sql " + e.getMessage());
				System.exit(9);
			}
			
			if (!hasColumn("admin","versioncode"))
			{
				sql2 = "ALTER TABLE admin ADD COLUMN versioncode CHARACTER VARYING(64) NOT NULL DEFAULT '1.0 Fixed'";
				try
				{
					jdbcTemplate.update(sql2);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					log.fatal("Error on executing sql " + e.getMessage());
					System.exit(9);
				}
			}
			if (!hasColumn("admin","apk"))
			{
				log.info("Patching admin for apk");
				sql2 = "ALTER TABLE admin ADD COLUMN apk BYTEA";
				try
				{
					jdbcTemplate.update(sql2);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					log.fatal("Error on executing sql " + e.getMessage());
					System.exit(9);
				}
			}
			
			if (!hasColumn("provider","url"))
			{
				patchProvidersUrl();
			}
			
			try {
				if (dx4AdminDao.getAdminProperties()==null)
				{
					log.info("Patching admin for initial values");
					List<Dx4SecureUser> users = dx4SecureUserDao.getBaseUsersByRole(Dx4Role.ROLE_ADMIN);
					dx4AdminDao.initializeAdminProperties((Dx4Admin) users.get(0));
				}
			} catch (Dx4PersistenceException e) {
				log.fatal("Error getting admin properties " + e.getMessage());
				System.exit(9);
			}
			
		//	dx4MetaBetDao.fixBetsAndRollups(mg);
			/*
			if (!hasColumn("bet","totalstake"))
			{
				sql = "alter table bet add column totalstake double precision not null default 0.0";
				try
				{
					jdbcTemplate.update(sql);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					log.fatal("Error on executing sql " + e.getMessage());
					System.exit(9);
				}
				dx4MetaBetDao.calcRetroTotalStakes(mg);
				
			}
			 */
		}
		
		private void patchProvidersUrl() {
			log.info("Patching providers for urls");
			try
			{
				jdbcTemplate.update("ALTER TABLE provider ADD COLUMN url varchar(255)");
				jdbcTemplate.update("UPDATE provider SET url='http://www.magnum4d.my' WHERE code = 'M'");
				jdbcTemplate.update("UPDATE provider SET url='https://www.damacai.com.my' WHERE code = 'K'");
				jdbcTemplate.update("UPDATE provider SET url='http://www.sportstoto.com.my' WHERE code = 'T'");
				jdbcTemplate.update("UPDATE provider SET url='http://www.singaporepools.com.sg/en/product/Pages/4d_results.aspx' WHERE code = 'S'");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				log.fatal("Error on executing sql " + e.getMessage());
				System.exit(9);
			}
		}

		@SuppressWarnings("unused")
		private void removeBetsForUndrawnSingapore() throws DataAccessException
		{
			// invalid would need the metabet adjustment too
			// leave playgame=10 out of stats instead
			String sql = "delete from bet where position('S' in providercodes) > 0 and metabetid in (select id from metabet where playgameid = 10)";
			jdbcTemplate.update(sql);
			sql = "delete from betrollup where playgameid = 10 and providercode = 'S'";
			jdbcTemplate.update(sql);
		}
		
		private boolean hasColumn(String table,String columnName)
		{
			List<String> cols = getColumns(table);
			return cols.contains(columnName.toLowerCase());
		}
		
		private List<String> getColumns(String table){

			List<String> cols = new ArrayList<String>();
	        try {
	            // Gets the metadata of the database
	        	Connection conn = jdbcTemplate.getDataSource().getConnection();
	            DatabaseMetaData dbmd = conn.getMetaData();

	            ResultSet rs = dbmd.getColumns(null, null, table, null);
	                while (rs.next()) {
	                    String colummName = rs.getString("COLUMN_NAME");
	                    cols.add(colummName.toLowerCase());
	            }
	            conn.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
				log.fatal("Error on executing sql " + e.getMessage());
				System.exit(9);
	        }
	        return cols;
	    }

}

