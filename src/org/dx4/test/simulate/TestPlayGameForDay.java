package org.dx4.test.simulate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.services.Dx4Services;
import org.dx4.util.DateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPlayGameForDay {
private static final Logger log = Logger.getLogger(TestPlayGameForDay.class);
	
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Home dx4Home = dx4Services.getDx4Home();
		Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.DAY_OF_MONTH, 16);
		gc.set(Calendar.MONTH, 3);
		gc.set(Calendar.YEAR,2017);
		Date date = DateUtil.getDateWithZeroedTime(gc.getTime());
		Dx4PlayGame playGame = metaGame.getPlayGame(date);
		
		if (playGame == null || playGame.isPlayed())
		{
			log.error("NO UNPLAYED PLAYGAME	- TERMINATING");
			System.exit(1);
		}
		
		playGame.setPlayedAt(new GregorianCalendar().getTime());
				
		
		dx4Services.setMetaBetResults(metaGame,playGame);
	}
		
}
