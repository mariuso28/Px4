package org.dx4.json.message;

import java.util.List;

public class Dx4BetDetailJson {
	private Dx4BetSummaryJson betSummary;
	private List<Dx4BetJson> bets;
	private List<String> choices;
	private List<Dx4WinJson> wins;
	
	public Dx4BetDetailJson()
	{
	}

	public void consolidateBets()
	{
		setBets(Dx4MetaBetJson.consolidateBets(bets));
	}
	
	public Dx4BetSummaryJson getBetSummary() {
		return betSummary;
	}


	public void setBetSummary(Dx4BetSummaryJson betSummary) {
		this.betSummary = betSummary;
	}


	public List<Dx4WinJson> getWins() {
		return wins;
	}

	public void setWins(List<Dx4WinJson> wins) {
		this.wins = wins;
	}

	public List<Dx4BetJson> getBets() {
		return bets;
	}

	public void setBets(List<Dx4BetJson> bets) {
		this.bets = bets;
	}

	public List<String> getChoices() {
		return choices;
	}

	public void setChoices(List<String> choices) {
		this.choices = choices;
	}

}
