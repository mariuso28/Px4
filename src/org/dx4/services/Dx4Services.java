package org.dx4.services;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4NumberExpo;
import org.dx4.account.Dx4Transaction;
import org.dx4.admin.Dx4Admin;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.excel.Dx4BetRollupMgrUSDMYR;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.bet.MetaBetType;
import org.dx4.bet.floating.Dx4FloatPayoutMgr;
import org.dx4.bet.persistence.NumberExpo;
import org.dx4.external.parser.ExternalComingDate;
import org.dx4.external.parser.ExternalGameResults;
import org.dx4.game.Dx4GameActivator;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.home.persistence.PersistenceRuntimeException;
import org.dx4.json.message.Dx4DrawResultIboxJson;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.secure.web.pdf.PDFStoreMetaBet;
import org.dx4.secure.web.pdf.PDFStoreTransaction;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.ModelMap;

public class Dx4Services 
{
	private static final Logger log = Logger.getLogger(Dx4Services.class);	
	private Dx4Home dx4Home;
	private PlatformTransactionManager transactionManager;
	private Mail mail;
	private ThreadPoolTaskScheduler scheduler;
	private String dx4PropertiesPath;
	private Dx4FloatPayoutMgr floatPayoutMgr;
	
	public void initServices()
	{
		Dx4Config.init(dx4PropertiesPath);
		
		String dataSourceUrl =  Dx4Config.getProperties().getProperty("dx4.dataSourceUrl");
		if (dataSourceUrl!=null)
		{
			log.info("Overriding datasouce url with : " + dataSourceUrl);
			dx4Home.overrideDataSourceUrl(dataSourceUrl);
		}
		
		List<Dx4MetaGame> metaGames = dx4Home.getMetaGames();
		dx4Home.performPatches(metaGames.get(0));
		
		String fpos = Dx4Config.getProperties().getProperty("dx4.floatingPayouts","off");
		
		if (fpos.equalsIgnoreCase("on"))
		{
			String floatNumberRefreshTime =  Dx4Config.getProperties().getProperty("dx4.floatNumberRefreshTime","180");
			Dx4PlayGame playGame = metaGames.get(0).getNextGameAvailableForBet();
			floatPayoutMgr = new Dx4FloatPayoutMgr(dx4Home,Long.parseLong(floatNumberRefreshTime),playGame);
		}
		else
			floatPayoutMgr = null;
		
		mail.setMailCcNotifications(Dx4Config.getProperties().getProperty("mailCcNotifications"));
		mail.setMailSendFilter(Dx4Config.getProperties().getProperty("mailSendFilter"));
		mail.setMailDisabled(Dx4Config.getProperties().getProperty("mailDisabled"));
		
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("Dx4 async scheduler- ");
		scheduler.initialize();
		
		String hedgeBets = Dx4Config.getProperties().getProperty("dx4.hedgeBets","off");
		
		if (hedgeBets.equals("on"))
			scheduleHandleHedgeBets();
		
		
		
	}
	
