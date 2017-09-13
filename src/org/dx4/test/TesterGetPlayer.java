package org.dx4.test;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.dx4.home.Dx4Home;
import org.dx4.player.Dx4Player;
import org.dx4.secure.web.player.PlayerDetailsForm;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TesterGetPlayer{
private static final Logger log = Logger.getLogger(TesterGetPlayer.class);
	
	public static void main(String[] args) {
		log.info("MEOW MEOW MEOW");
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
		"Dx4-Service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");

		Dx4Home dx4Home = (Dx4Home) dx4Services.getDx4Home();
		
		
		Dx4Player tester = dx4Home.getPlayerByUsername("mariuso.pk@gmail.com");
		
		HashSet<Long> currExpandedBets = new HashSet<Long>();
		HashSet<Long> currExpandedWins = new HashSet<Long>();
		
		@SuppressWarnings("unused")
		PlayerDetailsForm pdf = new PlayerDetailsForm(tester,dx4Services,currExpandedBets,currExpandedWins);
		
		
		log.info(tester.toString());
	}
}
