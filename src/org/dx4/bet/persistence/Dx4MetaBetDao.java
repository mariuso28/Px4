package org.dx4.bet.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.springframework.dao.DataAccessException;

public interface Dx4MetaBetDao 
{
	public Dx4ProviderJson getProviderByCode(Character code);
	public List<Dx4ProviderJson> getProviders();
	public Dx4ProviderJson getProviderByName(String name);
	public void insert(Dx4MetaBet metaBet);
	public void setPlaced(Dx4MetaBet metaBet);
	public void setPlayed(Dx4PlayGame playGame);
	public void update(Dx4PlayGame playGame,Dx4MetaBet metaBet);
	public Dx4MetaBet getMetaBetById(Long id);
	public List<Dx4MetaBet> getMetaBetsForPlayer(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame);
	public Integer getMetaBetsForPlayerCount(Dx4Player player,Dx4BetRetrieverFlag flag,Dx4MetaGame metaGame);
	public List<Dx4MetaBet> getAllMetaBetsForMetaGame(Dx4MetaGame metaGame, Dx4PlayGame playGame);
	public Dx4MetaBet getMetaBetForTransaction(Dx4Transaction transaction);
	public NumberExpo getSingleNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure,String number);
	public List<NumberExpo> getNumberExpo(String parentCode,Dx4PlayGame playGame,Dx4Role role,double maxExpo,boolean exposure);
	public List<Dx4NumberWin> getWinsForDate(Dx4SecureUser user,Date drawDate);
	public List<Dx4DateWin> getWinDates(Dx4SecureUser user);
	public List<Dx4Win> getAgentWinsForDate(Dx4SecureUser user,Date drawDate);
	public Double getTotalWinsForDate(Dx4SecureUser user,Date drawDate);
	public List<Dx4WinNumberSummary> getWinNumberSummary(String number,Character providerCode,Date drawDate);
	public void cleanUpBetsForPlayDate(Date drawDate);
		public List<Dx4MetaBetExpo> getDx4MetaBetExpos(Dx4Agent agent,
			Dx4PlayGame playGame, List<Dx4MetaBetExpoOrder> ordering);
	public Integer getDx4MetaBetExposCount(Dx4Agent agent,Dx4PlayGame playGame,List<Dx4MetaBetExpoOrder> ordering);
	public void updateNumberExpoBlocked(Dx4NumberExpo numberExpo);
	public void resetNumberExposBlocked();
	public List<String> getBlockedNumbers(Dx4MetaBet metaBet);
	public List<Dx4BetRollup> getTotalBetRollups(long playgameId, int digits) throws DataAccessException;
	public List<Dx4BetRollup> getTotalBetRollups(long playgameId, int digits, char providerCode) throws DataAccessException;
	public Dx4Bet getBetById(long betId);
	public List<WinNumber> getWinNumber(Dx4ProviderJson provider, long playGameId);
	
	public void storeMetabetId(UUID id);
	
	public List<Dx4BetNumberPayout> getBetNumberPayouts(Dx4Player player);
	public void deleteBetNumberPayouts(Dx4Player player);
	public void storeBetNumberPayout(Dx4Player player, Dx4BetNumberPayout bnp);
	
	public Map<Date,StakeWin>  getTotalWinStakes();
	
	// PATCH
	public void calcRetroTotalStakes(Dx4MetaGame mg) throws DataAccessException;
	public void fixBetsAndRollups(Dx4MetaGame mg) throws DataAccessException;
}
