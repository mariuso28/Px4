package org.dx4.game.d4;

import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultIboxJson;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4GameD4IBoxBig extends Dx4GameD4IBoxSmall {

	private static final long serialVersionUID = -3110651259620290862L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameD4IBoxBig.class);
	
	public Dx4GameD4IBoxBig()
	{
		setGtype(Dx4GameTypeJson.D4IBoxBig);
	}

	@Override
	public synchronized List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		Dx4DrawResultIboxJson dbi = result.getDrawResultIboxJson();
		
		List<Dx4Win> wins = super.calcWin(metaBet, bet, result);
		
		Dx4PayOutTypeJson pt = null;
		String use = bet.getChoice();
		Dx4IboxMatch match = dbi.isConsolation(use);
		if (match!=null)
		{
			for (int i=0; i<match.getMatches().size(); i++)
			{
				pt = getConsulationPayout(match.getSizes().get(i));
				wins.add(createWin(pt,metaBet,bet,result,use,match,i));
			}
		}
		match = dbi.isSpecial(use);
		if (match!=null)
		{
			for (int i=0; i<match.getMatches().size(); i++)
			{
				pt = getSpecialPayout(match.getSizes().get(i));
				wins.add(createWin(pt,metaBet,bet,result,use,match,i));
			}
		}
		return wins;
	}
	
	private Dx4PayOutTypeJson getSpecialPayout(int size)
	{
		switch (size)
		{
			case 24 : return Dx4PayOutTypeJson.SpecIB24;
			case 12 : return Dx4PayOutTypeJson.SpecIB12; 
			case 6 : return Dx4PayOutTypeJson.SpecIB6; 
			default : return Dx4PayOutTypeJson.SpecIB4; 
		}
	}
	
	private Dx4PayOutTypeJson getConsulationPayout(int size)
	{
		switch (size)
		{
			case 24 : return Dx4PayOutTypeJson.ConsIB24;
			case 12 : return Dx4PayOutTypeJson.ConsIB12; 
			case 6 : return Dx4PayOutTypeJson.ConsIB6; 
			default : return Dx4PayOutTypeJson.ConsIB4; 
		}
	}
	
}
