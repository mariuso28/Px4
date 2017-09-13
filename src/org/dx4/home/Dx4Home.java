package org.dx4.home;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dx4.account.Dx4Account;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.account.Dx4Rollup;
import org.dx4.account.Dx4Transaction;
import org.dx4.account.persistence.TransactionRowMapperPaginated;
import org.dx4.admin.Dx4Admin;
import org.dx4.admin.Dx4Version;
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
import org.dx4.bet.persistence.MetaBetExpoRowMapperPaginated;
import org.dx4.bet.persistence.MetaBetRowMapperPaginated;
import org.dx4.bet.persistence.NumberExpo;
import org.dx4.bet.persistence.WinNumber;
import org.dx4.external.persistence.PayoutBand;
import org.dx4.external.support.NumberSearchEntry;
import org.dx4.external.support.NumberSearchTerm;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.FxForPlayGame;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
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
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.springframework.dao.DataAccessException;


public interface Dx4Home {
	
	public Dx4SecureUser getByUsername(String username,@SuppressWarnings("rawtypes") Class userClass) throws Dx4PersistenceException;
	
	public void storePlayer(Dx4Player player,Dx4SecureUser parent);
	public Dx4Player getPlayerByUsername(String username);
	public Dx4Player getPlayerById(long id);
	public List<Dx4Player> getPlayersByPhone(String phone);
	public void storeFavourite(Dx4FavouriteJson favourite,long betId);
	public void deleteFavourite	(long favouritId);
	public List<Dx4FavouriteJson> getFavourites(UUID playerId);
	
	
	public List<Dx4MetaBet> getMetaBetsForPlayer(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame);
	public List<Dx4MetaBetExpo> getDx4MetaBetExpos(Dx4Agent agent,
			Dx4PlayGame playGame, List<Dx4MetaBetExpoOrder> ordering);
	public MetaBetExpoRowMapperPaginated getMetaBetExpoRowMapperPaginated(Dx4Agent agent,Dx4PlayGame playGame, 
						List<Dx4MetaBetExpoOrder> ordering, int pageSize);
	public Dx4MetaBet getMetaBetForTransaction(Dx4Transaction transaction) throws Dx4PersistenceException;
	public void populateMetaBet(Dx4SecureUser player,Dx4MetaBet metaBet);
	public MetaBetRowMapperPaginated getMetaBetRowMapperPaginated(int pageSize, Dx4Player player, Dx4BetRetrieverFlag flag, Dx4MetaGame metaGame  );
	public void updatePlayerMetaBets(Dx4Player player,Dx4PlayGame playGame);
	public void insertPlayerMetaBet(Dx4Player player, Dx4MetaBet metaBet);
	public void setMetaBetPlaced(Dx4MetaBet metaBet);	
	public List<Dx4MetaBet> getAllMetaBetsForMetaGame(Dx4MetaGame metaGame, Dx4PlayGame playGame);
	public void setPlayed(Dx4PlayGame playGame);
	public void updateMetaBet(Dx4PlayGame playGame,Dx4MetaBet metaBet);
	public List<Dx4BetRollup> getTotalBetRollups(long playgameId, int digits) throws DataAccessException;
	public List<Dx4BetRollup> getTotalBetRollups(long playgameId, int digits, char providerCode) throws DataAccessException;
	public Dx4Bet getBetById(long betId);
	public void storeMetabetId(UUID id);
	
	public void storeMetaGame(Dx4MetaGame game) throws PersistenceRuntimeException;
	public List<Dx4MetaGame> getMetaGames();
	public List<Dx4MetaGame> getUnplayedMetaGames();
	public Dx4MetaGame getMetaGame(String name);
	public void insertPlayGame(Dx4MetaGame metaGame,Dx4PlayGame playGame);
	public void updatePlayGame(Dx4PlayGame playGame);
	public Dx4MetaGame getMetaGameById(long gameId);
	public void updateProviderImage(String code, byte[] image);
	public void storeMetaGameImage(final long metaGameId,final String name,final byte[] image);
	public void updatePayOuts(Dx4Game game);
	public void storeFxForPlayGame(final FxForPlayGame fxpg);
	public FxForPlayGame getFxForPlayGame(long playGameId,String fromCcy,String toCcy);
	
	
	public Dx4Admin getAdminByUsername(String name);
	public void storeAdmin(Dx4Admin admin);
	
