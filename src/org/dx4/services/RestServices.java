package org.dx4.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.agent.Dx4Comp;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4DuplicateKeyException;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4BetJson;
import org.dx4.json.message.Dx4BetRetrieverFlag;
import org.dx4.json.message.Dx4BetStakeCombo;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4FavouriteJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4HoroscopeJson;
import org.dx4.json.message.Dx4MetaBetJson;
import org.dx4.json.message.Dx4MetaGameJson;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberRevenueJson;
import org.dx4.json.message.Dx4NumberStoreJson;
import org.dx4.json.message.Dx4NumberViewJson;
import org.dx4.json.message.Dx4PlacingJson;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.json.message.Dx4ResultJson;
import org.dx4.json.message.Dx4ZodiacJson;
import org.dx4.json.message.NumberPrizeJson;
import org.dx4.json.server.JsonServerServices;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.util.RegistrationValidator;
import org.dx4.utils.NumberGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

public class RestServices {

	private static final Logger log = Logger.getLogger(RestServices.class);
	
	@Autowired
	private Dx4Services dx4Services;
	
	public RestServices()
	{
	}
	
	public RestServices(Dx4Services dx4Services1)
	{
		this.dx4Services = dx4Services1;
	}
	
	public List<Dx4MetaGame> createDx4MetaGamePlayList(Dx4Player player) {
		
		List<Dx4MetaGame> dList = new  ArrayList<Dx4MetaGame>();
		List<Dx4MetaGame> gameList = dx4Services.getDx4Home().getUnplayedMetaGames();
		for (Dx4MetaGame metaGame : gameList)
		{
			if (player.getGameGroup().getGameActivator(metaGame)!=null)
			{
				synchronized(metaGame)
				{
					if (metaGame.getPlayGamesAvailableForBets().isEmpty())
						continue;
					dList.add(metaGame);
				}
			}
		}
		return dList;
	}
	
	public List<Dx4MetaGame> createDx4MetaGamePlayList() {
		
		List<Dx4MetaGame> dList = new  ArrayList<Dx4MetaGame>();
		List<Dx4MetaGame> gameList = dx4Services.getDx4Home().getUnplayedMetaGames();
		for (Dx4MetaGame metaGame : gameList)
		{
			synchronized(metaGame)
				{
					if (metaGame.getPlayGamesAvailableForBets().isEmpty())
						continue;
					dList.add(metaGame);
				}
		}
		return dList;
	}
	
	public Dx4MetaGame createDx4MetaGame() {
		return dx4Services.getDx4Home().getMetaGames().get(0);
	}
	
	public List<Dx4DrawResultJson> getDrawsByDate(Date date) throws RestServicesException{
		
		ExternalGameResults externalGameResults;
		try
		{
			log.info("Getting draws for date : " + date);
			externalGameResults =  dx4Services.getExternalService().getNearestExternalGameResults(date);
		}
		catch (Dx4ExternalServiceException e)
		{
			e.printStackTrace();
			String msg = "getDrawsByDate failed : " + e.getMessage();
			log.error(msg);
			throw new RestServicesException(msg);
		} 
		
		return externalGameResults.getDraws();
	}
	
	public List<Dx4DrawResultJson> getDrawsLatest() throws RestServicesException {
		try
		{
			log.info("Getting draws for latest");
			List<Dx4DrawResultJson> draws = dx4Services.getExternalService().getActualLatestExternalGameResults();
			return draws;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			String msg = "getDrawsLatest failed : " + e.getMessage();
			log.error(msg);
			throw new RestServicesException(msg);
		} 
	}
	
	public List<Dx4NumberPageElementJson> numberSearch(String searchTerm,Date startDate,Date endDate, Character dictionary)
	{
		log.info("Number Search With : " + searchTerm );
		NumberSearchHelper nsh = new NumberSearchHelper(dx4Services.getDx4Home(),searchTerm,dictionary);
		List<Dx4NumberPageElementJson> npes = nsh.getElements();
		populateNumberPageElements(npes,startDate,endDate);
		return npes;
	}
	
