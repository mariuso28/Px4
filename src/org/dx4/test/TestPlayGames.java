package org.dx4.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPlayGames {

	private static final Logger log = Logger.getLogger(TestPlayGames.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		// dx4Services.updatePlayGames();
//		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame("4D With ABC");
		List<Dx4MetaGame> metaGames = dx4Services.getDx4Home().getUnplayedMetaGames();
		for (Dx4MetaGame mg : metaGames)
			log.info(mg);
	}
}
