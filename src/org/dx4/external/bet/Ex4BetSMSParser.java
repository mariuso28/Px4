package org.dx4.external.bet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Account;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.game.Dx4MetaGame;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.player.Dx4Player;
import org.dx4.secure.domain.Dx4Role;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Ex4BetSMSParser 
{
	private List<Dx4MetaBet> metaBets;
	private Dx4MetaBet currMetaBet;
	private static final Logger log = Logger.getLogger(Ex4BetSMSParser.class);
	private Dx4Services services;
	private Dx4MetaGame metaGame;
	
	public Ex4BetSMSParser(Dx4Services dx4Services,String source,String game) throws Ex4Exception
	{
		setServices(dx4Services);
		metaGame = dx4Services.getDx4Home().getMetaGame(game);
		if (metaGame==null)
			throw new Ex4Exception("MetaGame : " + game + " not found");
		
		File file = new File(source);                
		InputStreamReader ins;
		try
		{
			ins = new InputStreamReader( new FileInputStream(file) );
		}
		catch (FileNotFoundException e)
		{
			throw new Ex4Exception("File : " + source + " not found " + e.getMessage());
		}
		BufferedReader bins = new BufferedReader( ins );
		metaBets = new ArrayList<Dx4MetaBet>();
		while (true)
		{
			String message;
			try
			{
				message = bins.readLine();
			}
			catch (IOException e)
			{
				try {
					bins.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				throw new Ex4Exception("Reading file : " + source + " - " + e.getMessage());
			}
			if (message == null)
				break;
			currMetaBet = null;
			try
			{
				createMetaBet(message.trim());
			}
			catch (Ex4MetaBetCreateException e)
			{
				log.warn(e.getMessage());
				log.warn("Couldn't create metabet from : " + message + " - ignored");
				continue;
			}
			if (currMetaBet == null)
				continue;
			log.info("created metabet: " + currMetaBet);
			metaBets.add(currMetaBet);
		}
		try {
			bins.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			log.error("Closing : " + source + " - " + e1.getMessage());
		}
	}
	
	private void createMetaBet(String message) throws Ex4MetaBetCreateException{
		
		if (message.isEmpty())
			return;
		Ex4BetSMS betSMS; 
		try
		{
			betSMS = new Ex4BetSMS(message);
		}
		catch (Ex4ParseException e)
		{
			log.warn("Parsing error on : " + message + " - ignored");
			return;
		}
		createMetaBet(betSMS);
		List<String> invalidNumbers = services.validateMetaBetExposure(currMetaBet); 
		if (invalidNumbers.size()>0)
		{
			String errMsg = "Metabet exposure exceeded for numbers: " + invalidNumbers;
			throw new Ex4MetaBetCreateException(betSMS.getMessage(),errMsg);
		}	
		Dx4Player player = (Dx4Player) currMetaBet.getPlayer();
		Dx4Account account = player.getAccount();
		double availableFunds = account.getBalance();
		availableFunds -= currMetaBet.getTotalStake();
		if (availableFunds<0)
		{
			String errMsg = "Player : " + player.getEmail() + "has insufficient funds for Metabet";
			throw new Ex4MetaBetCreateException(betSMS.getMessage(),errMsg);
		}	
	}
	
	private void createMetaBet(Ex4BetSMS betSMS) throws Ex4MetaBetCreateException
	{
		currMetaBet = new Dx4MetaBet();
		currMetaBet.setMetaGame(metaGame);
		currMetaBet.setPlayGame(metaGame.getPlayGames().get(0));
		createPlayer(betSMS);
//		createProviders(betSMS);
		createNumbers(betSMS);
		createStakes(betSMS);
	}

	private void createPlayer(Ex4BetSMS betSMS) throws Ex4MetaBetCreateException
	{
		try
		{
			Dx4SecureUser user = services.getDx4Home().getUserByCode(betSMS.getUserCode());
			if (user==null || user.getRole()!=Dx4Role.ROLE_PLAY)
				throw new Ex4MetaBetCreateException(betSMS.getMessage(),"Player : " + betSMS.getUserCode() + " not found");
			currMetaBet.setPlayer(user);
			Dx4SecureUser cp = services.getDx4Home().getParentForUser(user);
			currMetaBet.setCp(cp);
		}
		catch (Dx4PersistenceException e)
		{
			e.printStackTrace();
			throw new Ex4MetaBetCreateException("Persistence Exception",e.getMessage());
		}
	}
	
	/*
	private void createProviders(Ex4BetSMS betSMS) throws Ex4MetaBetCreateException
	{
		for (int i=0; i<betSMS.getProviderCodes().length(); i++)
		{
			Character code = betSMS.getProviderCodes().charAt(i);
			if (!Character.isAlphabetic(code))
				continue;
			Dx4ProviderJson provider = services.getDx4Home().getProviderByCode(Character.toUpperCase(code));
			if (provider!=null)
				currMetaBet.addProvider(provider);
		}
		if (currMetaBet.getProviderNum()==0)
			throw new Ex4MetaBetCreateException(betSMS.getMessage(),"Providers for : " +betSMS.getProviderCodes() + " not found");
	}
	*/
	
	private void createNumbers(Ex4BetSMS betSMS) throws Ex4MetaBetCreateException
	{
		for (String number : betSMS.getNumbers())
		{
			if (number.length()==3 || number.length()==4)
			{
				boolean valid = true;
				for (int i=0; i<number.length(); i++)
				{
					if (!Character.isDigit(number.charAt(i)))
					{
						valid = false;
						break;
					}
				}
				if (valid)
				{
				//	currMetaBet.getChoices().add(number);
					continue;
				}
			}
			log.warn("betSMS number : " + number + " cannot be parsed - ignored");
		}
	
	}
	
	private void createStakes(Ex4BetSMS betSMS) throws Ex4MetaBetCreateException
	{
		// these are inorder for standard 4D game 
		// 4Dsmall, 4Dbig, ABCA, ABCC
		int index=0;
		for (Double stake : betSMS.getStakes())
		{
			if (index==0)
				currMetaBet.getBets().add( new Dx4Bet(currMetaBet.getMetaGame().getGameByType(Dx4GameTypeJson.D4Small),stake) );
			else
			if (index==1)
				currMetaBet.getBets().add( new Dx4Bet(currMetaBet.getMetaGame().getGameByType(Dx4GameTypeJson.D4Big),stake) );
			else
			if (index==2)
				currMetaBet.getBets().add( new Dx4Bet(currMetaBet.getMetaGame().getGameByType(Dx4GameTypeJson.ABCA),stake) );
			else
			if (index==3)
				currMetaBet.getBets().add( new Dx4Bet(currMetaBet.getMetaGame().getGameByType(Dx4GameTypeJson.ABCC),stake) );
			else
				log.warn("betSMS stakes : " + betSMS.getStakes() + " unexpected value: " + stake + " - ignored");
			index++;
		}
	}
	
	public List<Dx4MetaBet> getMetaBets() {
		return metaBets;
	}

	public void setMetaBets(List<Dx4MetaBet> metaBets) {
		this.metaBets = metaBets;
	}

	public Dx4Services getServices() {
		return services;
	}

	public void setServices(Dx4Services services) {
		this.services = services;
	}

	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}

	public static void main(String[] argvs)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
								"Dx4-Service.xml");

		Dx4Services services = (Dx4Services) context.getBean("dx4Services");
		try {
			Ex4BetSMSParser parser = new Ex4BetSMSParser(services,"C:/_Development/gexZZ/4DX/sample/sample bets.txt","4D With ABC");
			for (Dx4MetaBet bet : parser.getMetaBets())
			{
				log.info(bet);
				double totalStake = bet.getTotalStake();
				log.info(totalStake);
			}
			
		} catch (Ex4Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
