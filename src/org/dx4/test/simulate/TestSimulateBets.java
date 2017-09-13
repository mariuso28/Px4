package org.dx4.test.simulate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.dx4.bet.floating.Dx4FloatPayoutMgr;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.json.message.Dx4BetJson;
import org.dx4.json.message.Dx4MetaBetJson;
import org.dx4.json.message.Dx4MetaGameJson;
import org.dx4.json.server.JsonServerServices;
import org.dx4.player.Dx4Player;
import org.dx4.services.Dx4Services;
import org.dx4.services.RestServices;
import org.dx4.services.RestServicesException;
import org.dx4.util.DateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSimulateBets 
{
	private static final Logger log = Logger.getLogger(TestSimulateBets.class);
	
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Home dx4Home = dx4Services.getDx4Home();
		Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.DAY_OF_MONTH, 23);
		gc.set(Calendar.MONTH, 7);
		gc.set(Calendar.YEAR,2017);
		Date date = DateUtil.getDateWithZeroedTime(gc.getTime());
		Dx4PlayGame playGame = metaGame.getPlayGame(date);
		
		gc.add(Calendar.DAY_OF_YEAR, -1);
		Date betDate = gc.getTime();
		
		if (playGame == null)
		{
			log.error("NO UNPLAYED PLAYGAME	- TERMINATING");
			System.exit(1);
		}
		
		String[] players = { "fan1@test.com", "fan2@test.com", "fan3@test.com", "fan4@test.com" };
		
		int combos = 0;
		JsonServerServices js = (JsonServerServices) context.getBean("jsonServerServices");
		RestServices restServices = (RestServices) context.getBean("restServices");
		
//		UUID id = UUID.randomUUID();
		int cnt = 0;
		for (int p=0; p<players.length; p++)
		{
			Dx4MetaBetJson metaBet = null;
			
			for (int i=0; i<3; i++)
			{
				try {
					Dx4MetaGameJson metaGameJson = js.createDx4MetaGameJson(dx4Services.getDx4Home().getMetaGameById(81),dx4Services);
					metaBet = MetaBetSimulate.createMetaBetJson(dx4Services, metaGameJson, playGame, betDate);
					if (metaBet.getBets().size()==0)
						continue;
					
					Dx4Player player = dx4Home.getPlayerByUsername(players[p]);
					
					if (dx4Services.getFloatPayoutMgr()!=null)
						simulateViewChoices(dx4Services.getFloatPayoutMgr(),player,metaBet);
					
					log.info("Making bet : " + cnt++ + "# combinations :" + metaBet.getBets().size());
					combos += metaBet.getBets().size();
					
					
					
	//				metaBet.setMetaBetUUID(id);
					
					String invalidChoices = restServices.placeMetaBet(player,metaBet,js);
					if (!invalidChoices.isEmpty())
						log.error(invalidChoices);
					
				} catch (Dx4PersistenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				} catch (RestServicesException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	//			if (i==2)
	//				System.exit(8);
			}
		}
		
		log.info("# Combos : " + combos);
	}


	private static void simulateViewChoices(Dx4FloatPayoutMgr fpm,Dx4Player player,Dx4MetaBetJson metaBet) {
		for (Dx4BetJson bet : metaBet.getBets())
		{
			fpm.getDx4NumberFloatPayoutJson(player,bet.getChoice());
		}
	}
}
