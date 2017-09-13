package org.dx4.bet.floating;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.external.persistence.PayoutBand;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.player.Dx4Player;
import org.dx4.services.Dx4ServicesRuntimeException;

public class Dx4FloatPayoutMgr 
{
	private static final Logger log = Logger.getLogger(Dx4FloatPayoutMgr.class);
	
	private Dx4Home home;
	private List<PayoutBand> payoutBands;
	private List<PayoutBand> payoutBands3;
	private Date lastDrawDate;
	private double totalDrawVolume;
	private long floatNumberRefreshTime;
	private Dx4PlayGame nextPlayGame;

	public Dx4FloatPayoutMgr(Dx4Home home,long floatNumberRefreshTime, Dx4PlayGame playGame)
	{
		setHome(home);
		setFloatNumberRefreshTime(floatNumberRefreshTime);
		initializeNumberFloatPayouts(playGame);
	}
	
	public void initializeNumberFloatPayouts(Dx4PlayGame playGame)
	{
		payoutBands = home.createPayoutBands();
		payoutBands3 = home.createPayoutBands3();
		resetLastDrawDate();
		home.initializeNumberFloatPayouts(lastDrawDate);
		setNextPlayGame(playGame);
	}
	
	public long getCurrentFloatNumberRefreshTime() {
		
		Date now = (new GregorianCalendar()).getTime();
		long diff = nextPlayGame.getPlayDate().getTime() - now.getTime();
		diff = (diff / 1000) / 60;
		if (diff <= 30)
			setFloatNumberRefreshTime(30);
		else
		if (diff <= 60)
			setFloatNumberRefreshTime(60);
		else
		if (diff <= 120)
			setFloatNumberRefreshTime(90);
		else
		if (diff <= 300)
			setFloatNumberRefreshTime(120);
		
		return floatNumberRefreshTime;
	}

	private void resetLastDrawDate()
	{
		totalDrawVolume = 0.0;
		GregorianCalendar gc = new GregorianCalendar();
		lastDrawDate = home.getPrevDrawDate(gc.getTime());
		if (lastDrawDate != null)
			return;
		gc.set(2001,0,1);
		lastDrawDate = gc.getTime();	
	}

	public Dx4NumberFloatPayoutJson getDx4NumberFloatPayoutJson(String number)
	{
		Dx4NumberFloatPayoutJson nfp = home.getDx4NumberFloatPayoutJson(number);
		if (nfp == null)
		{
			nfp = new Dx4NumberFloatPayoutJson();
			nfp.setNumber(number);
			home.insertNumberFloatPayout(nfp);
			nfp = home.getDx4NumberFloatPayoutJson(number);
		}
		if (nfp.getOdds()!=0)
			return nfp;
		
		setBandAndOdds(nfp);
		return nfp;
	}
	
	public void setBandAndOdds(Dx4NumberFloatPayoutJson nfp)
	{
		int band = 0;
		List<PayoutBand> useBands = payoutBands;
		if (nfp.getNumber().length()==3)
			useBands = payoutBands3;
			
		for (PayoutBand po : useBands)
		{
			if (nfp.getCount()>=po.getLower() && nfp.getCount()<po.getUpper())
			{
				nfp.setBand(band);
				nfp.setOdds(po.getOdds());
				break;
			}
			band++;
		}
		home.updateNumberFloatPayout(nfp);
	}
	
	public Dx4NumberFloatPayoutJson getDx4NumberFloatPayoutJson(Dx4Player player, String number) {
		
		Dx4NumberFloatPayoutJson nfp = getDx4NumberFloatPayoutJson(number);
		Dx4BetNumberPayout bnp = new Dx4BetNumberPayout(nfp.getNumber(),nfp.getOdds());
		home.storeBetNumberPayout(player, bnp);
		return nfp;
	}
	
