package org.dx4.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.game.d2.Dx4GameD2A;
import org.dx4.game.d4.Dx4GameD4Big;
import org.dx4.game.d4.Dx4GameD4Small;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGames
{
	private static final Logger log = Logger.getLogger(TestGames.class);
	
	private static void addPlayGame(Dx4Home dx4Home,Dx4MetaGame dx4MetaGame)
	{
		Dx4PlayGame dx4PlayGame = new Dx4PlayGame();
		GregorianCalendar gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
		gc.roll(Calendar.DAY_OF_YEAR,15);
		Date playDate = gc.getTime();
		dx4PlayGame.setPlayDate(playDate);
		dx4MetaGame.getPlayGames().add(dx4PlayGame);
		dx4Home.insertPlayGame(dx4MetaGame, dx4PlayGame);
	}
	
	@SuppressWarnings("unused")
	private static void testDx42DigitGame(Dx4MetaGame dx4MetaGame)
	{
		Dx4GameD2A game = new Dx4GameD2A();
		game.setMaxBet(100.0);
		game.setMinBet(1);
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,100));
		game.setStake(1);
		dx4MetaGame.getGames().add(game);
		
	}
	
	public static void testGames(Dx4Home dx4Home) throws Exception
	{
		Dx4MetaGame metaGame = new Dx4MetaGame();
		metaGame.setName("4 D Multi Bet");
		metaGame.setDescription("4 Digit with 6 different bets");
		
		log.info("BEFORE :");
		log.info(metaGame);
		dx4Home.storeMetaGame(metaGame);
		addPlayGame(dx4Home,metaGame);
		metaGame = dx4Home.getMetaGameById(metaGame.getId());
		log.info("AFTER :");
		log.info(metaGame);
	}
	
	private static void testDx44DigitBigSmall(Dx4MetaGame dx4MetaGame)
	{
		Dx4GameD4Big game = new Dx4GameD4Big();
		game.setMaxBet(100.0);
		game.setMinBet(1);
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,2500.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,1000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,500.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,180.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,60.00));
		game.setStake(1);
		dx4MetaGame.getGames().add(game);
		
		Dx4GameD4Small game1 = new Dx4GameD4Small();
		game1.setMaxBet(100.0);
		game1.setMinBet(1);
		game1.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,3500.00));
		game1.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,2000.00));
		game1.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,1000.00));
		game1.setStake(1);
		dx4MetaGame.getGames().add(game1);
	}
	
	public static void testGameMagnum(Dx4Home dx4Home) throws Exception
	{
		Dx4MetaGame metaGame = new Dx4MetaGame();
		metaGame.setName("4D Big Small");
		metaGame.setDescription("4 Digit Big/Small Specials and Consolation");
		
		testDx44DigitBigSmall(metaGame);
		
		log.info("BEFORE :");
		log.info(metaGame);
		dx4Home.storeMetaGame(metaGame);
		addPlayGame(dx4Home,metaGame);
		metaGame = dx4Home.getMetaGameById(metaGame.getId());
		log.info("AFTER :");
		log.info(metaGame);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home dx4Home = dx4Services.getDx4Home();

		log.info("WUFF WUFF WUFF");
		
		try
		{
			testGames(dx4Home);
			/*
			Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
			List<Dx4PlayGame> playGames = metaGame.getPlayGamesAvailableForBets(); 
			log.info("Play Games: " + playGames);
			Date date = metaGame.nextDrawCutOffTime();
			log.info("Next draw cut off: " + date);
			*/
		}
		catch (Exception e)
		{
			log.info("BOLLOCKS: " + e + " - " + e.getMessage());
		}
	}
}
