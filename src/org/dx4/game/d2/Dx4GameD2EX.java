package org.dx4.game.d2;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4GameD2EX extends Dx4GameD2C {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1646985669305814251L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Dx4GameD2EX.class);
	
	public Dx4GameD2EX()
	{
		setGtype(Dx4GameTypeJson.D2EX);
	}

	@Override
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		wins.addAll(super.calcWin(metaBet,bet,result));
		for (String special : result.getSpecials())
		{
			if (special.substring(2).equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Spec).getPayOut();
				Dx4Win win = new Dx4Win(metaBet,bet,special,Dx4PayOutTypeJson.Spec.name(), winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		for (String cons : result.getConsolations())
		{
			if (cons.substring(2).equals(bet.getChoice()))
			{
				double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Cons).getPayOut();
				Dx4Win win = new Dx4Win(metaBet,bet,cons,Dx4PayOutTypeJson.Cons.name(),winnings, result.getProvider().getCode());
				wins.add(win);
			}
		}
		return wins;
	}
	
}