	public Dx4PlayGame getPlayGameById(long playGameId);
	/*
	public Dx4PlayGame getPlayGameByPlayDate(Dx4MetaGame metaGame,Date playDate);
	public Dx4Game getGameById(long gameId);
	*/
	public void storeDrawResult(Dx4DrawResultJson result);
	public List<Dx4DrawResultJson> getDrawResultsForProvider(String provider);
	public List<Dx4DrawResultJson> getDrawResults(Date startDate, Date endDate);
	public List<Date> getDrawResultsDateRange();
	public List<Dx4DrawResultJson> getDrawResultsForNumber(String number, String type,Date currStartDate, Date currEndDate);
	public Dx4DrawResultJson getDrawResultForId(long id);
	public void populateDrawResultSpecialsAndConsolations(Dx4DrawResultJson result);
	public void populateDrawResults(List<Dx4DrawResultJson> results);
	public Date getPrevDrawDate(Date date);
	public Date  getNextDrawDate(Date date);
	public List<Dx4DrawResultJson> getLatestDrawResults();	
	
	public void getDownstreamForParent(Dx4SecureUser parent); 
	public void storeAgent(Dx4Agent agent, Dx4SecureUser currUser);
	public Dx4Agent getAgentByUsername(String name) throws Dx4PersistenceException;
	
	public void updateUserProfile(Dx4SecureUser user) throws Dx4PersistenceException;
	public void updateEnabled(Dx4SecureUser user) throws Dx4PersistenceException;
	public String getBaseUserEmailBySeqId(final long id) throws Dx4PersistenceException;
	public Dx4SecureUser getUserByCode(String code) throws Dx4PersistenceException;
	public Dx4SecureUser getBySeqId(long id,@SuppressWarnings("rawtypes") Class clazz) throws Dx4PersistenceException;
	public Dx4SecureUser getBySeqId(long id) throws Dx4PersistenceException;
	public String getCodeForId(long id) throws Dx4PersistenceException;
	public Dx4SecureUser getParentForUser(Dx4SecureUser user) throws Dx4PersistenceException;
	public void performPatches(Dx4MetaGame dx4MetaGame);
	
	public void storeAccount(Dx4SecureUser user);
	public void getAccount(Dx4SecureUser user);
	public void updateAccount(Dx4SecureUser user);
	public void updateAccountBalance(Dx4Account account);
	
	public void performWithdrawl(Dx4SecureUser user,double amount,Date date);
	public void performDeposit(Dx4SecureUser user,double amount,Date date);
	public Dx4Transaction performPayment(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId);
	public Dx4Transaction performCollect(Dx4SecureUser user,Dx4SecureUser cp,double amount,Date date, long refId);	
	public Dx4Rollup getRollup(Dx4SecureUser user);
	
	public Dx4MetaBet getMetaBetById(Dx4Player player,Long id);
	public Dx4Transaction getTransactionForId(long transId);
	public Map<Date,StakeWin>  getTotalWinStakes();
	
	public String getNextDrawNoForProvider(String provider);
	public List<Dx4NumberRevenueJson> getNumberRevenues(Date startDate,Date endDate);
	public double getRevenueForNumber(String number,Date startDate,Date endDate);
	public List<Dx4PlacingJson> getPlacingsForNumber(String number,Date startDate,Date endDate);
	public String getDescForNumber(String number);
	public Dx4NumberPageElementJson getNumberPageElement(String number, Character dictionary);
	public byte[] getRawImageForNumber(String number);
	public void storeDx4NumberPageElementJson(Dx4NumberPageElementJson npe, byte[] image);
	public void addNumber4D(final String number);
	public String getRandom(long playgameId, int digits);
	
