package org.dx4.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.agent.Dx4Agent;
import org.dx4.bet.Dx4MetaBetExpoOrder;
import org.dx4.bet.persistence.MetaBetExpoRowMapperPaginated;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDx4NumberExpoIntra {

	
private static final Logger log = Logger.getLogger(TestDx4NumberExpoIntra.class);
	
	
	public static void main(String[] args) throws Exception{
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Dx4-Service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home dx4Home = dx4Services.getDx4Home();
	
		List<Dx4MetaBetExpoOrder> ordering = new ArrayList<Dx4MetaBetExpoOrder>();
		ordering.add(Dx4MetaBetExpoOrder.choice);
		ordering.add(Dx4MetaBetExpoOrder.tbet);
		ordering.add(Dx4MetaBetExpoOrder.code);
	
		Dx4SecureUser currUser = (Dx4SecureUser) dx4Home.getUserByCode("c0");
		Dx4MetaGame metaGame = dx4Services.getDx4Home().getMetaGame("4D With ABC");
		Dx4PlayGame playGame = metaGame.getNextGameAvailableForBet(); 
		
		MetaBetExpoRowMapperPaginated mpg = dx4Home.getMetaBetExpoRowMapperPaginated((Dx4Agent) currUser, playGame, ordering, 8);
		log.info(mpg.getNextPage());
	}
}
