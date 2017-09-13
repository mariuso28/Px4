package org.dx4.test;

import org.apache.log4j.Logger;
import org.dx4.agent.Dx4Agent;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.secure.web.agent.AgentGamesForm;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TesterGetAgent{
private static final Logger log = Logger.getLogger(TesterGetAgent.class);
	
	public static void main(String[] args) throws Dx4PersistenceException {
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"DX4-Persistence.xml");
		Dx4Home dx4Home = (Dx4Home) context.getBean("dx4Home");
		
		
		Dx4Agent tester = dx4Home.getAgentByUsername("zma0@gmail.com");
		Dx4Agent parent = dx4Home.getAgentByUsername("company0@gmail.com");
		
		log.info(tester.toString());
		log.info(parent.toString());
		
	
/*		
		Dx4GameActivator ga1 = ag.getGameActivators().get(0);
		log.info(ga1);
		Dx4GameActivator ga2 = pg.getGameActivator(ga1.getMetaGame());
		log.info(ga1);
	*/	
		AgentGamesForm agf = new AgentGamesForm(parent, tester);
		
		log.info(agf);
		
	}
}
