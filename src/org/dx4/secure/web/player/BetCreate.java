package org.dx4.secure.web.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;

public class BetCreate implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1516358286451995528L;
	private List<BetMapping> betMappings;
	private String playGameDate;
	private List<String> useProviders;
	
	public BetCreate()
	{
		betMappings = new ArrayList<BetMapping>();
		useProviders = new ArrayList<String>();
	}
	
	public BetCreate(Dx4MetaGame metaGame)
	{
		this();
		for (Dx4Game game : metaGame.getGames())
		{
			BetMapping betMapping;
			betMapping = new BetMapping();
			betMapping.setGameId(game.getId());
			betMappings.add(betMapping);
			setPlayGameDate("NOT SET");
		}
		for (String provider : metaGame.getProviders())
			useProviders.add(provider);
	}
	
	public double getTotalStake()
	{
		double total = 0.0;
		for (BetMapping betMapping : betMappings)
		{
			if (betMapping.getChecked().equalsIgnoreCase("on"))
				total += betMapping.getStake();
		}
		return total;
	}
	
	public BetMapping getBetMappingByGameId(long gameId)
	{
		for (BetMapping betMapping : betMappings)
		{
			if (betMapping.getGameId()==gameId)
				return betMapping;
		}
		return null;
	}
	
	public void setBetMappings(List<BetMapping> betMappings) {
		this.betMappings = betMappings;
	}
	public List<BetMapping> getBetMappings() {
		return betMappings;
	}

	public void setPlayGameDate(String playGameDate) {
		this.playGameDate = playGameDate;
	}

	public String getPlayGameDate() {
		return playGameDate;
	}

	public void setBetMappingChoicesForGame(String number) {
		for (BetMapping mapping : getBetMappings()){
			if (mapping.getLookUpValue()!=0)
			{
				for (int i=0; i<number.length(); i++)
				{
					mapping.getChoices().set(i, Character.toString(number.charAt(i)));
				}
				mapping.setLookUpValue(0);
				return;
			}
		}
	}

	public List<String> getUseProviders() {
		return useProviders;
	}

	public void setUseProviders(List<String> useProviders) {
		this.useProviders = useProviders;
	}

	@Override
	public String toString() {
		return "BetCreate [betMappings=" + betMappings + ", playGameDate="
				+ playGameDate + ", useProviders=" + useProviders + "]";
	}

	
}
