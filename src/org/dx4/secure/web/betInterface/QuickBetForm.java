package org.dx4.secure.web.betInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.persistence.MetaBetRowMapperPaginated;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.Dx4Home;
import org.dx4.player.Dx4Player;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.secure.web.player.display.DisplayMetaBet;
import org.dx4.services.Dx4Config;
import org.dx4.services.Dx4Services;

public class QuickBetForm 
{
	private static final Logger log = Logger.getLogger(QuickBetForm.class);
	
	private QuickBet quickBet;
	private QuickBetCommand command;
	private double availableFunds;
	private double outstandingInvoiceAmount;
	private double totalStake;
	private double totalStakeSingleProvider;
	private String message;
	private List<Boolean> providers;
	private List<List<Double>> stakeLists; 
	private List<List<Dx4PayOut>> payOuts;
	private List<String> useGames;
	private boolean validBet;
	private int providerCount;
	private List<DisplayMetaBet> displayMetaBetList;
	private Integer currentPage;
	private Integer lastPage;
	private String number4D;
	private String numberStr;
	private Date nextDrawDate;
	private List<Date> availableDates;
	
	public QuickBetForm()
	{
	}
	
	public QuickBetForm(Dx4Services dx4Services,Dx4Player player,QuickBet quickBet,HashSet<Long> currExpandedBets,MetaBetRowMapperPaginated currMbrmp)
	{
		Dx4MetaBet metaBet = quickBet.createMetaBet(dx4Services,player,false);
		quickBet.setConfirmCommand(ConfirmCommand.None);
		availableDates = new ArrayList<Date>();
		for (Dx4PlayGame playGame : quickBet.getMetaGame().getPlayGamesAvailableForBets())
		{
			availableDates.add(playGame.getPlayDate());
		}
		nextDrawDate = quickBet.getMetaGame().nextDrawCutOffTime();
		
		setQuickBet(quickBet);
		setStakeLists(quickBet.getMetaGame());
		setProviders(dx4Services,metaBet);
		setPayouts(quickBet,metaBet);
		setStakes(metaBet);
		setUseGames(quickBet.getMetaGame());
		setAvailableFunds(dx4Services,player,metaBet);	
		quickBet.setNumberMaxed(false);
		String strictValidation = Dx4Config.getProperties().getProperty("dx4.StrictNumberValidation", "off");
		if (strictValidation.equalsIgnoreCase("on"))
		{
			List<String> invalidNumbers = dx4Services.validateMetaBetExposure(metaBet);
			if (invalidNumbers.size()>0)
			{
				setMessage("Number(s) : " + invalidNumbers + " maxed out");
				quickBet.setNumberMaxed(true);
			}
		}
			
		validBet = true;
		try
		{
			quickBet.validate();
		}
		catch (Dx4FormValidationException e)
		{
			log.trace("Dx4FormValidationException : " + e.getMessage());
			validBet=false;
		}
		createDisplayMetaBet(player,dx4Services.getDx4Home(),currExpandedBets,currMbrmp);
		number4D = "4D";
		if (quickBet.getNumbers().isEmpty())
		{
			if (!quickBet.isEnable4D())
				number4D = "3D";
		}
		else
		if (quickBet.getNumbers().get(quickBet.getNumbers().size()-1).length()==3)
		{
			number4D = "3D";		
		}
		numberStr = "";
	}
	
	private void createDisplayMetaBet(Dx4Player player,Dx4Home dx4Home,HashSet<Long> currExpandedBets,
			MetaBetRowMapperPaginated currMbrmp)
	{
		displayMetaBetList = new ArrayList<DisplayMetaBet>();
		log.trace("CURRENT PAGE : " + currMbrmp.getCurrentPage());
		setCurrentPage(currMbrmp.getCurrentPage());
		setLastPage(currMbrmp.getLastPage());
		for (Dx4MetaBet metaBet : currMbrmp.getPage(currMbrmp.getCurrentPage()))
		{
			dx4Home.populateMetaBet(player,metaBet);
			String expanded = "+";
			if (currExpandedBets!=null)
				if (currExpandedBets.contains(metaBet.getId()))
					expanded = "-";
			displayMetaBetList.add(new DisplayMetaBet(metaBet,dx4Home,expanded));
		}
		
		log.trace(displayMetaBetList);
	}
	
	public boolean isValidBet() {
		return validBet;
	}

	public void setValidBet(boolean validBet) {
		this.validBet = validBet;
	}
	
	public void createInvalidNosMessage(List<String> invalidNos)
	{
		String msg = "Number(s) : ";
		for (String number : invalidNos)
		{
			msg += number + ",";
		}
		msg = msg.substring(0,msg.length()-1);
		msg += " maxed out";
		setMessage(msg);
	}
	
	private void setStakes(Dx4MetaBet metaBet)
	{
		totalStake = metaBet.getTotalStake();
	}
	
	private void setProviders(Dx4Services dx4Services,Dx4MetaBet metaBet) {
		
		providers = new ArrayList<Boolean>();
		providerCount = 0;
		for (@SuppressWarnings("unused") String pstr : metaBet.getMetaGame().getProviders())
		{
			providerCount++;
			providers.add(true);
		}
	}

	private void setUseGames(Dx4MetaGame metaGame) {
		useGames = new ArrayList<String>();
		for (@SuppressWarnings("unused") Dx4Game game : metaGame.getGames())
		{
			useGames.add("on");
		}
		log.trace("Using games: " + useGames);
	}

