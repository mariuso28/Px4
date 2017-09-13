package org.dx4.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.util.DateUtil;


public class Dx4MetaGame implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4405471540037249680L;

	private static final Logger log = Logger.getLogger(Dx4MetaGame.class);
	
	private long id;
	private String name;
	private String description;
	private List<Dx4PlayGame> playGames;
	private List<Dx4Game> games;
	private List<String> providers;
	private Map<String,String> images;

	
	public Dx4MetaGame()
	{
		games = new ArrayList<Dx4Game>();
		playGames = new ArrayList<Dx4PlayGame>();
		providers = new ArrayList<String>();
		images = new HashMap<String,String>();
	}	
	
	public Dx4PlayGame getPlayGameById(long id)
	{
		for (Dx4PlayGame playGame : playGames)
			if (playGame.getId()==id)
				return playGame;
		return null;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Dx4Game> getGames() {
		return games;
	}

	public void setGames(List<Dx4Game> games) {
		this.games = games;
	}

	public Dx4Game getGameById(long gameId) {
		for (Dx4Game game : games)
			if (game.getId()==gameId)
				return game;
		return null;
	}

	public void setPlayGames(List<Dx4PlayGame> playGames) {
		this.playGames = playGames;
		sortPlayList();
	}

	public List<Dx4PlayGame> getPlayGames() {
		return playGames;
	}
	
	public void addPlayGame(Dx4PlayGame playGame)
	{
		playGames.add(playGame);
		sortPlayList();
	}

	private void sortPlayList()
	{
		Collections.sort(playGames, new PlayGameComparator());
			
	}

	public Dx4PlayGame getPlayGame(Date date) {
		date = DateUtil.getDateWithZeroedTime(date);
		for (Dx4PlayGame playGame : playGames)
		{
			Date pgDate = DateUtil.getDateWithZeroedTime(playGame.getPlayDate());
			if (pgDate.equals(date))		
			{														
				log.trace("MATCH");
				return playGame;
			}
			else
				log.trace("NO MATCH");
		}
		return null;
	}
	
	public Dx4Game getGameByType(Dx4GameTypeJson type)
	{
		for (Dx4Game game : games)
			if (game.getGtype()!=null)
				if (game.getGtype().equals(type))
					return game;
		return null;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public List<String> getProviders() {
		return providers;
	}
	
	public boolean isUnplayed() {
		for (Dx4PlayGame playGame : playGames)
			if (playGame.isPlayed()==false)
				return true;
		return false;
	}
	
	public Dx4PlayGame getNextGameAvailableForBet() {
		List<Dx4PlayGame> playGames = getPlayGamesAvailableForBets();
		if (playGames.isEmpty())
			return null;
		return playGames.get(0);
	}
	
	public List<Dx4PlayGame> getPlayGamesAvailableForBets()
	{
		List<Dx4PlayGame> availablePlayGames = new ArrayList<Dx4PlayGame>();
		for (Dx4PlayGame playGame : playGames)
		{
			if (playGame.availableForBets())
				availablePlayGames.add(playGame);
		}
		return availablePlayGames;
	}
	
	public Date nextDrawCutOffTime()
	{
		List<Dx4PlayGame> playGames = getPlayGamesAvailableForBets();
		GregorianCalendar gc = new GregorianCalendar();
		if (playGames.isEmpty())
		{
			gc.set(2020,01,01);
			return gc.getTime();
		}
		Date playDate = playGames.get(0).getPlayDate();
	
		return playDate;
	}

	public Dx4PlayGame getPlayGameByPlayDate(Date playDate) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(playDate);
		playDate = DateUtil.getDateWithZeroedTime(playDate);
		
		for (Dx4PlayGame playGame : playGames)
		{
			Date pgd = DateUtil.getDateWithZeroedTime(playGame.getPlayDate());
			if (pgd.equals(playDate))
				return playGame;
		}
		return null;
	}
	
	public List<Dx4Game> getDigitGames(int digits)
	{
		List<Dx4Game> digitGames = new ArrayList<Dx4Game>();
		for (Dx4Game game : games)
			if (game.getGtype().getDigits()==digits)
				digitGames.add(game);
		return digitGames;
	}
	
	public boolean hasDigits(int digits) {
		return getDigitGames(digits).size()>0;
	}
	
	public Map<String, String> getImages() {
		return images;
	}

	public void setImages(Map<String, String> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "Dx4MetaGame [id=" + id + ", name=" + name + ", description="
				+ description + ", playGames=" + playGames + ", games=" + games
				+ ", providers=" + providers + "]";
	}

	

	
}