	public List<Date> getDrawDates();
	public void getDescsForDrawResult(Dx4DrawResultJson result);
	public NumberExpo getSingleNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure,String number);
	public List<NumberExpo> getNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure);
	
	
	public Dx4GameGroup getGameGroup(Dx4SecureUser user);
	public void storeGameGroup(Dx4GameGroup group);
		
	public void storeDx4NumberExpo(Dx4NumberExpo numberExpo,char digits);
	public void deleteDx4NumberExpo(Dx4NumberExpo numberExpo,char digits);
	public void udateDx4NumberExpo(Dx4NumberExpo numberExpo,char digits);
	public List<Dx4NumberExpo> getDx4NumberExposForUser(Dx4SecureUser user,char digits,boolean winOrder);
	public Dx4NumberExpo getDx4DefaultNumberExpoForUser(Dx4SecureUser user,char digits);
	public Dx4NumberExpo getDx4NumberExpoForUser(Dx4SecureUser user,String number);
	
	public Dx4ProviderJson getProviderByCode(Character code);
	public List<Dx4ProviderJson> getProvidersFromCodes(String codes);
	public List<Dx4ProviderJson> getProviders();
	public Dx4ProviderJson getProviderByName(String name);
	
	public void deleteMember(Dx4SecureUser member) throws Dx4PersistenceException;
	
	public TransactionRowMapperPaginated getXTransactionRowMapperPaginated(long userId, int pageSize);
	
	
	public List<Dx4NumberWin> getWinsForDate(Dx4SecureUser user,Date drawDate);
	public List<Dx4DateWin> getWinDates(Dx4SecureUser user);
	public List<Dx4Win> getAgentWinsForDate(Dx4SecureUser user,Date drawDate);
	public Double getTotalWinsForDate(Dx4SecureUser user,Date drawDate);
	public List<Dx4WinNumberSummary> getWinNumberSummary(String number,Character providerCode,Date drawDate);
	
	public List<Dx4NumberPageElementJson> getNumberPageElements();
	public List<Dx4NumberPageElementJson> getNumberPageElements(String number);
	public List<Dx4PlayGame> getNextPlayGamesForUser(Dx4SecureUser user);
	public void updateNumberExpoBlocked(Dx4NumberExpo numberExpo);
	public void resetNumberExposBlocked();
	public List<String> getBlockedNumbers(Dx4MetaBet metaBet);
	public List<Dx4NumberPageElementJson> getNumberPageElementsByDesc(String searchTerm);
	public List<Dx4NumberPageElementJson> getNumberPageElementsRange(int num1, int num2, Character dictionary);
	public void updateImage(String code, byte[] image);
	public void getImagesForDrawResult(Dx4DrawResultJson result);
	public void storeNumberSearchTerm(NumberSearchTerm nst);
	public List<NumberSearchEntry> getNumbersFormTerm(String term);
	public void initializeNumberFloatPayouts(Date lastDrawDate);
	public void updateNumberFloatPayout(final Dx4NumberFloatPayoutJson nfp);
	public List<PayoutBand> createPayoutBands();
	public Dx4NumberFloatPayoutJson getDx4NumberFloatPayoutJson(String number);
	public void insertNumberFloatPayout(Dx4NumberFloatPayoutJson nfp);
	public Dx4NumberFloatPayoutJson getWorstDx4NumberFloatPayoutForBand(int band,Dx4NumberFloatPayoutJson nfp);
	public List<Dx4NumberFloatPayoutJson> getDx4NumberFloatPayoutTrending(int limit);
	public List<PayoutBand> createPayoutBands3();
	
	public void cleanUpBetsForPlayDate(Date drawDate);

	public void setDefaultPasswordForAll(String encoded);

	public void removeResultsForDate(Date date);

	public void removeResultsForProviderDate(Date date, char c);

	public void storeZodiacImage(String animal, int set, int year, byte[] image);
	public List<Dx4ZodiacJson> getZodiacs(int set);

	public void storeHoroscope(Dx4HoroscopeJson hj, byte[] image);
	public List<Dx4HoroscopeJson> getHoroscopes();

	public List<WinNumber> getWinNumber(Dx4ProviderJson provider, long playGameId);

	public List<Dx4BetNumberPayout> getBetNumberPayouts(Dx4Player player);
	public void deleteBetNumberPayouts(Dx4Player player);
	public void storeBetNumberPayout(Dx4Player player, Dx4BetNumberPayout bnp);

	public void overrideDataSourceUrl(String dataSourceUrl);
	
	public void updateVersion(final Dx4Version version);
	public Dx4Version getVersion();
	public String getVersionCode();
	
	public void initializeAdminProperties(Dx4Admin admin);

}
