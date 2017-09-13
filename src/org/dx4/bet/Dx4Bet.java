package org.dx4.bet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.game.Dx4Game;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4ProviderJson;

public class Dx4Bet implements Serializable
{
	private static final long serialVersionUID = 7311697841523357577L;
	private long id;
	private long gameId;
	private Dx4Game game;
	private double stake;
	private double totalStake;
	private String choice;
	private String providerCodes;				// String of codes of providers
	private List<Dx4ProviderJson> providers;
	private long metaBetId;
	private double odds;						// floating model only
	
	
	public Dx4Bet()
	{
		setProviders(new ArrayList<Dx4ProviderJson>());
	}
	
	public Dx4Bet(Dx4Game game,double stake)
	{
		this();
		setGame(game);
		setStake(stake);
	}
	
	@Override
	public Dx4Bet clone()
	{
		Dx4Bet bet = new Dx4Bet();
		bet.setChoice(choice);
		bet.setStake(stake);
		bet.setTotalStake(totalStake);
		bet.setGame(game);
		bet.setGameId(gameId);
		bet.setProviders(providers);
		bet.setProviderCodes(providerCodes);
		bet.setOdds(odds);
		return bet;
	}
	
	public void setProvidersFromCodes(Dx4Home dx4Home)
	{
		setProviders(dx4Home.getProvidersFromCodes(providerCodes));
	}
	
	public int getProviderNum()
	{
		return getProviderCodes().length();
	}
	
	public double calcHighWin()
	{
		Dx4Game game = getGame();
		double win = getStake()*game.getPayOutByType(game.getGtype().getTopPayout()).getPayOut();
		return win;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Dx4Game getGame() {
		return game;
	}
	
	public void setGame(Dx4Game game) {
		setGameId(game.getId());
		this.game = game;
	}
	
	public double getStake() {
		return stake;
	}
	
	public double calcTotalStake()
	{
		totalStake = game.getGtype().calcStake(choice, stake);
		return totalStake;
	}
	
	public double getTotalStake()
	{
		return totalStake;
	}
	
	public void setStake(double stake) {
		this.stake = stake;
	}
	
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public long getGameId() {
		return gameId;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public String getProviderCodes() {
		return providerCodes;
	}

	public void setProviderCodes(String providerCodes) {
		this.providerCodes = providerCodes;
	}

	public List<Dx4ProviderJson> getProviders() {
		return providers;
	}

	public void setProviders(List<Dx4ProviderJson> providers) {
		this.providers = providers;
	}

	public long getMetaBetId() {
		return metaBetId;
	}

	public void setMetaBetId(long metaBetId) {
		this.metaBetId = metaBetId;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}

	public void setTotalStake(double totalStake) {
		this.totalStake = totalStake;
	}

}
