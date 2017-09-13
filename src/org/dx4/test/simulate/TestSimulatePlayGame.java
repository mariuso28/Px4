package org.dx4.test.simulate;

import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSimulatePlayGame {

private static final Logger log = Logger.getLogger(TestSimulatePlayGame.class);
	
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Home dx4Home = dx4Services.getDx4Home();
		Dx4MetaGame metaGame = dx4Home.getMetaGameById(81);
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(2017,4,20);
		
		Dx4PlayGame playGame = metaGame.getPlayGame(gc.getTime());
		
		
		log.info("Simulating playgame : " + playGame);
		dx4Services.setMetaBetResults(metaGame,playGame);
	}
		
}