	private void populateNumberPageElements(List<Dx4NumberPageElementJson> npeList,Date startDate,Date endDate) {
		
		NumberGrid numberGrid4 = dx4Services.getExternalService().getNumberGridValues(4,startDate,endDate);
			
		for (Dx4NumberPageElementJson npe : npeList)
		{
			npe.setNumbers(new ArrayList<Dx4NumberStoreJson>());
			if (npe.getDictionary() == Dx4NumberPageElementJson.DICTIONARYMODERN4)
			{
				Dx4NumberStoreJson ns = numberGrid4.getNumberStore(npe.getToken());
				npe.getNumbers().add(ns);
				continue;
			}
			for (int c=0; c<10; c++)
			{
				String tok = Integer.toString(c) + npe.getToken();
				Dx4NumberStoreJson ns = numberGrid4.getNumberStore(tok);
				npe.getNumbers().add(ns);
			}
		}
	}
	
	public Dx4NumberViewJson getNumberView(String number,Date startDate,Date endDate,Character dictionary)
	{
		if (startDate==null && endDate==null)
		{
			GregorianCalendar gc = new GregorianCalendar();
			endDate = gc.getTime();
			gc.clear();
			gc.set(2008, 0, 1);
			startDate = gc.getTime();
		}
		
		double revenue = 0;
		List<Dx4PlacingJson> placings = new ArrayList<Dx4PlacingJson>();
		if (number.length()==4)
		{
			revenue = dx4Services.getDx4Home().getRevenueForNumber(number,startDate,endDate);
			placings = dx4Services.getDx4Home().getPlacingsForNumber(number,startDate,endDate);
		}
		Dx4NumberPageElementJson numberDesc = null;
		if (number.length()>2)
		{
			numberDesc = dx4Services.getDx4Home().getNumberPageElement(number,dictionary);
		}
		Dx4NumberViewJson nvj = new Dx4NumberViewJson(placings,number,numberDesc,startDate,endDate,revenue);
		return nvj;
	}

	private void checkPlayDate(Dx4PlayGame playGame) throws RestServicesException
	{
		Date now = (new GregorianCalendar()).getTime();
		if (now.after(playGame.getPlayDate()))
		{
			DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:SS");
			String dStr = df.format(playGame.getPlayDate());
			throw new RestServicesException("Draw date : " + dStr + " for bets has passed");
		}
	}
	
	public String placeMetaBet(Dx4Player player,Dx4MetaBetJson metaBetJson,JsonServerServices jsonServerServices) throws RestServicesException,Dx4DuplicateKeyException {
		
		dx4Services.getDx4Home().storeMetabetId(metaBetJson.getMetaBetUUID());
	
		if (metaBetJson.getBets().isEmpty())
			throw new RestServicesException("Invalid metaBetJson - No bets made");
		
		Dx4MetaGame mg = dx4Services.getDx4Home().getMetaGameById(metaBetJson.getMetaGameId());
		if (mg==null)
			throw new RestServicesException("Invalid metaGameId : " + metaBetJson.getMetaGameId());
		
		Dx4MetaGameJson metaGameJson = jsonServerServices.createDx4MetaGameJson(mg,dx4Services);
		validateFunds(player,metaBetJson,metaGameJson);	
		List<Dx4MetaBetJson> mbs = metaBetJson.createDx4MetaBetJsonForPlaygames(metaGameJson);
		
		String invalidBets = "";
		for (Dx4MetaBetJson mb : mbs)
		{
//			log.info("Placing broken out metabet :" + mb);
			
			Dx4MetaBet metaBet = new Dx4MetaBet();
			metaBet.setPlayer(player);
			copyMetaBetValues(metaBet, mb, mg, dx4Services.getDx4Home());
			metaBet.setCp(player.getParent());	
			metaBet.setMetaGame(mg);
			metaBet.setTotalStake(mb.getTotalStake(metaGameJson));

			Dx4PlayGame pg = mg.getPlayGameById(mb.getBets().get(0).getPlayGameId());
					dx4Services.getDx4Home().getPlayGameById(mb.getBets().get(0).getPlayGameId());
			checkPlayDate(pg);
			metaBet.setPlayGame(pg);

			List<String> invalidNumbers = dx4Services.validateMetaBetExposure(metaBet);
			if (!invalidNumbers.isEmpty())
			{
				removeChoices(invalidNumbers,metaBet);
				if (!invalidBets.isEmpty())
					invalidBets += "\n";
				invalidBets += makeInvalidChoicesMsg(pg.getPlayDate(),invalidNumbers);
			}
			
			dx4Services.makePlayerBet(player, metaBet, metaBetJson.getMetaBetUUID());
		}
		return invalidBets;
	}
	