	private void setStakeLists(Dx4MetaGame metaGame)
	{
		stakeLists = new ArrayList<List<Double>>();
		for (Dx4Game game : metaGame.getGames())
		{
			stakeLists.add(createStakeList(game.getStake()));
		}
	}
	
	private List<Double> createStakeList(double stake) {
	 
			List<Double> stakeList = new ArrayList<Double>();
			stakeList.add(1.00*stake);
			stakeList.add(2.00*stake);
			stakeList.add(3.00*stake);
			stakeList.add(4.00*stake);
			stakeList.add(5.00*stake);
			stakeList.add(6.00*stake);
			stakeList.add(7.00*stake);
			stakeList.add(8.00*stake);
			stakeList.add(9.00*stake);
			stakeList.add(10.00*stake);
			stakeList.add(12.00*stake);
			stakeList.add(15.00*stake);
			stakeList.add(16.00*stake);
			stakeList.add(18.00*stake);
			stakeList.add(20.00*stake);
			stakeList.add(30.00*stake);
			stakeList.add(50.00*stake);
			stakeList.add(100.00*stake);
			return stakeList;
	}

	private void setPayouts(QuickBet quickBet,Dx4MetaBet metaBet)
	{
		Dx4MetaGame metaGame = metaBet.getMetaGame();
		payOuts = new ArrayList<List<Dx4PayOut>>();
		int cnt=0;
		for (Dx4Game game : metaGame.getGames())
		{
			double stake = quickBet.getStakes().get(cnt++);
			List<Dx4PayOut> payOutList = game.getPayOuts();
			List<Dx4PayOut> payOutMods = new ArrayList<Dx4PayOut>();
			for (Dx4PayOut payOut : payOutList)
			{
				Dx4PayOut payOutMod = new Dx4PayOut(payOut); 
				payOutMod.setPayOut(payOut.getPayOut()*stake);
				payOutMods.add(payOutMod);
			}
			payOuts.add(payOutMods);
		}
	}
	
	private void setAvailableFunds(Dx4Services dx4Services,Dx4Player player,Dx4MetaBet metaBet)
	{
		Dx4Account account = player.getAccount();
		availableFunds = account.getBalance() - metaBet.getTotalStake();
		setOutstandingInvoiceAmount(outstandingInvoiceAmount);
	}
	
	public void setCommand(QuickBetCommand command) {
		this.command = command;
	}

	public QuickBetCommand getCommand() {
		return command;
	}

	public QuickBet getQuickBet() {
		return quickBet;
	}

	public void setQuickBet(QuickBet quickBet) {
		this.quickBet = quickBet;
	}

	public double getAvailableFunds() {
		return availableFunds;
	}

	public void setAvailableFunds(double availableFunds) {
		this.availableFunds = availableFunds;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public List<List<Double>> getStakeLists() {
		return stakeLists;
	}

	public void setStakeLists(List<List<Double>> stakeLists) {
		this.stakeLists = stakeLists;
	}

	public List<List<Dx4PayOut>> getPayOuts() {
		return payOuts;
	}

	public void setPayOuts(List<List<Dx4PayOut>> payOuts) {
		this.payOuts = payOuts;
	}

	public void setUseGames(List<String> useGames) {
		this.useGames = useGames;
	}

	public List<String> getUseGames() {
		return useGames;
	}

	public List<Boolean> getProviders() {
		return providers;
	}

	public void setProviders(List<Boolean> providers) {
		this.providers = providers;
	}
	
	public int getProviderCount() {
		return providerCount;
	}

	public void setProviderCount(int providerCount) {
		this.providerCount = providerCount;
	}

	public double getTotalStake() {
		return totalStake;
	}

	public void setTotalStakeSingleProvider(double totalStakeSingleProvider) {
		this.totalStakeSingleProvider = totalStakeSingleProvider;
	}

	public double getTotalStakeSingleProvider() {
		return totalStakeSingleProvider;
	}

	public void setTotalStake(double totalStake) {
		this.totalStake = totalStake;
	}

	public void setDisplayMetaBetList(List<DisplayMetaBet> displayMetaBetList) {
		this.displayMetaBetList = displayMetaBetList;
	}

	public List<DisplayMetaBet> getDisplayMetaBetList() {
		return displayMetaBetList;
	}
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getLastPage() {
		return lastPage;
	}

	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}
	
	public void setNumber4D(String number4D) {
		this.number4D = number4D;
	}

	public String getNumber4D() {
		return number4D;
	}
	
	
	public String getNumberStr() {
		return numberStr;
	}

	public void setNumberStr(String numberStr) {
		this.numberStr = numberStr;
	}
	
	

	public void setOutstandingInvoiceAmount(double outstandingInvoiceAmount) {
		this.outstandingInvoiceAmount = outstandingInvoiceAmount;
	}

	public double getOutstandingInvoiceAmount() {
		return outstandingInvoiceAmount;
	}

	@Override
	public String toString() {
		return "QuickBetForm [quickBet=" + quickBet + ", command=" + command
				+ ", availableFunds=" + availableFunds + ", message=" + message
				+ ", providers=" + providers + ", stakeLists=" + stakeLists
				+ ", payOuts=" + payOuts + ", useGames="
				+ useGames + "]";
	}

	public void setNextDrawDate(Date nextDrawDate) {
		this.nextDrawDate = nextDrawDate;
	}

	public Date getNextDrawDate() {
		return nextDrawDate;
	}

	public List<Date> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<Date> availableDates) {
		this.availableDates = availableDates;
	}

	
}
