package org.dx4.game.d4;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4GameD4Big extends Dx4GameD4Small {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5110423761600451922L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameD4Big.class);
	
	public Dx4GameD4Big()
	{
		setGtype(Dx4GameTypeJson.D4Big);
	}

	@Override
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		if (metaBet.isFloatingModel())
			return calcFloatingWin(metaBet,bet,result);
		
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		wins.addAll(super.calcWin(metaBet,bet,result));
		for (String special : result.getSpecials())
		{
			if (special.equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Spec).getPayOut();
				Dx4Win win = new Dx4Win(metaBet,bet,special,Dx4PayOutTypeJson.Spec.name(), winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		for (String cons : result.getConsolations())
		{
			if (cons.equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Cons).getPayOut();
				Dx4Win win = new Dx4Win(metaBet,bet,cons,Dx4PayOutTypeJson.Cons.name(),winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		return wins;
	}
	
	private List<Dx4Win> calcFloatingWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)
	{
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		String use = bet.getChoice();
		if (result.getFirstPlace().equals(use))
		{
			double winnings = bet.getStake()*bet.getOdds();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getFirstPlace(),Dx4PayOutTypeJson.First.name(),
					winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getSecondPlace().equals(use))
		{
			double winnings = bet.getStake()*bet.getOdds();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getSecondPlace(),Dx4PayOutTypeJson.Second.name(),
					winnings, result.getProvider().getCode());
			wins.add(win);
		}
		if (result.getThirdPlace().equals(use))
		{
			double winnings = bet.getStake()*bet.getOdds();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getThirdPlace(),Dx4PayOutTypeJson.Third.name(),
					winnings, result.getProvider().getCode());
			wins.add(win);
		}
		for (String special : result.getSpecials())
		{
			if (special.equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getOdds();
				Dx4Win win = new Dx4Win(metaBet,bet,special,Dx4PayOutTypeJson.Spec.name(), winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		for (String cons : result.getConsolations())
		{
			if (cons.equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getOdds();
				Dx4Win win = new Dx4Win(metaBet,bet,cons,Dx4PayOutTypeJson.Cons.name(),winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		return wins;
	}
}
