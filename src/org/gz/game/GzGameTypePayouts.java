package org.gz.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.game.payout.Dx4PayOut;
import org.dx4.json.message.Dx4GameTypeJson;

public class GzGameTypePayouts implements Serializable{

	private static final long serialVersionUID = 31924129167538589L;
	private long id;
	private Dx4GameTypeJson gameType;
	private double commission;
	private List<Dx4PayOut> payOuts = new ArrayList<Dx4PayOut>();
	
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

	public void mapPayOuts(List<Dx4PayOut> payouts) {
		for (Dx4PayOut payout : payouts)
			addInPayOut(payout);
	}
		
	public void addInPayOut(Dx4PayOut payout)
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

	public List<Dx4PayOut> getPayOuts() {
		return payOuts;
	}

	public void setPayOuts(List<Dx4PayOut> payOuts) {
		this.payOuts = payOuts;
	}


	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	@Override
	public String toString() {
		return "GzGameTypePayouts [gameType=" + gameType + ", commission=" + commission + ", payOuts=" + payOuts + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
}
