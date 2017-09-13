package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4GameGroup;
import org.dx4.game.Dx4MetaGame;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGameGroup {

	private static final Logger log = Logger.getLogger(TestGameGroup.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4SecureUser currUser = dx4Services.getDx4Home().getUserByCode("Dx4");
		Dx4GameGroup gg = currUser.getGameGroup();
		
		log.info(gg);
		
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame("Test Game");
		
		dx4Services.addMetaGameToGroup(currUser,metaGame);
		
		currUser = dx4Services.getDx4Home().getUserByCode("Dx4");
		
		gg = currUser.getGameGroup();
		
		log.info(gg);
		boolean boo = currUser.getGameGroup().containsMetaGame(metaGame);
		log.info(boo);
	}
}
