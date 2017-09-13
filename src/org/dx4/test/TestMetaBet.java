package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMetaBet {

	private static final Logger log = Logger.getLogger(TestMetaBet.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		
		Dx4Home dx4Home = dx4Services.getDx4Home();
		Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
		Dx4PlayGame playGame = metaGame.getPlayGameById(306);
			
		dx4Services.setMetaBetResults(metaGame,playGame);
	}
		
}

