package org.dx4.secure.web.betInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.Dx4FormValidationException;
import org.dx4.services.Dx4Services;
import org.dx4.services.Dx4ServicesException;

public class QuickBet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7698579366654105150L;

	private static final Logger log = Logger.getLogger(QuickBet.class);
	
	private List<String> numbers;
	private List<String> numberDescs;
	private List<String> numberDescsCh;
	private Boolean numberMaxed;
	private List<String> providers;
	private List<Double> stakes;
	private List<String> useGames;
	private List<String> restoreUseGames;
	private Dx4MetaGame metaGame;
	private boolean enable4D;
	private boolean enable3D;
	private int number3D;
	private int number4D;
	private ConfirmCommand confirmCommand;
	private String confirmCommandObject;
	private String displayString;
	private Boolean viewCalc;
	private Boolean viewPrev;
	private Dx4PlayGame playGame;
	
	public QuickBet(Dx4MetaGame metaGame)
	{
		setMetaGame(metaGame);
		numbers = new ArrayList<String>();
		numberDescs = new ArrayList<String>();
		numberDescsCh = new ArrayList<String>();
		stakes = new ArrayList<Double>();
		providers = new ArrayList<String>();
		for (String provider : metaGame.getProviders())			// have to create cos will be changed
			providers.add(new String(provider));				// so initialise check boxs to checked
		
		useGames = new ArrayList<String>();
		for (Dx4Game game : metaGame.getGames())
		{
			useGames.add("on");
			stakes.add(game.getStake());
		}
		updateEnablers();
		setPlayGame(metaGame.getNextGameAvailableForBet());
	}
	
	public QuickBet(Dx4MetaBet metaBet,Dx4Services dx4Services)
	{
		setMetaGame(metaBet.getMetaGame());
		numbers = new ArrayList<String>();
		numberDescs = new ArrayList<String>();
		numberDescsCh = new ArrayList<String>();
		stakes = new ArrayList<Double>();
		useGames = new ArrayList<String>();
		providers = new ArrayList<String>();
		
		for (String prov : metaGame.getProviders())			
		{
			providers.add(prov);
		}
		
		for (Dx4Game game : metaGame.getGames())
		{
			List<Dx4Bet> bets = metaBet.getBetsForGame(game);
			if (bets.isEmpty())
			{
				useGames.add("off");
				stakes.add(game.getStake());
				continue;
			}
			useGames.add("on");
			for (Dx4Bet bet : bets)
				stakes.add(bet.getStake());
		}
		
		setPlayGame(metaGame.getNextGameAvailableForBet());
		updateEnablers();
	}
	
	
	public void updateEnablers()
	{
		enable4D = false;
		enable3D = false;
		int index = 0;
		for (Dx4Game game : metaGame.getGames())
		{
			if (useGames.get(index).equals("on"))
			{
				if (game.getGtype().getDigits()==4)
					enable4D = true;
				else
				if (game.getGtype().getDigits()==3)
					enable3D = true;
			}
			index++;
		}
	}
	
	public void addNumber(String number,Dx4Services dx4Services)
	{
		if (numbers.contains(number))
			return;
		numbers.add(number);
		Dx4NumberPageElementJson npe = dx4Services.getDx4Home().getNumberPageElement(number,Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
		numberDescs.add(npe.getDescription());
		numberDescsCh.add(npe.getDescriptionCh());
	}
	
	public void removeNumber(String number) {
		
		int pos = numbers.indexOf(number);
		if (pos<0)
		{
			log.warn("removeNumber number not found : " + number + " - maybe caused by browser back button - pressed ignored");
			return;
		}
		numbers.remove(number);
		numberDescs.remove(pos);
	}
	
	public List<String> validateNumberExposure(Dx4Services dx4Services,Dx4MetaBet metaBet,String number)
	{
		List<String> invalidNumbers = new ArrayList<String>();

		try
		{
			dx4Services.validateNumberExposure(metaBet.getPlayer(),metaBet.getPlayGame(),number,metaBet.getBetExpo(number),metaBet.getWinExpo(number));
		}
		catch (Dx4ServicesException e)
		{
			invalidNumbers.add(number);
		}
		
		// if (number.length()==3 || !enable3D)				
		//		return invalidNumbers;
		// test 3D 
		number = number.substring(1);
		try
		{
			dx4Services.validateNumberExposure(metaBet.getPlayer(),metaBet.getPlayGame(),number,metaBet.getBetExpo(number),metaBet.getWinExpo(number));
		}
		catch (Dx4ServicesException e)
		{
			invalidNumbers.add(number);
		}
		
		return invalidNumbers;
	}

	
	public Dx4MetaBet createMetaBet(Dx4Services dx4Services,Dx4Player player,boolean complete)
	{
		// log.trace("CREATING METABET FROM : " + this.toString());
		
		Dx4MetaBet metaBet = new Dx4MetaBet();
		metaBet.setMetaGame(metaGame);
		metaBet.setPlayGame(playGame);
		Dx4SecureUser parent;
		try {
			parent = dx4Services.getDx4Home().getParentForUser(player);
		} catch (Dx4PersistenceException e) {
			e.printStackTrace();
			throw new Dx4ExceptionFatal(e.getMessage());
		}
		metaBet.setPlayer(player);
		metaBet.setCp(parent);
		
		
		boolean threeOnly = true;
		for (String number : numbers)
		{
			if (number.length()>3)
			{
				threeOnly = false;
				break;
			}
		}
		
		metaBet.getBets().addAll(createBets(threeOnly));
		
		return metaBet;
	}
	
	private List<String> getValidNumbers()
	{
		boolean pres4D = verifyGameForDigitsPresent(4);
		boolean pres3D = verifyGameForDigitsPresent(3);
		
		if (pres4D && pres3D)
		{
			return numbers;
		}
		List<String> validNumbers = new ArrayList<String>();
		if (!pres4D && !pres3D)
		{
			return validNumbers;
		}
		for (String number : numbers)
		{
			if (number.length()==3 && pres3D==false)
			{
				continue;
			}
			if (number.length()==4 && pres4D==false)
			{
				validNumbers.add(number.substring(1));				// use last 3
				continue;
			}	
			validNumbers.add(number);
		}
		return validNumbers;
	}
	
	private List<Dx4Bet> createBets(boolean threeOnly) 
	{
		List<Dx4Bet> bets = new ArrayList<Dx4Bet>();
		int index=0;
		for (Dx4Game game : metaGame.getGames())
		{
			if (useGames.get(index).equals("on"))
			{
				if (game.getGtype().getDigits()>3 && threeOnly)
				{
					index++;
					continue;
				}
				Dx4Bet bet = new Dx4Bet();
				bet.setGame(game);
				bet.setStake(stakes.get(index));
				bets.add(bet);
			}
			index++;
		}
		return bets;
	}
	
	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}

	public void setUseGames(List<String> useGames) {
		this.useGames = useGames;
	}

	public List<String> getUseGames() {
		return useGames;
	}

	public void setStakes(List<Double> stakes) {
		this.stakes = stakes;
	}

	public List<Double> getStakes() {
		return stakes;
	}
	
	public List<String> getProviders() {
		return providers;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public void updateProviders(QuickBetCommand command) {
		log.trace("UPDATING PROVIDERS : " + providers + " WITH : " + command.getUseProviders() + " INDEX:" + command.getChangedIndex() );
		if (command.getChangedIndex()<command.getUseProviders().size())
			providers.set(command.getChangedIndex(),command.getUseProviders().get(command.getChangedIndex()));
		else
			providers.set(command.getChangedIndex(),null);
		log.trace("UPDATED PROVIDERS : " + providers );
	}
	
	public void updateStakes(QuickBetCommand command) {
		int index = command.getChangedIndex();
		stakes.set(index,truncStake(command.getBetMappings().get(index).getStake()));
	}
	
	public void updateStakesQ(QuickBetCommand command) {
		int index = command.getChangedIndex();
		stakes.set(index,truncStake(command.getBetMappings().get(index).getQstake()));
	}
	
	private double truncStake(double stake)
	{// trunc to 1dp i.e. 10 cents divisions 
		int stakeInt = (int) (stake * 10);
		return stakeInt/10.0;
	}
	
	public void updateUseGames(QuickBetCommand command) {
		storeUseGames();
		int index = command.getChangedIndex();
		useGames.set(index,command.getBetMappings().get(index).getUse());
		log.trace("useGames : " + useGames);
		updateEnablers();
	}
	
	public void restoreUseGames()
	{
		useGames = new ArrayList<String>();
		for (String use : restoreUseGames)
		{
			if (use==null)
				useGames.add(null);
			else
				useGames.add(use);
		}
		updateEnablers();
	}
	
	public void storeUseGames()
	{
		restoreUseGames = new ArrayList<String>();
		for (String use : useGames)
		{
			if (use==null)
				restoreUseGames.add(null);
			else
				restoreUseGames.add(new String(use));
		}
	}
	
	public void removeEmptyNumbers() {
		boolean pres4D = verifyGameForDigitsPresent(4);
		boolean pres3D = verifyGameForDigitsPresent(3);
		
		List<String> newNumbers = new ArrayList<String>();
		for (String number : numbers)
		{
			if (number.length()==3 && pres3D==false)
				continue;
			if (number.length()==4 && pres4D==false)
				continue;
			newNumbers.add(number);
		}
		numbers = newNumbers;
	}
	
	public void verifyUseGames() throws Dx4FormValidationException
	{
		/*
		if (verifyGameForDigitsPresent(3)==false && number3D>0)
		{
			throw new Dx4FormValidationException("Action will remove all chosen 3D numbers");
		}
		if (verifyGameForDigitsPresent(4)==false && number4D>0)
		{
			throw new Dx4FormValidationException("Action will remove all chosen 4D numbers");
		}
		*/
	}

	private boolean verifyGameForDigitsPresent(int digits)
	{
		int index = 0;
		for (Dx4Game game : metaGame.getGames())
		{
			if (useGames.get(index++).equals("on"))
			{
				if (game.getGtype().getDigits()==digits)
				{
					return true;
				}
			}	
		}
		return false;
	}
	
	public void validate() throws Dx4FormValidationException
	{
		if (!playGame.availableForBets())
			throw new Dx4FormValidationException("Bet Cut Off Time Expired..");
				
		int pnum = 0;
		for (String provider : providers)
			if (provider!=null)
				pnum++;
		if (pnum==0)
			throw new Dx4FormValidationException("At least one provider must be set");
		pnum = 0;
		for (String use : useGames)
			if (use!=null)
				pnum++;
		if (pnum==0)
			throw new Dx4FormValidationException("At least one game must be set");
		if (numbers.size()==0)
			throw new Dx4FormValidationException("At least one number must be chosen");
		if (getValidNumbers().isEmpty())
			throw new Dx4FormValidationException("No valid numbers chosen for bet type");
	}
	
	public boolean checkAvailableFunds(Dx4Services dx4Services,Dx4Player player,Dx4MetaBet metaBet)
	{
		Dx4Account account = player.getAccount();
		double availableFunds = account.getBalance() - metaBet.getTotalStake();
		return availableFunds>0;
	}
	
	public boolean isEnable4D() {
		return enable4D;
	}

	public void setEnable4D(boolean enable4d) {
		enable4D = enable4d;
	}

	public boolean isEnable3D() {
		return enable3D;
	}

	public void setEnable3D(boolean enable3d) {
		enable3D = enable3d;
	}

	public int getNumber3D() {
		return number3D;
	}

	public void setNumber3D(int number3d) {
		number3D = number3d;
	}

	public int getNumber4D() {
		return number4D;
	}

	public void setNumber4D(int number4d) {
		number4D = number4d;
	}

	public void setConfirmCommand(ConfirmCommand confirmCommand) {
		this.confirmCommand = confirmCommand;
	}

	public ConfirmCommand getConfirmCommand() {
		return confirmCommand;
	}

	public void setRestoreUseGames(List<String> restoreUseGames) {
		this.restoreUseGames = restoreUseGames;
	}

	public List<String> getRestoreUseGames() {
		return restoreUseGames;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setNumberMaxed(Boolean numberMaxed) {
		this.numberMaxed = numberMaxed;
	}

	public Boolean getNumberMaxed() {
		return numberMaxed;
	}

	public List<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}
	
	public Boolean getViewCalc() {
		return viewCalc;
	}

	public void setViewCalc(Boolean viewCalc) {
		this.viewCalc = viewCalc;
	}

	public void setViewPrev(Boolean viewPrev) {
		this.viewPrev = viewPrev;
	}

	public Boolean getViewPrev() {
		return viewPrev;
	}
	
	public void setNumberDescs(List<String> numberDescs) {
		this.numberDescs = numberDescs;
	}

	public List<String> getNumberDescs() {
		return numberDescs;
	}

	public void setNumberDescsCh(List<String> numberDescsCh) {
		this.numberDescsCh = numberDescsCh;
	}

	public List<String> getNumberDescsCh() {
		return numberDescsCh;
	}

	public String getConfirmCommandObject() {
		return confirmCommandObject;
	}

	public void setConfirmCommandObject(String confirmCommandObject) {
		this.confirmCommandObject = confirmCommandObject;
	}

	public boolean hasNumber(String number) {
		for (String test : numbers)
			if (test.equals(number))
				return true;
		return false;
	}

	public void setPlayGame(Dx4PlayGame playGame) {
		this.playGame = playGame;
	}

	public Dx4PlayGame getPlayGame() {
		return playGame;
	}

	@Override
	public String toString() {
		return "QuickBet [numbers=" + numbers + ", numberMaxed=" + numberMaxed
				+ ", providers=" + providers + ", stakes=" + stakes
				+ ", useGames=" + useGames + ", restoreUseGames="
				+ restoreUseGames + ", metaGame=" + metaGame + ", enable4D="
				+ enable4D + ", enable3D=" + enable3D + ", number3D="
				+ number3D + ", number4D=" + number4D + ", confirmCommand="
				+ confirmCommand + ", displayString=" + displayString + "]";
	}

	
	
}
