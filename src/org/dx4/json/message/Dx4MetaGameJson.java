package org.dx4.json.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dx4MetaGameJson 
{
		private long id;
		private String name;
		private String description;
		private List<Dx4PlayGameJson> playGames;
		private List<Dx4GameJson> games;
		private Dx4GameGroupJson[] gameGroups;
		private List<String> providers;
		private Map<String,String> images;
		
		public Dx4MetaGameJson()
		{
		}
			
		public Dx4GameJson getGameForId(long id)
		{
			for (Dx4GameJson game : games)
			{
				if (game.getId()==id)
					return game;
			}
			return null;
		}
		
		public Dx4GameJson getGameForType(Dx4GameTypeJson gameType)
		{
			for (Dx4GameJson game : games)
			{
				if (game.getType().equals(gameType))
					return game;
			}
			return null;
		}
		
		public List<Dx4GameTypeJson> getGamesTypesForBet(Dx4BetJson bet) {
			
			List<Dx4GameTypeJson> gameTypes = new ArrayList<Dx4GameTypeJson>();
			switch (bet.getChoice().length())
			{
				case 2 : gameTypes.add(Dx4GameTypeJson.D2); break;
				case 3 : if (bet.getBig()>0)
							gameTypes.add(Dx4GameTypeJson.ABCC);
						 if (bet.getSmall()>0)
							 gameTypes.add(Dx4GameTypeJson.ABCA);
						 break;
				default: // 4 Digits
						if (bet.getStakeCombo().equals(Dx4BetStakeCombo.BOX))
						{
							if (bet.getBig()>0)
								gameTypes.add(Dx4GameTypeJson.D4BoxBig);
					    	if (bet.getSmall()>0)
					    		gameTypes.add(Dx4GameTypeJson.D4BoxSmall);
					    	break;
						}
						if (bet.getStakeCombo().equals(Dx4BetStakeCombo.IBOX))
						{
							if (bet.getBig()>0)
								gameTypes.add(Dx4GameTypeJson.D4IBoxBig);
					    	if (bet.getSmall()>0)
					    		gameTypes.add(Dx4GameTypeJson.D4IBoxSmall);
					    	break;
						}	
						if (bet.getBig()>0)
							gameTypes.add(Dx4GameTypeJson.D4Big);
				    	if (bet.getSmall()>0)
				    		gameTypes.add(Dx4GameTypeJson.D4Small);
			}
			return gameTypes;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<String> getProviders() {
			return providers;
		}

		public void setProviders(List<String> providers) {
			this.providers = providers;
		}

		public List<Dx4GameJson> getGames() {
			return games;
		}

		public void setGames(List<Dx4GameJson> games) {
			this.games = games;
		}

		public List<Dx4PlayGameJson> getPlayGames() {
			return playGames;
		}

		public void setPlayGames(List<Dx4PlayGameJson> playGames) {
			this.playGames = playGames;
		}

		public Dx4GameGroupJson[] getGameGroups() {
			return gameGroups;
		}

		public void setGameGroups(Dx4GameGroupJson[] gameGroups) {
			this.gameGroups = gameGroups;
		}

		public Map<String, String> getImages() {
			return images;
		}

		public void setImages(Map<String, String> images) {
			this.images = images;
		}

		

}
