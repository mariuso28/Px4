package org.gz.game;

import java.util.ArrayList;
import java.util.List;

import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutJson;

public class GzGameTypePayouts {

	private Dx4GameTypeJson gameType;
	private List<Dx4PayOutJson> payOuts = new ArrayList<Dx4PayOutJson>();
	
	public GzGameTypePayouts()
	{
		payOuts.add(null);
		payOuts.add(null);
		payOuts.add(null);
		payOuts.add(null);
		payOuts.add(null);
	}
	
	public GzGameTypePayouts(Dx4GameTypeJson gameType)
	{
		this();
		setGameType(gameType);
	}

	public void addInPayOut(Dx4PayOutJson payout)
	{
		switch (payout.getType().getPlace())
		{
			case 'F' : payOuts.set(0,payout); break;
			case 'S' : payOuts.set(1,payout); break;
			case 'T' : payOuts.set(2,payout); break;
			case 'P' : payOuts.set(3,payout); break;
			case 'C' : payOuts.set(4,payout); break;
			default : return;
		}
	}
	
	public Dx4GameTypeJson getGameType() {
		return gameType;
	}

	public void setGameType(Dx4GameTypeJson gameType) {
		this.gameType = gameType;
	}

	public List<Dx4PayOutJson> getPayOuts() {
		return payOuts;
	}

	public void setPayOuts(List<Dx4PayOutJson> payOuts) {
		this.payOuts = payOuts;
	}
	
}
