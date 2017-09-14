package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.d2.Dx4GameD2A;
import org.dx4.game.d2.Dx4GameD2B;
import org.dx4.game.d2.Dx4GameD2C;
import org.dx4.game.d2.Dx4GameD2D;
import org.dx4.game.d2.Dx4GameD2E;
import org.dx4.game.d2.Dx4GameD2EX;
import org.dx4.game.d3.Dx4GameABCA;
import org.dx4.game.d3.Dx4GameABCB;
import org.dx4.game.d3.Dx4GameABCC;
import org.dx4.game.d3.Dx4GameABCCC;
import org.dx4.game.d3.Dx4GameABCD;
import org.dx4.game.d3.Dx4GameABCE;
import org.dx4.game.d4.Dx4GameD4A;
import org.dx4.game.d4.Dx4GameD4B;
import org.dx4.game.d4.Dx4GameD4Big;
import org.dx4.game.d4.Dx4GameD4BoxBig;
import org.dx4.game.d4.Dx4GameD4BoxSmall;
import org.dx4.game.d4.Dx4GameD4C;
import org.dx4.game.d4.Dx4GameD4CC;
import org.dx4.game.d4.Dx4GameD4D;
import org.dx4.game.d4.Dx4GameD4E;
import org.dx4.game.d4.Dx4GameD4IBoxBig;
import org.dx4.game.d4.Dx4GameD4IBoxSmall;
import org.dx4.game.d4.Dx4GameD4Small;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGamesZZ
{
	private static final Logger log = Logger.getLogger(TestGamesZZ.class);
	
	
	private static void create2DGames(Dx4Home dx4Home, Dx4MetaGame dx4MetaGame) {
		Dx4Game game = new Dx4GameD2A();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,67.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD2C();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,22.5));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,22.5));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,22.5));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD2EX();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,2.8));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,2.8));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,2.8));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,2.8));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,2.8));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD2B();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,67.00));
		dx4MetaGame.getGames().add(game);
		
		game = new Dx4GameD2C();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,67.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD2D();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,6.7));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD2E();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,6.7));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
	}
	
	private static void create3DGames(Dx4Home dx4Home, Dx4MetaGame dx4MetaGame) {
		Dx4Game game = new Dx4GameABCA();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,670.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameABCC();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,225.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,225.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,225.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameABCB();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,670.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameABCCC();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,670.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameABCD();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,67.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameABCE();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,67.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
	}
	
	private static void create4DGames(Dx4Home dx4Home, Dx4MetaGame dx4MetaGame) {
		Dx4Game game = new Dx4GameD4Big();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,1000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,500.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,250.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,75.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4Small();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,3000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,1000.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4A();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,6000.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4C();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,2000.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4D();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,600.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4E();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,600.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4B();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,6000.00));
		dx4MetaGame.getGames().add(game);
		
		game = new Dx4GameD4CC();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,6000.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
	}
	
	private static void createBoxGames(Dx4Home dx4Home, Dx4MetaGame dx4MetaGame) {
		Dx4Game game = new Dx4GameD4IBoxBig();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,1000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,500.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,250.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,75.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4IBoxSmall();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,3000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,1000.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4BoxBig();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,1000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,500.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Spec,250.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Cons,75.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
		
		game = new Dx4GameD4BoxSmall();
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.First,3000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Second,2000.00));
		game.addPayOut(new Dx4PayOut(Dx4PayOutTypeJson.Third,1000.00));
		dx4MetaGame.getGames().add(game);
		dx4Home.storeGameForMetaGame(dx4MetaGame, game);
	}
	
	private static void createGames(Dx4Home dx4Home, Dx4MetaGame metaGame) {
		create4DGames(dx4Home,metaGame);
		create3DGames(dx4Home,metaGame);
		create2DGames(dx4Home,metaGame);
		createBoxGames(dx4Home,metaGame);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Px4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home dx4Home = dx4Services.getDx4Home();

		log.info("WUFF WUFF WUFF");
		
		try
		{
	//		Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
	//		createGames(dx4Home,metaGame);
			
	//		log.info("Done");
			
			Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
			for (Dx4Game game : metaGame.getGames())
				log.info(game);
		}
		
		catch (Exception e)
		{
			log.info("BOLLOCKS: " + e + " - " + e.getMessage());
		}
	}

	
}
