package org.dx4.json.message;

import java.util.UUID;

public class Dx4FavouriteJson {

	private long id;
	private long betId;
	private Dx4BetJson bet;
	private String description;
	private UUID playerId;
	
	public Dx4FavouriteJson()
	{
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

	public UUID getPlayerId() {
		return playerId;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}

	public Dx4BetJson getBet() {
		return bet;
	}

	public void setBet(Dx4BetJson bet) {
		this.bet = bet;
	}

	public long getBetId() {
		return betId;
	}

	public void setBetId(long betId) {
		this.betId = betId;
	}

	
}
