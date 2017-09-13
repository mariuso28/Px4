package org.dx4.bet.persistence;

import java.util.Date;
import java.util.List;

import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4DateWin;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4NumberWin;
import org.dx4.bet.Dx4Win;
import org.dx4.bet.Dx4WinNumberSummary;
import org.dx4.bet.floating.Dx4BetNumberPayout;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4SecureUser;


public interface Dx4BetDao {
	public void insertBets(Dx4MetaBet metaBet);
	public void updateBets(Dx4MetaBet metaBet);
	public List<Dx4Bet> getBetsForMetaBet(Dx4MetaBet metaBet);
	public List<Dx4Win> getWinsForMetaBet(Dx4MetaBet metaBet);
	public List<Dx4NumberWin> getWinsForDate(Dx4SecureUser user,Date drawDate);
	public List<Dx4DateWin> getWinDates(Dx4SecureUser user);
	public List<Dx4Win> getAgentWinsForDate(Dx4SecureUser user,Date drawDate);
	public Double getTotalWinsForDate(Dx4SecureUser user,Date drawDate);
	public List<Dx4WinNumberSummary> getWinNumberSummary(String number,Character providerCode,Date drawDate);
	public Dx4Bet getBetById(long betId);
	public List<WinNumber> getWinNumber(Dx4ProviderJson provider, long playGameId);
	public List<Dx4BetNumberPayout> getBetNumberPayouts(Dx4Player player);
	public void deleteBetNumberPayouts(Dx4Player player);
	public void storeBetNumberPayout(Dx4Player player, Dx4BetNumberPayout bnp);
	
	
}