	private String makeInvalidChoicesMsg(Date playDate,List<String> invalidNumbers)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String msg = "Numbers: " + invalidNumbers + " for draw : " + format.format(playDate) + " sold out.";
		log.info(msg);
		return msg;
	}
	
	private void removeChoices(List<String> invalidNumbers,Dx4MetaBet metaBet)
	{
		for (String number : invalidNumbers)
		{
			List<Dx4Bet> bets = metaBet.getBetsForChoice(number);
			for (Dx4Bet bet : bets)
				metaBet.getBets().remove(bet);
		}
	}
	
	private void copyMetaBetValues(Dx4MetaBet mb, Dx4MetaBetJson mbj,Dx4MetaGame mg, Dx4Home dx4Home) throws RestServicesException{
		
		mb.setMetaGameId(mbj.getMetaGameId());
		mb.setMetaGame(mg);
		
		List<Dx4Bet> bs = new ArrayList<Dx4Bet>();
		for (Dx4BetJson bj : mbj.getBets())
		{
			bs.addAll(createDx4Bets(bj,mg,dx4Home));
		}
		if (mbj.getBets().isEmpty())
			throw new RestServicesException("Invalid metaBetJson - No bets made");
		mb.setBets(bs);
		mb.setPlayGameId(mbj.getBets().get(0).getPlayGameId());
		
	}
	
	private List<Dx4Bet> createDx4Bets(Dx4BetJson bj,Dx4MetaGame mg,Dx4Home dx4Home) throws RestServicesException
	{
		validateDx4BetJson(bj);
		
		List<Dx4Bet> bets = new ArrayList<Dx4Bet>();
		Dx4Bet bet = new Dx4Bet();
		
		
		bet.setChoice(bj.getChoice());
		String provs = "";
		for (String p : bj.getProviders())
		{
			Dx4ProviderJson provider = dx4Home.getProviderByName(p);
			if (provider == null)
				throw new RestServicesException("Invalid bet - Invalid provider : " + p);
			provs += provider.getCode();
		}
		bet.setProviderCodes(provs);
		String choice = bj.getChoice();
		if (choice.length()==2)
		{
			Dx4GameTypeJson gType = Dx4GameTypeJson.D2A;
			bet.setGame(mg.getGameByType(gType));
			bets.add(bet);
			bet.setStake(bj.getSmall());
			return bets;
		}
		else
		if (choice.length() == 3)
		{
			if (bj.getBig()>0)
			{
				Dx4GameTypeJson gType = Dx4GameTypeJson.ABCC;
				bet.setGame(mg.getGameByType(gType));
				bets.add(bet);
				bet.setStake(bj.getBig());
			}
			if (bj.getSmall()>0)
			{
				if (bets.size()>0)
					bet = bet.clone();
				Dx4GameTypeJson gType = Dx4GameTypeJson.ABCA;
				bet.setGame(mg.getGameByType(gType));
				bets.add(bet);
				bet.setStake(bj.getSmall());
			}
			return bets;
		}
		
		// 4D	
		if (bj.getStakeCombo().equals(Dx4BetStakeCombo.STRAIGHT))
		{
			if (bj.getBig()>0)
			{
				Dx4GameTypeJson gType = Dx4GameTypeJson.D4Big;
				bet.setGame(mg.getGameByType(gType));
				bet.setStake(bj.getBig());
				bets.add(bet);
			}
			if (bj.getSmall()>0)
			{
				if (bets.size()>0)
					bet = bet.clone();
				Dx4GameTypeJson gType = Dx4GameTypeJson.D4Small;
				bet.setGame(mg.getGameByType(gType));
				bets.add(bet);
				bet.setStake(bj.getSmall());
			}
			return bets;
		}
		
		if (bj.getStakeCombo().equals(Dx4BetStakeCombo.BOX))
		{
			if (bj.getBig()>0)
			{
				Dx4GameTypeJson gType = Dx4GameTypeJson.D4BoxBig;
				bet.setGame(mg.getGameByType(gType));
				bet.setStake(bj.getBig());
				bets.add(bet);
			}
			if (bj.getSmall()>0)
			{
				if (bets.size()>0)
					bet = bet.clone();
				Dx4GameTypeJson gType = Dx4GameTypeJson.D4BoxSmall;
				bet.setGame(mg.getGameByType(gType));
				bet.setStake(bj.getSmall());
				bets.add(bet);
			}
			return bets;
		}
			
		if (bj.getStakeCombo().equals(Dx4BetStakeCombo.IBOX))
		{
			if (bj.getBig()>0)
			{
				Dx4GameTypeJson gType = Dx4GameTypeJson.D4IBoxBig;
				bet.setGame(mg.getGameByType(gType));
				bet.setStake(bj.getBig());
				bets.add(bet);
			}
			if (bj.getSmall()>0)
			{
				if (bets.size()>0)
					bet = bet.clone();
				Dx4GameTypeJson gType = Dx4GameTypeJson.D4IBoxSmall;
				bet.setGame(mg.getGameByType(gType));
				bet.setStake(bj.getSmall());
				bets.add(bet);
			}
		}
		return bets;
	}

	private void validateDx4BetJson(Dx4BetJson bj) throws RestServicesException{
		
		if (bj.getChoice()==null || bj.getChoice().length()<2 || bj.getChoice().length()>4)
			throw new RestServicesException("Invalid choice for bet");
		if (bj.getProviders() == null || bj.getProviders().isEmpty())
			throw new RestServicesException("Invalid bet - No providers supplied");
	}
	
	public Dx4MetaBet usePrevMetaBet(Dx4Player player, long metaBetId) throws RestServicesException {
		Dx4MetaBet mb = dx4Services.getDx4Home().getMetaBetById(player, metaBetId);
		mb.setCp(player.getParent());
		mb.setId(-1L);
		dx4Services.getDx4Home().insertPlayerMetaBet(player,mb);
		return mb;
	}
	
	public String getRandom(long playGameId,Dx4GameTypeJson gameType) throws RestServicesException{
		try
		{
			return dx4Services.getDx4Home().getRandom(playGameId,gameType.getDigits());
		}
		catch (PersistenceRuntimeException e)
		{
			throw new RestServicesException("Could not get random : " + e.getMessage());
		}
	}
	
	public List<Dx4NumberRevenueJson> getNumberRevenues(Date startDate, Date endDate)
	{
		return dx4Services.getDx4Home().getNumberRevenues(startDate, endDate);
	}
	
	public List<Date> getExternalSameDayGameDates()
	{
		return dx4Services.getExternalService().getExternalSameDayGameDates();
	}

	private void validateFunds(Dx4Player player, Dx4MetaBetJson metaBetJson, Dx4MetaGameJson metaGameJson) throws RestServicesException{
	
		if ((player.getAccount().getBalance() 
				-  metaBetJson.getTotalStake(metaGameJson)) < 0)
			throw new RestServicesException("Insufficient Funds To Place Bets");
	}

	public List<Dx4MetaBet> getMetaBetsForPlayer(Dx4Player player,Dx4BetRetrieverFlag retrieveFlag) {
		return dx4Services.getDx4Home().getMetaBetsForPlayer(player,retrieveFlag, null);
	}

	public void makeDeposit(Dx4Player player, double amount) throws RestServicesException {
		
		try
		{
			dx4Services.performWithdrawlDeposit(player, "Deposit", amount, "");
		}
		catch (PersistenceRuntimeException e)
		{
			throw new RestServicesException("Could not make deposit : " + e.getMessage());
		}
	}
	
	public void makeWithdrawl(Dx4Player player, double amount) throws RestServicesException {
		
		Dx4Account account = player.getAccount();
		if (account.getBalance() - amount < 0.0)
			throw new RestServicesException("Could not make withdrawal - insufficient funds.");
		try
		{
			dx4Services.performWithdrawlDeposit(player, "Withdrawl", amount, "");
		}
		catch (PersistenceRuntimeException e)
		{
			throw new RestServicesException("Could not make withdrawal : " + e.getMessage());
		}
	}

	public Dx4ProviderJson getProviderByName(String name) throws RestServicesException {
		Dx4ProviderJson pj = dx4Services.getDx4Home().getProviderByName(name);
		if (pj == null)
			throw new RestServicesException("Provider : " + name + " not supported");
		return pj;
	}
	
	public Dx4ResultJson numberSearchAll(@RequestParam("searchTerm") String searchTerm,Character dictionary)
	{
		Dx4ResultJson result = new Dx4ResultJson();
		try
		{
			List<Dx4NumberPageElementJson> search = numberSearch(searchTerm, null, null, dictionary);
			result.success(search);
			return result;
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			result.fail("Number Search failed - " + e.getMessage());
			return result;
		}
	}
	
	public Dx4ResultJson numberViewAll(@RequestParam("number") String number,@RequestParam("dictionary") Character dictionary){
		
		Dx4ResultJson result = new Dx4ResultJson();
		
		try
		{
			Dx4NumberViewJson nv = getNumberView(number, null, null, dictionary);
			result.success(nv);
			return result;
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			result.fail("Number View failed - " + e.getMessage());
			return result;
		}
	}

	public List<Dx4FavouriteJson> getFavourites(Dx4Player player)
	{
		List<Dx4FavouriteJson> favourites = dx4Services.getDx4Home().getFavourites(player.getId());
		for (Dx4FavouriteJson fav : favourites)
		{
			Dx4Bet bet = dx4Services.getDx4Home().getBetById(fav.getBetId());
			Dx4MetaBet metaBet = dx4Services.getDx4Home().getMetaBetById(player,bet.getMetaBetId());
			fav.setBet(JsonServerServices.createDx4BetJson( metaBet.getBetById(bet.getId())));
		}
		
		return favourites;
	}

	public List<Dx4ZodiacJson> getZodiac(int setNum) throws RestServicesException{
		try
		{
			return dx4Services.getDx4Home().getZodiacs(setNum);
		}
		catch (PersistenceRuntimeException e)
		{
			throw new RestServicesException("Could not perform getZodiac : " + e.getMessage());
		}
	}

	public List<Dx4HoroscopeJson> getHoroscope() throws RestServicesException {
		try
		{
			return dx4Services.getDx4Home().getHoroscopes();
		}
		catch (PersistenceRuntimeException e)
		{
			throw new RestServicesException("Could not perform getHoroscopes : " + e.getMessage());
		}
	}
		
	public List<Dx4NumberFloatPayoutJson> getTrendingLatestPayouts(int limit) throws RestServicesException{
		if (dx4Services.getFloatPayoutMgr()==null)
			throw new RestServicesException("Could not perform getTrendingLatestPayouts for fixed model");
		try
		{
			List<Dx4NumberFloatPayoutJson> nfps = dx4Services.getDx4Home().getDx4NumberFloatPayoutTrending(limit);
			Set<String> numbers = new HashSet<String>();
			for (Dx4NumberFloatPayoutJson nfp : nfps)
			{
				if (nfps.size()<limit)
					numbers.add(nfp.getNumber());
				if (nfp.getOdds()==0)
					dx4Services.getFloatPayoutMgr().setBandAndOdds(nfp);
			}
			if (nfps.size()==limit)
				return nfps;
			
			// top up
			Random rand = new Random();
			while (nfps.size()<limit)
			{
				int num=rand.nextInt(10000);
				if (rand.nextBoolean())
					num /= 10;
				String number = Integer.toString(num);
				if (!numbers.contains(number))
				{
					Dx4NumberFloatPayoutJson nfp = dx4Services.getFloatPayoutMgr().getDx4NumberFloatPayoutJson(number);
					nfps.add(nfp);
					numbers.add(number);
				}
			}
			
			return nfps;
		}
		catch (PersistenceRuntimeException e)
		{
			throw new RestServicesException("Could not perform getTrendingLatestPayouts : " + e.getMessage());
		}
	}

	public List<NumberPrizeJson> getTrending(boolean up) throws RestServicesException{
	
		List<Dx4NumberRevenueJson> topNumbers = getTopNumberRevenues();
		List<NumberPrizeJson> npjs = new ArrayList<NumberPrizeJson>();
		if (up)
		{
			for (int i=0; i<15; i++)
			{
				npjs.add(createNumberPrizeJson(topNumbers.get(i),up,i));
			}
        }
		else
		{
			for (int i=topNumbers.size()-1; i>topNumbers.size()-16; i--)
			{
				npjs.add(createNumberPrizeJson(topNumbers.get(i),up,i));
			}
		}
		return npjs;
	}
	
	public List<NumberPrizeJson> getTrendingLatest(boolean up) throws RestServicesException{
		
		List<Dx4NumberRevenueJson> topNumbers = getTopNumberRevenues();
		List<NumberPrizeJson> npjs = new ArrayList<NumberPrizeJson>();
		int pos = (new Random()).nextInt(20) + 20;
		for (int i=pos; i<pos+15; i++)
		{
			npjs.add(createNumberPrizeJson(topNumbers.get(i),(new Random()).nextBoolean(),i));
		}
		return npjs;
	}
	
	private List<Dx4NumberRevenueJson> getTopNumberRevenues()
	{
		GregorianCalendar gc = new GregorianCalendar();
		Date end = gc.getTime();
		gc.set(1,0,2009);
		Date start = gc.getTime();
		List<Dx4NumberRevenueJson> topNumbers = dx4Services.getDx4Home().getNumberRevenues(start, end);
		return topNumbers;
	}
	
	private NumberPrizeJson createNumberPrizeJson(Dx4NumberRevenueJson nr,boolean up,int cnt)
	{
		NumberPrizeJson npj = new NumberPrizeJson();
		npj.setNumber(nr.getNumber());
		double prize = 3000.0;
		if (cnt % 4 == 0)
		{
			npj.setNumber(npj.getNumber().substring(1));
			prize = 600.0;
		}
		double adjust = ((new Random()).nextInt(20)) / 100.0;
		if (up)
			prize -= (prize * adjust);
		else
			prize += (prize * adjust);
		npj.setPrize(prize);
		npj.setUp(up);
		return npj;
	}

	public String register(String upstream,String email, String password, String contact, String phone) throws RestServicesException{
		
		String msg = RegistrationValidator.validateFields(email, password, contact, phone);
		if (!msg.isEmpty())
			throw new RestServicesException("Missing or invalid fields: " + msg);
		
		Dx4SecureUser parent;
		try {
			parent = dx4Services.getDx4Home().getByUsername(upstream, Dx4Comp.class);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new RestServicesException("Cannot register at present- please try later");
		}
		
		if (parent == null)
			throw new RestServicesException("Upstream company/agent : " + upstream + " does not exist."); 
		
		Dx4SecureUser member;
		try {
			member = dx4Services.getDx4Home().getByUsername(email, Dx4SecureUser.class);
		} catch (Dx4PersistenceException e1) {
			e1.printStackTrace();
			throw new RestServicesException("Cannot register at present- please try later");
		}
		
		if (member!=null)
		{
			throw new RestServicesException("Cannot register : " + email + " aleady exists on system - please try alternative email.");
		}

		Dx4SecureUser user = new Dx4Player();
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));
		user.setContact(contact);
		user.setPhone(phone);
		user.setEmail(email);
		user.setRole(Dx4Role.ROLE_PLAY);
		user.setAccount(new Dx4Account());
		Dx4GameGroup gameGroup = new Dx4GameGroup(user);
		Dx4GameGroup parentGroup = dx4Services.getDx4Home().getGameGroup(parent);
		gameGroup.setGameActivators(parentGroup.getGameActivators());			// copy parent group for default
		user.setGameGroup(gameGroup);
		
		try
		{
			dx4Services.storeMember(user,parent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RestServicesException("Cannot register at present- please try later");
		}
		
		
		return "You've been successfully registered. Please contact : " + parent.getContact() + " to make payment."
					+ "(email : " + parent.getEmail() + " phone: " + parent.getPhone() + ")";
	}


}
