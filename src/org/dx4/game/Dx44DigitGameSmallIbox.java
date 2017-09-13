package org.dx4.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultIboxJson;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx44DigitGameSmallIbox extends Dx4Game {

	private static final long serialVersionUID = -3110651259620290862L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx44DigitGameSmallIbox.class);
	
	public Dx44DigitGameSmallIbox()
	{
		setGtype(Dx4GameTypeJson.D4IBoxSmall);
	}

	@Override
	public synchronized List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		Dx4DrawResultIboxJson dbi = result.getDrawResultIboxJson();
		
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		Dx4PayOutTypeJson pt = null;
		String use = bet.getChoice();
		
		Dx4IboxMatch match = dbi.isFirst(use);
		if (match!=null)
		{
			pt = getFirstPayout(match.getSizes().get(0));
			wins.add(createWin(pt,metaBet,bet,result,use,match,0));
		}

		match = dbi.isSecond(use);
		if (match!=null)
		{
			pt = getSecondPayout(match.getSizes().get(0));
			wins.add(createWin(pt,metaBet,bet,result,use,match,0));
		}

		match = dbi.isThird(use);
		if (match!=null)
		{
			pt = getThirdPayout(match.getSizes().get(0));
			wins.add(createWin(pt,metaBet,bet,result,use,match,0));
		}
		
		return wins;
	}
	
	protected Dx4Win createWin(Dx4PayOutTypeJson pt,Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result,String use,Dx4IboxMatch match,int index)
	{
		double winnings = bet.getStake()*bet.getGame().getPayOutByType(pt).getPayOut();
		return new Dx4Win(metaBet,bet,match.getMatches().get(index),pt.name(),winnings, result.getProvider().getCode());
	}
	
	protected Dx4PayOutTypeJson getFirstPayout(int size)
	{
		switch (size)
		{
			case 24 : return Dx4PayOutTypeJson.FirstIB24; 
			case 12 : return Dx4PayOutTypeJson.FirstIB12; 
			case 6 : return Dx4PayOutTypeJson.FirstIB6; 
			default : return Dx4PayOutTypeJson.FirstIB4;
		}
	}
	
	protected Dx4PayOutTypeJson getSecondPayout(int size)
	{
		switch (size)
		{
			case 24 : return Dx4PayOutTypeJson.SecondIB24; 
			case 12 : return Dx4PayOutTypeJson.SecondIB12; 
			case 6 : return Dx4PayOutTypeJson.SecondIB6; 
			default : return Dx4PayOutTypeJson.SecondIB4;
		}
	}
	
	protected Dx4PayOutTypeJson getThirdPayout(int size)
	{
		switch (size)
		{
			case 24 : return Dx4PayOutTypeJson.ThirdIB24; 
			case 12 : return Dx4PayOutTypeJson.ThirdIB12; 
			case 6 : return Dx4PayOutTypeJson.ThirdIB6; 
			default : return Dx4PayOutTypeJson.ThirdIB4;
		}
	}
	
}
