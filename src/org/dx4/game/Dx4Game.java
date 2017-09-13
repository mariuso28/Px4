package org.dx4.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4Game implements Serializable{
	
	private static final long serialVersionUID = -3054163520905634835L;
	private long id;
	private Dx4GameTypeJson gtype;
	private double stake;
	private double minBet;
	private double maxBet;
	private List<Dx4PayOut> payOuts;
	
	public Dx4Game()
	{
		payOuts = new ArrayList<Dx4PayOut>();
	}
	
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result) 
	{
		return new ArrayList<Dx4Win>();
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Dx4GameTypeJson getGtype() {
		return gtype;
	}

	public void setGtype(Dx4GameTypeJson gtype) {
		this.gtype = gtype;
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public double getMinBet() {
		return minBet;
	}

	public void setMinBet(double minBet) {
		this.minBet = minBet;
	}

	public double getMaxBet() {
		return maxBet;
	}

	public void setMaxBet(double maxBet) {
		this.maxBet = maxBet;
	}
	
	public List<Dx4PayOut> getPayOuts() {
		return payOuts;
	}
	
	public Dx4PayOut getPayOutByType(Dx4PayOutTypeJson type)
	{
		for (Dx4PayOut payOut : payOuts)
			if (payOut.getType().equals(type))
				return payOut;
		return null;
	}

	public void setPayOuts(List<Dx4PayOut> payOuts) {
		this.payOuts = payOuts;
	}

	public void addPayOut(Dx4PayOut payOut)
	{
		payOuts.add(payOut);
	}
	
	@Override
	public String toString() {
		return "Dx4Game [id=" + id + ", gtype=" + gtype + ", stake=" + stake
				+ ", minBet=" + minBet + ", maxBet=" + maxBet + ", payOuts="
				+ payOuts + "]";
	}

	
	
}
