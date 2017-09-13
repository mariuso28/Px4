package org.dx4.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.persistence.NumberExpo;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestNumberExpo {

	private static final Logger log = Logger.getLogger(TestNumberExpo.class);
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		/*
		List<Date> dates = dx4Services.getDx4Home().getDrawResultsDateRange();
		NumberGridForm numberGridForm = new NumberGridForm(dx4Services,"4",dates.get(0),dates.get(1));
		log.info(numberGridForm);
		
		System.exit(1);
		*/
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame("4D With ABC");
		Dx4PlayGame playGame = metaGame.getNextGameAvailableForBet(); 
		
		
		log.info("ROLE_COMP");
		List<NumberExpo> expos = dx4Services.getDx4Home().getNumberExpo("c0",playGame, Dx4Role.ROLE_COMP, 0.0,true);
		for (NumberExpo mg : expos)
			log.info(mg);
		log.info("ROLE_ZMA");
		expos = dx4Services.getDx4Home().getNumberExpo("c0z0", playGame,Dx4Role.ROLE_ZMA, 0.0,true);
		for (NumberExpo mg : expos)
			log.info(mg);
		log.info("ROLE_SMA");
		expos = dx4Services.getDx4Home().getNumberExpo("c0z0s0",playGame, Dx4Role.ROLE_SMA, 0.0,true);
		for (NumberExpo mg : expos)
			log.info(mg);
		log.info("ROLE_MA");
		expos = dx4Services.getDx4Home().getNumberExpo("c0z0s0m0",playGame, Dx4Role.ROLE_MA, 0.0,true);
		for (NumberExpo mg : expos)
			log.info(mg);
		log.info("ROLE_AGENT");
		expos = dx4Services.getDx4Home().getNumberExpo("c0z0s0m0a0",playGame, Dx4Role.ROLE_AGENT, 0.0,true);
		for (NumberExpo mg : expos)
			log.info(mg);
	}
}