	private void scheduleHandleHedgeBets() 
	{	
		GregorianCalendar gc = new GregorianCalendar();
		Date drawTime = Dx4Config.getDrawTime();
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.setTime(drawTime);
		gc1.add(Calendar.MINUTE,5);
		
		if (gc1.getTime().before(gc.getTime()))
			gc1.add(Calendar.DAY_OF_YEAR, 1);
		
		Date startAt = gc1.getTime();
	
	// TEST 
	//	gc.add(Calendar.MINUTE,1);
	//	startAt = gc.getTime();
	// END TEST	
		
		long interval = 24 * 60 * 60 * 1000L;
		
		log.info("SCHEDULING HANDLE AT INTERVAL : " + 24 + " HRS " + interval + " MS FROM : " + startAt);
		scheduler.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						
						log.info("Running scheduled event");
						try {
							handleHedgeBets();
						} catch (Exception e) {
							e.printStackTrace();
							log.error("Exception in scheduled HANDLING HEDGE BETS " + e.getMessage());
						}
					}
				}, startAt, interval);
	}
	
	public boolean inHedgeWindow()
	{
		GregorianCalendar gc = new GregorianCalendar();
		log.info("HANDLING HEDGE BETS AT : " + gc.getTime());
		Dx4MetaGame mg = dx4Home.getMetaGame("4D With ABC");
		Dx4PlayGame playGame = mg.getPlayGameByPlayDate(gc.getTime());
		if (playGame==null)
			return false;
		
		Date drawTime = Dx4Config.getDrawTime();
		Date actualDrawTime = Dx4Config.getActualDrawTime();
		Date now = gc.getTime();
		boolean inWindow = now.after(drawTime) && now.before(actualDrawTime);
		
		return inWindow;
	}
	
	public void handleHedgeBets() throws Exception {
		
		GregorianCalendar gc = new GregorianCalendar();
		log.info("HANDLING HEDGE BETS AT : " + gc.getTime());
		Dx4MetaGame mg = dx4Home.getMetaGame("4D With ABC");
		Dx4PlayGame playGame = mg.getPlayGameByPlayDate(gc.getTime());
		
		// TEST 
		// playGame = mg.getPlayGameById(2);
		// END TEST
		
		if (playGame==null)
		{
			log.info("No draw scheduled for today");
			return;
		}
		
		if (floatPayoutMgr!=null)
			return;
		
		Dx4BetRollupMgrUSDMYR brm = new Dx4BetRollupMgrUSDMYR(this,mg,playGame,false);
		String mailHedgeBets = Dx4Config.getProperties().getProperty("dx4.mailHedgeBets","off");
		if (mailHedgeBets.equals("on"))
			mailHedgeWorkbooks(brm);
	}
	
	private void mailHedgeWorkbooks(Dx4BetRollupMgrUSDMYR brm)
	{
		String mailHedgeNotifications = Dx4Config.getProperties().getProperty("mailHedgeNotifications");
		if (mailHedgeNotifications==null)
		{
			log.error("No mailHedgeNotifications set in dx4.properties");
			return;
		}
		
		log.info("Mailing Hedge Notifications");
		String[] recipients = mailHedgeNotifications.split(";");
		String text = "Dear 4DX Partner please review bet transactions attached.";
		for (String recipient : recipients)
		{
			for (String path : brm.getWorkbookPaths())
			{
				List<String> paths = new ArrayList<String>();
				paths.add(path);
				getMail().sendSimpleMail(recipient, "4DX Partner Update", text, paths );
			}
		}
	}
	
	public void mailWinWorkbooks(Dx4MetaGame mg,Dx4PlayGame playGame) throws Dx4ServicesException
	{
		if (floatPayoutMgr!=null)
			return;
		
		Dx4BetRollupMgrUSDMYR brm = new Dx4BetRollupMgrUSDMYR(this,mg,playGame,true);
		String mailWinBets = Dx4Config.getProperties().getProperty("dx4.mailWinBets","off").toLowerCase().trim();
		log.info("dx4.mailWinBets : is " + mailWinBets);
		if (mailWinBets.equals("on"))
			mailWinWorkbooks(brm);
	}
	
	private void mailWinWorkbooks(Dx4BetRollupMgrUSDMYR brm)
	{
		String mailWinNotifications = Dx4Config.getProperties().getProperty("mailWinNotifications");
		if (mailWinNotifications==null)
		{
			log.error("No mailWinNotifications set in dx4.properties");
			return;
		}
		
		log.info("Mailing Win Notifications");
		String[] recipients = mailWinNotifications.split(";");
		String text = "Dear 4DX Manager please review bet/wintransactions attached.";
		for (String recipient : recipients)
		{
			for (String path : brm.getWorkbookPaths())
			{
				List<String> paths = new ArrayList<String>();
				paths.add(path);
				getMail().sendSimpleMail(recipient, "4DX Manager Update", text, paths );
			}
		}
	}
	
	public void setDx4Home(Dx4Home dx4Home) {
		this.dx4Home = dx4Home;
	}

	public Dx4Home getDx4Home() {
		return dx4Home;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}
	
	public Dx4ExternalService getExternalService()
	{
		return new Dx4ExternalService(this);
	}
	

	public synchronized void performWithdrawlDeposit(final Dx4SecureUser user,final String dwType, final double dwAmount,final String comment)
	{
		log.trace("%%%performWithdrawlDeposit:");
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				doPerformWithdrawlDeposit(user,dwType,dwAmount,comment);
			}
		});
	}
	
	private void doPerformWithdrawlDeposit(Dx4SecureUser user,String dwType, final double dwAmount,String comment) {
		GregorianCalendar gc = new GregorianCalendar();
		if (dwType.equals("Deposit") && dwAmount>0.0)
		{
			dx4Home.performDeposit(user,dwAmount,gc.getTime());
			return;
		}
		if (dwType.equals("Withdrawl") && dwAmount>0.0)
		{
			dx4Home.performWithdrawl(user,dwAmount,gc.getTime());
		}
	}
	
	public synchronized void updatePlayerBet(final Dx4Player player,final Dx4MetaBet metaBet)
	{
		log.trace("%%%updatePlayerBet:");
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				doUpdateBet( player, metaBet );
			}
		});
	}
	
	private void doUpdateBet(Dx4Player player, Dx4MetaBet metaBet)
	{
		dx4Home.insertPlayerMetaBet(player, metaBet);
	}
	
	public synchronized void makePlayerBet(final Dx4Player player,final Dx4MetaBet metaBet, final UUID metaBetUUID)
	{
		log.trace("%%%makePlayerBet:");
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				doPerformMakeBet( player, metaBet, metaBetUUID );
			}
		});
	}
	
	private void doPerformMakeBet(Dx4Player player, Dx4MetaBet metaBet, final UUID metaBetUUID)
	{
		if (floatPayoutMgr!=null)
		{
			floatPayoutMgr.updateBetNumberPayouts(player,metaBet);
			metaBet.setFloatingModel(true);
		}
		
		GregorianCalendar gc = new GregorianCalendar();
		metaBet.setPlaced(gc.getTime());
		metaBet.getTotalStake();
		
		dx4Home.insertPlayerMetaBet(player, metaBet);
		dx4Home.setMetaBetPlaced(metaBet);
		Dx4Transaction transaction = dx4Home.performPayment(player,player.getParent(), metaBet.getTotalStake(), metaBet.getPlaced(), metaBet.getId());
			
		storeMetaBetPDF(player,metaBet,transaction);
		
		if (floatPayoutMgr!=null)
		{
			floatPayoutMgr.updateNumberFloatPayouts(metaBet);
			floatPayoutMgr.deleteBetNumberPayouts(player);
		}

	//	sendMetaBetTransactionEmail(player,metaBet,transaction);
	}
	
	@SuppressWarnings("unused")
	private void sendMetaBetTransactionEmail(final Dx4Player player,final Dx4MetaBet metaBet,final Dx4Transaction transaction)
	{	
		try {
			ModelMap model = new ModelMap();
			model.put("currAccountUser",player);
			model.put("currTransaction",transaction);
			setUpPdfModelValues(model,metaBet);
			PDFStoreTransaction pst = new PDFStoreTransaction(model,player.getParent(),player);
			String text = "Dear " + player.getContact() + ", please review bet transaction attached.";
			List<String> attachments = new ArrayList<String>();
			attachments.add(pst.getPdfPath());
			getMail().sendMail(player, "Dx4 bet transaction", text, attachments );
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error sending mail - " + e.getMessage());
		}
	}
	
	private void storeMetaBetPDF(final Dx4Player player,final Dx4MetaBet metaBet,final Dx4Transaction transaction)
	{	
		try {
			ModelMap model = new ModelMap();
			model.put("currAccountUser",player);
			model.put("currTransaction",transaction);
			setUpPdfModelValues(model,metaBet);
			new PDFStoreTransaction(model,player.getParent(),player);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error sending mail - " + e.getMessage());
		}
	}
	
	public void sendMetaBetEmail(final Dx4Player player,final Dx4MetaBet metaBet) throws Exception
	{	
	
		ModelMap model = new ModelMap();
		model.put("currAccountUser",player);
		setUpPdfModelValues(model,metaBet);
		PDFStoreMetaBet pst = new PDFStoreMetaBet(model,player.getParent(),player);
		String text = "Dear " + player.getContact() + ", please review your bet detail attached.";
		List<String> attachments = new ArrayList<String>();
		attachments.add(pst.getPdfPath());
		getMail().sendMail(player, "Dx4 bet detail", text, attachments );
	}
	
	public void setUpPdfModelValues(ModelMap model,Dx4MetaBet metaBet)
	{
		HashMap<String,byte[]> images = new HashMap<String,byte[]>();
		HashMap<String,Dx4NumberPageElementJson> npes = new HashMap<String,Dx4NumberPageElementJson>();
		for (Dx4ProviderJson prov : getDx4Home().getProviders())
		{
			images.put(prov.getName(), prov.getRawImage());
		}
		
		metaBet.setProvidersFromCodes(getDx4Home());
		if (!metaBet.getWins().isEmpty())
		{
			
			for (Dx4Win win : metaBet.getWins())
			{
				Dx4NumberPageElementJson npe = getDx4Home().getNumberPageElement(win.getResult(),Dx4NumberPageElementJson.DICTIONARYSTANDARD3);
				npes.put(win.getResult(),npe);
				images.put(win.getResult(),getDx4Home().getRawImageForNumber(win.getResult()));
			}
		}
		model.addAttribute("currNpes", npes);
		model.addAttribute("currImages",images);
		model.addAttribute("currMetaBet",metaBet);
	}
	
	
	public void setMetaBetResults(final Dx4MetaGame metaGame,final Dx4PlayGame playGame)
	{
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				doSetMetaBetResults( metaGame, playGame );
			}
		});
	}
	
	private synchronized void winPlayerBet(final Dx4PlayGame playGame,final Dx4MetaBet metaBet,final Dx4Player player)
	{
		log.trace("%%%winPlayerBet:");
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				if (metaBet.getType().equals(MetaBetType.AGENT))
					doPerformWinBet( playGame, metaBet, player );
				else
					throw new Dx4ExceptionFatal("winPlayerBet: illegal MetaBetType");
			}
		});
	}
	
	private void doPerformWinBet(Dx4PlayGame playGame,Dx4MetaBet metaBet,Dx4Player player)
	{
		metaBet.setPlayer(dx4Home.getPlayerById(metaBet.getPlayerId()));
		Dx4Transaction transaction = dx4Home.performCollect(player,player.getParent(), metaBet.getTotalWin(), metaBet.getPlaced(), metaBet.getId());
		metaBet.setOutstanding(false);
		dx4Home.updateMetaBet(playGame,metaBet);
		
		storeMetaBetPDF(player,metaBet,transaction);
	}
	
	private void doSetMetaBetResults(Dx4MetaGame metaGame,Dx4PlayGame playGame)
	{
		ExternalGameResults xgr = getExternalService().getActualExternalGameResults(playGame.getPlayDate());
		
		if (xgr==null)	
		{
			log.warn("NO EXTERNAL RESULTS FOR DATE : " + playGame.getPlayedAt());
			return;
		}
		
		for (Dx4DrawResultJson drj : xgr.getDraws())
		{
			Dx4DrawResultIboxJson drji = new Dx4DrawResultIboxJson();
			drji.create(drj);
			drj.setDrawResultIboxJson(drji);
		}
		
		playGame.setPlayedAt(xgr.getDraws().get(0).getDate());
		playGame.setPlayed(true);
		dx4Home.updatePlayGame(playGame);
		dx4Home.setPlayed(playGame);
//		List<Dx4MetaBet> metaBets = dx4Home.getMetaBetsForMetaGame(metaGame,playGame);
		List<Dx4MetaBet> metaBets = dx4Home.getAllMetaBetsForMetaGame(metaGame,playGame);				// USE THIS TILL WE ADD THE IBOX STUFF TO DPLACING TABLE
		log.info("GOT METABETS : " + metaBets);
		for (Dx4MetaBet metaBet : metaBets)
		{
			metaBet.setTotalWin(0.0);
			metaBet.performWinBet(dx4Home, xgr.getDraws());
			log.info("Metabet with win : " + metaBet);
			
			if (metaBet.getWins().isEmpty())
			{
				dx4Home.updateMetaBet(playGame,metaBet);
				continue;
			}
			
			Dx4Player player = dx4Home.getPlayerById(metaBet.getPlayerId());
			
			winPlayerBet( playGame, metaBet, player );
		}
		
		dx4Home.resetNumberExposBlocked();
		
	}
	
	public void storeDrawResult(final Dx4DrawResultJson result)
	{
		log.trace("%%%storeDrawResult:");
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				doStoreDrawResult( result );
			}
		});
	}
	
	private void doStoreDrawResult(Dx4DrawResultJson result)
	{
		dx4Home.storeDrawResult(result);
	}
	
	public synchronized void updatePlayGames()
	{
		log.trace("%%%updatePlayGames");
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				doUpdatePlayGames();
			}
		});
	}
	
	private void doUpdatePlayGames()
	{
		if (checkTestPastGame()==true)
			return;
		
		List<ExternalComingDate> dates = getExternalService().getExternalComingDates();
		List<Dx4MetaGame> metaGames = dx4Home.getMetaGames();
		for (ExternalComingDate playDate : dates)
		{
			for (Dx4MetaGame metaGame : metaGames)
			{
				log.info("Setting up playgame for metagame : " + metaGame + " for date :" + playDate.getDate());
				if (metaGame.getPlayGame(playDate.getDate()) == null)
				{
					storeNewPlayGame(metaGame,playDate.getDate(),playDate.getProviderCodes());
				}
				else
					log.warn("Playgame for date : " + playDate.getDate() + " For MetaGame : " + metaGame.getName() + " - already exists - ignored");
			}
		}
	}
	
	private boolean checkTestPastGame()
	{
		boolean allowPastGame = Dx4Config.getProperties().getProperty("dx4.allowPastGame", "false").equals("true");
		if (allowPastGame != true)
			return false;
		String dateStr = Dx4Config.getProperties().getProperty("dx4.testPastGameDate");
		if (dateStr==null)
			return false;
		String gameStr = Dx4Config.getProperties().getProperty("dx4.testPastMetaGame");
		if (gameStr==null)
			return false;
		
		Dx4MetaGame metaGame = dx4Home.getMetaGame(gameStr);
		if (metaGame==null)
		{
			log.error("Invalid metagame name for test pastgame : " + gameStr);
			return false;
		}
	
		Date date=null;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			log.error("Unparsable date for test pastgame : " + dateStr);
			return false;
		} 
		
		List<Dx4DrawResultJson> draws = dx4Home.getDrawResults(date,date);
		if (draws.isEmpty())
		{
			log.error("Cannot use datefor test pastgame : " + dateStr + " No draws exist");
			return false;
		}
		
		String providerCodes = "";
		for (Dx4DrawResultJson draw : draws)
		{
			providerCodes += draw.getProvider().getCode();
		}
		Dx4PlayGame playGame = metaGame.getPlayGame(date);
		if (playGame==null)
			storeNewPlayGame(metaGame,date,providerCodes);
		else
		{
			playGame.setPlayed(false);
			playGame.setPlayedAt(null);
			dx4Home.updatePlayGame(playGame);
			dx4Home.cleanUpBetsForPlayDate(date);
			log.info("CLEANING UP ANY BETS + ASSOCIATED INFO FOR HISTORIC GAME DATE : " + date);
		}
		
		log.info("USING HISTORIC TEST PLAYGAME FOR : " + metaGame.getName() + " DATE : " + date);
		return true;
	}
	
	private void storeNewPlayGame(Dx4MetaGame metaGame,Date playDate, String providerCodes)
	{
		Dx4PlayGame playGame = new Dx4PlayGame();
		playGame.setProviderCodes(providerCodes);
		playGame.setPlayDate(playDate);
		dx4Home.insertPlayGame(metaGame, playGame);
	}
	
	public synchronized void storeMetaGame(final Dx4Admin user, final Dx4MetaGame metaGame)
	{
		log.trace("%%%storeMetaGame : " + metaGame.getName() + " for : " + user.getEmail());
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				dx4Home.storeMetaGame(metaGame);
				Dx4GameGroup group = user.getGameGroup(); 
				if (group == null)
				{
					group = new Dx4GameGroup(user);
					user.setGameGroup(group);
				}
				group.getGameActivators().add(new Dx4GameActivator(metaGame,group));
				dx4Home.storeGameGroup(group);
			}
		});
	}

	public synchronized void addMetaGameToGroup(final Dx4SecureUser user, final Dx4MetaGame metaGame) 
	{
			log.trace("%%%addMetaGameToGroup : " + metaGame.getName() + " for : " + user.getEmail());
			new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					Dx4GameGroup group = user.getGameGroup(); 
					group.getGameActivators().add(new Dx4GameActivator(metaGame,group));
					dx4Home.storeGameGroup(group);
				}
			});
		}
	
	public synchronized void removeMetaGameFromGroup(final Dx4SecureUser user, final Dx4MetaGame metaGame) 
	{
			log.trace("%%%removeMetaGameFromGroup : " + metaGame.getName() + " for : " + user.getEmail());
			new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					doRemoveMetaGameFromGroups(user,metaGame);
				}
			});
		}
	
	private void doRemoveMetaGameFromGroups(Dx4SecureUser parent,Dx4MetaGame metaGame) 
	{
			log.trace("doRemoveMetaGameFromGroups : " + metaGame.getName() + " for : " + parent.getEmail());
			dx4Home.getDownstreamForParent(parent);
			for (Dx4SecureUser member : parent.getMembers()) 
			{
				doRemoveMetaGameFromGroups(member,metaGame);
			}
			removeMetaGameFromGroup(parent.getGameGroup(),metaGame);
		}
	
		private void removeMetaGameFromGroup(Dx4GameGroup group,Dx4MetaGame metaGame)
		{
			log.trace("removeMetaGameFromGroup : " + metaGame.getName());
			
			if (group == null)
				return;
			Dx4GameActivator ga = group.getGameActivator(metaGame);
			if (ga!=null)
			{
				group.getGameActivators().remove(ga);
				dx4Home.storeGameGroup(group);
			}
		}
	
		public synchronized void storeMember(final Dx4SecureUser member,final Dx4SecureUser parent) throws PersistenceRuntimeException
		{
			log.trace("%%%perform storeMember for:" + member.getEmail());
			member.setEnabled(true);			// default to enabled
			new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				
				member.setParent(parent);
				
				if (member.getRole().equals(Dx4Role.ROLE_PLAY))
					getDx4Home().storePlayer((Dx4Player) member,parent);
				else
				if (member.getRole().equals(Dx4Role.ROLE_ADMIN))
				{
					Dx4Admin dx4Admin = (Dx4Admin) member;
					dx4Admin.initializeGameGroups(dx4Home);
					getDx4Home().storeAdmin((Dx4Admin) member);
				}
				else
					getDx4Home().storeAgent((Dx4Agent) member,parent);
				// sendEmailConfirmation(member,parent);
			}
			});
		}
		  
		public synchronized void updateEnabled(final Dx4SecureUser user,final boolean flag)
		{
			log.trace("%%%perform updateEnabled for:" + user.getEmail());
			new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				doUpdateEnabled(user,flag);
			}
			});
		}
		
		private void doUpdateEnabled(Dx4SecureUser user,boolean flag)
		{
			user.setEnabled(flag);
			try {
				getDx4Home().updateEnabled(user);
			} catch (Dx4PersistenceException e) {
				e.printStackTrace();
				throw new PersistenceRuntimeException("", e.getMessage());
			}
			if (flag!=false)
				return;
			getDx4Home().getDownstreamForParent(user);
			for (Dx4SecureUser member : user.getMembers())
			{
				doUpdateEnabled(member,flag);
			}
		}
		
		public synchronized void deleteMember(final Dx4SecureUser member) throws PersistenceRuntimeException
		{
			// DELETES ALL MEMBER,CHILD MEMBER TREE, TRANSACTIONS, ETC. ONLY USE FOR DEV TESTING
			log.trace("%%%perform deleteMember for:" + member.getEmail());
			new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				try {
					getDx4Home().deleteMember(member);
				} catch (Dx4PersistenceException e) {
					e.printStackTrace();
				}
			}
			});
		}
		
		public synchronized void validateNumberExposure(Dx4SecureUser member,Dx4PlayGame playGame,String number,double newStake,double newWin) 
										throws Dx4ServicesException
		{
			Dx4SecureUser parent;
			try {
				parent = dx4Home.getParentForUser(member);
			} catch (Dx4PersistenceException e) {
				e.printStackTrace();
				throw new PersistenceRuntimeException("", e.getMessage());
			}
			while (parent.getRole().isAgentRole())
			{
				// exposure so far - calculated from outstanding bets
				NumberExpo numberExpo = getDx4Home().getSingleNumberExpo(parent.getCode(), playGame, parent.getRole(), 
						-1L, false, number);
				// update with proposed bet
//				log.info("testNumberExposure current numberExpo is :" +  numberExpo);
				numberExpo.setTbet(numberExpo.getTbet()+newStake);
				numberExpo.setExpo(numberExpo.getExpo()+newWin);
//				log.info("testNumberExposure updated numberExpo is :" +  numberExpo);
				Dx4NumberExpo expo = getDx4Home().getDx4NumberExpoForUser(parent, number);
				if (expo==null)
				{
					String digits = Integer.toString(number.length());
					expo = getDx4Home().getDx4DefaultNumberExpoForUser(parent, digits.charAt(0) );
				}
				if (numberExpo.getTbet() > expo.getBetExpo() && expo.getBetExpo()>=0)
				{
					log.info("testNumberExposure : failed for agent: " + parent.getEmail() +
							" for number: " + number + " total bet: " +
							numberExpo.getTbet() + " bigger than: " + expo.getBetExpo());
					throw new Dx4ServicesException("Number : " + number + " maxed out for bet on upstream agent.");
				}
				if (numberExpo.getExpo() > expo.getWinExpo() && expo.getWinExpo()>=0)
				{
					log.info("testNumberExposure : failed for agent: " + parent.getEmail() +
							" for number: " + number + " total win: " +
							numberExpo.getExpo() + " bigger than: " + expo.getWinExpo());
					throw new Dx4ServicesException("Number : " + number + " maxed out for win on upstream agent.");
				}
				try {
					parent = dx4Home.getParentForUser(parent);
				} catch (Dx4PersistenceException e) {
					e.printStackTrace();
					throw new PersistenceRuntimeException("", e.getMessage());
				}
			}
		}
		
		public synchronized List<String> validateMetaBetExposure(Dx4MetaBet metaBet) 
		{
			Dx4SecureUser member = metaBet.getPlayer();
			List<String> invalidNumbers = new ArrayList<String>();
			for (Dx4Bet bet : metaBet.getBets())
			{
				String number = bet.getChoice();
				double newStake = metaBet.getBetExpo(number);
				double newWin = metaBet.getWinExpo(number);
				try
				{
					validateNumberExposure(member,metaBet.getPlayGame(),number,newStake,newWin);
				}
				catch (Dx4ServicesException e)
				{
					log.trace("Number : " + number + " exceeds number exposure");
					invalidNumbers.add(number);
				}
				if (number.length()>3 && metaBet.contains3DGame())
				{
					number = number.substring(1);
					try
					{
						validateNumberExposure(member,metaBet.getPlayGame(),number,newStake,newWin);
					}
					catch (Dx4ServicesException e)
					{
						log.trace("Number : " + number + " exceeds number exposure");
						invalidNumbers.add(number);
					}
				}
			}
			return invalidNumbers;
		}
		
		public Mail getMail() {
			return mail;
		}

		public void setMail(Mail mail) {
			this.mail = mail;
		}

		public String getDx4PropertiesPath() {
			return dx4PropertiesPath;
		}

		public void setDx4PropertiesPath(String dx4PropertiesPath) {
			this.dx4PropertiesPath = dx4PropertiesPath;
		}

		public Dx4FloatPayoutMgr getFloatPayoutMgr() {
			return floatPayoutMgr;
		}

		public void setFloatPayoutMgr(Dx4FloatPayoutMgr floatPayoutMgr) {
			this.floatPayoutMgr = floatPayoutMgr;
		}

		public void resetPassword(Dx4SecureUser baseUser) throws Exception {
			int cnt=0;
			StringBuilder pw = new StringBuilder();
			while (cnt<10)
			{
				Random rand = new Random();
				
				switch (rand.nextInt(3))
				{
					case 0: int lim = rand.nextInt(25);
							char ch = 'A';
							for (; lim>0; ch++,lim--)
								;
							pw.append(ch); break;
					case 1: lim = rand.nextInt(8);
							ch = '1';
							for (; lim>0; ch++,lim--)
								;
							pw.append(ch); break;
					case 2: lim = rand.nextInt(25);
							ch = 'a';
							for (; lim>0; ch++,lim--)
								;
							pw.append(ch); break;
				}
				cnt++;
			}
			
			log.info("New password for : " + baseUser.getEmail() + " is : " + pw);
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			baseUser.setPassword(encoder.encode(pw.toString()));

			getDx4Home().updateUserProfile(baseUser);
			mail.notifyPasswordReset(baseUser,null,pw.toString());
		}
		
		public String tryResetPassword(String email)
		{
			log.info("Received request to password ");
			Dx4SecureUser baseUser;
			try {
				baseUser = getDx4Home().getByUsername(email, Dx4SecureUser.class);
			} catch (Exception e) {
				e.printStackTrace();
				return "Error on reset, please try later.";
			}
			if (baseUser==null)
			{
				return "User : " + email + " has not been registered - please consult your agent";
			}
			
			try {
				resetPassword(baseUser);
			} catch (Exception e) {
				e.printStackTrace();
				return "Error on mail server, please try later.";
			}
			return "";
		}
		

}