	public void updateNumberFloatPayouts(Dx4MetaBet mb)
	{
		for (Dx4Bet bet : mb.getBets())
		{
			Dx4NumberFloatPayoutJson nfp = getDx4NumberFloatPayoutJson(bet.getChoice());
			
			nfp.setLastTraded(mb.getPlaced());
			nfp.setVolume(nfp.getVolume()+bet.getTotalStake());
			resetFloatNumberBand(nfp);
		}
	}
	
	private void resetFloatNumberBand(Dx4NumberFloatPayoutJson nfp)
	{
		totalDrawVolume += nfp.getVolume();
		List<PayoutBand> useBands = payoutBands;
		if (nfp.getNumber().length()==3)
			useBands = payoutBands3;
		
		if (nfp.getBand()==(useBands.size()-1))
		{
			nfp.setLastChange(0);
			home.updateNumberFloatPayout(nfp);
			return;					// can't go down	- 	this needs reset bands
		}
		
		Dx4NumberFloatPayoutJson worst = home.getWorstDx4NumberFloatPayoutForBand(nfp.getBand()+1, nfp);
		if (worst == null)
		{
//			log.info("No worse NFP than : " + nfp + " It stays in band");
			nfp.setLastChange(0);
			home.updateNumberFloatPayout(nfp);
			return;
		}
		
//		log.info("SWAPPING " + nfp + " AND " + worst);
		double todds = nfp.getOdds();
		nfp.setBand(worst.getBand());
		nfp.setLastChange(worst.getOdds()-nfp.getOdds());
		nfp.setOdds(worst.getOdds());
		worst.setLastChange(todds - worst.getOdds());
		worst.setOdds(todds);
		worst.setBand(worst.getBand()-1);
		home.updateNumberFloatPayout(nfp);
		home.updateNumberFloatPayout(worst);
	}
	
	public void deleteBetNumberPayouts(Dx4Player player) {
		home.deleteBetNumberPayouts(player);
	}

	public void updateBetNumberPayouts(Dx4Player player,Dx4MetaBet metaBet) {
		List<Dx4BetNumberPayout> payouts = home.getBetNumberPayouts(player);
		for (Dx4Bet bet : metaBet.getBets())
		{
			Dx4BetNumberPayout payout = getPayoutForNumber(bet.getChoice(),payouts);
			if (payout==null)
			{
				String msg = "Bet payout for number : " + bet.getChoice() + " player: " + player.getEmail() + " not found";
				log.error(msg);
				throw new Dx4ServicesRuntimeException(msg);
			}
			bet.setOdds(payout.getOdds());
		}
	}
	
	private Dx4BetNumberPayout getPayoutForNumber(String number,List<Dx4BetNumberPayout> payouts)
	{
		for (Dx4BetNumberPayout payout : payouts)
		{
			if (payout.getNumber().equals(number))
				return payout;
		}
		return null;
	}
	
	public List<PayoutBand> getPayoutBands() {
		return payoutBands;
	}

	public void setPayoutBands(List<PayoutBand> payoutBands) {
		this.payoutBands = payoutBands;
	}

	
	public Date getLastDrawDate() {
		return lastDrawDate;
	}

	public double getTotalDrawVolume() {
		return totalDrawVolume;
	}

	public Dx4Home getHome() {
		return home;
	}

	public void setHome(Dx4Home home) {
		this.home = home;
	}

	public long getFloatNumberRefreshTime() {
		return floatNumberRefreshTime;
	}

	public void setFloatNumberRefreshTime(long floatNumberRefreshTime) {
		this.floatNumberRefreshTime = floatNumberRefreshTime;
	}

	public List<PayoutBand> getPayoutBands3() {
		return payoutBands3;
	}

	public void setPayoutBands3(List<PayoutBand> payoutBands3) {
		this.payoutBands3 = payoutBands3;
	}

	public Dx4PlayGame getNextPlayGame() {
		return nextPlayGame;
	}

	public void setNextPlayGame(Dx4PlayGame nextPlayGame) {
		this.nextPlayGame = nextPlayGame;
	}

	
}
