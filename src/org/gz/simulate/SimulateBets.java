package org.gz.simulate;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.log4j.Logger;
import org.gz.baseuser.GzBaseUser;
import org.gz.home.GzHome;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.json.GzBet;
import org.gz.json.GzGameType;
import org.gz.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimulateBets 
{
	private static Logger log = Logger.getLogger(SimulateBets.class);

	public static GzGameType createGameType()
	{
		int num = GzGameType.values().length;
		Random rand = new Random();
		return GzGameType.values()[rand.nextInt(num)];
	}
	
	public static String createChoice(GzGameType gameType)
	{
		Random rand = new Random();
		String choice = ""+ rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10);
		if (gameType.getDigits()==4)
			return choice +  rand.nextInt(10);
		return choice;
	}
	
	private static GzBet createBet(String memberId,Date playDate)
	{
		GzBet bet = new GzBet();
		bet.setMemberId(memberId);
		bet.setGameType(createGameType());
		bet.setChoice(createChoice(bet.getGameType()));			
		bet.setTotalStake((new Random()).nextInt(5)*10);
		bet.setPlayDate(playDate);
		return bet;
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome home = gzServices.getGzHome();
		
//		Map<Integer,GzBetRollup> rollups = new TreeMap<Integer,GzBetRollup>();
		GregorianCalendar gc = new GregorianCalendar();
		GzBet bet = createBet("0018",gc.getTime());
		bet.setChoice("2828");
		bet.setTotalStake(30.0);
		
		GzBaseUser player;
		try {
			player = home.getBaseUserByMemberId("0018");
			gzServices.getGzAccountMgr().createTransactions(player, bet.getTotalStake(), "test", bet.getGameType(), bet.getChoice());
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("Done");
		/*
		for (int i=0; i<10; i++)
		{
			GzBet bet = createBet("0018",gc.getTime());
			GzBetRollup r = rollups.get(bet.getGameType().getIndex());
			if (r==null)
			{
				r = new GzBetRollup();
				rollups.put(bet.getGameType().getIndex(),r);
			}
			r.setStake(r.getStake()+bet.getTotalStake());
			r.setGameType(bet.getGameType());
			r.setDate(bet.getPlayDate());
			r.setMemberId(bet.getMemberId());
			log.info(bet);
		}
		*/
	}
}
