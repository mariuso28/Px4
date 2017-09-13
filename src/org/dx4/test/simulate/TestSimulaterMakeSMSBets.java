package org.dx4.test.simulate;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.external.bet.Ex4BetSMSParser;
import org.dx4.external.bet.Ex4Exception;
import org.dx4.player.Dx4Player;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSimulaterMakeSMSBets{
	private static final Logger log = Logger.getLogger(TestSimulaterMakeSMSBets.class);
	

	public static void main(String[] argvs)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
								"Dx4-Service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		try {
			Ex4BetSMSParser parser = new Ex4BetSMSParser(dx4Services,"C:/_Development/gexZZ/4DX/sample/sample bets.txt","4D With ABC");
			for (Dx4MetaBet metaBet : parser.getMetaBets())
			{
				log.info(metaBet);
				
				dx4Services.makePlayerBet((Dx4Player) metaBet.getPlayer(), metaBet, UUID.randomUUID());
			}
		} catch (Ex4Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} 
		
		
	}

}
