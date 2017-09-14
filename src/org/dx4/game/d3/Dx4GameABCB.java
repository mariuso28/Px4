package org.dx4.game.d3;

import java.util.ArrayList;
import java.util.List;

import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.bet.Dx4Win;
import org.dx4.game.Dx4Game;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;

public class Dx4GameABCB extends Dx4Game {

	private static final long serialVersionUID = 3357350774453625064L;

	public Dx4GameABCB()
	{
		setGtype(Dx4GameTypeJson.ABCB);
	}
	
	public List<Dx4Win> calcWin(Dx4MetaBet metaBet,Dx4Bet bet,Dx4DrawResultJson result)  {
		List<Dx4Win> wins = new ArrayList<Dx4Win>();
		String use = bet.getChoice();
		if (result.getFirstPlace().substring(1).equals(use))
		{
			double winnings = bet.getStake()*bet.getGame().getPayOutByType(Dx4PayOutTypeJson.Second).getPayOut();
			Dx4Win win = new Dx4Win(metaBet,bet,result.getFirstPlace(),Dx4PayOutTypeJson.Second.name(),winnings, result.getProvider().getCode());
			wins.add(win);
		}
		return wins;
	}
}
