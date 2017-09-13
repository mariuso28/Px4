package org.dx4.test.simulate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.dx4.game.Dx4PlayGame;
import org.dx4.home.persistence.Dx4PersistenceException;
import org.dx4.json.message.Dx4BetJson;
import org.dx4.json.message.Dx4BetStakeCombo;
import org.dx4.json.message.Dx4MetaBetJson;
import org.dx4.json.message.Dx4MetaGameJson;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.services.Dx4Services;

public class MetaBetSimulate 
{	
	static double[] stakes = { 1,2,5,10,15,20 }; 
	
	public static Dx4MetaBetJson createMetaBetJson(Dx4Services dx4Services,Dx4MetaGameJson metaGame,
									Dx4PlayGame playGame,Date placed) throws Dx4PersistenceException
	{
		Dx4MetaBetJson metaBetJson = new Dx4MetaBetJson();
		
		metaBetJson.setMetaGameId(metaGame.getId());
		metaBetJson.setPlaced(placed);
		
		metaBetJson.setBets(createBets(playGame,dx4Services,dx4Services.getFloatPayoutMgr()!=null));
		
		return metaBetJson;
	}

	private static List<Dx4ProviderJson> getRandomProviders(List<Dx4ProviderJson> providers, Dx4PlayGame playGame)
	{
		List<Dx4ProviderJson> provs = new ArrayList<Dx4ProviderJson>();
		int size = playGame.getProviderCodes().length();
		int cnt = (new Random()).nextInt(size);
		while (provs.size()<=cnt)
		{
			int pos = (new Random()).nextInt(size);
			Dx4ProviderJson prov = providers.get(pos);
			if (!provs.contains(prov) && playGame.getProviderCodes().indexOf(prov.getCode())>=0)
				provs.add(prov);
		}
		
		return provs;
	}
	
	private static String createChoice() {
		Random rand = new Random();
		int numDigits = rand.nextInt(3)+2;
		return createRandomChoice(numDigits);
	}

	private static String createRandomChoice(int digits) {
		String choice = "";
		Random rand = new Random();
		for (; digits>0; digits--)
		{
			int num = rand.nextInt(10);
			choice += num;
		}
		return choice;
	}

	private static List<Dx4BetJson> createBets(Dx4PlayGame playGame,Dx4Services dx4Services,boolean floatingModel) {
		
		List<Dx4BetJson> bets = new ArrayList<Dx4BetJson>();
		Random rand = new Random();
		int numBets = rand.nextInt(10);
		
		Set<Integer> set = new HashSet<Integer>();
		for (; numBets>=0; numBets--)
		{
			rand = new Random();
			Integer index = rand.nextInt(10);
			if (set.contains(index))
				continue;
			List<Dx4ProviderJson> provs = getRandomProviders(dx4Services.getDx4Home().getProviders(),playGame);
			Dx4BetJson bet = createBet(playGame,provs,floatingModel);
			
			if (floatingModel && bet.getChoice().length()<3)
				continue;
			
			bets.add(bet);
			set.add(index);
		}
		return bets;
	}

	private static Dx4BetJson createBet(Dx4PlayGame playGame, List<Dx4ProviderJson> provs,boolean floatingModel) {
		
		Dx4BetJson bet = new Dx4BetJson();
		bet.setChoice(createChoice());
		bet.setPlayGameId(playGame.getId());
		
		List<String> providers = new ArrayList<String>();
		for (Dx4ProviderJson p : provs)
			providers.add(p.getName());
		bet.setProviders(providers);
		
		Random rand = new Random();
		if (bet.getChoice().length()==2)
		{
			bet.setSmall(stakes[rand.nextInt(6)]);
			return bet;
		}
		
		if (floatingModel)
		{
			bet.setBig(stakes[rand.nextInt(6)]);
			return bet;
		}
		
		switch (rand.nextInt(3))
		{
			case 1 : bet.setSmall(stakes[rand.nextInt(6)]); break;
			case 2 : bet.setBig(stakes[rand.nextInt(6)]); break;
			default : bet.setSmall(stakes[rand.nextInt(6)]); 
					  bet.setBig(stakes[rand.nextInt(6)]); break;
		}
		if (bet.getChoice().length()==3)
		{
			return bet;
		}
		
		
		switch (rand.nextInt(3))
		{
			case 1 : bet.setStakeCombo(Dx4BetStakeCombo.BOX); break;
			case 2 : bet.setStakeCombo(Dx4BetStakeCombo.IBOX); break;
		}
			
		if (bet.getStakeCombo().equals(Dx4BetStakeCombo.IBOX))
		{
			if (bet.getSmall()>0)
				bet.setSmall(stakes[rand.nextInt(3)]); 
			if (bet.getBig()>0)
				bet.setBig(stakes[rand.nextInt(3)]); 
		}
		if (rand.nextBoolean())
			bet.setSmall(stakes[rand.nextInt(6)]);
		else
			bet.setBig(stakes[rand.nextInt(6)]);
		return bet;
	}
	
}
